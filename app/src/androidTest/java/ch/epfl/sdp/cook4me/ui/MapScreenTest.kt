package ch.epfl.sdp.cook4me.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ch.epfl.sdp.cook4me.BuildConfig.MAPS_API_KEY
import ch.epfl.sdp.cook4me.ui.map.GoogleMapView
import ch.epfl.sdp.cook4me.ui.map.Locations
import ch.epfl.sdp.cook4me.ui.map.dummyMarkers
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import io.mockk.Ordering
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

const val STARTING_ZOOM = 10f
const val ASSERT_ROUNDING_ERROR = 0.01
const val ONE_MINUTE_IN_MILLISECONDS = 60000L

class GoogleMapViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val startingPosition = Locations.LAUSANNE
    private lateinit var cameraPositionState: CameraPositionState

    private fun initMap() {
        check(hasValidApiKey()) { "Maps API key not specified" }
        val countDownLatch = CountDownLatch(1)
        composeTestRule.setContent {
            GoogleMapView(
                markers = dummyMarkers,
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    countDownLatch.countDown()
                }
            )
        }
        val mapLoaded = countDownLatch.await(60, TimeUnit.SECONDS)
        assertTrue("Map loaded", mapLoaded)
    }

    @Before
    fun setUp() {
        cameraPositionState = spyk(
            CameraPositionState(
                position = CameraPosition.fromLatLngZoom(
                    startingPosition,
                    STARTING_ZOOM
                )
            )
        )
    }

    @Test
    fun testStartingCameraPosition() {
        initMap()
        startingPosition.assertEquals(cameraPositionState.position.target)
    }

    @Test
    fun testCameraReportsMoving() {
        initMap()
        assertEquals(CameraMoveStartedReason.NO_MOVEMENT_YET, cameraPositionState.cameraMoveStartedReason)
        zoom(shouldAnimate = true, zoomIn = true) {
            verify(ordering = Ordering.ORDERED, timeout = ONE_MINUTE_IN_MILLISECONDS) {
                cameraPositionState setProperty "cameraMoveStartedReason" value CameraMoveStartedReason.DEVELOPER_ANIMATION
                cameraPositionState setProperty "isMoving" value true
                cameraPositionState setProperty "isMoving" value false
            }
        }
    }

    @Test
    fun testCameraZoomInAnimation() {
        initMap()
        zoom(shouldAnimate = true, zoomIn = true) {
            assertMoveHappened(cameraPositionState)
            assertEquals(
                STARTING_ZOOM + 1f,
                cameraPositionState.position.zoom,
                ASSERT_ROUNDING_ERROR.toFloat()
            )
        }
    }

    @Test
    fun testCameraZoomOut() {
        initMap()
        zoom(shouldAnimate = false, zoomIn = false) {
            assertMoveHappened(cameraPositionState)
            assertEquals(
                STARTING_ZOOM - 1f,
                cameraPositionState.position.zoom,
                ASSERT_ROUNDING_ERROR.toFloat()
            )
        }
    }

    @Test
    fun testCameraZoomOutAnimation() {
        initMap()
        zoom(shouldAnimate = true, zoomIn = false) {
            assertMoveHappened(cameraPositionState)
            assertEquals(
                STARTING_ZOOM - 1f,
                cameraPositionState.position.zoom,
                ASSERT_ROUNDING_ERROR.toFloat()
            )
        }
    }

    @Test
    fun testLatLngInVisibleRegion() {
        initMap()
        composeTestRule.runOnUiThread {
            val projection = cameraPositionState.projection
            assertNotNull(projection)
            assertTrue(
                projection!!.visibleRegion.latLngBounds.contains(startingPosition)
            )
        }
    }

    private fun zoom(
        shouldAnimate: Boolean,
        zoomIn: Boolean,
        assertionBlock: () -> Unit
    ) {
        if (!shouldAnimate) {
            composeTestRule.onNodeWithTag("cameraAnimations")
                .assertIsDisplayed()
                .performClick()
        }
        composeTestRule.onNodeWithText(if (zoomIn) "+" else "-")
            .assertIsDisplayed()
            .performClick()

        assertionBlock()
    }
}

private fun assertMoveHappened(cameraPositionState: CameraPositionState) {
    verify(ordering = Ordering.ORDERED, timeout = ONE_MINUTE_IN_MILLISECONDS) {
        cameraPositionState setProperty "isMoving" value true
        cameraPositionState setProperty "isMoving" value false
    }
}

private fun hasValidApiKey(): Boolean =
    MAPS_API_KEY.isNotBlank()

private fun LatLng.assertEquals(other: LatLng) {
    assertEquals(latitude, other.latitude, ASSERT_ROUNDING_ERROR)
    assertEquals(longitude, other.longitude, ASSERT_ROUNDING_ERROR)
}

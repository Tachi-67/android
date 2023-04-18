package ch.epfl.sdp.cook4me.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import ch.epfl.sdp.cook4me.permissions.TestPermissionStatusProvider
import org.junit.Rule
import org.junit.Test

class MapPermissionWrapper {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun assertMapIsDisplayedWhenLocationIsEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(true, false)))

        composeTestRule.setContent {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                modifier = Modifier.fillMaxSize(),
                markers = dummyMarkers,
                testing = true
            )
        }

        composeTestRule.onRoot().printToLog("DEBUG:")
        composeTestRule.onNodeWithText("EPFL").assertIsDisplayed()
    }

    @Test
    fun assertMapIsNotDisplayedWhenLocationIsNotEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("location" to Pair(false, false)))

        composeTestRule.setContent {
            MapPermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                modifier = Modifier.fillMaxSize(),
                markers = dummyMarkers,
                testing = true
            )
        }

        composeTestRule.onNodeWithText("The location permission will grant a better experience in the app").assertIsDisplayed()
    }



}
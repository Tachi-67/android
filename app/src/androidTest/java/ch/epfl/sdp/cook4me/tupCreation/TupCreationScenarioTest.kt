package ch.epfl.sdp.cook4me.tupCreation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.core.app.ActivityOptionsCompat
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.TupCreationScreenWithState
import ch.epfl.sdp.cook4me.ui.TupCreationViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TupCreationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    val testUri: Uri = Uri.parse(
        "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.placeholder_tupperware
    )

    private val registryOwner = ActivityResultRegistryOwner {
        object : ActivityResultRegistry() {
            override fun <I : Any?, O : Any?> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                // don't launch an activity, just respond with the test Uri
                val intent = Intent().setData(testUri)
                this.dispatchResult(requestCode, Activity.RESULT_OK, intent)
            }
        }
    }

    // super hacky way to wait for AsyncImage to be displayed but seems to work
    // should be called with assertIsDisplayed as it doesn't do the exhaustive checks
    private fun ComposeContentTestRule.waitUntilDisplayed(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 2_000L
    ) {
        this.waitUntil(timeoutMillis) {
            // code taken from assertIsDisplayed()

            val node = this.onNode(matcher).fetchSemanticsNode()
            var returnValue = true

            (node.root as? ViewRootForTest)?.let {
                if (!ViewMatchers.isDisplayed().matches(it.view)) {
                    returnValue = false
                }
            }
            val globalRect = node.boundsInWindow
            // checks if node has zero area, I think
            returnValue && (globalRect.width > 0f && globalRect.height > 0f)
        }
    }

    private val mockService = MockTupperwareService("", "", listOf(), listOf())

    @Test
    fun submittingValidTupFormShouldOutputCorrectTupperwareObject() {
        val title = "Pizza"
        val desc = "Yeah the photo is not lying it's not good..."
        val tags = listOf<String>()
        val images = listOf(testUri.toString())
        val mockService = MockTupperwareService(title, desc, tags, images)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState(TupCreationViewModel(mockService))
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("TitleTextField").performTextInput("Pizza")
        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
            .performTextInput("Yeah the photo is not lying it's not good...")
        composeTestRule.onNodeWithText("Done").performClick()
    }

    @Test
    fun descriptionFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreenWithState(TupCreationViewModel(mockService))
        }
        composeTestRule.onNodeWithText(text = "Description").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("DescriptionTextField").assertIsDisplayed()
    }
    @Test
    fun titleFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreenWithState(TupCreationViewModel(mockService))
        }
        composeTestRule.onNodeWithText(text = "Tupperware Name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("TitleTextField").assertIsDisplayed()
    }

    @Test
    fun tagsFieldIsDisplayed() {

        composeTestRule.setContent {
            TupCreationScreenWithState(TupCreationViewModel(mockService))
        }
        composeTestRule.onNodeWithContentDescription("TagsTextField").performScrollTo()
        composeTestRule.onNodeWithText(text = "Tags").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("TagsTextField").assertIsDisplayed()
    }

    @Test
    fun headerIsDisplayed() {

        composeTestRule.setContent {
            TupCreationScreenWithState(TupCreationViewModel(mockService))
        }
        composeTestRule.onNodeWithText(text = "Header").assertIsDisplayed()
    }

    @Test
    fun buttonRowIsDisplayed() {

        composeTestRule.setContent {
            TupCreationScreenWithState(TupCreationViewModel(mockService))
        }
        composeTestRule.onNodeWithText(text = "Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Done").assertIsDisplayed()
    }

    // no title or no image or no description
    @Test
    fun tupperwareFormWithNoTitleShouldNotBeSubmittable() {
        val title = "Pizza"
        val desc = "Yeah the photo is not lying it's not good..."
        val tags = listOf<String>()
        val images = listOf(testUri.toString())
        val mockService = MockTupperwareService(title, desc, tags, images)

        val hasTextFieldError = SemanticsMatcher.expectValue(
            SemanticsProperties.StateDescription, "Error"
        )
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState(TupCreationViewModel(mockService))
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
            .performTextInput("Yeah the photo is not lying it's not good...")
        composeTestRule.onNodeWithText("Done").performClick()
        composeTestRule.onAllNodes(hasTextFieldError)[0].assertExists()
    }

    @Test
    fun tupperwareFormWithNoImageShouldNotBeSubmittable() {
        val title = "Pizza"
        val desc = "Yeah the photo is not lying it's not good..."
        val tags = listOf<String>()
        val images = listOf(testUri.toString())
        val mockService = MockTupperwareService(title, desc, tags, images)

        val hasTextFieldError = SemanticsMatcher.expectValue(
            SemanticsProperties.StateDescription, "Error"
        )
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState(TupCreationViewModel(mockService))
            }
        }

        composeTestRule.onNodeWithContentDescription("TitleTextField").performTextInput("Pizza")
        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
            .performTextInput("Yeah the photo is not lying it's not good...")
        composeTestRule.onNodeWithText("Done").performClick()
        composeTestRule.onAllNodes(hasTextFieldError)[0].assertExists()
    }

    @Test
    fun tupperwareFormWithNoDescriptionShouldNotBeSubmittable() {
        val title = "Pizza"
        val desc = "Yeah the photo is not lying it's not good..."
        val tags = listOf<String>()
        val images = listOf(testUri.toString())
        val mockService = MockTupperwareService(title, desc, tags, images)

        val hasTextFieldError = SemanticsMatcher.expectValue(
            SemanticsProperties.StateDescription, "Error"
        )
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState(TupCreationViewModel(mockService))
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("TitleTextField").performTextInput("Pizza")
        composeTestRule.onNodeWithText("Done").performClick()
        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
        composeTestRule.onAllNodes(hasTextFieldError)[0].assertExists()
    }

    @Test
    fun addingImageFromGalleryShouldDisplayImage() {
        val title = "Pizza"
        val desc = "Yeah the photo is not lying it's not good..."
        val tags = listOf<String>()
        val images = listOf(testUri.toString())
        val mockService = MockTupperwareService(title, desc, tags, images)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState(TupCreationViewModel(mockService))
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
    }
}

package ch.epfl.sdp.cook4me.ui.profile

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProfileScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var repository: ProfileRepository
    private val user = Profile(
        email = "donald.duck@epfl.ch",
        name = "Donald",
        allergies = "Hazelnut",
        bio = "I am a duck",
        favoriteDish = "Spaghetti",
        userImage = "Image of Donald",
        photos = listOf(""),
    )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        /*
        * IMPORTANT:
        * (Below code is already functional, no need to change anything)
        * Make sure you do this try-catch block,
        * otherwise when doing CI, there will be an exception:
        * kotlin.UninitializedPropertyAccessException: lateinit property firestore has not been initialized
        * */
        try {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: IllegalStateException) {
            // emulator already set
            // do nothing
        }
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        } catch (e: IllegalStateException) {
            // emulator already set
            // do nothing
        }
        repository = ProfileRepository()
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            repository.add(user)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            repository.delete(user.email)
            auth.signInWithEmailAndPassword("donald.duck@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun editScreenTest() {
        val profileViewModel = ProfileViewModel()

        // Set up the test and wait for the screen to be loaded
        val username = composeTestRule.activity.getString(R.string.TAG_USER_FIELD)
        val favFood = composeTestRule.activity.getString(R.string.tag_favoriteDish)
        val allergies = composeTestRule.activity.getString(R.string.tag_allergies)
        val bio = composeTestRule.activity.getString(R.string.tag_bio)

        // Set input
        // This test does not work because of some issue not finding the text fields
        // after the clearence of the text fields this happends on connected test
        // but not when the test is run on its own
        val usernameInput = "ronald"
        val favoriteDishInput = "Butterbeer"
        val allergiesInput = "Snails"
        val bioInput = "I'm just the friend of harry"

        // Set up the test
        composeTestRule.setContent { EditProfileScreen() }

        // Wait for the screen to be loaded
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Clear fields
        composeTestRule.onNodeWithTag(username).performTextClearance()
        composeTestRule.onNodeWithTag(favFood).performTextClearance()
        composeTestRule.onNodeWithTag(bio).performTextClearance()
        composeTestRule.onNodeWithTag(allergies).performTextClearance()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(username)
                .fetchSemanticsNodes().size == 1
        }

        // Set input
        composeTestRule.onNodeWithTag(username).performTextInput(usernameInput)
        composeTestRule.onNodeWithTag(favFood).performTextInput(favoriteDishInput)
        composeTestRule.onNodeWithTag(allergies).performTextInput(allergiesInput)
        composeTestRule.onNodeWithTag(bio).performTextInput(bioInput)

        // Wait ot be completed
        composeTestRule.waitForIdle()

        // Verify that the text fields display the correct values
        composeTestRule.onNodeWithText(usernameInput).assertExists()
        composeTestRule.onNodeWithText(favoriteDishInput).assertExists()
        composeTestRule.onNodeWithText(allergiesInput).assertExists()
        composeTestRule.onNodeWithText(bioInput).assertExists()

        // Click on the save button
        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()
    }

    @Test
    fun editProfileScreenStateTest() {
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent {
            EditProfileScreen(viewModel = profileViewModel)
        }

        profileViewModel.isLoading.value = true

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertExists()

        // Wait to be completed
        profileViewModel.isLoading.value = false

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertDoesNotExist()
    }

    @Test
    fun editProfileScreenCancelButtonTest() {
        var isCancelledClicked = false
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent {
            EditProfileScreen(
                onCancelListener = { isCancelledClicked = true },
                viewModel = profileViewModel
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Check that the cancel button is not clicked
        assert(!isCancelledClicked)

        // Click on the cancel button
        composeTestRule.onNodeWithStringId(R.string.btn_cancel).performClick()

        // Check that the cancel button is clicked
        assert(isCancelledClicked)
    }

    @Test
    fun editProfileScreenSaveButtonIsClicked() {
        var isSaveClicked = false
        val profileViewModel = ProfileViewModel()

        composeTestRule.setContent {
            EditProfileScreen(
                onSuccessListener = { isSaveClicked = true },
                viewModel = profileViewModel
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Check that the cancel button is not clicked
        assert(!isSaveClicked)

        // Click on the cancel button
        composeTestRule.onNodeWithStringId(R.string.btn_save).performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            !profileViewModel.isLoading.value
        }

        // Check that the cancel button is clicked
        assert(isSaveClicked)
    }
}

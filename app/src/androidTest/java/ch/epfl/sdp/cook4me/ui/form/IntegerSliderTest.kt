package ch.epfl.sdp.cook4me.ui.form

// @RunWith(AndroidJUnit4::class)
// class IntegerSliderTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    @Test
//    fun defaultInformationIsDisplayed() {
//        composeTestRule.setContent {
//            IntegerSlider(text = R.string.label, min = 0, max = 10, onValueChange = {})
//        }
//
//        composeTestRule.onNodeWithText("${getStringFromId(R.string.label)}:0").assertIsDisplayed()
//    }

//    @Test
//    fun integerSliderUpdatesValueWhenDragged() {
//        var value = 0
//        composeTestRule.setContent {
//            IntegerSlider(
//                text = R.string.label,
//                min = 0,
//                max = 10,
//                onValueChange = { value = it }
//            )
//        }
//        // Verify initial value
//        composeTestRule.onNodeWithText("${(getStringFromId(R.string.label))}:0").assertExists()
//        // Drag the slider to the right
//        composeTestRule.onNodeWithTag("slider").performTouchInput {
//            swipeRight(
//                startX = 0f,
//                endX = 2000f,
//                durationMillis = 2000
//            )
//        }
//
//        composeTestRule.onNodeWithText("${getStringFromId(R.string.label)}:10").assertExists()
//        assertEquals(10, value)
//    }
//
//    private fun getStringFromId(id: Int) = composeTestRule.activity.getString(id)
// }

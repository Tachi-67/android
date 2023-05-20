package ch.epfl.sdp.cook4me.ui.recipeform

// @RunWith(AndroidJUnit4::class)
// class RecipeCreationScenarioTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//    private fun getString(id: Int): String {
//        return composeTestRule.activity.getString(id)
//    }
//
//    private val mockRecipeRepository = mockk<RecipeRepository>(relaxed = true)
//
//    private val testUri: Uri = Uri.parse(
//        "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.placeholder_tupperware
//    )
//
//    private val expectedRecipe = Recipe(
//        name = "Sad Pizza",
//        ingredients = listOf("flour", "water", "salt"),
//        recipeSteps = listOf("Look at sad ingredients", "sob in bowl to add salt", "mix together", "enjoy"),
//        servings = 1,
//        difficulty = "Hard",
//        cookingTime = "4h00",
//        photos = listOf()
//    )
//
//    private val submitForm = { recipe: Recipe ->
//        assert(recipe == expectedRecipe)
//    }

//    @Test
//    fun validRecipeFormIsCorrectlySubmitted() {
//
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
//                // any composable inside this block will now use our mock ActivityResultRegistry
//                CreateRecipeScreen(repository = mockRecipeRepository)
//            }
//        }
//
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeNameTextFieldDesc))
//            .performTextInput(expectedRecipe.name)
//        composeTestRule.onNodeWithTag("AddImage").performClick()
//        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationServingsTextFieldDesc))
//            .performTextInput(expectedRecipe.servings.toString())
//        composeTestRule.onNodeWithContentDescription(getString(R.string.ingredientsTextFieldContentDesc))
//            .performTextInput(expectedRecipe.ingredients.reduce { x, y -> "$x\n$y" })
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc)).performScrollTo()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc)).performClick()
//        composeTestRule.waitUntilExists(hasText(expectedRecipe.difficulty))
//        composeTestRule.onNodeWithText(expectedRecipe.difficulty).performScrollTo()
//        composeTestRule.onNodeWithText(expectedRecipe.difficulty).performClick()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationCookingTimeDropDownMenuDesc)).performClick()
//        composeTestRule.onNodeWithText(expectedRecipe.cookingTime).performScrollTo()
//        composeTestRule.onNodeWithText(expectedRecipe.cookingTime).performClick()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).performScrollTo()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc))
//            .performTextInput(expectedRecipe.recipeSteps.reduce { x, y -> "$x\n$y" })
//        composeTestRule.onNodeWithText("Done").performClick()
//        verify {
//            runBlocking {
//                mockRecipeRepository.add(
//                    expectedRecipe,
//                    matchListWithoutOrder(testUri)
//                )
//            }
//        }
//        confirmVerified(mockRecipeRepository)
//    }

//    @Test
//    fun ingredientsComposableIsDisplayed() {
//        composeTestRule.setContent {
//            CreateRecipeScreen(repository = mockRecipeRepository)
//        }
//        composeTestRule.onNodeWithStringId(R.string.RecipeCreationIngredientsTitle)
//        composeTestRule.onNodeWithContentDescription(getString(R.string.ingredientsTextFieldContentDesc)).assertIsDisplayed()
//    }

//    @Test
//    fun recipeStepsComposableIsDisplayed() {
//        composeTestRule.setContent {
//            CreateRecipeScreen(repository = mockRecipeRepository)
//        }
//
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).performScrollTo()
//        composeTestRule.onNodeWithStringId(R.string.RecipePreparationTitle).assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeStepsTextFieldDesc)).assertIsDisplayed()
//    }

//    @Test
//    fun recipeNameIsDisplayed() {
//        composeTestRule.setContent {
//            CreateRecipeScreen(repository = mockRecipeRepository)
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.RecipeCreationRecipeTitle).assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeNameTextFieldDesc))
//    }

//    @Test
//    fun servingsComposableIsDisplayed() {
//        composeTestRule.setContent {
//            CreateRecipeScreen(repository = mockRecipeRepository)
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.RecipeCreationScreenServingsTitle).assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationServingsTextFieldDesc))
//    }

//    @Test
//    fun cookingTimeComposableIsDisplayed() {
//        composeTestRule.setContent {
//            CreateRecipeScreen(repository = mockRecipeRepository)
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.RecipeCreationCookingTimeEntryTitle).assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationCookingTimeDropDownMenuDesc))
//    }

//    @Test
//    fun difficultyComposableIsDisplayed() {
//        composeTestRule.setContent {
//            CreateRecipeScreen(repository = mockRecipeRepository)
//        }
//
//        composeTestRule.onNodeWithStringId(R.string.RecipeCreationDifficultyTitle).assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription(getString(R.string.RecipeCreationDifficultyDropDownMenuDesc))
//    }

//    private val registryOwner = object : ActivityResultRegistryOwner {
//        override val activityResultRegistry: ActivityResultRegistry =
//            object : ActivityResultRegistry() {
//                override fun <I : Any?, O : Any?> onLaunch(
//                    requestCode: Int,
//                    contract: ActivityResultContract<I, O>,
//                    input: I,
//                    options: ActivityOptionsCompat?
//                ) {
//                    // don't launch an activity, just respond with the test Uri
//                    val intent = Intent().setData(testUri)
//                    this.dispatchResult(requestCode, Activity.RESULT_OK, intent)
//                }
//            }
//    }
// }

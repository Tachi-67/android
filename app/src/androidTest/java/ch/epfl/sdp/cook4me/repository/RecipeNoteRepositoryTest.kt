package ch.epfl.sdp.cook4me.repository

// private const val COLLECTION_PATH = "recipeNotes"
// private const val USER_VOTE_PATH = "userVotes"
//
// @ExperimentalCoroutinesApi
// class RecipeNoteRepositoryTest {
//    private lateinit var recipeNoteRepository: RecipeNoteRepository
//    private lateinit var store: FirebaseFirestore

//    @Before
//    fun setUp() {
//        store = FirebaseFirestore.getInstance()
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setHost("10.0.2.2:8080") // connect to local firestore emulator
//            .setSslEnabled(false)
//            .setPersistenceEnabled(false)
//            .build()
//        store.firestoreSettings = settings
//        recipeNoteRepository = RecipeNoteRepository(store)
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
//            for (documentSnapshot in querySnapshot.documents) {
//                store.collection(COLLECTION_PATH).document(documentSnapshot.id).delete().await()
//            }
//
//            val userVotesSnapshot = store.collection(USER_VOTE_PATH).get().await()
//            for (documentSnapshot in userVotesSnapshot.documents) {
//                store.collection(USER_VOTE_PATH).document(documentSnapshot.id).delete().await()
//            }
//        }
//    }

//    @Test
//    fun storeNewRecipeNotes() = runTest {
//        recipeNoteRepository.addRecipeNote("recipeId", 1)
//        recipeNoteRepository.addRecipeNote("recipeId2", 2)
//        val allRecipeNotes = recipeNoteRepository.retrieveAllRecipeNotes()
//        assertThat(allRecipeNotes.values, containsInAnyOrder(1, 2))
//        assertThat(allRecipeNotes.keys, containsInAnyOrder("recipeId", "recipeId2"))
//    }

//    @Test
//    fun updateRecipeNoteReturnsTheNewNote() = runTest {
//        recipeNoteRepository.addRecipeNote("recipeId", 1)
//        recipeNoteRepository.addRecipeNote("recipeId2", 2)
//        recipeNoteRepository.updateRecipeNote("recipeId", 3, "a@gmail.com", 0)
//        val allRecipeNotes = recipeNoteRepository.retrieveAllRecipeNotes()
//        assertThat(allRecipeNotes.values, containsInAnyOrder(3, 2))
//    }

//    @Test
//    fun getRecipeNoteReturnNullIfRecipeNoteDoesNotExist() = runTest {
//        recipeNoteRepository.addRecipeNote("recipeId", 1)
//        val recipeNote = recipeNoteRepository.getRecipeNote("recipeId3")
//        assertThat(recipeNote, `is`(nullValue()))
//    }

//    @Test
//    fun getRecipeNoteReturnRecipeNoteIfRecipeNoteExists() = runTest {
//        recipeNoteRepository.addRecipeNote("recipeId", 1)
//        val recipeNote = recipeNoteRepository.getRecipeNote("recipeId")
//        assertThat(recipeNote, `is`(1))
//    }

//    @Test
//    fun addRecipeNoteWithUserCreatesNewUserVote() = runTest {
//        recipeNoteRepository.addRecipeNote("recipeId", 1, "user")
//        val userVote = recipeNoteRepository.retrieveAllUserVotes("user").get("recipeId")
//        assertThat(userVote, `is`(1))
//    }

//    @Test
//    fun updateRecipeNoteWithNotVotedUserCreateNewUserVote() = runTest {
//        recipeNoteRepository.addRecipeNote("recipeId", 1)
//        recipeNoteRepository.updateRecipeNote("recipeId", 2, "user", 1)
//        val userVote = recipeNoteRepository.retrieveAllUserVotes("user")["recipeId"]
//        assertThat(userVote, `is`(1))
//    }

//    @Test
//    fun updateRecipeNoteWithVotedUserUpdatesUserVote() = runTest {
//        // here user clicks on upvote twice => final vote is 0
//        recipeNoteRepository.addRecipeNote("recipeId", 1, "user")
//        recipeNoteRepository.updateRecipeNote("recipeId", 0, "user", -1)
//        val userVote = recipeNoteRepository.retrieveAllUserVotes("user")["recipeId"]
//        assertThat(userVote, `is`(0))
//    }
// }

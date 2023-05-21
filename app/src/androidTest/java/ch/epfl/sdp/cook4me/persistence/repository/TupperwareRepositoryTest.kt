package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.generateTempFiles
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

private const val USER = "user.a@epfl.ch"
private const val PASSWORD = "password_a"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TupperwareRepositoryTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val tupperwareRepository: TupperwareRepository =
        TupperwareRepository(store, storage, auth)

    @Before
    fun setup() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USER, PASSWORD).await()
            auth.signInWithEmailAndPassword(USER, PASSWORD).await()
        }
    }

    @After
    fun cleanup() {
        runBlocking {
            tupperwareRepository.deleteAll()
            auth.signInWithEmailAndPassword(USER, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun storeAndGetNewTupperwareTest() = runTest {
        val files = generateTempFiles(2)
        val ids = tupperwareRepository.addMultipleTestTupperware(files)
        ids.zip(files).forEachIndexed { i, data ->
            val actual = tupperwareRepository.getWithImageById(data.first)
            assertThat(actual, `is`(notNullValue()))
            actual?.let {
                assertThat(it.title, `is`("title$i"))
                assertThat(it.description, `is`("desc$i"))
                assertThat(it.user, `is`(auth.currentUser?.email))
                assertThat(it.image, `is`(data.second.readBytes()))
            }
        }
    }

    @Test
    fun getAllOwnIdsTest() = runTest {
        val files = generateTempFiles(3)
        val expectedIds = tupperwareRepository.addMultipleTestTupperware(files)
        val actualIds = tupperwareRepository.getAllTupperwareIdsAddedByUser(
            auth.currentUser?.email ?: error("shouldn't happen")
        )
        assertThat(actualIds, containsInAnyOrder(*expectedIds.toTypedArray()))
    }

    @Test
    fun getAllOtherIdsTest() = runTest {
        val files = generateTempFiles(3)
        val expectedIds = tupperwareRepository.addMultipleTestTupperware(files)
        val actualIds = tupperwareRepository.getAllTupperwareIdsNotAddedByUser("other.user@epfl.ch")
        assertThat(actualIds, containsInAnyOrder(*expectedIds.toTypedArray()))
    }
}

suspend fun TupperwareRepository.addMultipleTestTupperware(files: List<File>) =
    files.mapIndexed { i, file ->
        add(
            "title$i",
            "desc$i",
            Uri.fromFile(file)
        )
    }

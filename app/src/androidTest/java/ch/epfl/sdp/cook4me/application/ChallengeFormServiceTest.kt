package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import com.google.firebase.firestore.DocumentSnapshot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import java.util.Calendar

class ChallengeFormServiceTest {
    private val mockObjectRepository = mockk<ObjectRepository>(relaxed = true)

    private val challengeFormService = ChallengeFormService(mockObjectRepository)

    @Test
    fun submitValidChallengeStoresChallenge() = runBlocking {
        val dateTime = Calendar.getInstance()
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        val challenge = Challenge(
            name = "name",
            description = "description",
            dateTime = dateTime,
            participants = mapOf("participant1" to 0, "participant2" to 0),
            creator = "creator",
            type = "testType",
        )
        coEvery { mockObjectRepository.add(match { challenge.isValidChallenge }) } returns Unit
        val result = withTimeout(500L) {
            challengeFormService.submitForm(challenge)
        }
        // assert mockObjectRepository.add was called
        coVerify {
            mockObjectRepository.add(match { challenge.isValidChallenge })
        }
        assert(result == null)
    }

    @Test
    fun submitIncompleteChallengeReturnsErrorMessage() = runBlocking {
        val challenge = Challenge()
        val result = withTimeout(500L) {
            challengeFormService.submitForm(challenge)
        }
        // assert mockObjectRepository.add was not called
        coVerify(exactly = 0) {
            mockObjectRepository.add(match { challenge.isValidChallenge })
        }
        assert(result != null)
    }

    @Test
    fun getWithGivenFieldReturnsCorrectChallenges() = runBlocking {
        val field = "name"
        val query = "testName"
        val challenge1 = Challenge(mapOf("name" to "testName", "description" to "testDescription1"))
        val challenge2 = Challenge(mapOf("name" to "testName", "description" to "testDescription2"))

        val documentSnapshot1 = mockk<DocumentSnapshot> {
            every { id } returns "1"
            every { data } returns challenge1.toMap()
        }
        val documentSnapshot2 = mockk<DocumentSnapshot> {
            every { id } returns "2"
            every { data } returns challenge2.toMap()
        }

        coEvery {
            mockObjectRepository.getWithGivenField<Challenge>(field, query)
        } returns listOf(documentSnapshot1, documentSnapshot2)

        val result = withTimeout(500L) {
            challengeFormService.getWithGivenField(field, query)
        }

        assert(result.size == 2)
        assert(result["1"]?.name == challenge1.name)
        assert(result["1"]?.description == challenge1.description)
        assert(result["2"]?.name == challenge2.name)
        assert(result["2"]?.description == challenge2.description)
    }
    @Test
    fun getChallengeWithIdReturnsCorrectChallenge() = runBlocking {
        val id = "1"
        val challenge = Challenge(mapOf("name" to "testName", "description" to "testDescription"))

        val documentSnapshot = mockk<DocumentSnapshot> {
            every { data } returns challenge.toMap()
        }

        coEvery { mockObjectRepository.getWithId<Challenge>(id) } returns documentSnapshot

        val result = withTimeout(500L) {
            challengeFormService.getChallengeWithId(id)
        }

        assert(result?.name == challenge.name)
        assert(result?.description == challenge.description)
    }
}

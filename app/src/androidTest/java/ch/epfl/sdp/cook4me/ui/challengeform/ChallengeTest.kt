package ch.epfl.sdp.cook4me.ui.challengeform

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class ChallengeTest {

    private var challenge = Challenge()

    @Before
    fun init() {
        val dateTime = Calendar.getInstance()
        // to ensure event is in the future
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        challenge = Challenge(
            name = "name",
            description = "description",
            dateTime = dateTime,
            participants = mapOf("participant1" to 0, "participant2" to 0),
            creator = "darth.vader@epfl.ch",
            type = "French"
        )
    }

    @Test
    fun challengeWithValidInformationIsValid() {
        assert(challenge.isValidChallenge)
    }

    @Test
    fun showChallengeInformationDisplaysCorrectInformation() {
        // 01/01/2000 at 00:00
        challenge.dateTime.set(2000, 0, 1, 0, 0)
        var expected = """
            Name: name
            Description: description
            Date: 01/01/2000 at 00:00
            Participants: {participant1=0,participant2=0}
            Type: French
            Creator: darth.vader@epfl.ch
            Latitude-Longitude:(0.0,0.0)"""
        // remove all the spaces
        expected = expected.replace("\\s".toRegex(), "")
        val actual = challenge.challengeInformation.replace("\\s".toRegex(), "")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun challengeWithEmptyNameIsInvalid() {
        challenge = challenge.copy(name = "")
        assert(!challenge.isValidChallenge)
        val errorMsg = "Name is empty"
        Assert.assertEquals(errorMsg, challenge.challengeProblem)
    }

    @Test
    fun challengeWithEmptyDescriptionIsInvalid() {
        challenge = challenge.copy(description = "")
        assert(!challenge.isValidChallenge)
        val errorMsg = "Description is empty"
        Assert.assertEquals(errorMsg, challenge.challengeProblem)
    }

    @Test
    fun challengeWithDateInThePastIsInvalid() {
        val pastDateTime = Calendar.getInstance()
        pastDateTime.add(Calendar.HOUR_OF_DAY, -1)
        challenge = challenge.copy(dateTime = pastDateTime)
        assert(!challenge.isValidChallenge)
        val errorMsg = "Date is in the past"
        Assert.assertEquals(errorMsg, challenge.challengeProblem)
    }

    @Test
    fun addNewParticipantInNonFullEventAddsParticipant() {
        val participant = "participant3"
        val expected = challenge.copy(participants = challenge.participants + (participant to 0))
        val actual = addParticipant(challenge, participant)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun addExistingParticipantInNonFullEventDoesNotAddParticipant() {
        val participant = "participant1"
        val expected = challenge
        val actual = addParticipant(challenge, participant)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun changeScoreOfExistingParticipantUpdatesScore() {
        val participant = "participant1"
        val scoreChange = 5
        val updatedScore = challenge.participants[participant]!! + scoreChange
        val expected = challenge.copy(participants = challenge.participants.toMutableMap().apply { this[participant] = updatedScore })
        val actual = changeParticipantScore(challenge, participant, scoreChange)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun changeScoreOfNonExistingParticipantDoesNotUpdateScore() {
        val participant = "participant3"
        val scoreChange = 5
        val expected = challenge
        val actual = changeParticipantScore(challenge, participant, scoreChange)
        Assert.assertEquals(expected, actual)
    }
}

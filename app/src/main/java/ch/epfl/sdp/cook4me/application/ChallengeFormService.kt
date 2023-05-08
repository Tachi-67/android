package ch.epfl.sdp.cook4me.application

import ch.epfl.sdp.cook4me.persistence.repository.ObjectCollectionRepository
import ch.epfl.sdp.cook4me.persistence.repository.ObjectRepository
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import com.google.firebase.firestore.DocumentSnapshot

private const val CHALLENGE_PATH = "challenges"

class ChallengeFormService(
    private val objectRepository: ObjectRepository = ObjectRepository(objectPath = CHALLENGE_PATH),
    private val objectCollectionRepository: ObjectCollectionRepository =
        ObjectCollectionRepository(objectPath = CHALLENGE_PATH)
) {

    /**
     * Submits the form if it is valid, otherwise returns the error message
     * @param challenge the challenge to submit
     * @return null if the challenge is valid, the error message otherwise
     */
    suspend fun submitForm(challenge: Challenge): String? = if (challenge.isValidChallenge) {
        objectRepository.add(challenge)
        null
    } else {
        challenge.challengeProblem
    }

    /*
    * Retrieves challenge with query name at the given field
    * e.g. getWithGivenField("name", "darth.vadar") will return a map
    *     of challenges with name (challenge attr.) "darth.vadar"
    * map id: the id of the challenge, map value: the challenge object
    * When nothing is found, an empty map is returned
    * */
    suspend fun getWithGivenField(field: String, query: Any): Map<String, Challenge> {
        val result = objectRepository.getWithGivenField<Challenge>(field, query)
        return result.map { it.id to documentSnapshotToChallenge(it) }.toMap()
    }

    /*
    * To get the challenge of given id.
    * If nothing is found, null is returned
    * */
    suspend fun getChallengeWithId(id: String): Challenge? {
        val result = objectRepository.getWithId<Challenge>(id)
        return result?.let { documentSnapshotToChallenge(it) }
    }

    /*
    * Retrieve all challenges in a Map
    * */
    suspend fun retrieveAllChallenges(): Map<String, Challenge> {
        val result = objectCollectionRepository.retrieveAllDocuments<Challenge>()
        return result.map { it.id to documentSnapshotToChallenge(it) }.toMap()
    }

    /*
    * Notes: Firebase could not serialize to java.untl.Calender, I will add an constructor in Challenge.kt
    * to construct an Challenge object from a map.
    * This function is used to convert a document snapshot to an challenge object manually.
    * Also see: Challenge.kt
    * */
    private fun documentSnapshotToChallenge(documentSnapshot: DocumentSnapshot) =
        Challenge(documentSnapshot.data ?: emptyMap())
}

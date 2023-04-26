package ch.epfl.sdp.cook4me.persistence.model

@Suppress("DataClassShouldBeImmutable")
data class Profile(
    var id: String = "",
    var name: String = "",
    var allergies: String = "",
    var bio: String = "",
    var favoriteDish: String = "",
    var userImage: String = "",
    var photos: List<String> = listOf(),
)

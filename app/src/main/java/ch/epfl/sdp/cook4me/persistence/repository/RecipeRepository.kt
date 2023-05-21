package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val COLLECTION_PATH = "recipes"

class RecipeRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) :
    ObjectRepository(store, COLLECTION_PATH) {
    suspend fun add(recipe: Recipe, images: List<Uri> = listOf()) {
        auth.currentUser?.email?.let { email ->
            val recipeId = store.addObjectToCollection(
                recipe.copy(
                    user = email,
                    creationTime = Timestamp.now()
                ),
                COLLECTION_PATH
            )
            val storageRef = storage.reference
            images.forEach { path ->
                val ref =
                    storageRef.child(
                        "/images/$email/recipes/$recipeId/${UUID.randomUUID()}"
                    )
                ref.putFile(path).await()
            }
        }
    }

    suspend fun getAll() = store.getAllObjectsFromCollection<Recipe>(COLLECTION_PATH)

    suspend fun getRecipeByName(name: String): Recipe =
        store.getFirstObjectByFieldValue("name", name, COLLECTION_PATH)

    override suspend fun delete(id: String) {
        super.delete(id)
        auth.currentUser?.email?.let { email ->
            val images = storage.reference
                .child("/images/$email/recipes/$id")
                .listAll()
                .await()
            images.items.forEach {
                it.delete().await()
            }
        }
    }
}

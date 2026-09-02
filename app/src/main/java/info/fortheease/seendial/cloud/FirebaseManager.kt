package info.fortheease.seendial.cloud

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import info.fortheease.seendial.User

class FirebaseManager {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val storage: FirebaseStorage = Firebase.storage

    fun signInAnonymously(onComplete: (Boolean) -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun syncContactsToCloud(users: List<User>) {
        val currentUser = auth.currentUser ?: return
        val batch = firestore.batch()
        
        users.forEach { user ->
            val docRef = firestore.collection("users").document(currentUser.uid)
                                  .collection("contacts").document(user.uid)
            val data = mapOf(
                "phoneNumber" to user.phoneNum,
                "profilePhoto" to user.imgAddress
            )
            batch.set(docRef, data)
        }
        
        batch.commit()
    }
}

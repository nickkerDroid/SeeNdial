package info.fortheease.seendial.caregiver

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CaregiverMode {
    private val firestore: FirebaseFirestore = Firebase.firestore

    fun listenToUserContacts(targetUserId: String, onUpdate: () -> Unit) {
        firestore.collection("users").document(targetUserId)
            .collection("contacts")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    // Update local Room database with remote changes
                    onUpdate()
                }
            }
    }
}

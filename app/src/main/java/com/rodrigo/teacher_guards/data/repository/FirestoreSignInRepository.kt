package com.rodrigo.teacher_guards.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreSignInRepository(private val db : FirebaseFirestore) {

    // Private functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    fun getEmailId(email:String): String {
        return email.split("@")[0]
    }
    // Public functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Add functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Get Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private fun getTeacherDoc(email: String, documentSnapshot: (DocumentSnapshot) -> Unit) {
        val docRef = db.collection("users").document(email)
        docRef.get().addOnSuccessListener { doc ->
            documentSnapshot(doc)
        }
    }
    //// Update Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Delete Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // Private functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // Public functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Add functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Get Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    fun getTeacherRol(
        email: String,
        callback: (String) -> Unit
    ) {
        getTeacherDoc(getEmailId(email)) { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val tal = documentSnapshot.getString("rol").toString()
               callback(tal)
            }
        }
    }

    fun getTeacherSignInData(
        email: String,
        callback: (String, String, String) -> Unit
    ) {
        getTeacherDoc(getEmailId(email)) { documentSnapshot ->
            callback(
                documentSnapshot.getString("name").toString(),
                documentSnapshot.getString("email").toString(),
                documentSnapshot.getString("rol").toString()
            )
        }
    }
    //// Update Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Delete Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -




}
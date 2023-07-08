package com.rodrigo.teacher_guards.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rodrigo.teacher_guards.data.datamodels.Structure.DayGuardModel

class RepositoryStructure {

    val db = Firebase.firestore

    fun addGuardDaysStructure() {
        db.runTransaction { transaction ->
            for (i in 1..5) {
                for (j in 1..6) {
                    val day = "DAY$i"
                    val query = db.collection("guard_schedule").document(day + "_" + j)
                    transaction.set(query, DayGuardModel(day, j))
                }
            }
        }
    }
}
package com.rodrigo.teacher_guards.data.dependencies

import android.content.Context
import android.content.res.Resources
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rodrigo.teacher_guards.data.datamodels.UserPreferences
import com.rodrigo.teacher_guards.data.firebase.FirebaseConfig
import com.rodrigo.teacher_guards.data.repository.FirestoreAdminRepository
import com.rodrigo.teacher_guards.data.repository.FirestoreSignInRepository
import com.rodrigo.teacher_guards.data.repository.FirestoreTeacherRepository
import com.rodrigo.teacher_guards.data.repository.UserPreferencesRepository
import com.rodrigo.teacher_guards.ui.administrator.a_display_absence.A_DisplayAbsenceState
import com.rodrigo.teacher_guards.ui.teacher.t_display_absence.T_DisplayAbsenceState
import kotlinx.coroutines.flow.MutableStateFlow

class Appcontainer(context: Context) {

    private val Context.userDataStore by preferencesDataStore(name = UserPreferences.SETTINGS_FILE)

    private val db = Firebase.firestore

    val appResources: Resources by lazy { context.resources }

    private val _provideFirebaseAuth = FirebaseConfig.provideFirebaseAuth(appResources)
    val provideFirebaseAuth get() = _provideFirebaseAuth

    private val _firestoreAdminRepository = FirestoreAdminRepository(db)
    val firestoreAdminRepository = _firestoreAdminRepository

    private val _firestoreTeacherRepository = FirestoreTeacherRepository(db)
    val firestoreTeacherRepository = _firestoreTeacherRepository

    private val _firestoreSignInRepository = FirestoreSignInRepository(db)
    val firestoreSignInRepository = _firestoreSignInRepository

    private val _userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userDataStore)
    }
    val userPreferencesRepository get() = _userPreferencesRepository

    val teacherStateAbsence: MutableStateFlow<T_DisplayAbsenceState> by lazy {
        MutableStateFlow(T_DisplayAbsenceState())
    }
    val adminStatesAbsence: MutableStateFlow<A_DisplayAbsenceState> by lazy {
        MutableStateFlow(A_DisplayAbsenceState())
    }

}
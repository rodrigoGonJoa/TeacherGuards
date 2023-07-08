package com.rodrigo.teacher_guards.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.rodrigo.teacher_guards.data.datamodels.Structure.DayGuardModel
import com.rodrigo.teacher_guards.data.datamodels.Teacher
import com.rodrigo.teacher_guards.data.datamodels.TeacherGuardModel
import com.rodrigo.teacher_guards.data.datamodels.Structure.TeacherGetSchedule
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.AdminAbsenceListModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherCoveredGuardsModel
import com.rodrigo.teacher_guards.data.datamodels.enums.HourEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum

class FirestoreAdminRepository(private val db: FirebaseFirestore) {

    // Private functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    fun getEmailId(email: String): String {
        return email.split("@")[0]
    }
    //// Add functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun addScheduleDocs(
        schedule: TeacherGetSchedule,
        transaction: Transaction,
        docRef: DocumentReference
    ) {
        val daysOfWeek =
            listOf(schedule.day1, schedule.day2, schedule.day3, schedule.day4, schedule.day5)

        daysOfWeek.forEach { daySchedule ->
            val docRefDay = docRef.collection("schedule").document(daySchedule.day)
            transaction.set(docRefDay, daySchedule)
        }
    }

    //// Get functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun getGuardDays(teacherSchedule: TeacherGetSchedule): List<String> {
        val guardDays = mutableListOf<String>()
        guardDays.addAll(teacherSchedule.day1.getEmptyClasses())
        guardDays.addAll(teacherSchedule.day2.getEmptyClasses())
        guardDays.addAll(teacherSchedule.day3.getEmptyClasses())
        guardDays.addAll(teacherSchedule.day4.getEmptyClasses())
        guardDays.addAll(teacherSchedule.day5.getEmptyClasses())
        return guardDays
    }

    // Public functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Add functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    fun addTeacher(
        teacher: Teacher,
        schedule: TeacherGetSchedule,
        teacherGuardModel: TeacherGuardModel,
        resultAdded: (Boolean) -> Unit,
    ) {
        db.runTransaction { transaction ->
            val teacherDoc = db.collection("users").document(teacher.id)
            transaction.set(teacherDoc, teacher)
            addScheduleDocs(schedule, transaction, teacherDoc)
            addGuardTeacher(getGuardDays(schedule), teacherGuardModel, transaction)

        }.addOnSuccessListener {
            resultAdded(true)
        }.addOnFailureListener {
            resultAdded(false)
        }
    }

    private fun addGuardTeacher(
        guardDays: List<String>,
        teacherGuardModel: TeacherGuardModel,
        transaction: Transaction
    ) {
        for (day in guardDays) {
            val documentRef = db.collection("guard_schedule").document(day)
            transaction.update(documentRef, "users", FieldValue.arrayUnion(teacherGuardModel))
        }
    }


    //// Get Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    fun getTeacher(idTeacher: String, callback: (Teacher) -> Unit) {
        val docRef = db.collection("users").document(idTeacher)
        docRef.get()
            .addOnSuccessListener { doc ->
                doc.toObject(Teacher::class.java)?.let { callback(it) }
            }.addOnFailureListener {
            }
    }

    fun getTeachers(teacherList: (List<Teacher>) -> Unit) {
        val teacherListTemp: MutableList<Teacher> = mutableListOf()
        db.collection("users").get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    teacherListTemp.add(doc.toObject(Teacher::class.java))
                }
                teacherList(teacherListTemp)
            }
    }

    //// Update Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //// Delete Functions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    fun getAbsenceList(date: String, callback: (List<AdminAbsenceListModel>) -> Unit) {
        val listDateFrom: MutableList<AdminAbsenceListModel> = mutableListOf()
        val finalList: MutableList<AdminAbsenceListModel> = mutableListOf()

        db.collection("absence_list")
            .whereLessThanOrEqualTo("date_from", date)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    listDateFrom.add(document.toObject(AdminAbsenceListModel::class.java))
                }
                for (absence in listDateFrom) {
                    if (absence.date_to >= date) {
                        finalList.add(absence)
                    }
                }
                callback(finalList)
            }
    }

    fun getAbsence(idAbsence: String, callback: (TeacherAbsenceModel) -> Unit) {
        db.collection("absences").document(idAbsence).get().addOnSuccessListener { doc ->
            doc.toObject(TeacherAbsenceModel::class.java)?.let { callback(it) }
        }
    }

    fun loadDropTeacherList(day_hour: String, callBack: (List<TeacherGuardModel>) -> Unit) {
        db.collection("guard_schedule").document(day_hour).get().addOnSuccessListener { doc ->
            val fulldoc = doc.toObject(DayGuardModel::class.java)

            Log.d("tag11", fulldoc.toString())
            val teachersList = fulldoc!!.users
            if (teachersList != null) {
                callBack(teachersList)
            }
        }.addOnFailureListener {
            Log.d("tag11", it.message.toString())
        }
    }

    fun saveScore(
        idTeacherGuard: String,
        fullName: String,
        dayAbsentData: AbsentScheduleModel,
        date: String,
        idDay: String,
        callBack: (Boolean) -> Unit
    ) {
        val userGuard = db.collection("users")
            .document(idTeacherGuard)
            .collection("guards")
            .document(date + "_" + dayAbsentData.hour)
        val task = db.collection("tasks")
            .document(date)
            .collection("_$date")
            .document(dayAbsentData.hour.name + "_" + dayAbsentData.course.name)
        val guardSchedule = db.collection("guard_schedule").document(idDay)

        db.runTransaction { transaction ->
            val taskResult = transaction.get(task)
            val guardScheduleResult = transaction.get(guardSchedule)

            updateGuardSchedule(
                transaction,
                idTeacherGuard,
                dayAbsentData.course.score,
                guardSchedule,
                guardScheduleResult
            )
            updateTask(transaction, fullName, task, taskResult)
            val idAbsence = dayAbsentData.hour.name + "_" + dayAbsentData.course.name
            updateUserGuard(
                transaction,
                idAbsence,
                date,
                dayAbsentData.hour,
                userGuard
            )

        }.addOnSuccessListener {
            callBack(true)
        }.addOnFailureListener {
            callBack(false)
            Log.d("tag11", it.message.toString())
        }
    }

    private fun updateUserGuard(
        transaction: Transaction,
        idGuard: String,
        date: String,
        hour: HourEnum,
        userGuard: DocumentReference
    ) {
        val guard = TeacherCoveredGuardsModel(
            date = date,
            weekDay = WeekDayEnum.fromDate(date),
            hour = hour,
            taskRef = idGuard
        )
        transaction.set(userGuard, guard)

    }

    private fun updateTask(
        transaction: Transaction,
        fullName: String,
        task: DocumentReference,
        taskResult: DocumentSnapshot
    ) {
        if (taskResult.exists()) {
            transaction.update(task, "guardTeacherName", fullName)
        }
    }


    private fun updateGuardSchedule(
        transaction: Transaction,
        idTeacherGuard: String,
        score: Int,
        guardSchedule: DocumentReference,
        guardScheduleResult: DocumentSnapshot
    ) {
        if (guardScheduleResult.exists()) {
            val data = guardScheduleResult.data
            val teacherList = data?.get("users") as List<*>
            val finalList: MutableList<TeacherGuardModel> = mutableListOf()
            for (teacher in teacherList) {
                val teacherMap = teacher as HashMap<*, *>
                val teacherGuard = TeacherGuardModel(
                    guard_amount = (teacherMap["guard_amount"] as Long).toInt(),
                    id = teacherMap["id"] as String,
                    full_name = teacherMap["full_name"] as String,
                    score = (teacherMap["score"] as Long).toInt()
                )
                finalList.add(teacherGuard)
            }
            val index = finalList.indexOfFirst { it.id == idTeacherGuard }
            val teacherGuard = finalList[index]
            teacherGuard.score += score
            teacherGuard.guard_amount = teacherGuard.guard_amount + 1
            finalList[index] = teacherGuard
            transaction.update(guardSchedule, "users", finalList)
        }
    }
}


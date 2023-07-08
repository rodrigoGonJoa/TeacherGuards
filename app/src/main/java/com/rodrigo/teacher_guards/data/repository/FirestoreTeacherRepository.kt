package com.rodrigo.teacher_guards.data.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.rodrigo.teacher_guards.data.datamodels.Structure.TeacherGetSchedule
import com.rodrigo.teacher_guards.data.datamodels.Structure.TeacherGetScheduleDay
import com.rodrigo.teacher_guards.data.datamodels.Teacher
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.AdminAbsenceListModel
import com.rodrigo.teacher_guards.data.datamodels.DayAbsentModel
import com.rodrigo.teacher_guards.data.datamodels.TaskModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceListModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherCoveredGuardsModel
import com.rodrigo.teacher_guards.data.datamodels.enums.CourseEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.HourEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.SubjectEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FirestoreTeacherRepository(private val db: FirebaseFirestore) {

    fun getEmailId(email: String): String {
        return email.split("@")[0]
    }

    fun getTeacherDoc(email: String, documentSnapshot: (DocumentSnapshot) -> Unit) {
        val docRef = db.collection("users").document(getEmailId(email))
        docRef.get().addOnSuccessListener { doc ->
            documentSnapshot(doc)
        }
    }

    fun getTeacherData(idUser: String, teacher: (Teacher) -> Unit) {
        getTeacherDoc(idUser) { doc ->
            teacher(
                Teacher(
                    id = doc.getString("id")!!,
                    name = doc.getString("name")!!,
                    surname = doc.getString("surname")!!,
                    email = doc.getString("email")!!,
                    asistanceStatus = doc.getBoolean("asistanceStatus")!!
                )
            )
        }
    }
/////////////////// CREATE ABSENCE /////////////////


    @RequiresApi(Build.VERSION_CODES.O)
    fun createTasks(
        idUser: String,
        dateFrom: String,
        dateTo: String,
        context: Context,
        callBack: (TeacherAbsenceModel) -> Unit
    ) {
        val teacherAbsenceModel = runBlocking {
            val weekDayList = Utils.obtenerDiasSemanaEntreFechas(context, dateFrom, dateTo)
            val dayAbsentModelList = createDayAbsentList(weekDayList, dateFrom, context, idUser)

            TeacherAbsenceModel(
                date_from = dateFrom,
                date_to = dateTo,
                days_absent = dayAbsentModelList as MutableList<DayAbsentModel>
            )
        }
        callBack(teacherAbsenceModel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAbsentSchedule(
        day: String,
        dayAbsentModel: DayAbsentModel,
        teacherGetSchedule: TeacherGetSchedule,
        context: Context
    ): List<AbsentScheduleModel> {
        val absentScheduleModelList = mutableListOf<AbsentScheduleModel>()

        for (hour in 1..6) {
            val absentScheduleModel = AbsentScheduleModel(
                hour = HourEnum.toHourEnum(hour),
                absent = false,
                course = CourseEnum.valueOf(
                    teacherGetSchedule.obtenerDiaPorNombre(day, context)!!.obtenerClase(hour)
                ),
                taskRef = "",
                subject = SubjectEnum.valueOf(
                    teacherGetSchedule.obtenerDiaPorNombre(day, context)!!.obtenerSubject(hour)
                )
            )
            if (absentScheduleModel.subject.toString() != SubjectEnum.ERROR.toString()) {
                absentScheduleModel.taskRef =
                    AbsentScheduleModel.setTaskRef(dayAbsentModel, absentScheduleModel)
                absentScheduleModelList.add(absentScheduleModel)
            }
        }
        return absentScheduleModelList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun createDayAbsentList(
        list: List<String>,
        dateFrom: String,
        context: Context,
        idUser: String
    ): List<DayAbsentModel> {
        val dayAbsentModelList = mutableListOf<DayAbsentModel>()
        val teacherGetSchedule = getTeacherSchedule(idUser)

        for ((order, day) in list.withIndex()) {
            if (day != WeekDayEnum.ERROR.toString()) {
                val dayAbsentModel = DayAbsentModel(
                    order = order,
                    date = getDate(dateFrom, order),
                    weekDay = WeekDayEnum.valueOf(day),
                    absentSchedule = emptyList()
                )
                dayAbsentModel.absentSchedule =
                    createAbsentSchedule(day, dayAbsentModel, teacherGetSchedule, context)
                dayAbsentModelList.add(dayAbsentModel)
            }
        }
        return dayAbsentModelList
    }

    private suspend fun getTeacherSchedule(idUser: String): TeacherGetSchedule {
        val doc = db.collection("users").document(idUser).collection("schedule").get().await()
        val teacherGetSchedule = TeacherGetSchedule(
            day1 = TeacherGetScheduleDay(),
            day2 = TeacherGetScheduleDay(),
            day3 = TeacherGetScheduleDay(),
            day4 = TeacherGetScheduleDay(),
            day5 = TeacherGetScheduleDay()
        )

        for (daySnapshot in doc) {
            val day = daySnapshot.toObject(TeacherGetScheduleDay::class.java)
            when (daySnapshot.id) {
                WeekDayEnum.DAY1.toString() -> teacherGetSchedule.day1 = day
                WeekDayEnum.DAY2.toString() -> teacherGetSchedule.day2 = day
                WeekDayEnum.DAY3.toString() -> teacherGetSchedule.day3 = day
                WeekDayEnum.DAY4.toString() -> teacherGetSchedule.day4 = day
                WeekDayEnum.DAY5.toString() -> teacherGetSchedule.day5 = day
            }
        }
        return teacherGetSchedule
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(fecha: String, dias: Int): String {
        val formatoFecha = DateTimeFormatter.ofPattern("yyyyMMdd")
        val fechaLocal = LocalDate.parse(fecha, formatoFecha)
        val nuevaFecha = fechaLocal.plusDays(dias.toLong())
        return nuevaFecha.format(formatoFecha)
    }


    ////////////////////////////////////

    fun addAbsence(
        absence: TeacherAbsenceModel?,
        taskModelList: List<TaskModel>,
        teacher_id: String,
        callback: (Boolean) -> Unit

    ) {
        if (absence != null) {
            val docName = absence.date_from + "_" + absence.date_to + "_" + teacher_id
            val doc = db.collection("absences").document(docName)
            val docTeacherName = absence.date_from + "_" + absence.date_to
            val docTeacher = db.collection("users").document(teacher_id).collection("absences")
                .document(docTeacherName)
            val docAbsenceList = db.collection("absence_list").document(docName)
            db.runTransaction { transaction ->

                val teacherAbsenceListModel = TeacherAbsenceListModel(
                    dateFrom = absence.date_from,
                    dateTo = absence.date_to,
                    weekDay = WeekDayEnum.fromDate(absence.date_from),
                    absence_ref = docName
                )

                val absenceListModel = AdminAbsenceListModel(
                    date_from = absence.date_from,
                    date_to = absence.date_to,
                    absent_teacher_id = teacher_id,
                    absence_id = docName,
                    teacher_name = taskModelList[0].absentTeacherName,
                    teacher_surname = taskModelList[0].absentTeacherSurname
                )

                transaction.set(docAbsenceList, absenceListModel)
                transaction.set(doc, absence)
                transaction.set(docTeacher, teacherAbsenceListModel)

                for (task in taskModelList) {
                    val colName = "_" + task.date
                    val docTaskName = task.hour.name + "_" + task.course.name
                    val dayDocElement: HashMap<String, Any?> = hashMapOf("date" to task.date)
                    val docDate = db.collection("tasks").document(task.date)
                    val docTask = db.collection("tasks").document(task.date).collection(colName)
                        .document(docTaskName)
                    transaction.set(docDate, dayDocElement)
                    transaction.set(docTask, task)
                }
            }.addOnSuccessListener {
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }
        }
    }

    fun getTeacherAbsenceList(
        teacher_id: String,
        callBack: (List<TeacherAbsenceListModel>) -> Unit
    ) {
        val list: MutableList<TeacherAbsenceListModel> = mutableListOf()
        val col = db.collection("users").document(teacher_id).collection("absences")

        col.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                for (absences in querySnapshot) {
                    list.add(absences.toObject(TeacherAbsenceListModel::class.java))
                }
            }
            callBack(list)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getGuardDays(
        teacher_id: String,
        callback: (List<String>) -> Unit,
    ) {
        var guardList = listOf<String>()
        val col = db.collection("users").document(teacher_id).collection("schedule").document(
            WeekDayEnum.fromDate(Utils.getToday()).toString()
        )

        col.get().addOnSuccessListener { querySnapshot ->
            val guardsHours = querySnapshot
                .toObject(TeacherGetScheduleDay::class.java)
                ?.getEmptyClasses()
            if (guardsHours != null) {
                guardList = guardsHours
            }
        }
    }

    fun getTeacherGuards(idTeacher: String, callback: (List<TeacherCoveredGuardsModel>) -> Unit) {
        val guardsDoc = db.collection("users")
            .document(idTeacher)
            .collection("guards").get()
        guardsDoc.addOnSuccessListener { docs ->
            val list: MutableList<TeacherCoveredGuardsModel> = mutableListOf()
            for (doc in docs) {
                list.add(doc.toObject(TeacherCoveredGuardsModel::class.java))
            }
            callback(list)
        }.addOnFailureListener {
            Log.d("tag11", it.message.toString())
        }
    }

    fun getGuard(date: String, idGuard: String, callback: (TaskModel) -> Unit) {
        val guardDoc = db.collection("tasks")
            .document(date).collection("_$date").document(idGuard)
        guardDoc.get().addOnSuccessListener { doc ->
            callback(doc.toObject(TaskModel::class.java)!!)
        }
    }

}
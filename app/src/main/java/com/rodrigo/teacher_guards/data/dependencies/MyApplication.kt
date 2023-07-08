package com.rodrigo.teacher_guards.data.dependencies

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application(){
    private lateinit var _appContainer : Appcontainer
    val appcontainer get() = _appContainer

    override fun onCreate() {
        super.onCreate()
        _appContainer = Appcontainer(this)
    }
}
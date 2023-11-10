package com.daviddj.proyecto_final_djl

import android.app.Application
import com.daviddj.proyecto_final_djl.data.AppContainer
import com.daviddj.proyecto_final_djl.data.AppDataContainer

class NotasApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
package com.senac.smartprice

import android.app.Application
import com.senac.smartprice.util.Preferencias

class SmartPriceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Preferencias.aplicarTemaSalvo(this)
    }
}

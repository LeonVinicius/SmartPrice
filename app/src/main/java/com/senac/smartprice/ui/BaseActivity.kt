package com.senac.smartprice.ui

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.senac.smartprice.util.Preferencias

/** Todas as telas herdam desta classe para respeitar o tamanho de fonte escolhido no botão "Aa". */
abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val config = Configuration(newBase.resources.configuration)
        config.fontScale = Preferencias.escalaFonte(newBase)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }
}

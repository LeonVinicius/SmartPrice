package com.senac.smartprice.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

/** Guarda tema, tamanho da fonte e usuário logado (apenas o id). */
object Preferencias {
    private const val ARQUIVO = "smartprice_prefs"
    private fun prefs(c: Context) = c.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE)

    fun aplicarTemaSalvo(c: Context) {
        val modo = prefs(c).getInt("tema", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(modo)
    }

    fun alternarTema(c: Context) {
        val escuro = (c.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES
        val novo = if (escuro) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        prefs(c).edit().putInt("tema", novo).apply()
        AppCompatDelegate.setDefaultNightMode(novo)   // recria a tela automaticamente
    }

    fun escalaFonte(c: Context): Float = prefs(c).getFloat("escala_fonte", 1.0f)
    fun salvarEscala(c: Context, escala: Float) = prefs(c).edit().putFloat("escala_fonte", escala).apply()

    fun usuarioId(c: Context): Long = prefs(c).getLong("usuario_id", -1L)
    fun salvarUsuario(c: Context, id: Long) = prefs(c).edit().putLong("usuario_id", id).apply()
}

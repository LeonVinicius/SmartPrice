package com.senac.smartprice.ui

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senac.smartprice.R
import com.senac.smartprice.util.Preferencias

/** Componente de acessibilidade (nº 10 do mockup): tema claro/escuro e tamanho da fonte. */
object AcessibilidadeDialog {
    fun mostrar(activity: Activity) {
        val itens = arrayOf(
            activity.getString(R.string.acc_alternar_tema),
            activity.getString(R.string.acc_fonte_normal),
            activity.getString(R.string.acc_fonte_grande),
            activity.getString(R.string.acc_fonte_extra)
        )
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.acc_titulo)
            .setItems(itens) { _, opcao ->
                when (opcao) {
                    0 -> Preferencias.alternarTema(activity)
                    1 -> { Preferencias.salvarEscala(activity, 1.0f); activity.recreate() }
                    2 -> { Preferencias.salvarEscala(activity, 1.15f); activity.recreate() }
                    3 -> { Preferencias.salvarEscala(activity, 1.3f); activity.recreate() }
                }
            }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }
}

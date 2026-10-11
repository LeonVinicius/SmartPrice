package com.senac.smartprice.ui

import android.os.Bundle
import android.widget.TextView
import com.senac.smartprice.R

/** PROVISÓRIA: só confirma que o login funcionou. Será substituída pela tela de Consulta (RecyclerView). */
class ConsultaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = getString(R.string.consulta_provisoria)
            textSize = 20f
            setPadding(48, 96, 48, 48)
        })
    }
}

package com.senac.smartprice.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.senac.smartprice.R
import com.senac.smartprice.data.AppDatabase
import com.senac.smartprice.data.UsuarioRepository
import com.senac.smartprice.databinding.ActivityCadastroBinding
import kotlinx.coroutines.launch

/** Controller da tela de Cadastro. */
class CadastroActivity : BaseActivity() {

    companion object { const val EXTRA_EMAIL = "extra_email" }

    private lateinit var b: ActivityCadastroBinding
    private lateinit var repo: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(b.root)
        repo = UsuarioRepository(AppDatabase.obter(this).usuarioDao())

        // Spinner de bairros (lista pré-definida em res/values/strings.xml)
        ArrayAdapter.createFromResource(this, R.array.bairros, android.R.layout.simple_spinner_item)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            .also { b.spBairro.adapter = it }

        b.btnAcessibilidade.setOnClickListener { AcessibilidadeDialog.mostrar(this) }
        b.btnVoltar.setOnClickListener { finish() }
        b.btnCadastrar.setOnClickListener { cadastrar() }

        b.etNome.doAfterTextChanged { b.tilNome.error = null }
        b.etEmail.doAfterTextChanged { b.tilEmail.error = null }
        b.etSenha.doAfterTextChanged { b.tilSenha.error = null }
    }

    private fun perfilSelecionado() =
        if (b.rgPerfil.checkedRadioButtonId == R.id.rbComerciante) "COMERCIANTE" else "CONSUMIDOR"

    /** Valida todos os campos obrigatórios; devolve true se estiver tudo certo. */
    private fun validar(nome: String, email: String, senha: String): Boolean {
        var ok = true
        if (nome.length < 3) { b.tilNome.error = getString(R.string.erro_nome); ok = false }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            b.tilEmail.error = getString(R.string.erro_email_invalido); ok = false
        }
        if (senha.length < 6) { b.tilSenha.error = getString(R.string.erro_senha_curta); ok = false }
        if (b.spBairro.selectedItemPosition == 0) {
            Toast.makeText(this, R.string.toast_escolha_bairro, Toast.LENGTH_SHORT).show(); ok = false
        } else if (!b.cbTermos.isChecked) {
            Toast.makeText(this, R.string.toast_aceite_termos, Toast.LENGTH_SHORT).show(); ok = false
        }
        if (!ok && b.spBairro.selectedItemPosition != 0 && b.cbTermos.isChecked) {
            Toast.makeText(this, R.string.toast_preencha_campos, Toast.LENGTH_SHORT).show()
        }
        return ok
    }

    private fun cadastrar() {
        val nome = b.etNome.text.toString().trim()
        val email = b.etEmail.text.toString().trim().lowercase()
        val senha = b.etSenha.text.toString()
        if (!validar(nome, email, senha)) return

        b.btnCadastrar.isEnabled = false
        lifecycleScope.launch {
            val criado = repo.cadastrar(
                nome, email, senha, perfilSelecionado(), b.spBairro.selectedItem.toString()
            )
            b.btnCadastrar.isEnabled = true
            if (!criado) {
                b.tilEmail.error = getString(R.string.erro_email_em_uso)
                Toast.makeText(this@CadastroActivity, R.string.erro_email_em_uso, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@CadastroActivity, R.string.toast_conta_criada, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent().putExtra(EXTRA_EMAIL, email))
                finish()
            }
        }
    }
}

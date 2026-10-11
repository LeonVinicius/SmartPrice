package com.senac.smartprice.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.senac.smartprice.R
import com.senac.smartprice.data.AppDatabase
import com.senac.smartprice.data.UsuarioRepository
import com.senac.smartprice.databinding.ActivityLoginBinding
import com.senac.smartprice.util.Preferencias
import kotlinx.coroutines.launch

/** Controller da tela de Login. */
class LoginActivity : BaseActivity() {

    private lateinit var b: ActivityLoginBinding
    private lateinit var repo: UsuarioRepository

    // Recebe o e-mail de volta do Cadastro e já preenche o campo
    private val cadastroLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { r ->
            if (r.resultCode == RESULT_OK) {
                b.etEmail.setText(r.data?.getStringExtra(CadastroActivity.EXTRA_EMAIL))
                b.etSenha.requestFocus()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        repo = UsuarioRepository(AppDatabase.obter(this).usuarioDao())

        b.btnAcessibilidade.setOnClickListener { AcessibilidadeDialog.mostrar(this) }
        b.btnEntrar.setOnClickListener { entrar() }
        b.btnCriarConta.setOnClickListener {
            cadastroLauncher.launch(Intent(this, CadastroActivity::class.java))
        }
        b.etEmail.doAfterTextChanged { b.tilEmail.error = null }
        b.etSenha.doAfterTextChanged { b.tilSenha.error = null }
    }

    private fun entrar() {
        val email = b.etEmail.text.toString().trim().lowercase()
        val senha = b.etSenha.text.toString()

        var valido = true
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            b.tilEmail.error = getString(R.string.erro_email_invalido); valido = false
        }
        if (senha.isEmpty()) {
            b.tilSenha.error = getString(R.string.erro_senha_vazia); valido = false
        }
        if (!valido) {
            Toast.makeText(this, R.string.toast_preencha_campos, Toast.LENGTH_SHORT).show()
            return
        }

        b.btnEntrar.isEnabled = false   // evita toques repetidos enquanto valida
        lifecycleScope.launch {
            val usuario = repo.autenticar(email, senha)
            b.btnEntrar.isEnabled = true
            if (usuario == null) {
                Toast.makeText(this@LoginActivity, R.string.toast_login_incorreto, Toast.LENGTH_LONG).show()
            } else {
                Preferencias.salvarUsuario(this@LoginActivity, usuario.id)
                Toast.makeText(
                    this@LoginActivity, getString(R.string.toast_bem_vindo, usuario.nome), Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@LoginActivity, ConsultaActivity::class.java))
                finish()
            }
        }
    }
}

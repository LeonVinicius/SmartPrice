package com.senac.smartprice.data

import com.senac.smartprice.util.Seguranca

/**
 * Camada Model do MVC. Hoje usa o Room (local).
 * Na Etapa 3, as funções passam a chamar a API via Retrofit e o Room vira cache,
 * sem mudar as Activities.
 */
class UsuarioRepository(private val dao: UsuarioDao) {

    /** Retorna false se o e-mail já estiver cadastrado. */
    suspend fun cadastrar(
        nome: String, email: String, senha: String, perfil: String, bairro: String
    ): Boolean {
        if (dao.buscarPorEmail(email) != null) return false
        val salt = Seguranca.gerarSalt()
        dao.inserir(
            Usuario(
                nome = nome, email = email, perfil = perfil, bairro = bairro,
                senhaHash = Seguranca.hash(senha, salt), salt = salt
            )
        )
        return true
    }

    /** Retorna o usuário se e-mail e senha conferem, ou null. */
    suspend fun autenticar(email: String, senha: String): Usuario? {
        val usuario = dao.buscarPorEmail(email) ?: return null
        return if (Seguranca.hash(senha, usuario.salt) == usuario.senhaHash) usuario else null
    }
}

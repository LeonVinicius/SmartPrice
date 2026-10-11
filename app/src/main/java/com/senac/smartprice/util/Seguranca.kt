package com.senac.smartprice.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

/** A senha nunca é guardada em texto puro: salva-se hash + salt. */
object Seguranca {
    fun gerarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun hash(senha: String, salt: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest((salt + senha).toByteArray())
            .joinToString("") { "%02x".format(it) }
}

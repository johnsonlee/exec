package io.johnsonlee.kx.launcher

import java.security.MessageDigest

fun String.md5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
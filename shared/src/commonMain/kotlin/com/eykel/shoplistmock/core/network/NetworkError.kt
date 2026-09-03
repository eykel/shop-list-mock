package com.eykel.shoplistmock.core.network

/**
 * Typed transport failures, so a repository can map them to user copy instead of switching
 * on exception classes from whatever HTTP engine happens to be plugged in (real or mock).
 */
sealed class NetworkError(message: String) : Exception(message) {
    data object Timeout : NetworkError("A requisicao demorou demais para responder.")
    data object NoConnection : NetworkError("Sem conexao com a internet.")
    data class NotFound(val resourceId: String) : NetworkError("Recurso nao encontrado: $resourceId")
    data class Server(val code: Int) : NetworkError("O servidor respondeu com erro $code.")
    data class Unknown(val original: Throwable) : NetworkError("Erro inesperado: ${original.message}")
}

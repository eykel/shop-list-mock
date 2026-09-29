package com.eykel.shoplistmock.core.result

/**
 * Result wrapper every repository and use case returns, so failures travel as data instead
 * of exceptions crossing layers. Screens consume it with [onSuccess] / [onFailure].
 */
data class ModelResult<out T>(
    val status: Status,
    val data: T?,
    val throwable: Throwable?
) {
    enum class Status { SUCCESS, LOADING, ERROR }

    companion object {
        fun <T> success(data: T): ModelResult<T> = ModelResult(Status.SUCCESS, data, null)

        fun <T> loading(): ModelResult<T> = ModelResult(Status.LOADING, null, null)

        fun <T> error(throwable: Throwable): ModelResult<T> = ModelResult(Status.ERROR, null, throwable)
    }
}

inline fun <T> ModelResult<T>.onSuccess(action: (T) -> Unit): ModelResult<T> {
    if (status == ModelResult.Status.SUCCESS) data?.let(action)
    return this
}

inline fun <T> ModelResult<T>.onFailure(action: (Throwable) -> Unit): ModelResult<T> {
    if (status == ModelResult.Status.ERROR) throwable?.let(action)
    return this
}

/** Runs [block] and wraps whatever it returns (or throws) into a [ModelResult]. */
inline fun <T> resultOf(block: () -> T): ModelResult<T> =
    try {
        ModelResult.success(block())
    } catch (t: Throwable) {
        ModelResult.error(t)
    }

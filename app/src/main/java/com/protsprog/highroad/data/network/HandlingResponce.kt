package com.protsprog.highroad.data.network

/*
TO READ
https://medium.com/@andi.kitta77/better-way-for-error-handling-http-or-api-response-in-jetpack-compose-andandroid-views-xml-66c3e088ec26
 */
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.io.IOException

sealed interface SallyResponseResource<out T> {
    data class Success<T>(val data: T) : SallyResponseResource<T>
    data class Error(val exception: AppException, val errorCode: String? = null) :
        SallyResponseResource<Nothing>

    data class Loading(val status: Boolean) : SallyResponseResource<Nothing>
}

open class AppException(message: String? = null, cause: Throwable? = null) :
    Throwable(message, cause)

class NetworkException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class ServerException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class ClientException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class UnknownException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

suspend fun <T> asSallyResponseResourceSuspend(apiCall: suspend () -> T): SallyResponseResource<T> {
    return try {
        SallyResponseResource.Loading(true)
        val response = apiCall.invoke()
        SallyResponseResource.Success(response)
    } catch (error: Throwable) {
        val exception = when (error) {
            is HttpException -> {
                when (error.code()) {
                    in 400..499 ->
                        ClientException(
                            message = "${TRANSMITION_ERRORS.CLIENT_ERROR.message}: ${error.code()}",
                            cause = error
                        )

                    in 500..599 ->
                        ServerException(
                            message = "${TRANSMITION_ERRORS.SERVER_ERROR.message}: ${error.code()}",
                            cause = error
                        )

                    else -> UnknownException(
                        message = "${TRANSMITION_ERRORS.HTTP_UNKNOWN_ERROR.message}: ${error.code()}",
                        cause = error
                    )
                }
            }

            is IOException -> NetworkException(
                message = TRANSMITION_ERRORS.NETWORK_ERROR.message,
                cause = error
            )

            else -> AppException(
                message = TRANSMITION_ERRORS.UNKNOWN_ERROR.message,
                cause = error
            )
        }

        val errorCode = when (error) {
            is HttpException -> {
                when (error.code()) {
                    in 400..499 -> {
                        "#ER${error.code()}"
                    }

                    in 500..599 -> {
                        "#ER${error.code()}"
                    }

                    else -> {
                        "#ER${error.code()}"
                    }
                }
            }

            else -> {
                error.cause?.message.toString()
            }
        }

        SallyResponseResource.Error(exception, errorCode)
    } finally {
        SallyResponseResource.Loading(false)
    }
}

fun <T> Flow<T>.asSallyResponseResourceFlow(): Flow<SallyResponseResource<T>> {
    return this
        .map<T, SallyResponseResource<T>> {
            SallyResponseResource.Success(it)
        }
        .onStart { emit(SallyResponseResource.Loading(true)) }
        .onCompletion { emit(SallyResponseResource.Loading(false)) }
        .catch { error ->
            val exception = when (error) {
                is HttpException -> {
                    when (error.code()) {
                        in 400..499 -> {
                            ClientException(
                                message = "${TRANSMITION_ERRORS.CLIENT_ERROR.message}: ${error.code()}",
                                cause = error,
                            )
                        }

                        in 500..599 -> {
                            ServerException(
                                message = "${TRANSMITION_ERRORS.SERVER_ERROR.message}: ${error.code()}",
                                cause = error
                            )
                        }

                        else -> {
                            UnknownException(
                                message = "${TRANSMITION_ERRORS.HTTP_UNKNOWN_ERROR.message}: ${error.code()}",
                                cause = error
                            )
                        }
                    }
                }

                is IOException -> NetworkException(
                    message = TRANSMITION_ERRORS.NETWORK_ERROR.message,
                    cause = error
                )

                else -> AppException(
                    message = TRANSMITION_ERRORS.UNKNOWN_ERROR.message,
                    cause = error
                )
            }

            val errorCode = when (error) {
                is HttpException -> {
                    when (error.code()) {
                        in 400..499 -> {
                            "#ER${error.code()}"
                        }

                        in 500..599 -> {
                            "#ER${error.code()}"
                        }

                        else -> {
                            "#ER${error.code()}"
                        }
                    }
                }

                else -> {
                    error.cause?.message.toString()
                }
            }
            emit(SallyResponseResource.Error(exception, errorCode))
        }
}

enum class TRANSMITION_ERRORS(val message:String) {
    CLIENT_ERROR("Terjadi kesalahan, mohon periksa masukan anda"),
    SERVER_ERROR("Terjadi kesalahan pada Server, coba lagi nanti"),
    NETWORK_ERROR("Koneksi internet bermasalah, coba lagi nanti"),
    HTTP_UNKNOWN_ERROR("HTTP Error tidak diketahui (exc: 4xx/5xx)"),
    UNKNOWN_ERROR("Error tidak diketahui")
}

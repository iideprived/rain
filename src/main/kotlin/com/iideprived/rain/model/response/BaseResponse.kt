package com.iideprived.rain.model.response

import kotlinx.serialization.Serializable
import java.lang.reflect.InvocationTargetException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
abstract class BaseResponse<T : BaseResponse<T>> {

    // Properties
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    var statusCode: Int = 0
        private set
    var statusMessage: String? = null
        private set
    var errorCode: String? = null
        private set
    var errorMessage: String? = null
        private set


    init {
        this.asSuccess()
    }

    // Instance Methods
    @Suppress("UNCHECKED_CAST")
    fun asSuccess(): T {
        this.statusCode = 200
        this.statusMessage = DEFAULT_SUCCESS_STATUS_MESSAGE
        this.errorCode = null
        this.errorMessage = null
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun asFailure(e: Exception, errorCode: String? = DEFAULT_ERROR_CODE, statusCode: Int = DEFAULT_FAILURE_STATUS_CODE): T {
        this.statusCode = statusCode
        this.statusMessage = DEFAULT_FAILURE_STATUS_MESSAGE
        this.errorCode = errorCode
        this.errorMessage = e.message
        return this as T
    }

    // Companion Object

    companion object {
        const val DEFAULT_FAILURE_STATUS_CODE: Int = 400
        const val DEFAULT_FAILURE_STATUS_MESSAGE: String = "failure"
        const val DEFAULT_SUCCESS_STATUS_MESSAGE: String = "success"
        const val DEFAULT_ERROR_CODE: String = "ERR-01"

        // Static Methods

        inline fun <reified T : BaseResponse<T>> createInstance() : T {
            val clazz = T::class.java
            return try {
                clazz.getDeclaredConstructor().newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException("Failed to create instance of ${clazz.simpleName}", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Failed to access constructor of ${clazz.simpleName}", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("Constructor threw an exception for ${clazz.simpleName}", e)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException("No suitable constructor found for ${clazz.simpleName}", e)
            }
        }

        @Suppress("unused")
        inline fun <reified T : BaseResponse<T>> success(): T {
            return createInstance<T>().asSuccess()
        }

        inline fun <reified T : BaseResponse<T>> failure(e: Exception, errorCode: String? = DEFAULT_ERROR_CODE, statusCode: Int = DEFAULT_FAILURE_STATUS_CODE): T {
            return createInstance<T>().asFailure(e, errorCode, statusCode)
        }
    }
}

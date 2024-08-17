package com.iideprived.rain.model.response

import kotlinx.serialization.Serializable
import java.lang.reflect.InvocationTargetException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
abstract class BaseResponse<T : BaseResponse<T>> {

    // Properties
    private val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    private var statusCode: Int = 0
    private var statusMessage: String? = null
    private var errorCode: String? = null
    private var errorMessage: String? = null

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

        @Suppress("unused")
        fun success(): GenericResponse = success(GenericResponse::class.java)
        fun failure(e: Exception, errorCode: String? = DEFAULT_ERROR_CODE, statusCode: Int = DEFAULT_FAILURE_STATUS_CODE): GenericResponse = failure(e, GenericResponse::class.java, errorCode, statusCode)

        fun <T : BaseResponse<T>> success(clazz: Class<T>): T {
            return try {
                val instance = clazz.getDeclaredConstructor().newInstance()
                instance.asSuccess()
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

        fun <T : BaseResponse<T>> failure(e: Exception, clazz: Class<T>, errorCode: String? = DEFAULT_ERROR_CODE, statusCode: Int = DEFAULT_FAILURE_STATUS_CODE): T {
            return try {
                val instance = clazz.getDeclaredConstructor().newInstance()
                instance.asFailure(e, errorCode, statusCode)
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
    }
}

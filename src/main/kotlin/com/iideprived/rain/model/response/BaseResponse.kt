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
        this.statusMessage = "Success"
        this.errorCode = null
        this.errorMessage = null
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun asFailure(e: Exception, errorCode: String? = null): T {
        this.statusCode = 400
        this.statusMessage = "Failure"
        this.errorCode = errorCode
        this.errorMessage = e.message
        return this as T
    }

    // Companion Object

    companion object {
        // Static Methods

        fun success(): GenericResponse = success(GenericResponse::class.java)
        fun failure(e: Exception, errorCode: String? = null): GenericResponse = failure(e, GenericResponse::class.java, errorCode)

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

        fun <T : BaseResponse<T>> failure(e: Exception, clazz: Class<T>, errorCode: String? = null): T {
            return try {
                val instance = clazz.getDeclaredConstructor().newInstance()
                instance.asFailure(e, errorCode)
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

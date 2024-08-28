package com.iideprived.rain.util

import com.iideprived.rain.model.response.BaseResponse
import io.github.classgraph.MethodInfo
import io.github.classgraph.MethodParameterInfo
import io.github.classgraph.TypeSignature
import io.ktor.util.reflect.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf

// Utility function to convert path parameter to the specified type
internal fun String?.convertByString(typeName: String): Any? {
    return try {
        when (typeName) {
            String::class.qualifiedName, String::class.simpleName -> this
            Int::class.qualifiedName, Int::class.simpleName, "int" -> this?.toInt()
            Long::class.qualifiedName, Long::class.simpleName, "long" -> this?.toLong()
            Double::class.qualifiedName, Double::class.simpleName, "double" -> this?.toDouble()
            Float::class.qualifiedName, Float::class.simpleName, "float" -> this?.toFloat()
            Boolean::class.qualifiedName, Boolean::class.simpleName, "boolean" -> this.toBoolean()
            Byte::class.qualifiedName, Byte::class.simpleName, "byte" -> this?.toByte()
            Short::class.qualifiedName, Short::class.simpleName, "short" -> this?.toShort()
            Char::class.qualifiedName, Char::class.simpleName, "char" -> this?.first()
            UByte::class.qualifiedName, UByte::class.simpleName, "ubyte" -> this?.toUByte()
            UShort::class.qualifiedName, UShort::class.simpleName, "ushort" -> this?.toUShort()
            UInt::class.qualifiedName, UInt::class.simpleName, "uint" -> this?.toUInt()
            ULong::class.qualifiedName, ULong::class.simpleName, "ulong" -> this?.toULong()
            BigDecimal::class.qualifiedName, BigDecimal::class.simpleName -> this?.toBigDecimal()
            BigInteger::class.qualifiedName, BigInteger::class.simpleName -> this?.toBigInteger()
            Pattern::class.qualifiedName, Pattern::class.simpleName -> this?.toPattern()
            Regex::class.qualifiedName, Regex::class.simpleName -> this?.toRegex()
            CharArray::class.qualifiedName, CharArray::class.simpleName -> this?.toCharArray()
            else -> throw IllegalArgumentException("Unsupported type $typeName. Must be [String, Int, Long, Double, Float, Boolean, Byte, Short, Char, UByte, UShort, UInt, ULong, BigDecimal, CharArray, BigInteger, Pattern, or Regex]")
        }
    } catch (e: Exception) {
        null
    }
}

internal fun MethodParameterInfo.simpleType() : String = this.typeSignatureOrTypeDescriptor.toStringWithSimpleNames()
internal fun MethodParameterInfo.qualifiedType() : String = this.typeSignatureOrTypeDescriptor.toString()

internal fun String.toKClass(): KClass<*>? {
    return try {
        scanResult.loadClass(this, false).kotlin
    } catch (e: Exception) {
        when (this) {
            Boolean::class.qualifiedName, Boolean::class.simpleName -> Boolean::class
            Char::class.qualifiedName, Char::class.simpleName -> Char::class
            Byte::class.qualifiedName, Byte::class.simpleName -> Byte::class
            Short::class.qualifiedName, Short::class.simpleName -> Short::class
            Int::class.qualifiedName, Int::class.simpleName -> Int::class
            Long::class.qualifiedName, Long::class.simpleName -> Long::class
            Float::class.qualifiedName, Float::class.simpleName -> Float::class
            Double::class.qualifiedName, Double::class.simpleName -> Double::class
            else -> null
        }
    }
}

internal fun MethodParameterInfo.toKClass(): KClass<*> {
    return this.qualifiedType().toKClass() ?: Any::class
}

internal fun MethodParameterInfo.toType(): Type {
    return scanResult.loadClass(this.qualifiedType(), false)
}

internal fun MethodParameterInfo.toKType(): KType {
    val kClass = this.toKClass()
    return kClass.createType(nullable = true)
}

internal fun String.isPrimitiveType(): Boolean = when (this) {
    Boolean::class.qualifiedName, Boolean::class.simpleName -> true
    Char::class.qualifiedName, Char::class.simpleName -> true
    Byte::class.qualifiedName, Byte::class.simpleName -> true
    Short::class.qualifiedName, Short::class.simpleName -> true
    Int::class.qualifiedName, Int::class.simpleName -> true
    Long::class.qualifiedName, Long::class.simpleName -> true
    Float::class.qualifiedName, Float::class.simpleName -> true
    Double::class.qualifiedName, Double::class.simpleName -> true
    else -> false
}

internal fun String.isPrimitiveTypeOrString(): Boolean = when {
    this.isPrimitiveType() -> true
    this == String::class.qualifiedName -> true
    this == String::class.simpleName -> true
    else -> false
}

internal fun String.implementsInterface(clazzInterface: KClass<*>) : Boolean {
    return scanResult.getClassInfo(this)
        .superclasses
        .any { it.name == clazzInterface.qualifiedName }
}

internal fun MethodInfo.returnType() : TypeSignature = this.typeSignatureOrTypeDescriptor.resultType

internal fun MethodInfo.returnsBaseResponse() : Boolean = returnType().toString().implementsInterface(BaseResponse::class) || returnType().toString() == BaseResponse::class.qualifiedName

internal fun MethodParameterInfo.isNullable(): Boolean {
    val type = this.toKClass().createType(nullable = true)
    return this.toKClass().createType().isSubtypeOf(type)
}
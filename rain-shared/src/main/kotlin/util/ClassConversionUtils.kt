package util

import io.github.classgraph.MethodInfo
import io.github.classgraph.MethodParameterInfo
import io.github.classgraph.TypeSignature
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf

// Utility function to convert path parameter to the specified type
fun String?.convertByString(typeName: String): Any? {
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

fun MethodParameterInfo.simpleType() : String = this.typeSignatureOrTypeDescriptor.toStringWithSimpleNames()
fun MethodParameterInfo.qualifiedType() : String = this.typeSignatureOrTypeDescriptor.toString()

fun String.toKClass(classLoader: ClassLoader = scanResult.javaClass.classLoader): KClass<*>? {
    return try {
        classLoader.loadClass(this).kotlin
    } catch (e: Exception) {
        when (this) {
            Boolean::class.qualifiedName, Boolean::class.simpleName, "boolean" -> Boolean::class
            Char::class.qualifiedName, Char::class.simpleName, "char" -> Char::class
            Byte::class.qualifiedName, Byte::class.simpleName, "byte" -> Byte::class
            Short::class.qualifiedName, Short::class.simpleName, "short" -> Short::class
            Int::class.qualifiedName, Int::class.simpleName, "int" -> Int::class
            Long::class.qualifiedName, Long::class.simpleName, "long" -> Long::class
            Float::class.qualifiedName, Float::class.simpleName, "float" -> Float::class
            Double::class.qualifiedName, Double::class.simpleName, "double" -> Double::class
            Void::class.qualifiedName, Void::class.simpleName, "void" -> Void::class
            String::class.qualifiedName, String::class.simpleName -> String::class
            else -> null
        }
    }
}

fun MethodParameterInfo.toKClass(classLoader: ClassLoader = scanResult.javaClass.classLoader): KClass<*> {
    return this.qualifiedType().toKClass(classLoader) ?: Any::class
}

fun MethodParameterInfo.toType(classLoader: ClassLoader = scanResult.javaClass.classLoader): Type {
    return classLoader.loadClass(this.qualifiedType())
}

fun MethodParameterInfo.toKType(classLoader: ClassLoader = scanResult.javaClass.classLoader): KType {
    val kClass = this.toKClass(classLoader)
    return kClass.createType(nullable = true)
}

fun String.isPrimitiveType(): Boolean = when (this) {
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

fun String.isPrimitiveTypeOrString(): Boolean = when {
    this.isPrimitiveType() -> true
    this == String::class.qualifiedName -> true
    this == String::class.simpleName -> true
    else -> false
}

fun String.implementsInterface(clazzInterface: KClass<*>) : Boolean {
    val classInfo = scanResult.getClassInfo(this)
    val superclasses = classInfo?.superclasses
    val isSubclass = superclasses?.any { it.name == clazzInterface.qualifiedName } ?: false

    return isSubclass
}

fun MethodInfo.returnType() : TypeSignature = this.typeSignatureOrTypeDescriptor.resultType

fun MethodParameterInfo.isNullable(classLoader: ClassLoader = scanResult.javaClass.classLoader): Boolean {
    val type = this.toKClass(classLoader).createType(nullable = true)
    return this.toKClass(classLoader).createType().isSubtypeOf(type)
}
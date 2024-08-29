package com.iideprived.rain.util

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

private var scanResultInstance: ScanResult? = null

internal fun getScanResult(classLoader: ClassLoader): ScanResult {
    if (scanResultInstance == null) {
        scanResultInstance = ClassGraph()
            .overrideClassLoaders(classLoader) // Use the provided ClassLoader
            .enableAllInfo()
            .scan()
    }
    return scanResultInstance!!
}

internal val scanResult: ScanResult get() = scanResultInstance ?: ClassGraph().enableAllInfo().scan().apply { scanResultInstance = this }
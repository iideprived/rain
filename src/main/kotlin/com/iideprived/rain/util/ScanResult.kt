package com.iideprived.rain.util

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

private var scanResultInstance: ScanResult? = null

internal val scanResult get() : ScanResult {
    if (scanResultInstance == null) {
        scanResultInstance = ClassGraph().enableAllInfo().scan()
    }
    return scanResultInstance!!
}
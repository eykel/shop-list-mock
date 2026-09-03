package com.eykel.shoplistmock.core.format

/**
 * Manual BRL formatting: `java.text.NumberFormat` is JVM-only and this module also targets
 * iOS, so the grouping/decimal logic is written out instead of relying on a platform API.
 */
fun Double.toBRLCurrency(): String {
    val cents = kotlin.math.round(this * 100).toLong()
    val negative = cents < 0
    val absCents = kotlin.math.abs(cents)
    val reais = absCents / 100
    val centavos = absCents % 100
    val grouped = reais.toString().reversed().chunked(3).joinToString(".").reversed()
    val sign = if (negative) "-" else ""
    return "${sign}R$ $grouped,${centavos.toString().padStart(2, '0')}"
}

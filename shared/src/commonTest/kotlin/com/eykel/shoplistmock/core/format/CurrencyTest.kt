package com.eykel.shoplistmock.core.format

import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyTest {

    @Test
    fun formatsCentsAndGrouping() {
        assertEquals("R$ 8,90", 8.90.toBRLCurrency())
        assertEquals("R$ 1.234,50", 1234.50.toBRLCurrency())
        assertEquals("R$ 0,00", 0.0.toBRLCurrency())
    }
}

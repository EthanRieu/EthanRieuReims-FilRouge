package com.example.tps.ui.format

import org.junit.Assert.assertEquals
import org.junit.Test

class FrenchPluralTest {

    @Test
    fun zero_et_un_au_singulier() {
        assertEquals(1, frenchPluralQuantity(0))
        assertEquals(1, frenchPluralQuantity(1))
    }

    @Test
    fun deux_et_plus_au_pluriel() {
        assertEquals(2, frenchPluralQuantity(2))
        assertEquals(52, frenchPluralQuantity(52))
    }
}

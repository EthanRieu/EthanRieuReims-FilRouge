package com.example.tps.ui.format

import org.junit.Assert.assertEquals
import org.junit.Test

class FormattersTest {

    // NumberFormat utilise des espaces insécables : on les normalise pour comparer
    private fun String.normalizeSpaces() = replace(' ', ' ').replace(' ', ' ')

    @Test
    fun prix_au_format_francais() {
        assertEquals("699,99 €", formatPrice(699.99).normalizeSpaces())
        assertEquals("1 249,00 €", formatPrice(1249.0).normalizeSpaces())
        assertEquals("0,00 €", formatPrice(0.0).normalizeSpaces())
    }

    @Test
    fun remise_avec_au_plus_une_decimale() {
        assertEquals("-12,5 %", formatDiscount(12.5))
        assertEquals("-8 %", formatDiscount(8.0))
        assertEquals("-33,3 %", formatDiscount(33.333))
    }

    @Test
    fun note_avec_une_decimale() {
        assertEquals("4,6", formatRating(4.6))
        assertEquals("4,0", formatRating(4.0))
    }
}

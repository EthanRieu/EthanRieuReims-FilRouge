package com.example.tps.ui.format

import java.text.NumberFormat
import java.util.Locale

// Formatage des nombres au format français (virgule décimale, symbole € après le montant)

fun formatPrice(price: Double, locale: Locale = Locale.FRANCE): String {
    return NumberFormat.getCurrencyInstance(locale).format(price)
}

fun formatDiscount(discountPercentage: Double, locale: Locale = Locale.FRANCE): String {
    val number = NumberFormat.getNumberInstance(locale).apply {
        maximumFractionDigits = 1
    }
    return "-${number.format(discountPercentage)} %"
}

fun formatRating(rating: Double, locale: Locale = Locale.FRANCE): String {
    val number = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = 1
        maximumFractionDigits = 1
    }
    return number.format(rating)
}

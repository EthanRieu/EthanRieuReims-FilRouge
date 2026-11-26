package com.example.tps.ui.format

/**
 * Quantité à passer à pluralStringResource pour appliquer la règle française
 * (0 et 1 au singulier) quelle que soit la langue de l'appareil :
 * en anglais, 0 serait au pluriel (« 0 produits »).
 */
fun frenchPluralQuantity(count: Int): Int = if (count <= 1) 1 else count

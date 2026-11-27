package com.example.tps.ui.catalog

import com.example.tps.data.ProductUi

/**
 * État complet de l'écran catalogue à un instant donné.
 * Immuable : le ViewModel en publie une nouvelle copie à chaque changement.
 */
data class ProductCatalogUiState(
    val products: List<ProductUi> = emptyList(),   // liste affichée (filtrée puis triée)
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val showOnlyInStock: Boolean = false,
    val favoriteProductIds: List<Int> = emptyList(),
    val selectedCategory: String? = null,
    val showOnlyFavorites: Boolean = false,
    val sortOrder: SortOrder = SortOrder.DEFAULT
)

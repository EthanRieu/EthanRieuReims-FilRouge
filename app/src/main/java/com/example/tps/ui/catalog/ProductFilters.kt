package com.example.tps.ui.catalog

import androidx.annotation.StringRes
import com.example.tps.R
import com.example.tps.data.ProductUi

/**
 * Filtre la liste du catalogue.
 *
 * Fonction pure (sans état Compose) : facile à tester et réutilisable
 * quel que soit l'endroit où l'état est stocké (composable ou ViewModel).
 */
fun filterProducts(
    products: List<ProductUi>,
    searchQuery: String,
    showOnlyInStock: Boolean,
    selectedCategory: String? = null,
    showOnlyFavorites: Boolean = false,
    favoriteProductIds: List<Int> = emptyList()
): List<ProductUi> {
    val query = searchQuery.trim()
    return products.filter { product ->
        val matchesSearch =
            product.title.contains(query, ignoreCase = true) ||
                    product.brand.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)

        val matchesStock = !showOnlyInStock || product.stock > 0

        val matchesCategory = selectedCategory == null || product.category == selectedCategory

        val matchesFavorites = !showOnlyFavorites || product.id in favoriteProductIds

        matchesSearch && matchesStock && matchesCategory && matchesFavorites
    }
}

enum class SortOrder(@StringRes val labelRes: Int) {
    DEFAULT(R.string.sort_default),
    PRICE_ASCENDING(R.string.sort_price_ascending),
    PRICE_DESCENDING(R.string.sort_price_descending),
    BEST_RATED(R.string.sort_best_rated)
}

// Trie la liste (appliqué après les filtres)
fun sortProducts(products: List<ProductUi>, sortOrder: SortOrder): List<ProductUi> {
    return when (sortOrder) {
        SortOrder.DEFAULT -> products
        SortOrder.PRICE_ASCENDING -> products.sortedBy { it.price }
        SortOrder.PRICE_DESCENDING -> products.sortedByDescending { it.price }
        SortOrder.BEST_RATED -> products.sortedByDescending { it.rating }
    }
}

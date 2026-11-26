package com.example.tps.ui.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tps.data.ProductUi
import com.example.tps.data.sampleCategories
import com.example.tps.data.sampleProducts

/**
 * Possède l'état de l'écran catalogue et réagit aux actions de l'utilisateur.
 *
 * L'interface lit les valeurs (private set : elle ne peut pas les modifier directement)
 * et appelle les fonctions on…() pour signaler un évènement.
 * Le ViewModel survit à la recréation de l'écran (rotation).
 */
class ProductCatalogViewModel : ViewModel() {

    // Données

    private val allProducts: List<ProductUi> = sampleProducts()

    val categories: List<String> = sampleCategories()

    // Produit mis en avant sur l'écran d'accueil
    val featuredProduct: ProductUi = allProducts.first()

    // État de l'écran

    var searchQuery by mutableStateOf("")
        private set

    var showOnlyInStock by mutableStateOf(false)
        private set

    var favoriteProductIds by mutableStateOf(listOf<Int>())
        private set

    var selectedCategory by mutableStateOf<String?>(null)
        private set

    var showOnlyFavorites by mutableStateOf(false)
        private set

    var sortOrder by mutableStateOf(SortOrder.DEFAULT)
        private set

    // Liste affichée, recalculée à partir de l'état courant
    val filteredProducts: List<ProductUi>
        get() = sortProducts(
            products = filterProducts(
                products = allProducts,
                searchQuery = searchQuery,
                showOnlyInStock = showOnlyInStock,
                selectedCategory = selectedCategory,
                showOnlyFavorites = showOnlyFavorites,
                favoriteProductIds = favoriteProductIds
            ),
            sortOrder = sortOrder
        )

    fun getProduct(productId: Int): ProductUi? = allProducts.firstOrNull { it.id == productId }

    fun isFavorite(productId: Int): Boolean = favoriteProductIds.contains(productId)

    // Actions de l'utilisateur

    fun onSearchQueryChange(newValue: String) {
        searchQuery = newValue
    }

    fun onToggleStockFilter() {
        showOnlyInStock = !showOnlyInStock
    }

    fun onFavoriteClick(productId: Int) {
        favoriteProductIds = if (favoriteProductIds.contains(productId)) {
            favoriteProductIds - productId
        } else {
            favoriteProductIds + productId
        }
    }

    // Re-cliquer sur la catégorie active la désélectionne
    fun onCategoryClick(category: String) {
        selectedCategory = if (selectedCategory == category) null else category
    }

    // Sélection directe, par exemple depuis une catégorie de l'écran d'accueil
    fun onCategorySelected(category: String) {
        selectedCategory = category
    }

    fun onToggleFavoritesFilter() {
        showOnlyFavorites = !showOnlyFavorites
    }

    fun onSortOrderChange(newOrder: SortOrder) {
        sortOrder = newOrder
    }

    fun onResetFilters() {
        searchQuery = ""
        showOnlyInStock = false
        selectedCategory = null
        showOnlyFavorites = false
    }
}

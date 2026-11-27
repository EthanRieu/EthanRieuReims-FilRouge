package com.example.tps.ui.catalog

import androidx.lifecycle.ViewModel
import com.example.tps.data.ProductUi
import com.example.tps.data.sampleCategories
import com.example.tps.data.sampleProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Publie l'état de l'écran catalogue et réagit aux actions de l'utilisateur.
 *
 * Seul le ViewModel modifie _uiState ; l'interface observe uiState (lecture seule)
 * et appelle les fonctions on…() pour signaler un évènement.
 * Le ViewModel survit à la recréation de l'écran (rotation).
 */
class ProductCatalogViewModel : ViewModel() {

    // Données

    private val allProducts: List<ProductUi> = sampleProducts()
    private val allCategories: List<String> = sampleCategories()

    // Produit mis en avant sur l'écran d'accueil
    val featuredProduct: ProductUi = allProducts.first()

    // État de l'écran

    private val _uiState = MutableStateFlow(
        ProductCatalogUiState(
            products = allProducts,
            categories = allCategories
        )
    )

    val uiState: StateFlow<ProductCatalogUiState> = _uiState.asStateFlow()

    fun getProduct(productId: Int): ProductUi? = allProducts.firstOrNull { it.id == productId }

    // Actions de l'utilisateur

    fun onSearchQueryChange(newValue: String) = updateState {
        it.copy(searchQuery = newValue)
    }

    fun onToggleStockFilter() = updateState {
        it.copy(showOnlyInStock = !it.showOnlyInStock)
    }

    fun onFavoriteClick(productId: Int) = updateState {
        val newFavoriteIds = if (productId in it.favoriteProductIds) {
            it.favoriteProductIds - productId
        } else {
            it.favoriteProductIds + productId
        }
        it.copy(favoriteProductIds = newFavoriteIds)
    }

    // Re-cliquer sur la catégorie active la désélectionne
    fun onCategoryClick(category: String) = updateState {
        it.copy(selectedCategory = if (it.selectedCategory == category) null else category)
    }

    // Sélection directe, par exemple depuis une catégorie de l'écran d'accueil
    fun onCategorySelected(category: String) = updateState {
        it.copy(selectedCategory = category)
    }

    fun onToggleFavoritesFilter() = updateState {
        it.copy(showOnlyFavorites = !it.showOnlyFavorites)
    }

    fun onSortOrderChange(newOrder: SortOrder) = updateState {
        it.copy(sortOrder = newOrder)
    }

    fun onResetFilters() = updateState {
        it.copy(
            searchQuery = "",
            showOnlyInStock = false,
            selectedCategory = null,
            showOnlyFavorites = false
        )
    }

    // Applique le changement avec update + copy, puis recalcule la liste affichée
    private fun updateState(change: (ProductCatalogUiState) -> ProductCatalogUiState) {
        _uiState.update { currentState ->
            val newState = change(currentState)
            newState.copy(products = visibleProducts(newState))
        }
    }

    private fun visibleProducts(state: ProductCatalogUiState): List<ProductUi> {
        val filtered = filterProducts(
            products = allProducts,
            searchQuery = state.searchQuery,
            showOnlyInStock = state.showOnlyInStock,
            selectedCategory = state.selectedCategory,
            showOnlyFavorites = state.showOnlyFavorites,
            favoriteProductIds = state.favoriteProductIds
        )
        return sortProducts(filtered, state.sortOrder)
    }
}

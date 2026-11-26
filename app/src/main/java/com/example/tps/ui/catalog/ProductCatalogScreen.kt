package com.example.tps.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tps.data.ProductUi
import com.example.tps.data.sampleCategories
import com.example.tps.data.sampleProducts
import com.example.tps.ui.components.CategoryRow
import com.example.tps.ui.theme.TPsTheme

// Composable stateful : possède l'état du catalogue (recherche, filtres, favoris)
@Composable
fun ProductCatalogContainer(
    products: List<ProductUi>,
    categories: List<String>,
    onProductClick: (ProductUi) -> Unit,
    modifier: Modifier = Modifier,
    requestedCategory: String? = null,
    onRequestedCategoryHandled: () -> Unit = {}
) {
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var showOnlyInStock by rememberSaveable {
        mutableStateOf(false)
    }

    var favoriteProductIds by rememberSaveable {
        mutableStateOf(listOf<Int>())
    }

    var selectedCategory by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var showOnlyFavorites by rememberSaveable {
        mutableStateOf(false)
    }

    var sortOrder by rememberSaveable {
        mutableStateOf(SortOrder.DEFAULT)
    }

    // Catégorie demandée depuis un autre écran (ex. clic sur une catégorie de l'accueil)
    LaunchedEffect(requestedCategory) {
        if (requestedCategory != null) {
            selectedCategory = requestedCategory
            onRequestedCategoryHandled()
        }
    }

    val filteredProducts = remember(
        products, searchQuery, showOnlyInStock, selectedCategory, showOnlyFavorites, favoriteProductIds, sortOrder
    ) {
        val filtered = filterProducts(
            products = products,
            searchQuery = searchQuery,
            showOnlyInStock = showOnlyInStock,
            selectedCategory = selectedCategory,
            showOnlyFavorites = showOnlyFavorites,
            favoriteProductIds = favoriteProductIds
        )
        sortProducts(filtered, sortOrder)
    }

    fun toggleFavorite(productId: Int) {
        favoriteProductIds = if (favoriteProductIds.contains(productId)) {
            favoriteProductIds - productId
        } else {
            favoriteProductIds + productId
        }
    }

    ProductCatalogScreen(
        products = filteredProducts,
        categories = categories,
        selectedCategory = selectedCategory,
        onCategoryClick = { category ->
            // Re-cliquer sur la catégorie active la désélectionne
            selectedCategory = if (selectedCategory == category) null else category
        },
        searchQuery = searchQuery,
        onSearchQueryChange = { newValue ->
            searchQuery = newValue
        },
        showOnlyInStock = showOnlyInStock,
        onToggleStockFilter = {
            showOnlyInStock = !showOnlyInStock
        },
        favoriteProductIds = favoriteProductIds,
        onFavoriteClick = { productId ->
            toggleFavorite(productId)
        },
        showOnlyFavorites = showOnlyFavorites,
        onToggleFavoritesFilter = {
            showOnlyFavorites = !showOnlyFavorites
        },
        sortOrder = sortOrder,
        onSortOrderChange = { newOrder ->
            sortOrder = newOrder
        },
        onResetFilters = {
            searchQuery = ""
            showOnlyInStock = false
            selectedCategory = null
            showOnlyFavorites = false
        },
        onProductClick = onProductClick,
        modifier = modifier
    )
}

// Composable stateless : affiche les valeurs reçues et fait remonter les événements
@Composable
fun ProductCatalogScreen(
    products: List<ProductUi>,
    categories: List<String>,
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    showOnlyInStock: Boolean,
    onToggleStockFilter: () -> Unit,
    favoriteProductIds: List<Int>,
    onFavoriteClick: (Int) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavoritesFilter: () -> Unit,
    sortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
    onProductClick: (ProductUi) -> Unit,
    modifier: Modifier = Modifier,
    onResetFilters: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Catalogue produits",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Text(
                text = "Explorez une sélection de produits disponibles localement.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = {
                    Text(text = "Rechercher un produit")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Effacer la recherche"
                            )
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            CatalogFilterRow(
                showOnlyInStock = showOnlyInStock,
                onToggleStockFilter = onToggleStockFilter,
                showOnlyFavorites = showOnlyFavorites,
                favoriteCount = favoriteProductIds.size,
                onToggleFavoritesFilter = onToggleFavoritesFilter
            )
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${products.size} produit(s) affiché(s)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                SortMenu(
                    sortOrder = sortOrder,
                    onSortOrderChange = onSortOrderChange
                )
            }
        }

        item {
            CategoryRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategoryClick = onCategoryClick
            )
        }

        if (products.isEmpty()) {
            item {
                EmptyCatalogMessage(onResetFiltersClick = onResetFilters)
            }
        }

        items(
            items = products,
            key = { product -> product.id }
        ) { product ->
            ProductListItem(
                product = product,
                isFavorite = favoriteProductIds.contains(product.id),
                onFavoriteClick = {
                    onFavoriteClick(product.id)
                },
                onClick = {
                    onProductClick(product)
                }
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "Catalogue - Light Theme"
)
@Composable
fun ProductCatalogContainerLightPreview() {
    TPsTheme(darkTheme = false) {
        ProductCatalogContainer(
            products = sampleProducts(),
            categories = sampleCategories(),
            onProductClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Catalogue - Dark Theme"
)
@Composable
fun ProductCatalogContainerDarkPreview() {
    TPsTheme(darkTheme = true) {
        ProductCatalogContainer(
            products = sampleProducts(),
            categories = sampleCategories(),
            onProductClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCatalogScreenStatelessPreview() {
    TPsTheme {
        ProductCatalogScreen(
            products = sampleProducts(),
            categories = sampleCategories(),
            selectedCategory = null,
            onCategoryClick = {},
            searchQuery = "",
            onSearchQueryChange = {},
            showOnlyInStock = false,
            onToggleStockFilter = {},
            favoriteProductIds = listOf(1),
            onFavoriteClick = {},
            showOnlyFavorites = false,
            onToggleFavoritesFilter = {},
            sortOrder = SortOrder.DEFAULT,
            onSortOrderChange = {},
            onProductClick = {}
        )
    }
}

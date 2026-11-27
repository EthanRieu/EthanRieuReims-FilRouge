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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tps.R
import com.example.tps.data.ProductUi
import com.example.tps.data.sampleCategories
import com.example.tps.data.sampleProducts
import com.example.tps.ui.components.CategoryRow
import com.example.tps.ui.format.frenchPluralQuantity
import com.example.tps.ui.theme.TPsTheme

// Passerelle entre le ViewModel et l'écran stateless : collecte l'état et transmet les callbacks
@Composable
fun ProductCatalogContainer(
    onProductClick: (ProductUi) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductCatalogViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ProductCatalogScreen(
        products = uiState.products,
        categories = uiState.categories,
        selectedCategory = uiState.selectedCategory,
        onCategoryClick = viewModel::onCategoryClick,
        searchQuery = uiState.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        showOnlyInStock = uiState.showOnlyInStock,
        onToggleStockFilter = viewModel::onToggleStockFilter,
        favoriteProductIds = uiState.favoriteProductIds,
        onFavoriteClick = viewModel::onFavoriteClick,
        showOnlyFavorites = uiState.showOnlyFavorites,
        onToggleFavoritesFilter = viewModel::onToggleFavoritesFilter,
        sortOrder = uiState.sortOrder,
        onSortOrderChange = viewModel::onSortOrderChange,
        onResetFilters = viewModel::onResetFilters,
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
                text = stringResource(R.string.catalog_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Text(
                text = stringResource(R.string.catalog_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = {
                    Text(text = stringResource(R.string.search_label))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = stringResource(R.string.clear_search)
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
                    text = pluralStringResource(R.plurals.products_count, frenchPluralQuantity(products.size), products.size),
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

// Les previews testent directement l'écran stateless : pas besoin de ViewModel
@Preview(
    showBackground = true,
    name = "Catalogue - Light Theme"
)
@Composable
fun ProductCatalogScreenLightPreview() {
    TPsTheme(darkTheme = false) {
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

@Preview(
    showBackground = true,
    name = "Catalogue - Dark Theme"
)
@Composable
fun ProductCatalogScreenDarkPreview() {
    TPsTheme(darkTheme = true) {
        ProductCatalogScreen(
            products = sampleProducts(),
            categories = sampleCategories(),
            selectedCategory = null,
            onCategoryClick = {},
            searchQuery = "audio",
            onSearchQueryChange = {},
            showOnlyInStock = true,
            onToggleStockFilter = {},
            favoriteProductIds = listOf(2),
            onFavoriteClick = {},
            showOnlyFavorites = false,
            onToggleFavoritesFilter = {},
            sortOrder = SortOrder.DEFAULT,
            onSortOrderChange = {},
            onProductClick = {}
        )
    }
}

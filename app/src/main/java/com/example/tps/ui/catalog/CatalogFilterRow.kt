package com.example.tps.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tps.R

// Filtres rapides du catalogue : stock et favoris
@Composable
fun CatalogFilterRow(
    showOnlyInStock: Boolean,
    onToggleStockFilter: () -> Unit,
    showOnlyFavorites: Boolean,
    favoriteCount: Int,
    onToggleFavoritesFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = showOnlyInStock,
            onClick = onToggleStockFilter,
            label = { Text(text = stringResource(R.string.filter_in_stock)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Inventory2,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        )
        FilterChip(
            selected = showOnlyFavorites,
            onClick = onToggleFavoritesFilter,
            label = { Text(text = stringResource(R.string.filter_favorites, favoriteCount)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        )
    }
}

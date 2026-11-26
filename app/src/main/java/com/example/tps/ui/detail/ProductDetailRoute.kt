package com.example.tps.ui.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.tps.R
import com.example.tps.data.ProductUi
import com.example.tps.data.sampleProduct
import com.example.tps.ui.components.FavoriteButton
import com.example.tps.ui.theme.TPsTheme
import kotlinx.coroutines.launch

/**
 * Écran de détail complet : barre du haut avec retour et favori, contenu du TP4 et
 * confirmation « ajouté au panier » dans une snackbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailRoute(
    product: ProductUi,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val addedToCartMessage = stringResource(R.string.added_to_cart, product.title)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = product.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    FavoriteButton(
                        isFavorite = isFavorite,
                        onClick = onFavoriteClick
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        ProductDetailScreen(
            product = product,
            onAddToCartClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(addedToCartMessage)
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailRoutePreview() {
    TPsTheme {
        ProductDetailRoute(
            product = sampleProduct(),
            isFavorite = true,
            onFavoriteClick = {},
            onBackClick = {}
        )
    }
}

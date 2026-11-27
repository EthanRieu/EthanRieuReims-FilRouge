package com.example.tps.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.tps.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tps.ui.catalog.ProductCatalogContainer
import com.example.tps.ui.catalog.ProductCatalogViewModel
import com.example.tps.ui.detail.ProductDetailRoute
import com.example.tps.ui.home.ProductHomeScreen

enum class AppTab(@StringRes val labelRes: Int, val icon: ImageVector) {
    HOME(R.string.tab_home, Icons.Filled.Home),
    CATALOG(R.string.tab_catalog, Icons.AutoMirrored.Filled.List)
}

/**
 * Point d'entrée de l'interface : onglets Accueil / Catalogue et écran de détail.
 *
 * La navigation est un simple état Compose (onglet courant + produit ouvert),
 * conservé à la rotation grâce à rememberSaveable.
 * Le ViewModel du catalogue est partagé par les écrans (même instance dans l'activité) :
 * les favoris sont donc les mêmes dans le catalogue et dans le détail.
 */
@Composable
fun ProductExplorerApp(
    modifier: Modifier = Modifier,
    catalogViewModel: ProductCatalogViewModel = viewModel()
) {
    var currentTab by rememberSaveable { mutableStateOf(AppTab.CATALOG) }
    var selectedProductId by rememberSaveable { mutableStateOf<Int?>(null) }

    // Conserve l'état de chaque onglet (recherche, filtres, défilement) quand on change d'écran
    val stateHolder = rememberSaveableStateHolder()

    val selectedProduct = selectedProductId?.let { catalogViewModel.getProduct(it) }

    // Retour système : détail → écran précédent, Accueil → Catalogue (écran de départ)
    BackHandler(enabled = selectedProduct != null) {
        selectedProductId = null
    }
    BackHandler(enabled = selectedProduct == null && currentTab != AppTab.CATALOG) {
        currentTab = AppTab.CATALOG
    }

    if (selectedProduct != null) {
        // Collecté ici pour que le cœur du détail se mette à jour
        val catalogState by catalogViewModel.uiState.collectAsState()

        ProductDetailRoute(
            product = selectedProduct,
            isFavorite = selectedProduct.id in catalogState.favoriteProductIds,
            onFavoriteClick = { catalogViewModel.onFavoriteClick(selectedProduct.id) },
            onBackClick = { selectedProductId = null },
            modifier = modifier
        )
    } else {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                AppNavigationBar(
                    currentTab = currentTab,
                    onTabClick = { tab -> currentTab = tab }
                )
            }
        ) { innerPadding ->
            stateHolder.SaveableStateProvider(key = currentTab.name) {
                when (currentTab) {
                    AppTab.HOME -> ProductHomeScreen(
                        featuredProduct = catalogViewModel.featuredProduct,
                        onFeaturedProductClick = {
                            selectedProductId = catalogViewModel.featuredProduct.id
                        },
                        onSearchClick = { currentTab = AppTab.CATALOG },
                        onCategoryClick = { category ->
                            catalogViewModel.onCategorySelected(category)
                            currentTab = AppTab.CATALOG
                        },
                        modifier = Modifier.padding(innerPadding)
                    )

                    AppTab.CATALOG -> ProductCatalogContainer(
                        onProductClick = { product -> selectedProductId = product.id },
                        modifier = Modifier.padding(innerPadding),
                        viewModel = catalogViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigationBar(
    currentTab: AppTab,
    onTabClick: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        AppTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = tab == currentTab,
                onClick = { onTabClick(tab) },
                icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                label = { Text(text = stringResource(tab.labelRes)) }
            )
        }
    }
}

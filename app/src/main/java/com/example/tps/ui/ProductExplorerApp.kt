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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tps.R
import com.example.tps.data.ProductUi
import com.example.tps.data.sampleCategories
import com.example.tps.data.sampleProducts
import com.example.tps.ui.catalog.ProductCatalogContainer
import com.example.tps.ui.detail.ProductDetailRoute
import com.example.tps.ui.home.ProductHomeScreen
import com.example.tps.ui.theme.TPsTheme

enum class AppTab(@StringRes val labelRes: Int, val icon: ImageVector) {
    HOME(R.string.tab_home, Icons.Filled.Home),
    CATALOG(R.string.tab_catalog, Icons.AutoMirrored.Filled.List)
}

/**
 * Point d'entrée de l'interface : onglets Accueil / Catalogue et écran de détail.
 *
 * La navigation est un simple état Compose (onglet courant + produit ouvert),
 * conservé à la rotation grâce à rememberSaveable.
 */
@Composable
fun ProductExplorerApp(
    products: List<ProductUi>,
    categories: List<String>,
    modifier: Modifier = Modifier
) {
    var currentTab by rememberSaveable { mutableStateOf(AppTab.CATALOG) }
    var selectedProductId by rememberSaveable { mutableStateOf<Int?>(null) }
    var requestedCategory by rememberSaveable { mutableStateOf<String?>(null) }

    // Conserve l'état de chaque onglet (recherche, filtres, défilement) quand on change d'écran
    val stateHolder = rememberSaveableStateHolder()

    val selectedProduct = products.firstOrNull { it.id == selectedProductId }

    // Retour système : détail → écran précédent, Accueil → Catalogue (écran de départ)
    BackHandler(enabled = selectedProduct != null) {
        selectedProductId = null
    }
    BackHandler(enabled = selectedProduct == null && currentTab != AppTab.CATALOG) {
        currentTab = AppTab.CATALOG
    }

    if (selectedProduct != null) {
        ProductDetailRoute(
            product = selectedProduct,
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
                        featuredProduct = products.first(),
                        onFeaturedProductClick = { selectedProductId = products.first().id },
                        onSearchClick = { currentTab = AppTab.CATALOG },
                        onCategoryClick = { category ->
                            requestedCategory = category
                            currentTab = AppTab.CATALOG
                        },
                        modifier = Modifier.padding(innerPadding)
                    )

                    AppTab.CATALOG -> ProductCatalogContainer(
                        products = products,
                        categories = categories,
                        onProductClick = { product -> selectedProductId = product.id },
                        modifier = Modifier.padding(innerPadding),
                        requestedCategory = requestedCategory,
                        onRequestedCategoryHandled = { requestedCategory = null }
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

@Preview(showBackground = true)
@Composable
fun ProductExplorerAppPreview() {
    TPsTheme {
        ProductExplorerApp(
            products = sampleProducts(),
            categories = sampleCategories()
        )
    }
}

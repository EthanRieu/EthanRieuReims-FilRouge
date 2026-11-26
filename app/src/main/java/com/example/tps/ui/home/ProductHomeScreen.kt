package com.example.tps.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tps.data.ProductUi
import com.example.tps.data.sampleProduct
import com.example.tps.ui.theme.TPsTheme

@Composable
fun ProductHomeScreen(
    featuredProduct: ProductUi,
    onFeaturedProductClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HomeHeader()
        Spacer(modifier = Modifier.height(24.dp))
        SearchPreviewBar(onClick = onSearchClick)
        Spacer(modifier = Modifier.height(24.dp))
        FeaturedProductSection(
            product = featuredProduct,
            onClick = onFeaturedProductClick
        )
        Spacer(modifier = Modifier.height(24.dp))
        CategoriesSection(onCategoryClick = onCategoryClick)
        Spacer(modifier = Modifier.height(24.dp))
        DailyOfferBox()
    }
}

@Preview(showBackground = true)
@Composable
fun ProductHomeScreenPreview() {
    TPsTheme {
        ProductHomeScreen(
            featuredProduct = sampleProduct(),
            onFeaturedProductClick = {}
        )
    }
}

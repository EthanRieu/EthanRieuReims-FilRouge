package com.example.tps.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tps.R
import com.example.tps.data.ProductUi
import com.example.tps.ui.components.CategoryChip
import com.example.tps.ui.format.formatPrice
import com.example.tps.ui.format.formatRating

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SearchPreviewBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Text(
            text = stringResource(R.string.home_search_placeholder),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun FeaturedProductSection(
    product: ProductUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.featured_product_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = product.brand,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProductQuickInfoRow(
                price = product.price,
                rating = product.rating,
                stock = product.stock
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onClick) {
                Text(text = stringResource(R.string.see_product))
            }
        }
    }
}

@Composable
fun ProductQuickInfoRow(
    price: Double,
    rating: Double,
    stock: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = formatPrice(price),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "★ ${formatRating(rating)}",
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(R.string.quick_info_stock, stock),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CategoriesSection(
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.categories_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            CategoryChip(label = "Smartphones", onClick = { onCategoryClick("Smartphones") })
            Spacer(modifier = Modifier.weight(1f))
            CategoryChip(label = "Audio", onClick = { onCategoryClick("Audio") })
            Spacer(modifier = Modifier.weight(1f))
            CategoryChip(label = "Maison", onClick = { onCategoryClick("Maison") })
        }
    }
}

@Composable
fun DailyOfferBox(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.daily_offer_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.daily_offer_text),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            shape = MaterialTheme.shapes.small,
            tonalElevation = 6.dp
        ) {
            Text(
                text = stringResource(R.string.badge_new),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

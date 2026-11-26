package com.example.tps.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tps.R
import com.example.tps.ui.format.formatDiscount
import com.example.tps.ui.format.formatPrice
import com.example.tps.ui.format.formatRating
import com.example.tps.ui.format.frenchPluralQuantity

@Composable
fun ProductImage(
    productTitle: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
        contentDescription = stringResource(R.string.product_image_description, productTitle),
        modifier = modifier
    )
}

@Composable
fun ProductHeader(
    title: String,
    brand: String,
    category: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = brand,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProductPriceCard(
    price: Double,
    discountPercentage: Double,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = formatPrice(price),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.discount_label, formatDiscount(discountPercentage)),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProductRating(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = stringResource(R.string.rating_description)
        )
        Text(
            text = stringResource(R.string.rating_value, formatRating(rating)),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun ProductAvailabilityCard(
    stock: Int,
    shippingInformation: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.availability_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = pluralStringResource(R.plurals.stock_units, frenchPluralQuantity(stock), stock),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = shippingInformation,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProductDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.description_title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun ProductWarrantyField(
    warrantyInformation: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = warrantyInformation,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(text = stringResource(R.string.warranty_label))
        },
        modifier = modifier
    )
}

@Composable
fun AddToCartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(text = stringResource(if (enabled) R.string.add_to_cart else R.string.product_unavailable))
    }
}

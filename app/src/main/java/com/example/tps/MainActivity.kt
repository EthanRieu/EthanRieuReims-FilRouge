package com.example.tps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.tps.data.sampleCategories
import com.example.tps.data.sampleProducts
import com.example.tps.ui.catalog.ProductCatalogContainer
import com.example.tps.ui.theme.TPsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TPsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProductCatalogContainer(
                        products = sampleProducts(),
                        categories = sampleCategories(),
                        onProductClick = {
                            // Plus tard : ouvrir le détail du produit
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

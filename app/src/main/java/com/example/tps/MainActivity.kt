package com.example.tps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.tps.ui.ProductExplorerApp
import com.example.tps.ui.theme.TPsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TPsTheme {
                ProductExplorerApp(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

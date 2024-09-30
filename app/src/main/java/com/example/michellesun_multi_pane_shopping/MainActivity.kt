package com.example.michellesun_multi_pane_shopping

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.michellesun_multi_pane_shopping.ui.theme.MichelleSunMultipaneshoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MichelleSunMultipaneshoppingTheme {
                MultiPane(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

// Data class representing a product with a name, price, and description
data class Product(val name: String, val price: String, val description: String)

@Composable
fun MultiPane(modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val products = listOf(
        Product("Product A", "$100", "This is a great product A."),
        Product("Product B", "$150", "This is product B with more features."),
        Product("Product C", "$200", "Premium product C.")
    )
    var selectedProductName by rememberSaveable { mutableStateOf<String?>(null) }
    // .finds = selected product based on the name stored in the state
    val selectedProduct = products.find { it.name == selectedProductName }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            // Double pane
            Row(modifier = modifier.fillMaxSize()) {
                // List on Left, Details on Right
                ProductList(
                    products = products,
                    onProductClick = { selectedProductName = it.name },
                    modifier = Modifier.weight(1f) // 1f for half (width)
                )
                DisplayProduct(
                    product = selectedProduct,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            // Single pane
            if (selectedProduct == null) {
                // If no product is selected, show the product list
                ProductList(
                    products = products,
                    onProductClick = { selectedProductName = it.name },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                DisplayProduct(
                    product = selectedProduct,
                    onBack = { selectedProductName = null }, // On back, go to list
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// Composable function to display a list of products using LazyColumn
@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(products) { product ->
            Text(
                text = product.name,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onProductClick(product) } // Clickable product
            )
        }
    }
}

@Composable
fun DisplayProduct(
    product: Product?,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null // Callback to handle for back button (Optional based on orientation)
) {
    Column(modifier = modifier.padding(16.dp)) {
        if (product == null) {
            // If no product is selected, show a placeholder message
            // Shouldn't get in here
            Text(text = "Select a product to view details.")
        } else {
            // If a back button is provided (portrait mode), display it
            onBack?.let {
                Button(onClick = { onBack() }) {
                    Text(text = "Back")
                }
            }
            Text(text = "Name: ${product.name}")
            Text(text = "Price: ${product.price}")
            Text(text = "Description: ${product.description}")
        }
    }
}
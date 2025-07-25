package com.example.pos.ui.pos

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.journeyapps.barcodescanner.ScanContract

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String
)

data class CartItem(
    val product: Product,
    var quantity: Int,
    var variant: String = "Variant Name",
    var isExpanded: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosMainScreen(
    onBackClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
    var showQRScanner by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Sample products
    val products = remember {
        listOf(
            Product("1", "Short name", 2000.0, "https://images.pexels.com/photos/1775043/pexels-photo-1775043.jpeg"),
            Product("2", "Sample Item Name Long Name Long Name", 2000.0, "https://images.pexels.com/photos/1552630/pexels-photo-1552630.jpeg"),
            Product("3", "Sample Item NameSample Item Name", 8000.0, "https://images.pexels.com/photos/1633578/pexels-photo-1633578.jpeg"),
            Product("4", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1633525/pexels-photo-1633525.jpeg"),
            Product("5", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg"),
            Product("6", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1633578/pexels-photo-1633578.jpeg"),
            Product("7", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1775043/pexels-photo-1775043.jpeg"),
            Product("8", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1552630/pexels-photo-1552630.jpeg"),
            Product("9", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1633525/pexels-photo-1633525.jpeg"),
            Product("10", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg"),
            Product("11", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1775043/pexels-photo-1775043.jpeg"),
            Product("12", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1552630/pexels-photo-1552630.jpeg"),
            Product("13", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1633578/pexels-photo-1633578.jpeg"),
            Product("14", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1633525/pexels-photo-1633525.jpeg"),
            Product("15", "Sample Item NameSample Item Name", 2000.0, "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg")
        )
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showQRScanner = true
        }
    }

    // QR Scanner launcher
    val qrScannerLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result.contents != null) {
            // Handle QR scan result
            showQRScanner = false
        }
    }

    fun addToCart(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            cartItems = cartItems.map {
                if (it.product.id == product.id) {
                    it.copy(quantity = it.quantity + 1)
                } else it
            }
        } else {
            cartItems = cartItems + CartItem(product, 1)
        }
    }

    fun removeFromCart(productId: String) {
        cartItems = cartItems.filter { it.product.id != productId }
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(productId)
        } else {
            cartItems = cartItems.map {
                if (it.product.id == productId) {
                    it.copy(quantity = newQuantity)
                } else it
            }
        }
    }

    fun toggleExpanded(productId: String) {
        cartItems = cartItems.map {
            if (it.product.id == productId) {
                it.copy(isExpanded = !it.isExpanded)
            } else it
        }
    }

    fun updateVariant(productId: String, variant: String) {
        cartItems = cartItems.map {
            if (it.product.id == productId) {
                it.copy(variant = variant)
            } else it
        }
    }

    val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
    val totalItems = cartItems.sumOf { it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFF0D923), CircleShape),
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )

                    // Menu icon
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }

                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Search...",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .height(40.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }
            },
            actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Offline indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WifiOff,
                            contentDescription = "Offline",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Orders button
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = "Orders",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Orders",
                            fontSize = 12.sp
                        )
                    }

                    // Cart button with dropdown
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Cart $totalItems",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Menu icon
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.Black
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Category tabs and action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CategoryTab("All", true)
                CategoryTab("Label", false)
                CategoryTab("Label", false)
                CategoryTab("Label", false)
            }

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Customer",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Customer",
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "App Orders",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "App Orders",
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Main content area
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Products grid (70%)
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { addToCart(product) }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Cart section (30%)
            Card(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (cartItems.isEmpty()) {
                        // Empty cart
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Empty Cart",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        // Cart items
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(cartItems) { cartItem ->
                                CartItemRow(
                                    cartItem = cartItem,
                                    onQuantityChange = { newQuantity ->
                                        updateQuantity(cartItem.product.id, newQuantity)
                                    },
                                    onRemove = { removeFromCart(cartItem.product.id) },
                                    onToggleExpanded = { toggleExpanded(cartItem.product.id) },
                                    onVariantChange = { variant ->
                                        updateVariant(cartItem.product.id, variant)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Cart summary
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Subtotal",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Rs.${String.format("%.2f", totalAmount)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Payable Amount",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Rs.${String.format("%.2f", totalAmount)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF0D923),
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Proceed",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // QR Scanner Dialog
    if (showQRScanner) {
        QRScannerDialog(
            onDismiss = { showQRScanner = false },
            onScanResult = { result ->
                showQRScanner = false
                // Handle scan result
            }
        )
    }
}

@Composable
private fun CategoryTab(
    text: String,
    isSelected: Boolean
) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Black else Color.White,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.height(32.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onProductClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CartItemRow(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    onToggleExpanded: () -> Unit,
    onVariantChange: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpanded() }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Expand/Collapse arrow
                Icon(
                    imageVector = if (cartItem.isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = if (cartItem.isExpanded) "Collapse" else "Expand",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )

                // Quantity badge
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFF2196F3), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cartItem.quantity.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Product name
                Text(
                    text = cartItem.product.name,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Price
                Text(
                    text = "Rs.${String.format("%.2f", cartItem.product.price * cartItem.quantity)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                // Remove button
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Expanded content
        if (cartItem.isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 8.dp, bottom = 8.dp)
            ) {
                // Variant dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Variant",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cartItem.variant,
                                    fontSize = 12.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                    }

                    // Quantity controls
                    Column {
                        Text(
                            text = "Quantity",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Decrease button
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .clickable { onQuantityChange(cartItem.quantity - 1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "−",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }

                            // Quantity display
                            Text(
                                text = cartItem.quantity.toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.width(20.dp),
                                textAlign = TextAlign.Center
                            )

                            // Increase button
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .clickable { onQuantityChange(cartItem.quantity + 1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Divider
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun QRScannerDialog(
    onDismiss: () -> Unit,
    onScanResult: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(400.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scan App Order",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Black
                            )
                        }
                    }

                    // Scanner area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp)
                            .background(Color.Black, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Scanner frame
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .border(2.dp, Color(0xFF2196F3), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Scanner",
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFF2196F3)
                            )
                        }
                    }

                    // Instructions
                    Text(
                        text = "Position the QR code within the frame to scan",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
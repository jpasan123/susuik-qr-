package com.example.pos.ui.pos

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import com.example.pos.ui.customer.AddCustomerDialog
import com.example.pos.ui.order.OrderSuccessScreen
import com.example.pos.ui.payment.PaymentScreen
import com.journeyapps.barcodescanner.ScanContract

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosMainScreen(
    onBackClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
    var showQRScanner by remember { mutableStateOf(false) }
    var showClearMenu by remember { mutableStateOf(false) }
    var showMenuDropdown by remember { mutableStateOf(false) }
    var offlineMode by remember { mutableStateOf(true) }
    var showCustomerDialog by remember { mutableStateOf(false) }
    var editingCustomer by remember { mutableStateOf<com.example.pos.ui.customer.Customer?>(null) }
    var showAddCustomerDialog by remember { mutableStateOf(false) }
    var customer by remember { mutableStateOf<com.example.pos.ui.customer.Customer?>(null) }
    var orders by remember { mutableStateOf(listOf<com.example.pos.ui.customer.Order>()) }
    var showOrdersScreen by remember { mutableStateOf(false) }
    var showCustomerSelectionDialog by remember { mutableStateOf(false) }
    var showRFIDDialog by remember { mutableStateOf(false) }
    var showQRCodeDialog by remember { mutableStateOf(false) }
    var showPaymentScreen by remember { mutableStateOf(false) }
    var showOrderSuccess by remember { mutableStateOf(false) }

    fun handleAddOrEditCustomer(newCustomer: com.example.pos.ui.customer.Customer) {
        customer = newCustomer
        showCustomerDialog = false
        editingCustomer = null
    }

    fun handleOrder() {
        if (customer != null && cartItems.isNotEmpty()) {
            val order = com.example.pos.ui.customer.Order(
                id = System.currentTimeMillis().toString(),
                customerId = customer!!.id,
                items = cartItems,
                total = cartItems.sumOf { it.product.price * it.quantity }
            )
            orders = orders + order
            cartItems = emptyList()
        }
    }
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

    // Filter products by search query
    val filteredProducts = remember(searchQuery, products) {
        if (searchQuery.isBlank()) products
        else products.filter { it.name.contains(searchQuery, ignoreCase = true) }
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
            // Assign category based on product name or ID
            val category = if (product.name.contains("drink", ignoreCase = true) ||
                product.name.contains("beer", ignoreCase = true) ||
                product.name.contains("juice", ignoreCase = true)) "Drinks" else "Food"
            cartItems = cartItems + CartItem(product, 1, category = category)
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

    // Fix unresolved reference: clearAllItems
    fun clearAllItems() {
        cartItems = emptyList()
    }

    // Fix unresolved reference: toggleExpanded
    fun toggleExpanded(productId: String) {
        cartItems = cartItems.map {
            if (it.product.id == productId) {
                it.copy(isExpanded = !it.isExpanded)
            } else it
        }
    }

    // Fix unresolved reference: updateVariant
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
                    // Logo image icon (ensure always visible)
                    AsyncImage(
                        model = "https://i.ibb.co/SXh30bLz/7caa64de6cf61243f19471a911ec70fd42ddaf2c.jpg",
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )

                    // Hamburger menu icon and dropdown
                    Box(
                        contentAlignment = Alignment.TopStart
                    ) {
                        IconButton(
                            onClick = { showMenuDropdown = !showMenuDropdown },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = showMenuDropdown,
                            onDismissRequest = { showMenuDropdown = false },
                            modifier = Modifier
                                .width(220.dp)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Settings",
                                            tint = Color.Black,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Settings",
                                            color = Color.Black,
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                onClick = { showMenuDropdown = false }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.WifiOff,
                                            contentDescription = "Offline Mode",
                                            tint = Color.Black,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Offline Mode",
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Switch(
                                            checked = offlineMode,
                                            onCheckedChange = { offlineMode = it },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.Black,
                                                uncheckedThumbColor = Color.Gray
                                            )
                                        )
                                    }
                                },
                                onClick = {},
                                enabled = false
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Logout,
                                            contentDescription = "Close Session",
                                            tint = Color.Red,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Close Session",
                                            color = Color.Red,
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                onClick = {
                                    showMenuDropdown = false
                                    onBackClick()
                                }
                            )
                        }
                    }

                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { value -> searchQuery = value },
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
                            .width(280.dp)
                            .height(40.dp)
                            .padding(horizontal = 8.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                        singleLine = true,
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
                        onClick = { showOrdersScreen = true },
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
                    onClick = {
                        if (customer == null) {
                            showAddCustomerDialog = true
                        } else {
                            showCustomerSelectionDialog = true
                        }
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
                        imageVector = Icons.Default.Person,
                        contentDescription = "Add Customer",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (customer == null) "Add Customer" else "${customer!!.name} (Edit)",
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        showQRScanner = true
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

                // Three dots icon for clear menu (only show if cart is not empty)
                if (cartItems.isNotEmpty()) {
                    Box {
                        IconButton(
                            onClick = { showClearMenu = !showClearMenu },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = Color.Black
                            )
                        }
                        if (showClearMenu) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 36.dp, end = 0.dp)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .clickable {
                                            cartItems = emptyList()
                                            showClearMenu = false
                                        }
                                        .background(
                                            Color(0xFFFFEBEE),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .border(
                                            1.dp,
                                            Color(0xFFE57373),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Clear All",
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Clear All Items",
                                        color = Color(0xFFD32F2F),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
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
                if (filteredProducts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No products found",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { addToCart(product) }
                        )
                    }
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
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Empty Cart",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        // Cart items
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
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
                    }

                    // Cart summary - always at bottom
                    Column(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
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

                        // Show Order button only if a customer is selected
                        if (customer != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Order button with chips for each category
                                Button(
                                    onClick = { showOrdersScreen = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Order",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        // Show chips for each category in cart
                                        val categoryCounts = cartItems
                                            .groupBy { it.category }
                                            .mapValues { entry -> entry.value.sumOf { it.quantity } }
                                        categoryCounts.forEach { (category, count) ->
                                            Box(
                                                modifier = Modifier
                                                    .padding(horizontal = 2.dp)
                                                    .background(Color(0xFF212121), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "$category $count",
                                                    color = Color.White,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                // Proceed button
                                Button(
                                    onClick = { showPaymentScreen = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF0D923),
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Proceed",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        } else {
                            // Only show Proceed button if no customer
                            Button(
                                onClick = { showPaymentScreen = true },
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

    // Add Customer Dialog
    if (showAddCustomerDialog) {
        AddCustomerDialog(
            onDismiss = { showAddCustomerDialog = false },
            onNFCSelected = {
                showAddCustomerDialog = false
                // Handle NFC selection
            },
            onRFIDSelected = {
                showAddCustomerDialog = false
                showRFIDDialog = true
            },
            onQRCodeSelected = {
                showAddCustomerDialog = false
                showQRCodeDialog = true
            },
            onCustomerSelectionSelected = {
                showAddCustomerDialog = false
                showCustomerSelectionDialog = true
            },
            onEnterMobileSelected = {
                showAddCustomerDialog = false
                // Handle mobile number entry
            },
            onEnterCustomerNumberSelected = {
                showAddCustomerDialog = false
                // Handle customer number entry
            },
            onEnterEmailSelected = {
                showAddCustomerDialog = false
                // Handle email entry
            },
            onCustomerAccountSelected = {
                showAddCustomerDialog = false
                // Handle customer account
            }
        )
    }

    // RFID Dialog
    if (showRFIDDialog) {
        com.example.pos.ui.customer.RFIDDialog(
            onDismiss = { showRFIDDialog = false },
            onNFCSelected = {
                showRFIDDialog = false
                showAddCustomerDialog = true
            },
            onQRCodeSelected = {
                showRFIDDialog = false
                showQRCodeDialog = true
            },
            onCustomerSelectionSelected = {
                showRFIDDialog = false
                showCustomerSelectionDialog = true
            }
        )
    }

    // QR Code Dialog
    if (showQRCodeDialog) {
        com.example.pos.ui.customer.QRCodeDialog(
            onDismiss = { showQRCodeDialog = false },
            onNFCSelected = {
                showQRCodeDialog = false
                showAddCustomerDialog = true
            },
            onRFIDSelected = {
                showQRCodeDialog = false
                showRFIDDialog = true
            },
            onCustomerSelectionSelected = {
                showQRCodeDialog = false
                showCustomerSelectionDialog = true
            },
            onQRScanned = { scannedData ->
                // Handle QR scan result
                // You can create a customer from scanned data or process it
                showQRCodeDialog = false
            }
        )
    }

    // Customer Selection Dialog
    if (showCustomerSelectionDialog) {
        com.example.pos.ui.customer.CustomerSelectionDialog(
            onDismiss = { showCustomerSelectionDialog = false },
            onCustomerSelected = { selectedCustomer ->
                customer = selectedCustomer
                showCustomerSelectionDialog = false
            }
        )
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

    // Customer add/edit dialog
    if (showCustomerDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showCustomerDialog = false }
        ) {
            com.example.pos.ui.customer.CustomerScreen(
                customer = editingCustomer,
                onSave = { handleAddOrEditCustomer(it) },
                onCancel = { showCustomerDialog = false }
            )
        }
    }

    // Orders list dialog
    if (showOrdersScreen) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showOrdersScreen = false }
        ) {
            com.example.pos.ui.customer.OrderListScreen(
                orders = orders.filter { it.customerId == customer?.id },
                onOrderClick = { showOrdersScreen = false }
            )
        }
    }

    // Payment Screen
    if (showPaymentScreen) {
        PaymentScreen(
            totalAmount = totalAmount,
            onDismiss = { showPaymentScreen = false },
            onPaymentComplete = { showPaymentScreen = false },
            onOrderSuccess = {
                showPaymentScreen = false
                showOrderSuccess = true
            }
        )
    }

    // Order Success Screen
    if (showOrderSuccess) {
        OrderSuccessScreen(
            orderNumber = "545456",
            onDismiss = { showOrderSuccess = false },
            onPrintReceipt = {
                // Handle print receipt
                showOrderSuccess = false
            },
            onSendWhatsApp = { number ->
                // Handle WhatsApp send
                println("Sending to WhatsApp: $number")
            },
            onSendEmail = { email ->
                // Handle email send
                println("Sending to Email: $email")
            }
        )
    }

    // Orders Screen
    if (showOrdersScreen) {
        com.example.pos.ui.orders.OrdersScreen(
            onDismiss = { showOrdersScreen = false }
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
            .size(160.dp)
            .clickable { onProductClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF0D923), RoundedCornerShape(14.dp))
                    .border(2.dp, Color(0xFFE0E0E0), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.name,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rs.${String.format("%.2f", product.price)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
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
        // Main cart item row
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
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
                // Variant and Quantity row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Variant dropdown
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Variant",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
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
                                    fontSize = 12.sp,
                                    color = Color.Black
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
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Decrease button
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
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
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )

                            // Increase button
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
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

                // Divider
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Gray.copy(alpha = 0.3f)
                )
            }
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
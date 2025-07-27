package com.example.pos.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

data class CustomerSearchResult(
    val id: String,
    val name: String,
    val email: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSelectionDialog(
    onDismiss: () -> Unit,
    onCustomerSelected: (Customer) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Dummy customer data
    val allCustomers = remember {
        listOf(
            CustomerSearchResult("1", "Sushil ABC", "sushil@gmail.com"),
            CustomerSearchResult("2", "Oditha W.", "oditha@gmail.com"),
            CustomerSearchResult("3", "John Doe", "john@gmail.com"),
            CustomerSearchResult("4", "Jane Smith", "jane@gmail.com"),
            CustomerSearchResult("5", "Mike Johnson", "mike@gmail.com")
        )
    }

    val filteredCustomers = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allCustomers
        } else {
            allCustomers.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.email.contains(searchQuery, ignoreCase = true)
            }
        }
    }

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
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(600.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header with blue border
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Add Customer",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
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
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Left side - Options (same as before but Customer Selection highlighted)
                        Column(
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomerOption(
                                icon = Icons.Default.Nfc,
                                text = "NFC",
                                isSelected = false,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.CreditCard,
                                text = "RFID",
                                isSelected = false,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.QrCode,
                                text = "QR Code",
                                isSelected = false,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.Person,
                                text = "Customer Selection",
                                isSelected = true,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.Phone,
                                text = "Enter Mobile Number",
                                isSelected = false,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.Badge,
                                text = "Enter Customer Number",
                                isSelected = false,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.Email,
                                text = "Enter Email",
                                isSelected = false,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.AccountCircle,
                                text = "Customer Account",
                                isSelected = false,
                                onClick = { }
                            )
                        }

                        // Right side - Search and Results
                        Column(
                            modifier = Modifier
                                .weight(0.6f)
                                .fillMaxHeight()
                                .padding(start = 16.dp)
                        ) {
                            // Search bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text("Sushil", color = Color.Gray) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                Button(
                                    onClick = { /* Search action */ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF0D923),
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(48.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Customer results
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredCustomers) { customer ->
                                    CustomerResultItem(
                                        customer = customer,
                                        onClick = {
                                            val selectedCustomer = Customer(
                                                id = customer.id,
                                                name = customer.name,
                                                email = customer.email,
                                                phone = ""
                                            )
                                            onCustomerSelected(selectedCustomer)
                                            onDismiss()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = if (isSelected) Color(0xFFF0D923) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp),
            tint = if (isSelected) Color.Black else Color.Gray
        )

        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color.Black else Color.Black,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
private fun CustomerResultItem(
    customer: CustomerSearchResult,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Customer",
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )

        Column {
            Text(
                text = customer.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = customer.email,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
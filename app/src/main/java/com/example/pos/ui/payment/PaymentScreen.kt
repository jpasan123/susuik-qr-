package com.example.pos.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    totalAmount: Double,
    onDismiss: () -> Unit,
    onPaymentComplete: () -> Unit,
    onOrderSuccess: () -> Unit = {}
) {
    var selectedPaymentMethod by remember { mutableStateOf("Card (Rs)") }
    var enteredAmount by remember { mutableStateOf("10000.00") }
    var referenceText by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }

    val cardAmount = 10000.00
    val cashAmount = 9000.00
    val balance = 0.00
    val remaining = 0.00

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
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
                    .width(900.dp)
                    .height(650.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header
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
                                text = "Select Payment Method",
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

                    // Main Content
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        // Left side - Payment Methods
                        Column(
                            modifier = Modifier
                                .weight(0.25f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PaymentMethodOption(
                                icon = Icons.Default.CreditCard,
                                text = "Card (Rs)",
                                isSelected = selectedPaymentMethod == "Card (Rs)",
                                onClick = { selectedPaymentMethod = "Card (Rs)" }
                            )

                            PaymentMethodOption(
                                icon = Icons.Default.Money,
                                text = "Cash (Rs)",
                                isSelected = selectedPaymentMethod == "Cash (Rs)",
                                onClick = { selectedPaymentMethod = "Cash (Rs)" }
                            )

                            PaymentMethodOption(
                                icon = Icons.Default.AccountCircle,
                                text = "Customer Account",
                                isSelected = selectedPaymentMethod == "Customer Account",
                                onClick = { selectedPaymentMethod = "Customer Account" }
                            )

                            PaymentMethodOption(
                                icon = Icons.Default.Stars,
                                text = "Loyalty Points (Rs)",
                                isSelected = selectedPaymentMethod == "Loyalty Points (Rs)",
                                onClick = { selectedPaymentMethod = "Loyalty Points (Rs)" }
                            )

                            PaymentMethodOption(
                                icon = Icons.Default.Receipt,
                                text = "Vouchers (%)",
                                isSelected = selectedPaymentMethod == "Vouchers (%)",
                                onClick = { selectedPaymentMethod = "Vouchers (%)" }
                            )

                            PaymentMethodOption(
                                icon = Icons.Default.LocalOffer,
                                text = "Coupons (Rs)",
                                isSelected = selectedPaymentMethod == "Coupons (Rs)",
                                onClick = { selectedPaymentMethod = "Coupons (Rs)" }
                            )
                        }

                        // Middle section - Payment Details
                        Column(
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxHeight()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Payable Amount
                            Text(
                                text = "Payable Amount",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = "Rs.${String.format("%.2f", totalAmount)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Payment breakdown
                            PaymentBreakdownItem("Card", cardAmount, true)
                            PaymentBreakdownItem("Cash", cashAmount, true)
                            PaymentBreakdownItem("Balance", balance, false)
                            PaymentBreakdownItem("Remaining", remaining, false)

                            Spacer(modifier = Modifier.height(24.dp))

                            // Dynamic content based on selected payment method
                            when (selectedPaymentMethod) {
                                "Card (Rs)" -> {
                                    // Last 4 Digits for reference
                                    Text(
                                        text = "Last 4 Digits for reference (Optional)",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    OutlinedTextField(
                                        value = referenceText,
                                        onValueChange = { referenceText = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Gray,
                                            unfocusedBorderColor = Color.Gray
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Additional Notes
                                    Text(
                                        text = "Additional Notes (Optional)",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    OutlinedTextField(
                                        value = additionalNotes,
                                        onValueChange = { additionalNotes = it },
                                        placeholder = { Text("Type here...") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Gray,
                                            unfocusedBorderColor = Color.Gray
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }

                                "Cash (Rs)" -> {
                                    // Cash Balance section
                                    Text(
                                        text = "Cash Balance",
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = "Rs.1,000.00",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        // Right side - Enter Amount and Number Keypad
                        Column(
                            modifier = Modifier
                                .weight(0.35f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Enter Amount section
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Enter Amount",
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                OutlinedTextField(
                    value = enteredAmount,
                    onValueChange = { value -> enteredAmount = value },
                    placeholder = { Text("Rs. 10000.00") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, color = Color.Black),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { enteredAmount = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Number Keypad at bottom
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                val numbers = listOf(
                                    listOf("1", "2", "3"),
                                    listOf("4", "5", "6"),
                                    listOf("7", "8", "9"),
                                    listOf("0", ".", "⌫")
                                )

                                numbers.forEach { row ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        row.forEach { number ->
                                            NumberButton(
                                                text = number,
                                                onClick = {
                                                    when (number) {
                                                        "⌫" -> {
                                                            if (enteredAmount.isNotEmpty()) {
                                                                enteredAmount = enteredAmount.dropLast(1)
                                                            }
                                                        }
                                                        else -> {
                                                            enteredAmount += number
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Validate Order Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                onPaymentComplete()
                                onOrderSuccess()
                            },
                            modifier = Modifier
                                .width(300.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF0D923),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Validate Order",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodOption(
    icon: ImageVector,
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
private fun PaymentBreakdownItem(
    label: String,
    amount: Double,
    hasCloseIcon: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Rs.${String.format("%.2f", amount)}",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            if (hasCloseIcon) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
private fun NumberButton(
    text: String,
    onClick: () -> Unit
) {
    val backgroundColor = if (text == "⌫") Color.Red else Color(0xFFF5F5F5)
    val textColor = if (text == "⌫") Color.White else Color.Black

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
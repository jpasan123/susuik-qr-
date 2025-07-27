package com.example.pos.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddCustomerDialog(
    onDismiss: () -> Unit,
    onNFCSelected: () -> Unit = {},
    onRFIDSelected: () -> Unit = {},
    onQRCodeSelected: () -> Unit = {},
    onCustomerSelectionSelected: () -> Unit = {},
    onEnterMobileSelected: () -> Unit = {},
    onEnterCustomerNumberSelected: () -> Unit = {},
    onEnterEmailSelected: () -> Unit = {},
    onCustomerAccountSelected: () -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf("NFC") }

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
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .border(
                                width = 2.dp,
                                color = Color(0xFF2196F3),
                                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                            )
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
                        // Left side - Options
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomerOption(
                                icon = Icons.Default.Nfc,
                                text = "NFC",
                                isSelected = selectedOption == "NFC",
                                onClick = {
                                    selectedOption = "NFC"
                                    onNFCSelected()
                                }
                            )

                            CustomerOption(
                                icon = Icons.Default.CreditCard,
                                text = "RFID",
                                isSelected = selectedOption == "RFID",
                                onClick = {
                                    selectedOption = "RFID"
                                    onRFIDSelected()
                                }
                            )

                            CustomerOption(
                                icon = Icons.Default.QrCode,
                                text = "QR Code",
                                isSelected = selectedOption == "QR Code",
                                onClick = {
                                    selectedOption = "QR Code"
                                    onQRCodeSelected()
                                }
                            )

                            CustomerOption(
                                icon = Icons.Default.Person,
                                text = "Customer Selection",
                                isSelected = selectedOption == "Customer Selection",
                                onClick = {
                                    selectedOption = "Customer Selection"
                                    onCustomerSelectionSelected()
                                }
                            )

                            CustomerOption(
                                icon = Icons.Default.Phone,
                                text = "Enter Mobile Number",
                                isSelected = selectedOption == "Enter Mobile Number",
                                onClick = {
                                    selectedOption = "Enter Mobile Number"
                                    onEnterMobileSelected()
                                }
                            )

                            CustomerOption(
                                icon = Icons.Default.Badge,
                                text = "Enter Customer Number",
                                isSelected = selectedOption == "Enter Customer Number",
                                onClick = {
                                    selectedOption = "Enter Customer Number"
                                    onEnterCustomerNumberSelected()
                                }
                            )

                            CustomerOption(
                                icon = Icons.Default.Email,
                                text = "Enter Email",
                                isSelected = selectedOption == "Enter Email",
                                onClick = {
                                    selectedOption = "Enter Email"
                                    onEnterEmailSelected()
                                }
                            )

                            CustomerOption(
                                icon = Icons.Default.AccountCircle,
                                text = "Customer Account",
                                isSelected = selectedOption == "Customer Account",
                                onClick = {
                                    selectedOption = "Customer Account"
                                    onCustomerAccountSelected()
                                }
                            )
                        }

                        // Right side - Content based on selection
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            when (selectedOption) {
                                "NFC" -> {
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .background(Color(0xFFF0D923), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "NFC Character",
                                            modifier = Modifier.size(60.dp),
                                            tint = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Please hold your device close\nto the NFC TAG",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 20.sp
                                    )
                                }
                                "RFID" -> {
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .background(Color(0xFFF0D923), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "RFID Character",
                                            modifier = Modifier.size(60.dp),
                                            tint = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Please hold your device close\nto the RFID",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 20.sp
                                    )
                                }
                                "QR Code" -> {
                                    Box(
                                        modifier = Modifier
                                            .size(280.dp)
                                            .background(Color.Black, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // White scanner frame
                                        Box(
                                            modifier = Modifier
                                                .size(120.dp)
                                                .background(Color.White, RoundedCornerShape(4.dp))
                                                .border(2.dp, Color.White, RoundedCornerShape(4.dp))
                                        )
                                        // Scanner crosshair
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Scanner Target",
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .background(Color(0xFFF0D923), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Character",
                                            modifier = Modifier.size(60.dp),
                                            tint = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Please follow the instructions\nfor $selectedOption",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 20.sp
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
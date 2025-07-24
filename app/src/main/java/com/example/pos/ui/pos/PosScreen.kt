package com.example.pos.ui.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pos.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    viewModel: PosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(POSBackground)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "POS",
                    color = POSTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* Handle settings */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = POSTextPrimary
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle receipt */ }) {
                    Icon(
                        imageVector = Icons.Default.List, // Using List as placeholder for Receipt
                        contentDescription = "Receipt",
                        tint = POSTextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Main Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Cash-in Hand Input Section
                Text(
                    text = "Enter Cash-in Hand Value",
                    fontSize = 16.sp,
                    color = POSTextPrimary,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Input Field
                    OutlinedTextField(
                        value = uiState.cashInHand,
                        onValueChange = viewModel::updateCashInHand,
                        placeholder = {
                            Text(
                                text = "Rs.",
                                color = POSTextSecondary
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(200.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = POSPrimary,
                            unfocusedBorderColor = POSBorder,
                            focusedLabelColor = POSPrimary
                        )
                    )

                    // Power Button
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF424242)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Power",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Start Session Button
                Button(
                    onClick = { viewModel.startSession() },
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = POSYellow,
                        contentColor = Color.Black
                    ),
                    enabled = uiState.cashInHand.isNotBlank()
                ) {
                    Text(
                        text = "Start POS Session",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Bottom Navigation
        BottomNavigation()
    }
}

// Placeholder implementation - you should replace this with your actual Hilt ViewModel provider
fun hiltViewModel(): PosViewModel {
    TODO("Not yet implemented")
}

@Composable
private fun BottomNavigation() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.QrCode, // Using QrCode as placeholder for QrCodeScanner
                label = "Scan",
                isSelected = false
            )
            BottomNavItem(
                icon = Icons.Default.List, // Using List as placeholder for Receipt
                label = "KOT",
                isSelected = false
            )
            BottomNavItem(
                icon = Icons.Default.LocalOffer,
                label = "Coupons",
                isSelected = false
            )
            BottomNavItem(
                icon = Icons.Default.CloudOff,
                label = "Offline",
                isSelected = false
            )
            BottomNavItem(
                icon = Icons.Default.PointOfSale,
                label = "POS",
                isSelected = true
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) POSPrimary else POSIconGray,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) POSPrimary else POSIconGray,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}
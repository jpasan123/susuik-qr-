package com.example.pos.ui.customer

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.journeyapps.barcodescanner.ScanContract

@Composable
fun QRCodeDialog(
    onDismiss: () -> Unit,
    onNFCSelected: () -> Unit = {},
    onRFIDSelected: () -> Unit = {},
    onQRCodeSelected: () -> Unit = {},
    onCustomerSelectionSelected: () -> Unit = {},
    onEnterMobileSelected: () -> Unit = {},
    onEnterCustomerNumberSelected: () -> Unit = {},
    onEnterEmailSelected: () -> Unit = {},
    onCustomerAccountSelected: () -> Unit = {},
    onQRScanned: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // QR Scanner launcher
    val qrScannerLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result.contents != null) {
            onQRScanned(result.contents)
            onDismiss()
        }
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            qrScannerLauncher.launch(
                com.journeyapps.barcodescanner.ScanOptions().apply {
                    setPrompt("Scan QR Code")
                    setBeepEnabled(false)
                    setOrientationLocked(true)
                }
            )
        }
    }

    // Auto-start scanning when dialog opens
    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
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
                                .weight(0.4f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomerOption(
                                icon = Icons.Default.Nfc,
                                text = "NFC",
                                isSelected = false,
                                onClick = onNFCSelected
                            )

                            CustomerOption(
                                icon = Icons.Default.CreditCard,
                                text = "RFID",
                                isSelected = false,
                                onClick = onRFIDSelected
                            )

                            CustomerOption(
                                icon = Icons.Default.QrCode,
                                text = "QR Code",
                                isSelected = true,
                                onClick = { }
                            )

                            CustomerOption(
                                icon = Icons.Default.Person,
                                text = "Customer Selection",
                                isSelected = false,
                                onClick = onCustomerSelectionSelected
                            )

                            CustomerOption(
                                icon = Icons.Default.Phone,
                                text = "Enter Mobile Number",
                                isSelected = false,
                                onClick = onEnterMobileSelected
                            )

                            CustomerOption(
                                icon = Icons.Default.Badge,
                                text = "Enter Customer Number",
                                isSelected = false,
                                onClick = onEnterCustomerNumberSelected
                            )

                            CustomerOption(
                                icon = Icons.Default.Email,
                                text = "Enter Email",
                                isSelected = false,
                                onClick = onEnterEmailSelected
                            )

                            CustomerOption(
                                icon = Icons.Default.AccountCircle,
                                text = "Customer Account",
                                isSelected = false,
                                onClick = onCustomerAccountSelected
                            )
                        }

                        // Right side - QR Scanner Area
                        Box(
                            modifier = Modifier
                                .weight(0.6f)
                                .fillMaxHeight()
                                .background(Color.Black, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // White scanning frame
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .background(Color.White, RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Optional: Add scanning animation or content here
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Scanner crosshair
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Scanner Target",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.White
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Scan button
                                Button(
                                    onClick = {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF0D923),
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Start Scanning",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
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
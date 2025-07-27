package com.example.pos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pos.ui.pos.PosMainScreen
import com.example.pos.ui.pos.PosScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = "login"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            com.example.pos.ui.login.LoginScreen(
                onLoginSuccess = {
                    navController.navigate("pos_screen") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("pos_screen") {
            PosScreen(
                onStartSession = {
                    navController.navigate("pos_main_screen") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("pos_main_screen") {
            PosMainScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
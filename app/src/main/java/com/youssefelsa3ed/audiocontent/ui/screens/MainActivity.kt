package com.youssefelsa3ed.audiocontent.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youssefelsa3ed.audiocontent.ui.theme.AudioContentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioContentTheme {
                AudioContentApp()
            }
        }
    }
}

@Composable
fun AudioContentApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate("search")
                }
            )
        }
        composable("search") {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
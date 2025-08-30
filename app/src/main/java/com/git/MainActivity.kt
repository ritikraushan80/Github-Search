package com.git

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.git.ui.screens.ProfileScreen
import com.git.ui.screens.SearchScreen
import com.git.ui.theme.GitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            GitTheme(darkTheme = isDarkTheme) {
                val statusBarColor = MaterialTheme.colorScheme.primaryContainer.toArgb()
                SideEffect {
                    window.statusBarColor = statusBarColor
                    WindowInsetsControllerCompat(
                        window,
                        window.decorView
                    ).isAppearanceLightStatusBars = true
                }
                App(
                    isDarkTheme = isDarkTheme, onToggleTheme = { isDarkTheme = it })
            }
        }
    }
}

@Composable
fun App(isDarkTheme: Boolean, onToggleTheme: (Boolean) -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "search") {
        composable("search") { SearchScreen(navController, isDarkTheme, onToggleTheme) }
        composable(
            "profile/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ProfileScreen(username, isDarkTheme, onToggleTheme, onNavigateBack = {
                navController.previousBackStackEntry?.savedStateHandle?.set("clear_results", true)
                navController.popBackStack()
            })
        }
    }
}
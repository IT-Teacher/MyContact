package uz.itteacher.mycontact.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.itteacher.mycontact.layout.CreateContactScreen
import uz.itteacher.mycontact.layout.HistoryScreen
import uz.itteacher.mycontact.layout.MainContactScreen

@Composable
fun ContactNavigation(
    navController: NavHostController,
    startDestination: String = "main",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            MainContactScreen(navController)
        }
        composable("create") {
            CreateContactScreen(navController,
                onSave = { name, phone ->

                })
        }
        composable("history") {
            HistoryScreen(navController)
        }
    }
}
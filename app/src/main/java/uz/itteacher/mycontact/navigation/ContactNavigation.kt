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
        composable("create/{id}") {
            val id = it.arguments?.getString("id")
            CreateContactScreen(navController, id!!,
                onSave = { name, phone ->

                })
        }
        composable("history") {
            HistoryScreen(navController)
        }

    }
}
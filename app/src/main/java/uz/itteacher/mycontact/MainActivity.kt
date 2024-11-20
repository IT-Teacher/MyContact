package uz.itteacher.mycontact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import uz.itteacher.mycontact.layout.MainContactScreen
import uz.itteacher.mycontact.navigation.ContactNavigation
import uz.itteacher.mycontact.ui.theme.MyContactTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyContactTheme {
               Column(modifier = Modifier.padding()) {
                  ContactNavigation(navController = rememberNavController())
              }

            }
        }
    }
}
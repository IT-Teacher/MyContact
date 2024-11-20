package uz.itteacher.mycontact.layout


import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import uz.itteacher.mycontact.database.AppDataBase
import uz.itteacher.mycontact.model.ContactUser
import java.io.File


data class Contact(val name: String, val phone: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContactScreen(navHostController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
//    val contacts = remember {
//        mutableStateListOf(
//            Contact("Alice Johnson", "123-456-7890"),
//            Contact("Bob Smith", "234-567-8901"),
//            Contact("Charlie Brown", "345-678-9012"),
//            Contact("Diana Prince", "456-789-0123")
//        )
//    }
    val context = LocalContext.current
    val appDataBase = AppDataBase.getIntance(LocalContext.current)
    val contacts by appDataBase.contactUserDAO().getAll().collectAsState(initial = emptyList())
//    val filteredContacts = if (searchQuery.isEmpty()) {
//        contacts
//    } else {
//        contacts.filter { it.name.contains(searchQuery, ignoreCase = true) }
//    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { navHostController.navigate("create") }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Contact",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Contacts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Contact List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(contacts) { contact ->
                    ContactItem(contact)
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: ContactUser) {

    val filePath = contact.imageIdRes
    val file = File(filePath)
    val uriFromFile: Uri = Uri.fromFile(file)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle contact click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                if(contact.imageIdRes != null){
                    AsyncImage(
                        model = uriFromFile,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Crop,  // Adjust how the image fits inside the container
                        error = painterResource(id = android.R.drawable.sym_call_missed),  // Optional error image
                        placeholder = painterResource(id = android.R.drawable.sym_call_missed) // Optional placeholder
                    )
                }
                else{
                    Text(
                        text = contact.userName.first().toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = contact.userName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = contact.phone,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}



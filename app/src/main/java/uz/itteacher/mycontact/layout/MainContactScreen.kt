package uz.itteacher.mycontact.layout


import android.R.attr.onClick
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uz.itteacher.mycontact.database.AppDataBase
import uz.itteacher.mycontact.model.ContactUser
import java.io.File


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContactScreen(navHostController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }

    val appDataBase = AppDataBase.getIntance(LocalContext.current)

    var contacts by remember { mutableStateOf(listOf<ContactUser>()) }

    val filteredContacts = if (searchQuery.isEmpty()) {
        contacts
    } else {
        contacts.filter { it.userName.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) {
        contacts = appDataBase.contactUserDAO().getAll()
    }

    Log.d("TAG", "MainContactScreen: $contacts")




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    var id = -1
                    IconButton(onClick = { navHostController.navigate("create/$id") }) {
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



            LazyColumn {
                items(filteredContacts, key = { it.id }) { contact ->
                    ContactItem(
                        contact = contact,
                            onDelete = {
                                    appDataBase.contactUserDAO().deleteContactUser(contact)
                                    contacts = appDataBase.contactUserDAO().getAll()

                            },
                        onEdit = {
                            navHostController.navigate("create/${contact.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: ContactUser,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {

    var swipeOffset by remember { mutableStateOf(0f) }
    val maxSwipeOffset = 300f


    Box(
        modifier = Modifier
            .fillMaxWidth()) {

        Row(
            modifier = Modifier
                .background(Color.Red)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Edit tugmasi
            IconButton(
                onClick = onEdit,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White)
            }

            // Delete tugmasi
            IconButton(
                onClick = onDelete,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Edit", tint = Color.White)
            }


        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeOffset.toInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        swipeOffset = (swipeOffset + delta).coerceIn(0f, maxSwipeOffset)
                    },
                    onDragStopped = {
                        swipeOffset = if (swipeOffset > maxSwipeOffset / 2) {
                            maxSwipeOffset
                        } else {
                            0f
                        }
                    }
                )
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
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
                        if (contact.imageIdRes != null) {
                            val filePath = contact.imageIdRes
                            val uriFromFile: Uri? = filePath?.let { Uri.fromFile(File(it)) }
                            AsyncImage(
                                model = uriFromFile,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(150.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
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
    }
}




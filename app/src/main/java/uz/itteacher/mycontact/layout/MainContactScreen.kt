package uz.itteacher.mycontact.layout


import android.R.attr.onClick
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uz.itteacher.mycontact.database.AppDataBase
import uz.itteacher.mycontact.model.ContactUser
import java.io.File
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import uz.itteacher.mycontact.R
import uz.itteacher.mycontact.model.CallHistory
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.log


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
    val recentCalls by remember { mutableStateOf(appDataBase.callHistoryDAO().getRecentCalls()) }
    var permissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }



    LaunchedEffect(Unit) {
        contacts = appDataBase.contactUserDAO().getAll()
    }






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

            RecentCallsCard(recentCalls = recentCalls) {
                if (permissionGranted) {
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${contacts[0].phone}")
                    }
                    val date = Date(System.currentTimeMillis())
                    val formatter = SimpleDateFormat("hh:mm:ss/dd:MM:yyyy", Locale.getDefault())
                    val dateString = formatter.format(date)
                    context.startActivity(intent)
//                            Log.d("000000000000000000000000", "ContactItem: ${contact.id}, ${dateString}")
                    appDataBase
                        .callHistoryDAO()
                        .insertCallHistory(
                            CallHistory(
                                userContactId = contacts[0].id,
                                callType = uz.itteacher.mycontact.model.CALLTYPE.OUTGOING,
                                callDate = dateString
                            )
                        )

                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
            }



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
                        },
                        navHostController
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
    onEdit: () -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val appDataBase = AppDataBase.getIntance(context)
    var swipeOffset by remember { mutableStateOf(0f) }
    val maxSwipeOffset = 300f
    var permissionGranted by remember { mutableStateOf(false) }


    // Launcher for permission request
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }) {

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
                    .clickable {

                        val intent = Intent(Intent.ACTION_CALL).apply {
                            data = Uri.parse("tel:${contact.phone}")
                        }
                        val date = Date(System.currentTimeMillis())
                        val formatter = SimpleDateFormat("hh:mm:ss/dd:MM:yyyy", Locale.getDefault())
                        val dateString = formatter.format(date)
                        if (permissionGranted) {
                            context.startActivity(intent)
//                            Log.d("000000000000000000000000", "ContactItem: ${contact.id}, ${dateString}")
                            appDataBase
                                .callHistoryDAO()
                                .insertCallHistory(
                                    CallHistory(
                                        userContactId = contact.id,
                                        callType = uz.itteacher.mycontact.model.CALLTYPE.OUTGOING,
                                        callDate = dateString
                                    )
                                )

                        } else {
                            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
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

                    IconButton(
                        onClick = {
                            navHostController.navigate("history/${contact.id}")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "info"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentCallsCard(recentCalls: List<CallHistory>, onCallClick: (ContactUser) -> Unit) {
    val pagerState = rememberPagerState(pageCount = {recentCalls.size})
    val appdataBase = AppDataBase.getIntance(LocalContext.current)
    if (recentCalls.isNotEmpty()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) { page ->
            val contact = appdataBase.contactUserDAO().getById(recentCalls[page].userContactId)
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Contact image and details

                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = contact.userName, style = MaterialTheme.typography.headlineLarge)
                        Text(text = contact.phone, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    // Call button
                    Button(onClick = { onCallClick(contact) }) {
                        Text("Call")
                    }
                }
            }
        }
    }
}





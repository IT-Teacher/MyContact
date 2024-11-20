package uz.itteacher.mycontact.layout


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uz.itteacher.mycontact.database.AppDataBase
import uz.itteacher.mycontact.model.ContactUser
import uz.itteacher.mycontact.saveImageToFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateContactScreen(navHostController: NavHostController, onSave: (String, String) -> Unit = { _, _ -> }) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var isNameValid by remember { mutableStateOf(true) }
    var isPhoneValid by remember { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val appDataBase = AppDataBase.getIntance(context)

    // Launcher for gallery or camera
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Save the image to the file system and get the file path
            val savedImagePath = saveImageToFile(context, it.toString()) // Save the image
            if (savedImagePath != null) {
                imageUri = savedImagePath // Store the file path (image URI)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        // Use a temporary content resolver URI for this bitmap if required
        // Here, we'll assume it's handled by the app logic
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Contact") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Display the selected image
            Image(
                painter = if (imageUri != null) {
                    rememberAsyncImagePainter(model = imageUri)
                } else {
                    painterResource(id = android.R.drawable.ic_menu_gallery)
                },
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(164.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // "+" Button
            Button(
                onClick = {
                    galleryLauncher.launch("image/*") // Launch the gallery
                },
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Image", tint = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    isNameValid = it.text.isNotBlank()
                },
                label = { Text("Name") },
                isError = !isNameValid,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isNameValid) {
                Text(
                    text = "Name cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Input
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    isPhoneValid = it.text.matches(Regex("^[0-9]{10,15}$"))
                },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = !isPhoneValid,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isPhoneValid) {
                Text(
                    text = "Enter a valid phone number",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = {
                    if (isNameValid && isPhoneValid) {
                        appDataBase.contactUserDAO()
                            .insertContactUser(ContactUser(userName = name.text, phone = phone.text, email = "", imageIdRes = imageUri ))
                        navHostController.navigate("main")
                    }
                },
                enabled = isNameValid && isPhoneValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Contact")
            }
        }
    }
}

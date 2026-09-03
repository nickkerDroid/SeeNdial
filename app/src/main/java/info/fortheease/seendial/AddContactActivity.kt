package info.fortheease.seendial

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import info.fortheease.seendial.ui.theme.SeeNdialTheme
import kotlinx.coroutines.launch

class AddContactActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepository

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepository = UserRepository(application)

        setContent {
            SeeNdialTheme {
                var phoneNumber by remember { mutableStateOf("") }
                var imageUri by remember { mutableStateOf<Uri?>(null) }
                val coroutineScope = rememberCoroutineScope()

                val galleryLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    imageUri = uri
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Add Contact") }
                        )
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Selected Photo",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clickable { galleryLauncher.launch("image/*") }
                            )
                        } else {
                            Button(onClick = { galleryLauncher.launch("image/*") }) {
                                Text("Select Photo")
                            }
                        }

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (imageUri != null && phoneNumber.isNotBlank()) {
                                    coroutineScope.launch {
                                        // Save logic (In real app, copy URI to internal storage first)
                                        val user = User(
                                            phoneNum = phoneNumber,
                                            imgAddress = imageUri.toString()
                                        )
                                        userRepository.insert(user)
                                        finish()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Contact")
                        }
                    }
                }
            }
        }
    }
}

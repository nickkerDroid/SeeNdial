package info.fortheease.seendial

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import info.fortheease.seendial.safety.EmergencyManager
import info.fortheease.seendial.ui.theme.SeeNdialTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var userRepository: UserRepository
    private lateinit var emergencyManager: EmergencyManager
    
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Permissions handled dynamically when requested
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        tts = TextToSpeech(this, this)
        userRepository = UserRepository(application)
        emergencyManager = EmergencyManager(this)
        
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS
            )
        )

        setContent {
            SeeNdialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val users by userRepository.allUsers.collectAsState(initial = emptyList())
                    SeeNdialApp(
                        users = users,
                        onContactTap = { user -> speakAndDial(user.uid, user.phoneNum) },
                        onAddContact = {
                            if (users.size >= 9) {
                                startActivity(Intent(this@MainActivity, info.fortheease.seendial.billing.PaywallActivity::class.java))
                            } else {
                                startActivity(Intent(this@MainActivity, AddContactActivity::class.java))
                            }
                        },
                        onEmergencyTap = {
                            emergencyManager.triggerSOS("911") // default emergency number
                        }
                    )
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
        }
    }
    
    fun speakAndDial(name: String, phoneNumber: String) {
        tts.speak("Calling $name", TextToSpeech.QUEUE_FLUSH, null, null)
        window.decorView.postDelayed({
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
            try {
                startActivity(intent)
            } catch (e: SecurityException) {
                // Ignore for now
            }
        }, 2000)
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeNdialApp(
    users: List<User>,
    onContactTap: (User) -> Unit,
    onAddContact: () -> Unit,
    onEmergencyTap: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SeeNdial") },
                actions = {
                    IconButton(onClick = onEmergencyTap) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddContact) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                ContactItem(user = user, onClick = { onContactTap(user) })
            }
        }
    }
}

@Composable
fun ContactItem(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // We will need Coil for image loading.
        val painter = rememberAsyncImagePainter(model = File(user.imgAddress))
        Image(
            painter = painter,
            contentDescription = "Contact Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

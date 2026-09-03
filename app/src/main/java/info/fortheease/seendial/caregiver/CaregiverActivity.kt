package info.fortheease.seendial.caregiver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import info.fortheease.seendial.ui.theme.SeeNdialTheme
import kotlin.random.Random

class CaregiverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simulating generating a code
        val generatedCode = Random.nextInt(100000, 999999).toString()

        setContent {
            SeeNdialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CaregiverScreen(
                        pairingCode = generatedCode,
                        onCodeEntered = { code ->
                            // TODO: Verify code in Firestore and link accounts
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverScreen(
    pairingCode: String,
    onCodeEntered: (String) -> Unit
) {
    var inputCode by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Caregiver Setup",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your Pairing Code", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = pairingCode,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Give this code to your caregiver to link accounts.")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "- OR -", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = inputCode,
            onValueChange = { inputCode = it },
            label = { Text("Enter a code to become a caregiver") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { onCodeEntered(inputCode) },
            modifier = Modifier.fillMaxWidth(),
            enabled = inputCode.length == 6
        ) {
            Text("Link Account")
        }
    }
}

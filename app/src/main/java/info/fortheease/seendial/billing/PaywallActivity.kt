package info.fortheease.seendial.billing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import info.fortheease.seendial.ui.theme.SeeNdialTheme

class PaywallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // In a real scenario, you'd pass the BillingManager here
        // and trigger the purchase flow when buttons are clicked.

        setContent {
            SeeNdialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PaywallScreen(
                        onPurchasePro = { 
                            // TODO: Call billingClient.launchBillingFlow for PRO_PRODUCT_ID
                            finish()
                        },
                        onSubscribeCaregiver = {
                            // TODO: Call billingClient.launchBillingFlow for CAREGIVER_PRODUCT_ID
                            finish()
                        },
                        onDismiss = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun PaywallScreen(
    onPurchasePro: () -> Unit,
    onSubscribeCaregiver: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Upgrade to SeeNdial Pro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "You've reached the limit of 9 free contacts. Upgrade to unlock more features!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Pro Lifetime", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(text = "• Unlimited Contacts\n• Cloud Backup\n• Custom Themes")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onPurchasePro, modifier = Modifier.fillMaxWidth()) {
                    Text("Unlock for $1.99")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Caregiver Mode", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(text = "• Remote Contact Management\n• Real-time Sync\n• Priority Support")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onSubscribeCaregiver, modifier = Modifier.fillMaxWidth()) {
                    Text("Subscribe for $9.99/yr")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        TextButton(onClick = onDismiss) {
            Text("Maybe Later")
        }
    }
}

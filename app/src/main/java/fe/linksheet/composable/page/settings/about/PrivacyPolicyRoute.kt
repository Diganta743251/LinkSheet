package fe.linksheet.composable.page.settings.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fe.linksheet.R
import fe.linksheet.composable.component.page.SaneScaffoldSettingsPage

@Composable
fun PrivacyPolicyRoute(
    onBackPressed: () -> Unit,
) {
    SaneScaffoldSettingsPage(
        headline = stringResource(id = R.string.privacy_policy),
        onBackPressed = onBackPressed
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PrivacyTip,
                        contentDescription = stringResource(id = R.string.privacy_policy),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "NO DATA COLLECTION",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "All links are stored locally on your device. LinkMaster does not collect, transmit, or store any of your browsing data on external servers.",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "What We Don't Collect:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("• Browsing history")
            Text("• Personal information")
            Text("• Device identifiers")
            Text("• Location data")
            Text("• Analytics or tracking")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "AdMob Usage (Free Version Only)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "AdMob is used only in the free version and follows Google's privacy policy. Pro Version: 0 trackers, 0 internet permissions, 100% offline.",
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Link Processing:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("• Links are processed locally on your device")
            Text("• No external services used for link analysis")
            Text("• Your browsing patterns remain private")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Data Export:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("When you export your link history, data is saved directly to your device using Android's Storage Access Framework. No data is transmitted to external servers.")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Permissions:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("• INTERNET: Only for AdMob ads in free version")
            Text("• ACCESS_NETWORK_STATE: To check connectivity for ads")
            Text("• FOREGROUND_SERVICE: For battery monitoring")
            Text("• POST_NOTIFICATIONS: For app notifications")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Contact:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("For privacy questions, contact us through the app's About section.")
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Last updated: January 2025",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
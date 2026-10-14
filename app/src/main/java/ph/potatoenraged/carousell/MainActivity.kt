package ph.potatoenraged.carousell

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CarousellHome()
                }
            }
        }
    }
}

@Composable
private fun CarousellHome() {
    val context = LocalContext.current
    var query by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text("Carousell Companion", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("A faster personal marketplace launcher", style = MaterialTheme.typography.bodyMedium)
            }
        }
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            label = { Text("Search Carousell Philippines") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Your marketplace, one tap away", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("This first build is a native starting point. Account, chat, favorites, and listing actions will use supported Carousell flows rather than private endpoints.")
                Button(
                    onClick = {
                        val base = Uri.parse("https://www.carousell.ph/")
                        val target = if (query.isBlank()) {
                            base
                        } else {
                            base.buildUpon()
                                .appendPath("search")
                                .appendQueryParameter("query", query.trim())
                                .build()
                        }
                        context.startActivity(Intent(Intent.ACTION_VIEW, target))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (query.isBlank()) "Open Carousell" else "Search on Carousell") }
                OutlinedButton(
                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.carousell.ph/"))) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Browse marketplace") }
            }
        }
        Spacer(Modifier.height(2.dp))
        Text("Next: refine search handoff, add favorites for app shortcuts, and test navigation on-device.", style = MaterialTheme.typography.bodySmall)
    }
}

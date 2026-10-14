package ph.potatoenraged.carousell

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private const val CAROUSELL_HOME = "https://www.carousell.ph/"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkMode by rememberSaveable { mutableStateOf(false) }
            MaterialTheme(
                colorScheme = if (darkMode) androidx.compose.material3.darkColorScheme()
                else androidx.compose.material3.lightColorScheme()
            ) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CarousellHome(
                        darkMode = darkMode,
                        onToggleDarkMode = { darkMode = !darkMode }
                    )
                }
            }
        }
    }
}

@Composable
private fun CarousellHome(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val context = LocalContext.current
    var query by rememberSaveable { mutableStateOf("") }
    val recentSearches = rememberSaveable { mutableStateListOf<String>() }
    val categories = listOf("Electronics", "Fashion", "Home & Living", "Hobbies", "Vehicles")

    fun openCarousell(url: String = CAROUSELL_HOME) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun searchCarousell(value: String) {
        val cleaned = value.trim()
        if (cleaned.isEmpty()) {
            openCarousell()
            return
        }
        recentSearches.remove(cleaned)
        recentSearches.add(0, cleaned)
        while (recentSearches.size > 5) recentSearches.removeAt(recentSearches.lastIndex)

        val target = Uri.parse(CAROUSELL_HOME).buildUpon()
            .appendPath("search")
            .appendPath(cleaned)
            .build()
        openCarousell(target.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Storefront,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Carousell Companion",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("A lightweight personal launcher", style = MaterialTheme.typography.bodyMedium)
            }
            OutlinedButton(onClick = onToggleDarkMode) {
                Icon(Icons.Default.DarkMode, contentDescription = "Toggle dark mode")
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            label = { Text("What are you looking for?") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        Button(
            onClick = { searchCarousell(query) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (query.isBlank()) "Open Carousell" else "Search Carousell")
        }

        Text("Browse categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = false,
                    onClick = { searchCarousell(category) },
                    label = { Text(category) }
                )
            }
        }

        if (recentSearches.isNotEmpty()) {
            Text("Recent searches", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                recentSearches.forEach { recent ->
                    OutlinedButton(
                        onClick = {
                            query = recent
                            searchCarousell(recent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(recent)
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "Your marketplace, one tap away",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Search and browsing open on Carousell's official site. This app does not ask for your Carousell password or use private endpoints."
                )
                OutlinedButton(
                    onClick = { openCarousell() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Browse marketplace")
                }
            }
        }

        Spacer(Modifier.height(2.dp))
        Text(
            "Personal-use preview • Direct account features require an authorized integration.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

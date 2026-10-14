package ph.potatoenraged.carousell

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.json.JSONArray
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private const val CAROUSELL_HOME = "https://www.carousell.ph/"
private const val PREFERENCES_NAME = "carousell_companion"
private const val RECENT_SEARCHES_KEY = "recent_searches"
private const val MAX_RECENT_SEARCHES = 8

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
                    CarousellHome(onToggleDarkMode = { darkMode = !darkMode })
                }
            }
        }
    }
}

private fun loadRecentSearches(context: Context): List<String> {
    val saved = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        .getString(RECENT_SEARCHES_KEY, null) ?: return emptyList()
    return try {
        val array = JSONArray(saved)
        buildList {
            for (index in 0 until array.length()) {
                val value = array.optString(index).trim()
                if (value.isNotEmpty()) add(value)
            }
        }.distinctBy { it.lowercase() }.take(MAX_RECENT_SEARCHES)
    } catch (_: Exception) {
        emptyList()
    }
}

private fun saveRecentSearches(context: Context, searches: List<String>) {
    val array = JSONArray()
    searches.forEach { array.put(it) }
    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(RECENT_SEARCHES_KEY, array.toString())
        .apply()
}

@Composable
private fun CarousellHome(onToggleDarkMode: () -> Unit) {
    val context = LocalContext.current
    var query by rememberSaveable { mutableStateOf("") }
    var recentSearches by remember { mutableStateOf(loadRecentSearches(context)) }
    var launchError by rememberSaveable { mutableStateOf(false) }
    val categories = listOf("Electronics", "Fashion", "Home & Living", "Hobbies", "Vehicles")

    fun openCarousell(url: String = CAROUSELL_HOME) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            launchError = false
        } catch (_: ActivityNotFoundException) {
            launchError = true
        }
    }

    fun searchCarousell(value: String) {
        val cleaned = value.trim()
        if (cleaned.isEmpty()) {
            openCarousell()
            return
        }

        recentSearches = (listOf(cleaned) +
            recentSearches.filterNot { it.equals(cleaned, ignoreCase = true) })
            .take(MAX_RECENT_SEARCHES)
        saveRecentSearches(context, recentSearches)

        val target = buildCarousellSearchUrl(cleaned)
        if (target == null) {
            openCarousell()
        } else {
            openCarousell(target)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            IconButton(onClick = onToggleDarkMode) {
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

        if (launchError) {
            Text(
                "No browser or Carousell app was found to open this link.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text("Browse categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = false,
                    onClick = { query = category; searchCarousell(category) },
                    label = { Text(category) }
                )
            }
        }

        if (recentSearches.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recent searches", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = {
                    recentSearches = emptyList()
                    saveRecentSearches(context, recentSearches)
                }) {
                    Text("Clear")
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                recentSearches.forEach { recent ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                query = recent
                                searchCarousell(recent)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(recent, maxLines = 1)
                        }
                        IconButton(onClick = {
                            recentSearches = recentSearches.filterNot { it == recent }
                            saveRecentSearches(context, recentSearches)
                        }) {
                            Text("×", style = MaterialTheme.typography.titleLarge)
                        }
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

package ph.potatoenraged.carousell

import android.content.Context
import android.os.Bundle
import org.json.JSONArray
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

private const val PREFERENCES_NAME = "carousell_companion"
private const val RECENT_SEARCHES_KEY = "recent_searches"
private const val DARK_MODE_KEY = "dark_mode"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkMode by rememberSaveable {
                mutableStateOf(
                    getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                        .getBoolean(DARK_MODE_KEY, false)
                )
            }
            MaterialTheme(
                colorScheme = if (darkMode) androidx.compose.material3.darkColorScheme()
                else androidx.compose.material3.lightColorScheme()
            ) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CarousellHome(isDarkMode = darkMode, onToggleDarkMode = {
                        darkMode = !darkMode
                        getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                            .edit()
                            .putBoolean(DARK_MODE_KEY, darkMode)
                            .apply()
                    })
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
        normalizeSearchHistory(buildList {
            for (index in 0 until array.length()) add(array.optString(index))
        })
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
private fun CarousellHome(isDarkMode: Boolean, onToggleDarkMode: () -> Unit) {
    val context = LocalContext.current
    var query by rememberSaveable { mutableStateOf("") }
    var activeSearch by rememberSaveable { mutableStateOf<String?>(null) }
    var recentSearches by remember { mutableStateOf(loadRecentSearches(context)) }
    val categories = listOf("Electronics", "Fashion", "Home & Living", "Hobbies", "Vehicles")

    fun submitSearch(value: String) {
        val cleaned = value.trim()
        if (cleaned.isEmpty()) return
        query = cleaned
        recentSearches = addRecentSearch(cleaned, recentSearches)
        saveRecentSearches(context, recentSearches)
        activeSearch = cleaned
    }

    BackHandler(enabled = activeSearch != null) {
        activeSearch = null
    }

    if (activeSearch != null) {
        NativeSearchScreen(
            query = activeSearch!!,
            onBack = { activeSearch = null },
            onEditSearch = { query = it; activeSearch = null }
        )
        return
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
            Icon(Icons.Default.Storefront, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text("Carousell Companion", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Your native marketplace companion", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onToggleDarkMode) {
                Icon(
                    Icons.Default.DarkMode,
                    contentDescription = if (isDarkMode) {
                        "Switch to light mode"
                    } else {
                        "Switch to dark mode"
                    }
                )
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            label = { Text("What are you looking for?") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { submitSearch(query) })
        )

        Button(onClick = { submitSearch(query) }, modifier = Modifier.fillMaxWidth()) {
            Text("Search marketplace")
        }

        Text("Explore categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = false,
                    onClick = { query = category; submitSearch(category) },
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
                }) { Text("Clear") }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                recentSearches.forEach { recent ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { query = recent; submitSearch(recent) },
                            modifier = Modifier.weight(1f)
                        ) { Text(recent, maxLines = 1) }
                        IconButton(onClick = {
                            recentSearches = removeRecentSearch(recent, recentSearches)
                            saveRecentSearches(context, recentSearches)
                        }) { Icon(Icons.Default.Close, contentDescription = "Remove $recent") }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Built for a native experience", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Search stays inside this app. Live Carousell listings will appear here once a supported data integration is available.")
            }
        }
    }
}

@Composable
private fun NativeSearchScreen(
    query: String,
    onBack: () -> Unit,
    onEditSearch: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Search", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(query, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Native results screen", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    "This screen is inside Carousell Companion. Live listings are not connected yet, so we won't show invented products or redirect you to a browser."
                )
                Text(
                    "Next requirement: an authorized Carousell integration or another permitted listing data source.",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedButton(onClick = { onEditSearch(query) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Edit search")
                }
            }
        }
        Spacer(Modifier.height(2.dp))
    }
}


package ph.potatoenraged.carousell

internal const val MAX_RECENT_SEARCHES = 8

internal fun normalizeSearchHistory(searches: List<String>): List<String> {
    val unique = LinkedHashMap<String, String>()
    searches.forEach { raw ->
        val cleaned = raw.trim()
        if (cleaned.isNotEmpty()) {
            val key = cleaned.lowercase()
            if (!unique.containsKey(key)) unique[key] = cleaned
        }
    }
    return unique.values.take(MAX_RECENT_SEARCHES)
}

internal fun addRecentSearch(query: String, current: List<String>): List<String> {
    val cleaned = query.trim()
    if (cleaned.isEmpty()) return normalizeSearchHistory(current)
    return normalizeSearchHistory(listOf(cleaned) + current.filterNot {
        it.trim().equals(cleaned, ignoreCase = true)
    })
}

internal fun removeRecentSearch(query: String, current: List<String>): List<String> =
    normalizeSearchHistory(current.filterNot { it.equals(query, ignoreCase = true) })

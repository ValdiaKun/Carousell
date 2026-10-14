package ph.potatoenraged.carousell

internal const val MAX_RECENT_SEARCHES = 8

internal fun normalizeSearchHistory(searches: List<String>): List<String> {
    val unique = mutableListOf<String>()
    for (raw in searches) {
        val cleaned = raw.trim()
        if (cleaned.isNotEmpty() && unique.none { it.equals(cleaned, ignoreCase = true) }) {
            unique.add(cleaned)
            if (unique.size == MAX_RECENT_SEARCHES) break
        }
    }
    return unique
}

internal fun addRecentSearch(query: String, current: List<String>): List<String> {
    val cleaned = query.trim()
    if (cleaned.isEmpty()) return normalizeSearchHistory(current)
    return normalizeSearchHistory(listOf(cleaned) + current.filterNot {
        it.trim().equals(cleaned, ignoreCase = true)
    })
}

internal fun removeRecentSearch(query: String, current: List<String>): List<String> {
    val cleaned = query.trim()
    if (cleaned.isEmpty()) return normalizeSearchHistory(current)
    return normalizeSearchHistory(current.filterNot {
        it.trim().equals(cleaned, ignoreCase = true)
    })
}

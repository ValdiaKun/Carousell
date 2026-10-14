package ph.potatoenraged.carousell

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchHistoryTest {
    @Test
    fun addingSearchTrimsAndMovesDuplicateToFront() {
        assertEquals(
            listOf("IPHONE", "camera"),
            addRecentSearch(" IPHONE ", listOf("camera", "iphone"))
        )
    }

    @Test
    fun blankSearchDoesNotCreateHistoryEntry() {
        assertEquals(listOf("camera"), addRecentSearch("   ", listOf("camera")))
    }

    @Test
    fun normalizingHistoryDropsBlankAndCaseInsensitiveDuplicates() {
        assertEquals(
            listOf("Camera", "iPhone"),
            normalizeSearchHistory(listOf(" Camera ", "", "camera", "iPhone"))
        )
    }

    @Test
    fun historyIsLimitedToMaximumEntries() {
        val entries = (1..(MAX_RECENT_SEARCHES + 3)).map { "item$it" }
        assertEquals(MAX_RECENT_SEARCHES, normalizeSearchHistory(entries).size)
        assertEquals(entries.take(MAX_RECENT_SEARCHES), normalizeSearchHistory(entries))
    }

    @Test
    fun removeSearchIsCaseInsensitive() {
        assertEquals(
            listOf("camera"),
            removeRecentSearch("IPHONE", listOf("iPhone", "camera"))
        )
    }
}

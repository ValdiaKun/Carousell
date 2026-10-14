package ph.potatoenraged.carousell

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SearchUrlTest {
    @Test
    fun blankQueryReturnsNull() {
        assertNull(buildCarousellSearchUrl(""))
        assertNull(buildCarousellSearchUrl("   "))
    }

    @Test
    fun trimsQueryAndBuildsSearchRoute() {
        assertEquals(
            "https://www.carousell.ph/search/iphone%2013/",
            buildCarousellSearchUrl("  iphone 13  ")
        )
    }

    @Test
    fun encodesReservedCharactersAsOnePathSegment() {
        assertEquals(
            "https://www.carousell.ph/search/C%2B%2B%20phone%2Fcase%3F/",
            buildCarousellSearchUrl("C++ phone/case?")
        )
    }

    @Test
    fun encodesNonAsciiCharactersAsUtf8() {
        assertEquals(
            "https://www.carousell.ph/search/%E7%9B%B8%E6%9C%BA/",
            buildCarousellSearchUrl("相机")
        )
    }
}

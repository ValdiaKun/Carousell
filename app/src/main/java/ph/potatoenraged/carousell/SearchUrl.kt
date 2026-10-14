package ph.potatoenraged.carousell

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Builds the public Carousell Philippines search URL without depending on Android APIs.
 * Returns null for blank input so callers can open the marketplace homepage instead.
 */
internal fun buildCarousellSearchUrl(query: String): String? {
    val cleaned = query.trim()
    if (cleaned.isEmpty()) return null

    val encoded = URLEncoder.encode(cleaned, StandardCharsets.UTF_8.name())
        .replace("+", "%20")
    return "https://www.carousell.ph/search/$encoded/"
}

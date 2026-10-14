# Carousell Integration Research

Last reviewed: 2026-10-14

## Goal and constraints

The project goal is a **native Android marketplace experience**, not a browser wrapper and not a launcher that immediately redirects to the official app. Search, navigation, and result presentation should stay inside Carousell Companion.

A native interface alone does not provide genuine marketplace data. Until a permitted listing source is confirmed, the app must show an honest empty/integration state rather than inventing products or implying that a search succeeded.

## Findings so far

### 1. No supported public consumer API identified

The independent [API Evangelist Carousell profile](https://github.com/api-evangelist/carousell) describes itself as a third-party profile, not Carousell's API. Its `apis.yml` has an empty `apis` list and describes Carousell as having no publicly documented developer API surface. This is a research lead, **not an official statement from Carousell**, and does not prove that no private partner API exists.

**Decision:** do not build around guessed endpoints, private/reverse-engineered APIs, or credentials captured from the official app. If live listing access is needed, seek official documentation or written authorization from Carousell first.

### 2. Official Carousell GitHub organization

The public [Carousell GitHub organization](https://github.com/carousell) contains engineering repositories, but the repositories reviewed did not provide a documented consumer marketplace API or an SDK for third-party clients:

- [Android-Coding-Challenge](https://github.com/carousell/Android-Coding-Challenge) — archived Android coding challenge, not a marketplace SDK.
- [ktGen-Android](https://github.com/carousell/ktGen-Android) — Kotlin code-generation Gradle task, unrelated to marketplace access.
- [Orion](https://github.com/carousell/Orion) — Go microservice framework, not relevant to this client.
- [sellee](https://github.com/carousell/sellee) — an agent for marketplace workflows through a visible browser, not a documented API/SDK for this app. Do not add autonomous listing, negotiation, or transaction automation to this project.

This was a targeted review, not an exhaustive audit of every repository or private partner offering.

## Implementation decision

- Keep all app screens and navigation native with Kotlin and Jetpack Compose.
- Do not add a WebView, Custom Tabs, or automatic handoff as a substitute for native search results.
- Do not scrape marketplace pages or use private/reverse-engineered endpoints.
- Do not ask for, collect, or store Carousell passwords, session cookies, or other credentials.
- Do not show sample/fake listings as real results.
- Keep local features such as recent-search history and theme preference independent of any future listing integration.

## Next integration steps

1. Look for official Carousell developer/partner documentation or a documented permitted data feed.
2. If no public route exists, ask Carousell about authorized access for a personal native client.
3. Record any confirmed authorization, API documentation, permitted use, rate limits, and attribution requirements before coding the integration.
4. Once a permitted source exists, define a small listing model and repository interface, then implement native loading, error, empty, result-card, and detail states.
5. Add integration tests against a mock repository without pretending mock data is live marketplace data.

## Sources reviewed

- [Carousell GitHub organization](https://github.com/carousell)
- [Carousell Android coding challenge](https://github.com/carousell/Android-Coding-Challenge)
- [Carousell ktGen-Android](https://github.com/carousell/ktGen-Android)
- [Carousell Orion](https://github.com/carousell/Orion)
- [Carousell sellee](https://github.com/carousell/sellee)
- [API Evangelist Carousell profile](https://github.com/api-evangelist/carousell)
- [Carousell Philippines Terms of Service](https://www.carousell.ph/terms.html)

Re-check official documentation before implementing an integration.

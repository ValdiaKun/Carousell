# Carousell Integration Research

Last reviewed: 2026-10-14

## Findings

### 1. No supported public consumer API identified

The independent [API Evangelist Carousell profile](https://github.com/api-evangelist/carousell) explicitly describes itself as a third-party profile, not Carousell's API. Its `apis.yml` currently has an empty `apis` list and describes Carousell as having no publicly documented developer API surface. This is a useful research lead, **not an official statement from Carousell** and not proof that no private partner API exists.

**Decision:** do not build account features around guessed endpoints, private/reverse-engineered APIs, or credentials captured from the official app. If account-level features become essential, seek written authorization and official integration documentation from Carousell first.

### 2. Official Carousell GitHub organization

The public [Carousell GitHub organization](https://github.com/carousell) contains useful engineering repositories, but the repositories found in this review do not provide a documented consumer marketplace API or an SDK for third-party clients:

- [Android-Coding-Challenge](https://github.com/carousell/Android-Coding-Challenge) — an archived Android coding challenge. It may offer historical examples of Android project conventions, but it is not a Carousell app SDK.
- [ktGen-Android](https://github.com/carousell/ktGen-Android) — a Kotlin code-generation Gradle task that reads YAML definitions. It is unrelated to marketplace account access.
- [Orion](https://github.com/carousell/Orion) — a Go microservice framework; not relevant to this Android client.
- [sellee](https://github.com/carousell/sellee) — an agent that automates marketplace workflows through a visible browser. It is not an API/SDK for this app, and we will not add autonomous listing, negotiation, or transaction automation to this personal search companion.

This was a targeted repository search, not an exhaustive audit of every repository in the organization.

### 3. Project direction

Keep the app as a lightweight native Android companion that hands browsing/searches to Carousell's official website or app. Improve the native experience without bypassing the marketplace's normal access controls:

1. Verify search URL behavior on a real Android device and gracefully fall back to the Carousell homepage if a route fails.
2. Persist recent searches locally, with a clear action to remove them.
3. Improve accessibility, loading/error handling, and UI tests.
4. Add account-dependent features only after a supported and authorized integration path is confirmed.

## Sources

- [Carousell GitHub organization](https://github.com/carousell)
- [Carousell Android coding challenge](https://github.com/carousell/Android-Coding-Challenge)
- [Carousell ktGen-Android](https://github.com/carousell/ktGen-Android)
- [Carousell Orion](https://github.com/carousell/Orion)
- [Carousell sellee](https://github.com/carousell/sellee)
- [API Evangelist Carousell profile](https://github.com/api-evangelist/carousell)
- [Carousell Terms of Service](https://www.carousell.ph/terms.html)

This document records the findings available on the review date. Re-check official documentation before implementing integrations.

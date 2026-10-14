# Carousell Android Companion

A personal-use native Android app for Carousell Philippines, built with Kotlin and Jetpack Compose. The project prioritizes a fast native interface, quick search, recent-search history, category shortcuts, and light/dark theme support.

## Current status

The app has a native home screen and a native search-state screen. Recent searches and the light/dark preference are stored locally on-device. Search submission and category shortcuts stay inside the app; the app no longer launches a browser or the official Carousell app from the search screen.

**Important limitation:** live marketplace listings are not connected yet. The search screen explicitly explains this instead of showing fabricated products or silently redirecting the user. The app is currently a native UI foundation, not yet a full marketplace client.

The source is continuously validated by GitHub Actions. Because each commit can change the result, use the [workflow runs page](https://github.com/ValdiaKun/Carousell/actions) to check the latest run for the exact commit you intend to install. A queued or running workflow is not a successful build, and an artifact should only be downloaded from a completed successful run. Physical-device testing remains outstanding.

## Build and test

The repository includes a GitHub Actions workflow at `.github/workflows/android-ci.yml`. Pushes to `main`, pull requests targeting `main`, and manual workflow dispatches run JVM unit tests, Android emulator UI tests, and a debug APK build on JDK 17 with Gradle 8.13. The emulator UI test job covers native search/edit, system Back navigation, category shortcuts, history removal and persistence, empty search, and theme preference persistence. Check the latest workflow run to verify the current suite; the existence of these tests does not itself mean they have passed.

Download the `carousell-companion-debug` artifact from the relevant successful GitHub Actions run. Install only APKs from a build you trust.

## Planned stack

- Kotlin
- Jetpack Compose / Material 3
- AndroidX activity and lifecycle
- GitHub Actions CI

## Integration research and safety

See [`docs/integration-research.md`](docs/integration-research.md) for the current integration research. No supported public consumer API or third-party SDK has been identified in the sources reviewed so far. A genuine native listings experience depends on finding an authorized, permitted data source.

- Do not scrape Carousell or use private/reverse-engineered endpoints.
- Do not collect or store Carousell credentials in this app.
- Do not display invented listings as real marketplace results.
- Do not claim account, messaging, favorites, orders, or seller-listing support until implemented through a supported integration.
- Keep all user-facing search UI native; do not replace the app with a browser wrapper.

## Next milestones

1. Install and test the latest debug APK on a real Android phone; record any UI or navigation problems.
2. Verify the new emulator UI tests in GitHub Actions and expand coverage for recent-search persistence, removal, empty queries, and theme preference.
3. Investigate official/authorized listing integration options before implementing real result cards.
4. Once a permitted data source exists, implement native listing models, loading/error/empty states, result cards, listing details, and local favorites.
5. Measure startup and interaction performance on-device before claiming optimizations.

# Carousell Android Companion

A personal-use Android launcher for Carousell Philippines, focused on responsive native controls, quick search handoff, category shortcuts, recent searches, and a light/dark theme toggle.

## Current status

The initial Kotlin/Jetpack Compose project and GitHub Actions build workflow are committed. The app currently opens Carousell's official website for browsing/search handoff; it is **not** yet a full native Carousell client and does not directly access account data, messages, favorites, orders, or seller listings.

The Android build has not been independently verified on a physical device yet. The CI workflow attempts to compile a debug APK and publish it as a workflow artifact.

## Build

The repository includes a GitHub Actions workflow at `.github/workflows/android-ci.yml`. Pushes to `main`, pull requests targeting `main`, and manual workflow dispatches run `gradle assembleDebug` on JDK 17 with Gradle 8.13.

When a build succeeds, download the `carousell-companion-debug` artifact from the corresponding GitHub Actions run. Install only APKs from a build you trust.

## Planned stack

- Kotlin
- Jetpack Compose / Material 3
- Android Gradle Plugin
- AndroidX lifecycle and Compose state

## Integration and security principles

- Do not scrape Carousell or use private/reverse-engineered endpoints.
- Do not collect or store Carousell credentials in this app.
- Use official Carousell app/site flows for account-sensitive actions unless an authorized integration becomes available.
- Verify URL routes and behavior on-device rather than assuming the website's URL structure is stable.
- Measure performance before claiming optimizations.

## Next milestones

1. Confirm CI build success and fix any compilation errors.
2. Test search handoff and external-app/browser behavior on Android.
3. Add local preferences and robust URL handling.
4. Investigate official integration options for account-dependent features.
5. Add automated UI tests and performance checks.

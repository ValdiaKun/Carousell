# Carousell Android Client

A personal Android companion for Carousell Philippines, focused on fast browsing, efficient search, and a polished native experience.

## Project status

Initial project setup. Before implementing direct marketplace integrations, verify the official and permitted integration surfaces. Carousell does not publish a documented general-purpose public marketplace API, and its Terms of Service restrict automated access without prior express written permission.

## Planned stack

- Kotlin
- Jetpack Compose
- MVVM and repository pattern
- Room for local app data
- Coil for image loading
- AndroidX Navigation

## Principles

- Do not scrape Carousell or use private/reverse-engineered endpoints.
- Do not collect or store Carousell credentials in this app.
- Use official Carousell app/site flows for account-sensitive actions unless authorized integrations become available.
- Measure performance before claiming optimizations.

## Initial priorities

1. Smooth, responsive native interface
2. Fast search and filtering within data the app is permitted to access
3. Clear navigation and accessible UI
4. Secure handling of local preferences and cached data

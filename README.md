# LifeMeter

**Your life, by the numbers.**

LifeMeter turns your birthdate into a live dashboard — days alive, seconds, food consumed, global deaths and births, your horoscope sign, and a few absurd curiosities. Set your date once, and the app remembers it forever.

[![CI](https://github.com/cocodedk/lifemeter/actions/workflows/ci.yml/badge.svg)](https://github.com/cocodedk/lifemeter/actions/workflows/ci.yml)
[![Release](https://github.com/cocodedk/lifemeter/actions/workflows/release-apk.yml/badge.svg)](https://github.com/cocodedk/lifemeter/actions/workflows/release-apk.yml)

## Website
- [English](https://cocodedk.github.io/lifemeter/)
- [فارسی (Persian)](https://cocodedk.github.io/lifemeter/fa/)

---

## Features

- Days alive — total days since your birthdate
- Seconds alive — live counter, ticks every second
- kg food consumed — rough estimate based on days alive
- Deaths since birth — global deaths since you were born, live
- Session counter — seconds, deaths, and births since app open
- Horoscope — your zodiac sign with symbol and ruling planets

## Download

[**Download LifeMeter.apk**](https://github.com/cocodedk/lifemeter/releases/latest/download/LifeMeter.apk)

Requires Android 5.0+. Enable *Install from unknown sources* when prompted.

---

## Build from Source

**Prerequisites:** Android Studio (Ladybug+), JDK 17, Android SDK 34

```bash
git clone https://github.com/cocodedk/lifemeter.git
cd lifemeter

# Install git hooks
./scripts/install-hooks.sh

# Run unit tests
./gradlew :app:test

# Build debug APK
./gradlew assembleDebug

# Full smoke check (build + tests + lint)
./gradlew buildSmoke --no-daemon
```

---

## Architecture

```
app/src/main/java/com/example/first/
  MainActivity.kt          — single activity, all UI logic

app/src/test/java/com/example/first/
  FormatNumberTest.kt      — number formatting (7 tests)
  HoroscopeTest.kt         — horoscope sign lookup (16 tests)

website/                   — Vite + React GitHub Pages site
  src/components/
    Calculator.jsx         — live birthdate calculator (browser demo)
    Hero.jsx / Features.jsx / Install.jsx / About.jsx
```

| Layer | Technology |
|---|---|
| Android app | Kotlin, SDK 34, Material Components 3 |
| State | SharedPreferences for birthdate persistence |
| UI gesture | SwipeRefreshLayout |
| Website | Vite + React |

### CI/CD

| Workflow | Trigger | What it does |
|---|---|---|
| `ci.yml` | PR / push (all branches) | Runs buildSmoke (build + tests + lint) |
| `release-apk.yml` | Push to master + workflow_dispatch | Builds signed APK, creates GitHub Release |
| `deploy-pages.yml` | Push to master (website changes) | Deploys React site to GitHub Pages |

**Signing secrets required for release builds:**
`KEYSTORE_BASE64` · `KEYSTORE_PASSWORD` · `KEY_ALIAS` · `KEY_PASSWORD`

Run `./scripts/setup-signing.sh` to generate a keystore and upload secrets automatically.

---

## Author

**Babak Bandpey** — [cocode.dk](https://cocode.dk) | [LinkedIn](https://linkedin.com/in/babakbandpey) | [GitHub](https://github.com/cocodedk)

---

Apache-2.0 | © 2026 [Cocode](https://cocode.dk) | Created by [Babak Bandpey](https://linkedin.com/in/babakbandpey)

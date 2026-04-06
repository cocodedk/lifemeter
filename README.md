# LifeMeter

**Your life, by the numbers.**

LifeMeter turns your birthdate into a live dashboard — days alive, seconds, food consumed, global deaths and births, your horoscope sign, and a few absurd curiosities. Set your date once, and the app remembers it forever.

[![CI](https://github.com/cocodedk/lifemeter/actions/workflows/ci.yml/badge.svg)](https://github.com/cocodedk/lifemeter/actions/workflows/ci.yml)
[![Release](https://github.com/cocodedk/lifemeter/actions/workflows/release-apk.yml/badge.svg)](https://github.com/cocodedk/lifemeter/actions/workflows/release-apk.yml)

🌐 **[cocodedk.github.io/lifemeter](https://cocodedk.github.io/lifemeter/)** — live demo & download

---

## What it shows

| Stat | Description |
|---|---|
| Days alive | How many days since your birthdate |
| Seconds alive | Live — ticks every second |
| kg food consumed | Rough estimate based on days alive |
| Deaths since birth | Global deaths since you were born — live |
| Session counter | Seconds, deaths, and births while you've had the app open |
| Horoscope | Your zodiac sign with symbol and ruling planets |

---

## Install

1. Download **[LifeMeter.apk](https://github.com/cocodedk/lifemeter/releases/latest/download/LifeMeter.apk)**
2. Enable *Install from unknown sources* when prompted
3. Open the app, tap **Set birth date**, and your dashboard appears

Requires Android 5.0+.

---

## Development

```bash
# Clone
git clone https://github.com/cocodedk/lifemeter.git
cd lifemeter

# Enable pre-commit hook (runs unit tests before each commit)
git config core.hooksPath .githooks

# Run unit tests
./gradlew :app:test

# Build debug APK
./gradlew assembleDebug
```

### Project structure

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

### CI/CD

| Workflow | Trigger | What it does |
|---|---|---|
| `ci.yml` | PR / push (non-master) | Runs unit tests |
| `release-apk.yml` | Push to master | Builds signed APK, creates GitHub Release |
| `deploy-pages.yml` | Push to master (website changes) | Deploys React site to GitHub Pages |

**Signing secrets required for release builds:**
`KEYSTORE_BASE64` · `KEYSTORE_PASSWORD` · `KEY_ALIAS` · `KEY_PASSWORD`

---

## Built with

- Kotlin · Android SDK 34 · Material Components 3
- SharedPreferences for birthdate persistence
- SwipeRefreshLayout for pull-to-change gesture
- Vite + React (website)

---

**Created by [Babak Bandpey](https://cocode.dk) · Built by [Cocode](https://cocode.dk) · Apache-2.0**

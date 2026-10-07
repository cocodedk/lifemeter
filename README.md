# LifeMeter

**Your life, by the numbers.**

LifeMeter turns your birth date into a live dashboard of numbers about your life: days and seconds alive, an estimate of the food you have eaten, estimated worldwide deaths since you were born, your horoscope sign, and a few curiosities. You set your birth date once, and the app saves it for future visits.

[![CI](https://github.com/cocodedk/lifemeter/actions/workflows/ci.yml/badge.svg)](https://github.com/cocodedk/lifemeter/actions/workflows/ci.yml)
[![Release](https://github.com/cocodedk/lifemeter/actions/workflows/release-apk.yml/badge.svg)](https://github.com/cocodedk/lifemeter/actions/workflows/release-apk.yml)

## Website
- [English](https://lifemeter.cocode.dk/)
- [Dansk](https://lifemeter.cocode.dk/da/)

---

## Download

<!-- cocode-apps:install:start -->
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/dk.cocode.lifemeter/)
- [Download the Android installation file (APK) from GitHub](https://github.com/cocodedk/lifemeter/releases/latest/download/LifeMeter.apk)
- [Add the app to Obtainium, an app that keeps it up to date](https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/cocodedk/lifemeter)
<!-- cocode-apps:install:end -->

Requires Android 5.0 or newer. If you download the installation file (APK) from GitHub, open it and, if Android asks, allow the app you opened it from (your browser or file manager) to install apps.

---

## Features

- Days alive: the days since your birth date
- Seconds alive: recalculated every second; large totals are rounded
- Estimated food consumed (kg): a rough estimate from your days alive and a world average of 1.7 kg a day
- Estimated worldwide deaths since birth: updated while the app is open
- This session: the seconds the app has been open, and estimated worldwide births and deaths in that time; it starts again when you return to the app or pick a new date
- Horoscope sign: your sign, its symbol, and the planets, sun or moon linked to it
- Danish and English, following your phone's language

## Privacy

LifeMeter does not collect, send or share any personal data. The only thing it saves is the birth date you pick, in the app's private storage on your phone. If you use Android backup, a copy may also be kept in your own backup. The app requests no internet permission and does not ask for access to your location, contacts, camera, microphone or storage. It has no analytics, crash reporting or ads. The About screen has buttons that open web pages (F-Droid, this policy, the website, GitHub) in your phone's browser when you tap them; the app itself never goes online. Read the full [privacy policy](https://lifemeter.cocode.dk/privacy/).

---

## Build

**Prerequisites:** Android Studio Panda 1 (2025.3.1) or newer, Android SDK 34. Gradle runs on the JetBrains JDK 21 named in `gradle/gradle-daemon-jvm.properties`; the app's source targets Java 17.

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
  MainActivity.kt          — the main screen
  Dashboard.kt             — fills in the numbers on the main screen
  AboutActivity.kt         — the About page (opened from the toolbar's info icon)
  AboutLinks.kt            — where each About link goes

app/src/main/res/
  values/strings.xml       — the app's English text
  values-da/strings.xml    — the app's Danish text

app/src/test/java/com/example/first/
  AboutLinksTest.kt        — About link targets
  FormatNumberTest.kt      — number formatting
  HoroscopeTest.kt         — horoscope sign lookup (16 tests)

website/                   — Vite + React GitHub Pages site
  src/components/
    Calculator.jsx         — live birthdate calculator (browser demo)
    Hero.jsx / Features.jsx / Install.jsx / About.jsx
  src/da/                  — the Danish home page (same layout, Danish text)
```

| Layer | Technology |
|---|---|
| Android app | Kotlin, Android SDK 34, Material Components 1.12.0 |
| State | SharedPreferences for birthdate persistence |
| UI gesture | SwipeRefreshLayout |
| Website | Vite + React |

### CI/CD

| Workflow | Trigger | What it does |
|---|---|---|
| `ci.yml` | Pull requests targeting master; pushes to all branches | Runs buildSmoke (build + tests + lint) |
| `release-apk.yml` | workflow_dispatch | Builds signed APK, creates GitHub Release |
| `deploy-pages.yml` | Push to master (website changes) | Deploys React site to GitHub Pages |

**Signing secrets required for release builds:**
`KEYSTORE_BASE64` · `KEYSTORE_PASSWORD` · `KEY_ALIAS` · `KEY_PASSWORD`

Run `./scripts/setup-signing.sh` to generate a keystore and upload secrets automatically.

---

## Contributing

Issues and pull requests are welcome; see [CONTRIBUTING.md](CONTRIBUTING.md) and [SECURITY.md](SECURITY.md).

---

## Author

**Babak Bandpey** — [cocode.dk](https://cocode.dk) | [LinkedIn](https://linkedin.com/in/babakbandpey) | [GitHub](https://github.com/cocodedk)

---

## License

Apache-2.0, see [LICENSE](LICENSE). © 2026 [Cocode](https://cocode.dk) | Created by [Babak Bandpey](https://linkedin.com/in/babakbandpey)

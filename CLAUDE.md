# CLAUDE.md — LifeMeter

## Project Overview

LifeMeter is an Android app that turns your birth date into a live dashboard. It shows days and seconds alive, estimated food consumption, estimated worldwide deaths since birth, estimated worldwide births and deaths during the current session, your horoscope sign, and a few curiosities. Set your date once and the app saves it for future visits (SharedPreferences, which Android backup may copy). The app and website are in English and Danish.

- **Language / Runtime**: Kotlin, Java 17 (desugar enabled)
- **Framework**: Android SDK 34, Material Components 1.12.0, AndroidX
- **Architecture**: One activity for the main screen (state held in the activity, birth date in SharedPreferences), one for the About page
- **Package / Namespace**: `com.example.first`; **application id**: `dk.cocode.lifemeter`

---

## Required Skills — ALWAYS Invoke These

These skills **must** be invoked when the relevant situation arises. Never skip them.

| Situation | Skill |
|-----------|-------|
| Before any new feature or screen | `superpowers:brainstorming` |
| Planning multi-step changes | `superpowers:writing-plans` |
| Writing or fixing core logic | `superpowers:test-driven-development` |
| First sign of a bug or failure | `superpowers:systematic-debugging` |
| Before completing a feature branch | `superpowers:requesting-code-review` |
| Before claiming any task done | `superpowers:verification-before-completion` |
| Working on UI / frontend | `frontend-design:frontend-design` |
| After implementing — reviewing quality | `simplify` |

---

## Architecture

```
lifemeter/
├── app/src/main/java/com/example/first/
│   ├── MainActivity.kt          ← the main screen: birth date, layout state, session timer
│   ├── Dashboard.kt             ← fills in the numbers on the main screen
│   ├── AboutActivity.kt         ← the About page, opened from the toolbar's info icon
│   ├── AboutLinks.kt            ← where each About link goes (unit-tested)
│   └── Horoscope.kt / FormatNumber.kt / BirthDateStore.kt
├── app/src/main/res/values/ and values-da/   ← app text, English and Danish (keep both in step)
├── app/src/test/java/com/example/first/
│   ├── AboutLinksTest.kt         ← About link targets
│   ├── FormatNumberTest.kt       ← number formatting
│   └── HoroscopeTest.kt          ← horoscope sign lookup (16 tests)
├── website/                      ← Vite + React GitHub Pages site
│   ├── src/components/
│   │   ├── Calculator.jsx        ← simplified birth date calculator (browser preview)
│   │   └── Hero.jsx / Features.jsx / Install.jsx / About.jsx
│   └── src/da/                   ← the Danish home page (same layout, Danish text)
├── .github/workflows/            ← CI, release, pages automation
├── .githooks/                    ← pre-commit, commit-msg hooks
├── scripts/                      ← install-hooks.sh, setup-repo.sh, setup-signing.sh
└── gradle.properties             ← VERSION_NAME / VERSION_CODE, the app's version
```

### Layer Rules
- The main screen lives in `MainActivity.kt` and `Dashboard.kt`; the About page is its own `AboutActivity`
- Tests are in `app/src/test/` (unit) and `app/src/androidTest/` (instrumented)
- Website is an independent Vite/React project in `website/`

---

## Coding Conventions

- [ ] All models are **immutable** — use `copy()` for mutations
- [ ] Functions are **pure** where possible — no hidden side effects
- [ ] No hardcoded strings — use `strings.xml` resources
- [ ] Strict Kotlin types everywhere

---

## Engineering Principles

### File Size
- **200-line maximum per file** — extract a class, function, or module when approaching the limit

### DRY · SOLID · KISS · YAGNI
- Extract shared logic into named utilities; never copy-paste
- Single Responsibility: one class/function does one thing
- Don't add features not yet needed
- Delete dead code immediately

### TDD
- Write the failing test first, make it pass, then refactor
- Test names describe behaviour: `"should reject duplicate email"`
- One assertion per test — keep tests focused and readable

### Commit hygiene
- Follow Conventional Commits: `feat: ...` / `fix: ...` / `chore: ...`
- The `commit-msg` hook enforces this automatically

---

## Build Commands

```bash
./gradlew assembleDebug                    # Build debug APK
./gradlew :app:test                        # Run unit tests
./gradlew :app:lintDebug                   # Lint
./gradlew buildSmoke --no-daemon           # Full smoke check (build + tests + lint)
./gradlew assembleRelease --no-daemon      # Build signed release APK (requires signing env vars)
```

---

## Key Files

| File | Purpose |
|------|---------|
| `CLAUDE.md` | This file — project conventions and session startup |
| `gradle.properties` | Also holds VERSION_NAME/VERSION_CODE, the app's version — bump both before a release |
| `.github/workflows/ci.yml` | CI on pull requests targeting master and on pushes to all branches |
| `.github/workflows/release-apk.yml` | Signed APK build of the version in `gradle.properties` + GitHub Release; refuses one already tagged |
| `.github/workflows/deploy-pages.yml` | GitHub Pages deployment |
| `.githooks/pre-commit` | Runs buildSmoke before commit |
| `.githooks/commit-msg` | Enforces Conventional Commits |
| `scripts/install-hooks.sh` | One-time hook installer |
| `scripts/setup-repo.sh` | Branch protection + repo settings |
| `scripts/setup-signing.sh` | Keystore generation + GitHub Secrets upload |

---

## Starting a New Session

1. Read this file
2. Run `./gradlew buildSmoke --no-daemon` to confirm everything passes
3. Invoke `superpowers:brainstorming` before touching any feature
4. Follow the Required Skills table — every skill is mandatory, not optional

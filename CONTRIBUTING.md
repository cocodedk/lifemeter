# Contributing to LifeMeter

## Local Setup
1. Install Android Studio (Ladybug or newer) and JDK 17.
2. Clone the repository and open it in Android Studio.
3. Sync Gradle dependencies.
4. Connect an Android device or start an emulator (API 21+).

## Install Git Hooks
```
./scripts/install-hooks.sh
```

## Local Git Setup
Run these once after cloning:
```bash
git config pull.rebase true
git config core.autocrlf input
git config push.autoSetupRemote true
git config init.defaultBranch main
```
Windows contributors: use `core.autocrlf true` instead of `input`.

## Build and Test Commands
```bash
./gradlew assembleDebug          # Build debug APK
./gradlew :app:test              # Run unit tests
./gradlew :app:lintDebug         # Lint
./gradlew buildSmoke --no-daemon # Full smoke check (build + tests + lint)
```

## Coding Style
- Kotlin with Android SDK conventions
- Single Activity architecture
- Keep files small and focused (200-line maximum)
- Follow Conventional Commits for all commit messages

## Branch Naming
| Branch prefix | Use for |
|---|---|
| `feature/` | New features (`feat:`) |
| `fix/` | Bug fixes (`fix:`) |
| `chore/` | Maintenance (`chore:`) |
| `docs/` | Documentation (`docs:`) |
| `refactor/` | Code refactoring (`refactor:`) |
| `ci/` | CI changes (`ci:`) |

Branch names use kebab-case. Never commit directly to `master` — always open a PR.

## PR Checklist
- [ ] Smoke check passes (`./gradlew buildSmoke --no-daemon`)
- [ ] Manual test completed for changed functionality
- [ ] Updated docs if behavior changed
- [ ] Commit messages follow Conventional Commits format

# LifeMeter — UI/UX Overhaul Design Spec

**Date:** 2026-04-06  
**Scope:** Full experience overhaul — flow, persistence, language, stats presentation, curiosities, tarot removal

---

## Decisions Made

| Topic | Decision |
|---|---|
| Language | All English throughout |
| Tarot | Remove entirely (activity, DB helpers, menu) |
| Date entry | Single "Set birth date" button → immediate results |
| Saved date | SharedPreferences; auto-load dashboard on re-launch |
| Approach | Full overhaul (not just bug fixes) |

---

## Screen States

### State 1 — First Launch (no saved date)

- Toolbar: title "LifeMeter", subtitle unchanged
- Hero section: hourglass emoji, heading "How long have you lived?", subtext "Set your birth date to see your life in numbers."
- Single card (secondary background) with: hint text "Pick your date once — the app will remember it." and a full-width "Set birth date →" primary button
- Tapping the button opens `DatePickerDialog` with default year set to `currentYear - 30` (not today)
- On date confirmed: save to SharedPreferences → reveal dashboard with a smooth fade/slide-in animation → scroll to top of results

### State 2 — Returning User (saved date found)

- On `onCreate`, read SharedPreferences for saved date
- If found: skip the hero section entirely; show compact date row at top + full dashboard immediately
- Compact date row: secondary card, "YOUR BIRTHDAY" label, formatted date (dd / MM / yyyy), right-aligned "Change" button (secondary style)
- Tapping "Change" opens `DatePickerDialog` pre-filled with current saved date
- On new date confirmed: overwrite SharedPreferences → dashboard updates live

### State 3 — Swipe-to-change

- `SwipeRefreshLayout` wraps the `NestedScrollView`
- Pulling down from anywhere in the scroll view triggers `DatePickerDialog`
- On confirm, same save + live-update flow as "Change" button
- A small pull-down hint ("↓ Pull down anywhere to change birth date") appears at the bottom of the content

---

## Screen Layout — MainActivity

```
Toolbar (LifeMeter / subtitle)
│
├── [First launch only] Hero section
│     hourglass icon · heading · subtext
│
├── Date card (compact if returning, full if first launch)
│
├── [visible after date set] Lifetime totals
│     2×2 grid of stat cards
│
├── [visible after date set] This session
│     3-row card: seconds · deaths · births
│
└── [visible after date set] Curiosities
      Horoscope sign card
      Pull-down hint
```

---

## Stat Cards — Lifetime Totals

Each of the 4 cards shows:
- **Large number** (top): formatted value — see Number Formatting below
- **Small label** (bottom): plain English, no unit in the number string

| Card | Value | Label |
|---|---|---|
| Days alive | `secondsSinceBirth / 86400` | "Days alive" |
| Seconds alive | `secondsSinceBirth + timePassed` (live) | "Seconds alive" |
| Food consumed | `days / 2` | "kg food consumed" |
| Deaths since birth | `(secondsSinceBirth + timePassed) * 2` (live) | "Deaths since birth" |

The number and label are in **separate TextViews**. No more concatenating label into the value string.

---

## Number Formatting

All displayed numbers use a helper function `formatNumber(n: Long): String`:

- `n >= 1_000_000_000` → `"%.2fB".format(n / 1_000_000_000.0)` e.g. `"1.14B"`
- `n >= 1_000_000` → `"%.1fM".format(n / 1_000_000.0)` e.g. `"2.3M"`
- `n < 1_000_000` → plain with commas via `NumberFormat.getNumberInstance(Locale.US)` e.g. `"123,456"`

Check thresholds in descending order (B before M before plain).

---

## This Session Card

Three rows, each: label (left, small, secondary color) + live number (right, bold, primary color).

| Row | Label | Value |
|---|---|---|
| 1 | "Seconds on screen" | `timePassed` |
| 2 | "Deaths while watching" | `round(timePassed * 1.8)` |
| 3 | "Births while watching" | `round(timePassed * 4.0)` |

Session timer behavior:
- Timer starts on `onResume`, cancels on `onPause`
- `secondsSinceBirth` is recalculated fresh on each `onResume` from the saved date

---

## Curiosities Card

- Horoscope row: Unicode symbol (♈♉♊♋♌♍♎♏♐♑♒♓) + sign name (bold, highlight color) + planet string (small, secondary color)
- Divider line
- Sex line (only shown if age ≥ 15): "Estimated time having sex: **X,XXX hrs**" — value is `daysAfterFifteen / 6`, formatted with commas, no ":D:D"
- If age < 15, the sex line is `View.GONE` (not just empty)

Horoscope sign lookup returns a `data class HoroscopeResult(val symbol: String, val name: String, val planets: String)` instead of a plain string. Defined as a top-level private data class in `MainActivity.kt`.

---

## Removed Files

- `TarotCardActivity.kt`
- `TarotCardDBHelper.kt`
- `TarotCardModel.kt`
- `DBContract.kt`
- `app/src/main/res/menu/menu_main.xml`
- `onCreateOptionsMenu` and `onOptionsItemSelected` overrides in `MainActivity`
- Toolbar overflow menu

---

## SharedPreferences

Key: `"birthdate"`  
Format: ISO string `"yyyy-MM-dd"` stored as a String  
Helper: two private functions in `MainActivity`:
- `saveBirthDate(date: LocalDate)` — writes to prefs
- `loadBirthDate(): LocalDate?` — reads from prefs, returns null if absent

---

## Animation

- Dashboard reveal (first date set): `resultsContainer` fades in + slides up 32dp over 300ms using `ViewPropertyAnimator`
- Live counter digits: no special animation (CountDownTimer already updates every second; smooth enough)
- Swipe refresh: standard `SwipeRefreshLayout` spinner, dismissed immediately after `DatePickerDialog` opens

---

## Strings (all English)

All user-visible strings go in `res/values/strings.xml`. No hardcoded strings in layouts or Kotlin.

Key new/changed strings:

```xml
<string name="hero_heading">How long have you lived?</string>
<string name="hero_subtext">Set your birth date to see your life in numbers.</string>
<string name="set_birth_date">Set birth date</string>
<string name="change_birth_date">Change</string>
<string name="your_birthday_label">Your birthday</string>
<string name="pull_to_change">↓ Pull down anywhere to change birth date</string>
<string name="stat_days_alive">Days alive</string>
<string name="stat_seconds_alive">Seconds alive</string>
<string name="stat_food_consumed">kg food consumed</string>
<string name="stat_deaths_since_birth">Deaths since birth</string>
<string name="session_seconds">Seconds on screen</string>
<string name="session_deaths">Deaths while watching</string>
<string name="session_births">Births while watching</string>
<string name="curiosities_sex_hours">Estimated time having sex: %s hrs</string>
<string name="pick_date_hint">Pick your date once — the app will remember it.</string>
```

---

## Out of Scope

- Dark mode
- Notifications or widgets
- Any new screen beyond MainActivity
- Localization beyond English

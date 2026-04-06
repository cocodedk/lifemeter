# LifeMeter UI/UX Overhaul Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Full UI/UX overhaul — remove tarot code, persist birthdate in SharedPreferences, single-tap date flow, English strings, redesigned stat cards with separate number/label TextViews, number formatting, swipe-to-change gesture, session timer reset on resume, and curiosities cleanup with HoroscopeResult.

**Architecture:** All logic lives in `MainActivity.kt`. Two top-level pure functions (`formatNumber`, `getHoroscopeSign`) and one data class (`HoroscopeResult`) are defined above the class for unit testability. Layout splits into `activity_main.xml` (toolbar + SwipeRefreshLayout wrapper) and `content_main.xml` (all screen content). SharedPreferences stores the birthdate as an ISO string.

**Tech Stack:** Kotlin, Android SDK 34, Material Components 1.12, SwipeRefreshLayout 1.1.0, JUnit 4 for unit tests, `java.time.LocalDate` (desugared)

---

## File Map

| Action | File |
|---|---|
| Delete | `app/src/main/java/com/example/first/TarotCardActivity.kt` |
| Delete | `app/src/main/java/com/tarotcard/` (entire directory) |
| Delete | `app/src/main/res/menu/menu_main.xml` |
| Modify | `app/src/main/AndroidManifest.xml` |
| Modify | `app/build.gradle` |
| Modify | `app/src/main/res/values/strings.xml` |
| Modify | `app/src/main/res/values/styles.xml` |
| Modify | `app/src/main/res/layout/activity_main.xml` |
| Rewrite | `app/src/main/res/layout/content_main.xml` |
| Rewrite | `app/src/main/java/com/example/first/MainActivity.kt` |
| Create | `app/src/test/java/com/example/first/FormatNumberTest.kt` |
| Create | `app/src/test/java/com/example/first/HoroscopeTest.kt` |

---

## Task 1: Remove all tarot code

**Files:**
- Delete: `app/src/main/java/com/example/first/TarotCardActivity.kt`
- Delete: `app/src/main/java/com/tarotcard/` (whole directory)
- Delete: `app/src/main/res/menu/menu_main.xml`
- Modify: `app/src/main/AndroidManifest.xml`
- Modify: `app/src/main/java/com/example/first/MainActivity.kt`

- [ ] **Step 1: Delete tarot files**

```bash
rm app/src/main/java/com/example/first/TarotCardActivity.kt
rm -rf app/src/main/java/com/tarotcard
rm app/src/main/res/menu/menu_main.xml
```

- [ ] **Step 2: Remove TarotCardActivity from AndroidManifest.xml**

Open `app/src/main/AndroidManifest.xml`. Remove this block entirely:

```xml
<activity
    android:name=".TarotCardActivity"
    android:exported="false" />
```

Result — the manifest should have only `MainActivity` in `<application>`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_app_icon"
        android:label="@string/app_name"
        android:roundIcon="@drawable/ic_app_icon"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/AppTheme.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

- [ ] **Step 3: Remove menu code from MainActivity.kt**

In `app/src/main/java/com/example/first/MainActivity.kt`, delete these two overrides and their imports:

Remove the imports:
```kotlin
import android.view.Menu
import android.view.MenuItem
```

Remove the overrides:
```kotlin
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
        R.id.action_tarotCards -> {
            val intent = Intent(this, TarotCardActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
}
```

Also remove `import android.content.Intent` (no longer needed).

- [ ] **Step 4: Verify the project compiles**

In Android Studio: **Build → Make Project** (or `./gradlew assembleDebug` from terminal).

Expected: BUILD SUCCESSFUL with no errors.

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "feat: remove all tarot code and menu"
```

---

## Task 2: Add dependency, strings, and secondary button style

**Files:**
- Modify: `app/build.gradle`
- Modify: `app/src/main/res/values/strings.xml`
- Modify: `app/src/main/res/values/styles.xml`

- [ ] **Step 1: Add SwipeRefreshLayout dependency to app/build.gradle**

In the `dependencies` block, add:

```groovy
implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
```

- [ ] **Step 2: Sync Gradle**

In Android Studio: **File → Sync Project with Gradle Files**.

Expected: Sync completes with no errors.

- [ ] **Step 3: Replace strings.xml**

Overwrite `app/src/main/res/values/strings.xml` with:

```xml
<resources>
    <string name="app_name">LifeMeter</string>
    <string name="main_title">LifeMeter</string>
    <string name="main_subtitle">A warmer look at time, age and tiny absurdities.</string>

    <!-- Hero (first launch) -->
    <string name="hero_heading">How long have you lived?</string>
    <string name="hero_subtext">Set your birth date to see your life in numbers.</string>
    <string name="pick_date_hint">Pick your date once — the app will remember it.</string>
    <string name="set_birth_date">Set birth date</string>

    <!-- Compact date row -->
    <string name="your_birthday_label">Your birthday</string>
    <string name="change_birth_date">Change</string>

    <!-- Swipe hint -->
    <string name="pull_to_change">↓ Pull down anywhere to change birth date</string>

    <!-- Section labels -->
    <string name="overview_label">Lifetime totals</string>
    <string name="session_label">This session</string>
    <string name="story_label">Curiosities</string>

    <!-- Stat card labels -->
    <string name="stat_days_alive">Days alive</string>
    <string name="stat_seconds_alive">Seconds alive</string>
    <string name="stat_food_consumed">kg food consumed</string>
    <string name="stat_deaths_since_birth">Deaths since birth</string>

    <!-- Session row labels -->
    <string name="session_seconds">Seconds on screen</string>
    <string name="session_deaths">Deaths while watching</string>
    <string name="session_births">Births while watching</string>

    <!-- Curiosities -->
    <string name="curiosities_sex_hours">Estimated time having sex: %s hrs</string>
</resources>
```

- [ ] **Step 4: Add AppSecondaryButton style to styles.xml**

In `app/src/main/res/values/styles.xml`, add this style before the closing `</resources>` tag:

```xml
<style name="AppSecondaryButton" parent="Widget.MaterialComponents.Button.OutlinedButton">
    <item name="android:textAllCaps">false</item>
    <item name="cornerRadius">14dp</item>
    <item name="strokeColor">@color/colorDivider</item>
    <item name="android:textColor">@color/colorTextSecondary</item>
    <item name="android:insetTop">0dp</item>
    <item name="android:insetBottom">0dp</item>
</style>
```

- [ ] **Step 5: Commit**

```bash
git add app/build.gradle app/src/main/res/values/strings.xml app/src/main/res/values/styles.xml
git commit -m "feat: add SwipeRefreshLayout dep, English strings, secondary button style"
```

---

## Task 3: TDD — formatNumber()

**Files:**
- Create: `app/src/test/java/com/example/first/FormatNumberTest.kt`
- Modify: `app/src/main/java/com/example/first/MainActivity.kt`

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/example/first/FormatNumberTest.kt`:

```kotlin
package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatNumberTest {

    @Test fun `small number below 1000 no separator`() {
        assertEquals("999", formatNumber(999L))
    }

    @Test fun `exactly 1000 uses comma`() {
        assertEquals("1,000", formatNumber(1_000L))
    }

    @Test fun `number below 1 million uses commas`() {
        assertEquals("123,456", formatNumber(123_456L))
    }

    @Test fun `exactly 1 million uses M with one decimal`() {
        assertEquals("1.0M", formatNumber(1_000_000L))
    }

    @Test fun `2300000 formats as 2 point 3M`() {
        assertEquals("2.3M", formatNumber(2_300_000L))
    }

    @Test fun `exactly 1 billion uses B with two decimals`() {
        assertEquals("1.00B", formatNumber(1_000_000_000L))
    }

    @Test fun `1140000000 formats as 1 point 14B`() {
        assertEquals("1.14B", formatNumber(1_140_000_000L))
    }
}
```

- [ ] **Step 2: Run tests to confirm they fail**

In Android Studio: right-click `FormatNumberTest.kt` → Run. Or:

```bash
./gradlew test --tests "com.example.first.FormatNumberTest"
```

Expected: FAILED — `formatNumber` is not defined.

- [ ] **Step 3: Add formatNumber() at the top of MainActivity.kt (above the class declaration)**

Open `app/src/main/java/com/example/first/MainActivity.kt`. Add these imports and the function before `class MainActivity`:

```kotlin
import java.text.NumberFormat
import java.util.Locale
```

```kotlin
fun formatNumber(n: Long): String = when {
    n >= 1_000_000_000L -> "%.2fB".format(n / 1_000_000_000.0)
    n >= 1_000_000L     -> "%.1fM".format(n / 1_000_000.0)
    else                -> NumberFormat.getNumberInstance(Locale.US).format(n)
}
```

Place it immediately above `class MainActivity : AppCompatActivity() {`.

- [ ] **Step 4: Run tests to confirm they pass**

```bash
./gradlew test --tests "com.example.first.FormatNumberTest"
```

Expected: 7 tests, all PASSED.

- [ ] **Step 5: Commit**

```bash
git add app/src/test/java/com/example/first/FormatNumberTest.kt \
        app/src/main/java/com/example/first/MainActivity.kt
git commit -m "feat: add formatNumber() with unit tests"
```

---

## Task 4: TDD — HoroscopeResult and getHoroscopeSign()

**Files:**
- Create: `app/src/test/java/com/example/first/HoroscopeTest.kt`
- Modify: `app/src/main/java/com/example/first/MainActivity.kt`

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/example/first/HoroscopeTest.kt`:

```kotlin
package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Test

class HoroscopeTest {
    // month parameter is 0-indexed (Jan=0, Feb=1, ..., Dec=11)

    @Test fun `aries - march 21`() {
        val r = getHoroscopeSign(2, 21)
        assertEquals("♈", r.symbol); assertEquals("Aries", r.name)
    }

    @Test fun `taurus - april 20`() {
        val r = getHoroscopeSign(3, 20)
        assertEquals("♉", r.symbol); assertEquals("Taurus", r.name)
    }

    @Test fun `gemini - june 1`() {
        val r = getHoroscopeSign(5, 1)
        assertEquals("♊", r.symbol); assertEquals("Gemini", r.name)
    }

    @Test fun `cancer - july 4`() {
        val r = getHoroscopeSign(6, 4)
        assertEquals("♋", r.symbol); assertEquals("Cancer", r.name)
    }

    @Test fun `leo - august 10`() {
        val r = getHoroscopeSign(7, 10)
        assertEquals("♌", r.symbol); assertEquals("Leo", r.name)
    }

    @Test fun `virgo - september 1`() {
        val r = getHoroscopeSign(8, 1)
        assertEquals("♍", r.symbol); assertEquals("Virgo", r.name)
    }

    @Test fun `libra - october 10`() {
        val r = getHoroscopeSign(9, 10)
        assertEquals("♎", r.symbol); assertEquals("Libra", r.name)
    }

    @Test fun `scorpio - november 1`() {
        val r = getHoroscopeSign(10, 1)
        assertEquals("♏", r.symbol); assertEquals("Scorpio", r.name)
    }

    @Test fun `sagittarius - december 1`() {
        val r = getHoroscopeSign(11, 1)
        assertEquals("♐", r.symbol); assertEquals("Sagittarius", r.name)
    }

    @Test fun `capricorn - january 15`() {
        val r = getHoroscopeSign(0, 15)
        assertEquals("♑", r.symbol); assertEquals("Capricorn", r.name)
    }

    @Test fun `aquarius - february 1`() {
        val r = getHoroscopeSign(1, 1)
        assertEquals("♒", r.symbol); assertEquals("Aquarius", r.name)
    }

    @Test fun `pisces - march 15`() {
        val r = getHoroscopeSign(2, 15)
        assertEquals("♓", r.symbol); assertEquals("Pisces", r.name)
    }

    @Test fun `boundary - aries starts march 21`() {
        assertEquals("Pisces", getHoroscopeSign(2, 20).name)
        assertEquals("Aries",  getHoroscopeSign(2, 21).name)
    }

    @Test fun `boundary - capricorn spans dec-jan`() {
        assertEquals("Capricorn", getHoroscopeSign(11, 22).name)
        assertEquals("Capricorn", getHoroscopeSign(0, 19).name)
        assertEquals("Aquarius",  getHoroscopeSign(0, 20).name)
    }
}
```

- [ ] **Step 2: Run tests to confirm they fail**

```bash
./gradlew test --tests "com.example.first.HoroscopeTest"
```

Expected: FAILED — `HoroscopeResult` and `getHoroscopeSign` not defined.

- [ ] **Step 3: Add HoroscopeResult data class and getHoroscopeSign() to MainActivity.kt**

In `app/src/main/java/com/example/first/MainActivity.kt`, add the following immediately above `fun formatNumber(...)` (i.e., below the imports, above the existing top-level function):

```kotlin
data class HoroscopeResult(val symbol: String, val name: String, val planets: String)

fun getHoroscopeSign(month: Int, day: Int): HoroscopeResult {
    val m = month + 1
    return when {
        (m == 3 && day >= 21) || (m == 4 && day <= 19)  -> HoroscopeResult("♈", "Aries", "Mars")
        (m == 4 && day >= 20) || (m == 5 && day <= 20)  -> HoroscopeResult("♉", "Taurus", "Venus · Moon")
        (m == 5 && day >= 21) || (m == 6 && day <= 20)  -> HoroscopeResult("♊", "Gemini", "Mercury")
        (m == 6 && day >= 21) || (m == 7 && day <= 22)  -> HoroscopeResult("♋", "Cancer", "Moon · Jupiter")
        (m == 7 && day >= 23) || (m == 8 && day <= 22)  -> HoroscopeResult("♌", "Leo", "Sun")
        (m == 8 && day >= 23) || (m == 9 && day <= 22)  -> HoroscopeResult("♍", "Virgo", "Mercury")
        (m == 9 && day >= 23) || (m == 10 && day <= 22) -> HoroscopeResult("♎", "Libra", "Venus · Saturn")
        (m == 10 && day >= 23) || (m == 11 && day <= 21)-> HoroscopeResult("♏", "Scorpio", "Mars · Pluto")
        (m == 11 && day >= 22) || (m == 12 && day <= 21)-> HoroscopeResult("♐", "Sagittarius", "Jupiter")
        (m == 12 && day >= 22) || (m == 1 && day <= 19) -> HoroscopeResult("♑", "Capricorn", "Saturn · Mars")
        (m == 1 && day >= 20) || (m == 2 && day <= 18)  -> HoroscopeResult("♒", "Aquarius", "Saturn · Uranus")
        (m == 2 && day >= 19) || (m == 3 && day <= 20)  -> HoroscopeResult("♓", "Pisces", "Jupiter · Neptune · Venus")
        else -> HoroscopeResult("", "", "")
    }
}
```

- [ ] **Step 4: Run all unit tests**

```bash
./gradlew test --tests "com.example.first.HoroscopeTest" --tests "com.example.first.FormatNumberTest"
```

Expected: 21 tests total, all PASSED.

- [ ] **Step 5: Commit**

```bash
git add app/src/test/java/com/example/first/HoroscopeTest.kt \
        app/src/main/java/com/example/first/MainActivity.kt
git commit -m "feat: add HoroscopeResult + getHoroscopeSign() with unit tests"
```

---

## Task 5: Rewrite content_main.xml

**Files:**
- Rewrite: `app/src/main/res/layout/content_main.xml`

- [ ] **Step 1: Replace the entire file**

Overwrite `app/src/main/res/layout/content_main.xml` with:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <!-- ── Hero section (first launch only) ───────────────────────── -->
    <LinearLayout
        android:id="@+id/hero_section"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingBottom="20dp"
        android:visibility="gone"
        tools:visibility="visible">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:paddingBottom="10dp"
            android:text="⏳"
            android:textAlignment="center"
            android:textSize="52sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="sans-serif-medium"
            android:paddingBottom="8dp"
            android:text="@string/hero_heading"
            android:textAlignment="center"
            android:textColor="@color/colorTextPrimary"
            android:textSize="26sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/hero_subtext"
            android:textAlignment="center"
            android:textColor="@color/colorTextSecondary"
            android:textSize="15sp" />
    </LinearLayout>

    <!-- ── Date card ─────────────────────────────────────────────── -->
    <com.google.android.material.card.MaterialCardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="18dp"
        app:cardBackgroundColor="@color/colorCardSecondary">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="18dp">

            <!-- Compact row: shown for returning users -->
            <LinearLayout
                android:id="@+id/date_compact_row"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:gravity="center_vertical"
                android:orientation="horizontal"
                android:visibility="gone"
                tools:visibility="visible">

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:orientation="vertical">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:letterSpacing="0.08"
                        android:text="@string/your_birthday_label"
                        android:textAllCaps="true"
                        android:textColor="@color/colorTextSecondary"
                        android:textSize="10sp" />

                    <TextView
                        android:id="@+id/selected_birth_date_text"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="4dp"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary"
                        android:textSize="16sp"
                        tools:text="15 / 03 / 1990" />
                </LinearLayout>

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/change_birth_date_button"
                    style="@style/AppSecondaryButton"
                    android:layout_width="wrap_content"
                    android:layout_height="40dp"
                    android:text="@string/change_birth_date" />
            </LinearLayout>

            <!-- First launch content: shown only before a date is set -->
            <LinearLayout
                android:id="@+id/first_launch_content"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:visibility="visible"
                tools:visibility="visible">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:paddingBottom="14dp"
                    android:text="@string/pick_date_hint"
                    android:textColor="@color/colorTextSecondary"
                    android:textSize="14sp" />

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/set_birth_date_button"
                    android:layout_width="match_parent"
                    android:layout_height="52dp"
                    android:text="@string/set_birth_date" />
            </LinearLayout>
        </LinearLayout>
    </com.google.android.material.card.MaterialCardView>

    <!-- ── Results container (hidden until date is set) ──────────── -->
    <LinearLayout
        android:id="@+id/results_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:visibility="gone">

        <!-- LIFETIME TOTALS -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="12dp"
            android:fontFamily="sans-serif-medium"
            android:text="@string/overview_label"
            android:textAllCaps="true"
            android:textColor="@color/colorTextSecondary"
            android:textSize="12sp" />

        <androidx.gridlayout.widget.GridLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:alignmentMode="alignMargins"
            android:columnCount="2"
            android:useDefaultMargins="true">

            <com.google.android.material.card.MaterialCardView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_columnWeight="1"
                android:layout_gravity="fill_horizontal">
                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:orientation="vertical" android:padding="16dp">
                    <TextView android:id="@+id/days_value"
                        android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary" android:textSize="28sp"
                        tools:text="13,181" />
                    <TextView android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:layout_marginTop="4dp" android:text="@string/stat_days_alive"
                        android:textColor="@color/colorTextSecondary" android:textSize="12sp" />
                </LinearLayout>
            </com.google.android.material.card.MaterialCardView>

            <com.google.android.material.card.MaterialCardView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_columnWeight="1"
                android:layout_gravity="fill_horizontal">
                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:orientation="vertical" android:padding="16dp">
                    <TextView android:id="@+id/seconds_value"
                        android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary" android:textSize="28sp"
                        tools:text="1.14B" />
                    <TextView android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:layout_marginTop="4dp" android:text="@string/stat_seconds_alive"
                        android:textColor="@color/colorTextSecondary" android:textSize="12sp" />
                </LinearLayout>
            </com.google.android.material.card.MaterialCardView>

            <com.google.android.material.card.MaterialCardView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_columnWeight="1"
                android:layout_gravity="fill_horizontal">
                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:orientation="vertical" android:padding="16dp">
                    <TextView android:id="@+id/food_value"
                        android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary" android:textSize="28sp"
                        tools:text="6,590" />
                    <TextView android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:layout_marginTop="4dp" android:text="@string/stat_food_consumed"
                        android:textColor="@color/colorTextSecondary" android:textSize="12sp" />
                </LinearLayout>
            </com.google.android.material.card.MaterialCardView>

            <com.google.android.material.card.MaterialCardView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_columnWeight="1"
                android:layout_gravity="fill_horizontal">
                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:orientation="vertical" android:padding="16dp">
                    <TextView android:id="@+id/deaths_value"
                        android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary" android:textSize="28sp"
                        tools:text="2.28B" />
                    <TextView android:layout_width="match_parent" android:layout_height="wrap_content"
                        android:layout_marginTop="4dp" android:text="@string/stat_deaths_since_birth"
                        android:textColor="@color/colorTextSecondary" android:textSize="12sp" />
                </LinearLayout>
            </com.google.android.material.card.MaterialCardView>

        </androidx.gridlayout.widget.GridLayout>

        <!-- THIS SESSION -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="12dp"
            android:fontFamily="sans-serif-medium"
            android:text="@string/session_label"
            android:textAllCaps="true"
            android:textColor="@color/colorTextSecondary"
            android:textSize="12sp" />

        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp">
            <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                android:orientation="vertical" android:padding="18dp">

                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:orientation="horizontal" android:paddingBottom="12dp">
                    <TextView android:layout_width="0dp" android:layout_height="wrap_content"
                        android:layout_weight="1" android:text="@string/session_seconds"
                        android:textColor="@color/colorTextSecondary" android:textSize="14sp" />
                    <TextView android:id="@+id/session_seconds_value"
                        android:layout_width="wrap_content" android:layout_height="wrap_content"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary" android:textSize="18sp"
                        tools:text="47" />
                </LinearLayout>

                <View android:layout_width="match_parent" android:layout_height="1dp"
                    android:background="@color/colorDivider" />

                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:paddingTop="12dp" android:paddingBottom="12dp">
                    <TextView android:layout_width="0dp" android:layout_height="wrap_content"
                        android:layout_weight="1" android:text="@string/session_deaths"
                        android:textColor="@color/colorTextSecondary" android:textSize="14sp" />
                    <TextView android:id="@+id/session_deaths_value"
                        android:layout_width="wrap_content" android:layout_height="wrap_content"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary" android:textSize="18sp"
                        tools:text="84" />
                </LinearLayout>

                <View android:layout_width="match_parent" android:layout_height="1dp"
                    android:background="@color/colorDivider" />

                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:orientation="horizontal" android:paddingTop="12dp">
                    <TextView android:layout_width="0dp" android:layout_height="wrap_content"
                        android:layout_weight="1" android:text="@string/session_births"
                        android:textColor="@color/colorTextSecondary" android:textSize="14sp" />
                    <TextView android:id="@+id/session_births_value"
                        android:layout_width="wrap_content" android:layout_height="wrap_content"
                        android:fontFamily="sans-serif-medium"
                        android:textColor="@color/colorTextPrimary" android:textSize="18sp"
                        tools:text="188" />
                </LinearLayout>

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- CURIOSITIES -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="12dp"
            android:fontFamily="sans-serif-medium"
            android:text="@string/story_label"
            android:textAllCaps="true"
            android:textColor="@color/colorTextSecondary"
            android:textSize="12sp" />

        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="12dp"
            app:cardBackgroundColor="@color/colorCardSecondary">
            <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                android:orientation="vertical" android:padding="20dp">

                <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:gravity="center_vertical" android:orientation="horizontal">
                    <TextView android:id="@+id/horoscope_symbol"
                        android:layout_width="wrap_content" android:layout_height="wrap_content"
                        android:layout_marginEnd="12dp"
                        android:textColor="@color/colorHighlight" android:textSize="34sp"
                        tools:text="♓" />
                    <LinearLayout android:layout_width="0dp" android:layout_height="wrap_content"
                        android:layout_weight="1" android:orientation="vertical">
                        <TextView android:id="@+id/horoscope_name"
                            android:layout_width="wrap_content" android:layout_height="wrap_content"
                            android:fontFamily="sans-serif-medium"
                            android:textColor="@color/colorHighlight" android:textSize="20sp"
                            tools:text="Pisces" />
                        <TextView android:id="@+id/horoscope_planets"
                            android:layout_width="wrap_content" android:layout_height="wrap_content"
                            android:layout_marginTop="2dp"
                            android:textColor="@color/colorTextSecondary" android:textSize="13sp"
                            tools:text="Jupiter · Neptune · Venus" />
                    </LinearLayout>
                </LinearLayout>

                <View android:layout_width="match_parent" android:layout_height="1dp"
                    android:layout_marginTop="14dp" android:background="@color/colorDivider" />

                <TextView android:id="@+id/sex_text"
                    android:layout_width="match_parent" android:layout_height="wrap_content"
                    android:layout_marginTop="12dp"
                    android:textColor="@color/colorTextPrimary" android:textSize="14sp"
                    android:visibility="gone"
                    tools:text="Estimated time having sex: 1,406 hrs"
                    tools:visibility="visible" />

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Pull-down hint -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:layout_marginBottom="4dp"
            android:gravity="center"
            android:text="@string/pull_to_change"
            android:textColor="@color/colorTextSecondary"
            android:textSize="12sp" />

    </LinearLayout>

</LinearLayout>
```

- [ ] **Step 2: Commit**

```bash
git add app/src/main/res/layout/content_main.xml
git commit -m "feat: redesign content_main.xml with hero, compact date row, and split stat cards"
```

---

## Task 6: Update activity_main.xml with SwipeRefreshLayout

**Files:**
- Modify: `app/src/main/res/layout/activity_main.xml`

- [ ] **Step 1: Replace activity_main.xml**

Overwrite `app/src/main/res/layout/activity_main.xml` with:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/bg_screen"
    tools:context=".MainActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:elevation="0dp"
        android:theme="@style/AppTheme.AppBarOverlay">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="@color/colorToolbar"
            android:elevation="0dp"
            app:popupTheme="@style/AppTheme.PopupOverlay"
            app:subtitle="@string/main_subtitle"
            app:title="@string/main_title" />

    </com.google.android.material.appbar.AppBarLayout>

    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        android:id="@+id/swipe_refresh_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">

        <androidx.core.widget.NestedScrollView
            android:id="@+id/main_scroll_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:clipToPadding="false"
            android:fillViewport="true"
            android:paddingStart="20dp"
            android:paddingTop="16dp"
            android:paddingEnd="20dp"
            android:paddingBottom="24dp">

            <include layout="@layout/content_main" />

        </androidx.core.widget.NestedScrollView>

    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

- [ ] **Step 2: Build and check for layout errors**

**Build → Make Project** in Android Studio.

Expected: BUILD SUCCESSFUL. No missing resource errors.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/layout/activity_main.xml
git commit -m "feat: wrap scroll view in SwipeRefreshLayout"
```

---

## Task 7: Rewrite MainActivity.kt

**Files:**
- Rewrite: `app/src/main/java/com/example/first/MainActivity.kt`

The top-level declarations added in Tasks 3 & 4 (`HoroscopeResult`, `getHoroscopeSign`, `formatNumber`) are preserved exactly. Only the `class MainActivity` body is replaced.

- [ ] **Step 1: Replace the full file**

Overwrite `app/src/main/java/com/example/first/MainActivity.kt` with:

```kotlin
package com.example.first

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

private const val PREFS_NAME = "lifemeter_prefs"
private const val PREFS_KEY_BIRTHDATE = "birthdate"

data class HoroscopeResult(val symbol: String, val name: String, val planets: String)

fun getHoroscopeSign(month: Int, day: Int): HoroscopeResult {
    val m = month + 1
    return when {
        (m == 3 && day >= 21) || (m == 4 && day <= 19)  -> HoroscopeResult("♈", "Aries", "Mars")
        (m == 4 && day >= 20) || (m == 5 && day <= 20)  -> HoroscopeResult("♉", "Taurus", "Venus · Moon")
        (m == 5 && day >= 21) || (m == 6 && day <= 20)  -> HoroscopeResult("♊", "Gemini", "Mercury")
        (m == 6 && day >= 21) || (m == 7 && day <= 22)  -> HoroscopeResult("♋", "Cancer", "Moon · Jupiter")
        (m == 7 && day >= 23) || (m == 8 && day <= 22)  -> HoroscopeResult("♌", "Leo", "Sun")
        (m == 8 && day >= 23) || (m == 9 && day <= 22)  -> HoroscopeResult("♍", "Virgo", "Mercury")
        (m == 9 && day >= 23) || (m == 10 && day <= 22) -> HoroscopeResult("♎", "Libra", "Venus · Saturn")
        (m == 10 && day >= 23) || (m == 11 && day <= 21)-> HoroscopeResult("♏", "Scorpio", "Mars · Pluto")
        (m == 11 && day >= 22) || (m == 12 && day <= 21)-> HoroscopeResult("♐", "Sagittarius", "Jupiter")
        (m == 12 && day >= 22) || (m == 1 && day <= 19) -> HoroscopeResult("♑", "Capricorn", "Saturn · Mars")
        (m == 1 && day >= 20) || (m == 2 && day <= 18)  -> HoroscopeResult("♒", "Aquarius", "Saturn · Uranus")
        (m == 2 && day >= 19) || (m == 3 && day <= 20)  -> HoroscopeResult("♓", "Pisces", "Jupiter · Neptune · Venus")
        else -> HoroscopeResult("", "", "")
    }
}

fun formatNumber(n: Long): String = when {
    n >= 1_000_000_000L -> "%.2fB".format(n / 1_000_000_000.0)
    n >= 1_000_000L     -> "%.1fM".format(n / 1_000_000.0)
    else                -> NumberFormat.getNumberInstance(Locale.US).format(n)
}

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mainScrollView: NestedScrollView
    private lateinit var heroSection: LinearLayout
    private lateinit var dateCompactRow: LinearLayout
    private lateinit var firstLaunchContent: LinearLayout
    private lateinit var selectedBirthDateText: TextView
    private lateinit var changeBirthDateButton: MaterialButton
    private lateinit var setBirthDateButton: MaterialButton
    private lateinit var resultsContainer: LinearLayout
    private lateinit var daysValue: TextView
    private lateinit var secondsValue: TextView
    private lateinit var foodValue: TextView
    private lateinit var deathsValue: TextView
    private lateinit var sessionSecondsValue: TextView
    private lateinit var sessionDeathsValue: TextView
    private lateinit var sessionBirthsValue: TextView
    private lateinit var horoscopeSymbol: TextView
    private lateinit var horoscopeName: TextView
    private lateinit var horoscopePlanets: TextView
    private lateinit var sexText: TextView

    private var timer: CountDownTimer? = null
    private var selectedBirthDate: LocalDate? = null
    private var sessionStartTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        bindViews()

        selectedBirthDate = loadBirthDate()

        if (selectedBirthDate != null) {
            showReturningUserState()
        } else {
            showFirstLaunchState()
        }

        setBirthDateButton.setOnClickListener { showBirthDatePicker() }
        changeBirthDateButton.setOnClickListener { showBirthDatePicker() }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            showBirthDatePicker()
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedBirthDate != null) {
            sessionStartTime = System.currentTimeMillis() / 1000
            startTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }

    private fun bindViews() {
        swipeRefreshLayout    = findViewById(R.id.swipe_refresh_layout)
        mainScrollView        = findViewById(R.id.main_scroll_view)
        heroSection           = findViewById(R.id.hero_section)
        dateCompactRow        = findViewById(R.id.date_compact_row)
        firstLaunchContent    = findViewById(R.id.first_launch_content)
        selectedBirthDateText = findViewById(R.id.selected_birth_date_text)
        changeBirthDateButton = findViewById(R.id.change_birth_date_button)
        setBirthDateButton    = findViewById(R.id.set_birth_date_button)
        resultsContainer      = findViewById(R.id.results_container)
        daysValue             = findViewById(R.id.days_value)
        secondsValue          = findViewById(R.id.seconds_value)
        foodValue             = findViewById(R.id.food_value)
        deathsValue           = findViewById(R.id.deaths_value)
        sessionSecondsValue   = findViewById(R.id.session_seconds_value)
        sessionDeathsValue    = findViewById(R.id.session_deaths_value)
        sessionBirthsValue    = findViewById(R.id.session_births_value)
        horoscopeSymbol       = findViewById(R.id.horoscope_symbol)
        horoscopeName         = findViewById(R.id.horoscope_name)
        horoscopePlanets      = findViewById(R.id.horoscope_planets)
        sexText               = findViewById(R.id.sex_text)
    }

    private fun showFirstLaunchState() {
        heroSection.visibility        = View.VISIBLE
        dateCompactRow.visibility     = View.GONE
        firstLaunchContent.visibility = View.VISIBLE
        resultsContainer.visibility   = View.GONE
    }

    private fun showReturningUserState() {
        heroSection.visibility        = View.GONE
        dateCompactRow.visibility     = View.VISIBLE
        firstLaunchContent.visibility = View.GONE
        resultsContainer.visibility   = View.VISIBLE
        renderSelectedBirthDate()
        updateDashboard()
    }

    private fun showBirthDatePicker() {
        val current = selectedBirthDate ?: LocalDate.of(LocalDate.now().year - 30, 1, 1)
        DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedBirthDate = LocalDate.of(year, month + 1, day)
                saveBirthDate(selectedBirthDate!!)
                renderSelectedBirthDate()
                if (resultsContainer.visibility != View.VISIBLE) {
                    heroSection.visibility        = View.GONE
                    dateCompactRow.visibility     = View.VISIBLE
                    firstLaunchContent.visibility = View.GONE
                    revealDashboard()
                } else {
                    updateDashboard()
                }
                sessionStartTime = System.currentTimeMillis() / 1000
                startTimer()
            },
            current.year,
            current.monthValue - 1,
            current.dayOfMonth
        ).show()
    }

    private fun renderSelectedBirthDate() {
        val d = selectedBirthDate ?: return
        selectedBirthDateText.text = "%02d / %02d / %d".format(d.dayOfMonth, d.monthValue, d.year)
    }

    private fun revealDashboard() {
        resultsContainer.visibility   = View.VISIBLE
        resultsContainer.alpha        = 0f
        resultsContainer.translationY = 32f * resources.displayMetrics.density
        resultsContainer.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .start()
        updateDashboard()
        mainScrollView.post { mainScrollView.smoothScrollTo(0, resultsContainer.top) }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(Long.MAX_VALUE / 2, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timePassed = System.currentTimeMillis() / 1000 - sessionStartTime
                sessionSecondsValue.text = formatNumber(timePassed)
                sessionDeathsValue.text  = formatNumber(Math.round(timePassed * 1.8))
                sessionBirthsValue.text  = formatNumber(Math.round(timePassed * 4.0))
                updateLiveStats()
            }
            override fun onFinish() {}
        }.also { it.start() }
    }

    private fun updateDashboard() {
        val birth = selectedBirthDate ?: return
        val now   = LocalDateTime.now()
        val secondsSinceBirth = dateToEpoch(now.year, now.monthValue, now.dayOfMonth) -
                dateToEpoch(birth.year, birth.monthValue, birth.dayOfMonth)
        val days = secondsSinceBirth / 86400

        daysValue.text = formatNumber(days)
        foodValue.text = formatNumber(days / 2)

        val sign = getHoroscopeSign(birth.monthValue - 1, birth.dayOfMonth)
        horoscopeSymbol.text  = sign.symbol
        horoscopeName.text    = sign.name
        horoscopePlanets.text = sign.planets

        if (now.year - birth.year >= 15) {
            val secAfter15   = dateToEpoch(now.year, now.monthValue, now.dayOfMonth) -
                    dateToEpoch(birth.year + 15, birth.monthValue, birth.dayOfMonth)
            val daysAfter15  = secAfter15 / 86400
            sexText.text       = getString(R.string.curiosities_sex_hours, formatNumber(daysAfter15 / 6))
            sexText.visibility = View.VISIBLE
        } else {
            sexText.visibility = View.GONE
        }

        updateLiveStats()
    }

    private fun updateLiveStats() {
        val birth = selectedBirthDate ?: return
        val now   = LocalDateTime.now()
        val secondsSinceBirth = dateToEpoch(now.year, now.monthValue, now.dayOfMonth) -
                dateToEpoch(birth.year, birth.monthValue, birth.dayOfMonth)
        val timePassed = if (sessionStartTime > 0L) System.currentTimeMillis() / 1000 - sessionStartTime else 0L
        val total = secondsSinceBirth + timePassed
        secondsValue.text = formatNumber(total)
        deathsValue.text  = formatNumber(total * 2)
    }

    private fun saveBirthDate(date: LocalDate) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREFS_KEY_BIRTHDATE, date.toString())
            .apply()
    }

    private fun loadBirthDate(): LocalDate? {
        val s = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(PREFS_KEY_BIRTHDATE, null) ?: return null
        return try { LocalDate.parse(s) } catch (e: Exception) { null }
    }

    private fun dateToEpoch(year: Int, month: Int, day: Int): Long =
        LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
}
```

- [ ] **Step 2: Run all unit tests**

```bash
./gradlew test
```

Expected: All tests PASSED (FormatNumberTest: 7, HoroscopeTest: 14).

- [ ] **Step 3: Build**

```bash
./gradlew assembleDebug
```

Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/first/MainActivity.kt
git commit -m "feat: rewrite MainActivity with SharedPreferences, one-tap flow, and redesigned dashboard"
```

---

## Task 8: Manual verification checklist

- [ ] **Install and run on device or emulator**

```bash
./gradlew installDebug
```

- [ ] **First launch**: Hero section visible, "Set birth date" button present, no results visible
- [ ] **Pick a date**: DatePickerDialog opens at ~30 years ago. Pick any past date. Dashboard fades in and slides up. Date shows in compact row at top.
- [ ] **Stat cards**: Each shows a large number and a small English label. Seconds and Deaths values increment every second.
- [ ] **Session card**: Seconds, Deaths while watching, Births while watching all increment every second.
- [ ] **Curiosities**: Horoscope symbol + name + planets visible. Sex line shows if age ≥ 15, hidden if < 15.
- [ ] **Change button**: Tapping "Change" opens picker pre-filled with current date. Picking new date updates all stats.
- [ ] **Swipe down**: Pull down on the scroll view opens the date picker.
- [ ] **Kill and relaunch**: Saved date is remembered. Dashboard loads immediately with no tap required.
- [ ] **Background/foreground**: Session timer resets when returning to the app. "Seconds on screen" starts from 0 again.
- [ ] **No tarot**: Overflow menu is gone. No tarot-related screens or crashes.

- [ ] **Commit if any minor fixes were made during verification**

```bash
git add -A
git commit -m "fix: manual verification fixes"
```

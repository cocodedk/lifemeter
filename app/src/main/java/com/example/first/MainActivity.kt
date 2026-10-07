package com.example.first

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import java.time.LocalDate

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

    private lateinit var dashboard: Dashboard
    private val birthDateStore by lazy { BirthDateStore(this) }
    private var timer: CountDownTimer? = null
    private var selectedBirthDate: LocalDate? = null
    private var sessionStartTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        bindViews()
        dashboard = Dashboard(this)

        selectedBirthDate = birthDateStore.load()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_about -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
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

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
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
    }

    private fun showFirstLaunchState() {
        heroSection.visibility        = View.VISIBLE
        dateCompactRow.visibility     = View.GONE
        firstLaunchContent.visibility = View.VISIBLE
        resultsContainer.visibility   = View.GONE
    }

    private fun transitionToReturningLayout() {
        heroSection.visibility        = View.GONE
        dateCompactRow.visibility     = View.VISIBLE
        firstLaunchContent.visibility = View.GONE
    }

    private fun showReturningUserState() {
        transitionToReturningLayout()
        resultsContainer.visibility = View.VISIBLE
        renderSelectedBirthDate()
        updateDashboard()
    }

    private fun showBirthDatePicker() {
        val current = selectedBirthDate ?: LocalDate.of(LocalDate.now().year - 30, 1, 1)
        DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedBirthDate = LocalDate.of(year, month + 1, day)
                birthDateStore.save(selectedBirthDate!!)
                renderSelectedBirthDate()
                if (resultsContainer.visibility != View.VISIBLE) {
                    transitionToReturningLayout()
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
        selectedBirthDateText.text = getString(R.string.birth_date_format, d.dayOfMonth, d.monthValue, d.year)
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
                dashboard.showSession(System.currentTimeMillis() / 1000 - sessionStartTime)
                updateLiveStats()
            }
            override fun onFinish() {}
        }.also { it.start() }
    }

    private fun updateDashboard() {
        dashboard.showBirth(selectedBirthDate ?: return)
    }

    private fun updateLiveStats() {
        dashboard.showLive(selectedBirthDate ?: return)
    }
}

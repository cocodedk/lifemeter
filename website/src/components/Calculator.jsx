import { useState, useEffect, useRef } from 'react'
import styles from './Calculator.module.css'

function dateToEpochSeconds(year, month, day) {
  return Math.floor(new Date(year, month - 1, day).getTime() / 1000)
}

function getHoroscope(month, day) {
  const signs = [
    { name: 'Capricorn', symbol: '♑', check: (m, d) => (m === 12 && d >= 22) || (m === 1 && d <= 19) },
    { name: 'Aquarius',  symbol: '♒', check: (m, d) => (m === 1 && d >= 20) || (m === 2 && d <= 18) },
    { name: 'Pisces',    symbol: '♓', check: (m, d) => (m === 2 && d >= 19) || (m === 3 && d <= 20) },
    { name: 'Aries',     symbol: '♈', check: (m, d) => (m === 3 && d >= 21) || (m === 4 && d <= 19) },
    { name: 'Taurus',    symbol: '♉', check: (m, d) => (m === 4 && d >= 20) || (m === 5 && d <= 20) },
    { name: 'Gemini',    symbol: '♊', check: (m, d) => (m === 5 && d >= 21) || (m === 6 && d <= 20) },
    { name: 'Cancer',    symbol: '♋', check: (m, d) => (m === 6 && d >= 21) || (m === 7 && d <= 22) },
    { name: 'Leo',       symbol: '♌', check: (m, d) => (m === 7 && d >= 23) || (m === 8 && d <= 22) },
    { name: 'Virgo',     symbol: '♍', check: (m, d) => (m === 8 && d >= 23) || (m === 9 && d <= 22) },
    { name: 'Libra',     symbol: '♎', check: (m, d) => (m === 9 && d >= 23) || (m === 10 && d <= 22) },
    { name: 'Scorpio',   symbol: '♏', check: (m, d) => (m === 10 && d >= 23) || (m === 11 && d <= 21) },
    { name: 'Sagittarius',symbol:'♐', check: (m, d) => (m === 11 && d >= 22) || (m === 12 && d <= 21) },
  ]
  return signs.find(s => s.check(month, day)) ?? { name: '', symbol: '' }
}

function fmt(n) {
  if (n >= 1_000_000_000) return (n / 1_000_000_000).toFixed(2) + 'B'
  if (n >= 1_000_000)     return (n / 1_000_000).toFixed(1) + 'M'
  return n.toLocaleString('en-US')
}

function computeStats(birthdate, sessionStart) {
  if (!birthdate) return null
  const [y, m, d] = birthdate.split('-').map(Number)
  const now = new Date()
  const todayEpoch = Math.floor(new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime() / 1000)
  const birthEpoch = dateToEpochSeconds(y, m, d)
  const days = Math.floor((todayEpoch - birthEpoch) / 86400)
  const sessionSecs = Math.floor((now.getTime() - sessionStart) / 1000)
  // Seconds from midnight on the birth date to now, as the app counts them.
  const total = Math.floor(now.getTime() / 1000) - birthEpoch

  const sign = getHoroscope(m, d)

  return {
    days: fmt(days),
    seconds: fmt(total),
    food: fmt(Math.round(days * 1.7)),
    deaths: fmt(total * 2),
    sessionSecs: fmt(sessionSecs),
    sessionDeaths: fmt(Math.round(sessionSecs * 1.8)),
    sessionBirths: fmt(Math.round(sessionSecs * 4.0)),
    sign,
  }
}

export default function Calculator() {
  const today = new Date()
  const defaultDate = `${today.getFullYear() - 30}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`
  const [birthdate, setBirthdate] = useState('')
  const [stats, setStats] = useState(null)
  const sessionStart = useRef(Date.now())
  const timerRef = useRef(null)

  useEffect(() => {
    if (!birthdate) { setStats(null); return }
    sessionStart.current = Date.now()
    setStats(computeStats(birthdate, sessionStart.current))
    timerRef.current = setInterval(() => {
      setStats(computeStats(birthdate, sessionStart.current))
    }, 1000)
    return () => clearInterval(timerRef.current)
  }, [birthdate])

  return (
    <section id="try" className={`${styles.section} reveal-2`}>
      <p className={styles.kicker}>Try it right here</p>
      <h2 className={styles.h2}>Enter your birth date</h2>
      <p className={styles.sub}>A simplified preview in your browser. Its estimates differ from those in the Android app.</p>

      <input
        type="date"
        aria-label="Birth date"
        className={styles.datePicker}
        value={birthdate}
        max={new Date().toISOString().split('T')[0]}
        onChange={e => setBirthdate(e.target.value)}
        placeholder={defaultDate}
      />

      {stats ? (
        <div className={styles.dashboard}>
          <p className={styles.sectionLabel}>Lifetime totals</p>
          <div className={styles.grid}>
            <StatCard value={stats.days}   label="Days alive" />
            <StatCard value={stats.seconds} label="Seconds alive" live />
            <StatCard value={stats.food}   label="Estimated food consumed (kg)" />
            <StatCard value={stats.deaths} label="Estimated worldwide deaths since birth" live />
          </div>

          <p className={styles.sectionLabel} style={{marginTop: '24px'}}>This session</p>
          <div className={`${styles.sessionCard} glass`}>
            <SessionRow label="Seconds since you chose the date" value={stats.sessionSecs} />
            <SessionRow label="Estimated worldwide deaths since you chose the date" value={stats.sessionDeaths} />
            <SessionRow label="Estimated worldwide births since you chose the date" value={stats.sessionBirths} />
          </div>

          <p className={styles.sectionLabel} style={{marginTop: '24px'}}>Horoscope sign</p>
          <div className={`${styles.horoscopeCard} glass`}>
            <span className={styles.signSymbol}>{stats.sign.symbol}</span>
            <span className={styles.signName}>{stats.sign.name}</span>
          </div>
        </div>
      ) : (
        <div className={styles.placeholder}>
          <span className={styles.hourglass}>⏳</span>
          <p>Pick a date above to see your numbers</p>
        </div>
      )}
    </section>
  )
}

function StatCard({ value, label, live }) {
  return (
    <div className={`${styles.statCard} glass`}>
      <div className={`${styles.statValue} ${live ? styles.live : ''}`}>{value}</div>
      <div className={styles.statLabel}>{label}</div>
    </div>
  )
}

function SessionRow({ label, value }) {
  return (
    <div className={styles.sessionRow}>
      <span className={styles.rowLabel}>{label}</span>
      <span className={styles.rowValue}>{value}</span>
    </div>
  )
}

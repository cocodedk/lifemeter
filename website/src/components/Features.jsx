import styles from './Features.module.css'

const features = [
  { title: 'Live lifetime totals', body: 'Days alive, seconds alive, kilos of food consumed, and deaths since you were born — all ticking in real time.' },
  { title: 'Session counter', body: 'Seconds on screen, deaths and births while you\'ve been watching. Resets every time you return to the app.' },
  { title: 'Horoscope & curiosities', body: 'Your zodiac sign with symbol. Plus one more number that tends to raise eyebrows.' },
  { title: 'Remembers your date', body: 'Set it once. The dashboard loads instantly on every launch — no re-entering, no tapping Continue.' },
  { title: 'Pull-down to change', body: 'Swipe down anywhere on the screen to open the date picker. Try it with a friend\'s birthdate.' },
  { title: 'Stays on device', body: 'No backend, no account, no analytics. Your birthdate lives in Android SharedPreferences and nowhere else.' },
]

export default function Features() {
  return (
    <section id="features" className={`${styles.section} reveal`}>
      <h2 className={styles.h2}>What it does</h2>
      <div className={styles.grid}>
        {features.map(f => (
          <article key={f.title} className={`${styles.card} glass`}>
            <h3 className={styles.cardTitle}>{f.title}</h3>
            <p className={styles.cardBody}>{f.body}</p>
          </article>
        ))}
      </div>

      <blockquote className={`${styles.quote} glass`}>
        LifeMeter makes no judgements, tracks nothing, and sends nothing anywhere. It just counts.
      </blockquote>
    </section>
  )
}

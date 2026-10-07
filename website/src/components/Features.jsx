import styles from './Features.module.css'

const features = [
  { title: 'Lifetime totals', body: 'See your days alive, seconds alive, estimated food consumed, and estimated worldwide deaths since you were born. The seconds and the death estimate are recalculated every second while the app is open. Large totals are rounded.' },
  { title: 'This session', body: 'See how many seconds the app has been open, and the estimated worldwide births and deaths in that time. The counters start again when you return to the app or pick a new birth date.' },
  { title: 'Horoscope and curiosities', body: 'See your horoscope sign and its symbol, and the planets, sun or moon linked to it. The Curiosities card also estimates the hours spent having sex since your 16th birthday, using survey averages.' },
  { title: 'Saves your birth date', body: 'Enter your birth date once. Your dashboard opens with your numbers every time you start the app, with nothing to enter again.' },
  { title: 'Change your birth date', body: 'Tap Change birth date, or scroll to the top and pull down, to pick another date. Try it with a friend\'s birth date.' },
  { title: 'Stays on your phone', body: 'No account and no analytics, and the app has no internet permission. Your birth date is saved in the app\'s private storage and may be included in your Android backup.' },
]

export default function Features() {
  return (
    <section id="features" className={`${styles.section} reveal`}>
      <h2 id="how" className={styles.h2}>What it does</h2>
      <div className={styles.grid}>
        {features.map(f => (
          <article key={f.title} className={`${styles.card} glass`}>
            <h3 className={styles.cardTitle}>{f.title}</h3>
            <p className={styles.cardBody}>{f.body}</p>
          </article>
        ))}
      </div>

      <blockquote className={`${styles.quote} glass`}>
        LifeMeter does not track you, and the app has no internet permission. It only does the
        arithmetic.
      </blockquote>
    </section>
  )
}

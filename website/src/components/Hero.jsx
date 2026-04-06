import styles from './Hero.module.css'

const APK_URL = 'https://github.com/cocodedk/lifemeter/releases/latest/download/LifeMeter.apk'
const GITHUB_URL = 'https://github.com/cocodedk/lifemeter'

export default function Hero() {
  return (
    <section className={`${styles.hero} reveal`}>
      <p className={styles.kicker}>Android · Local-first · No accounts</p>
      <h1 className={styles.h1}>How long have<br />you been alive?</h1>
      <p className={styles.copy}>
        LifeMeter turns your birthdate into a live dashboard — days alive, seconds,
        food consumed, global deaths and births, your horoscope sign, and a few
        absurd curiosities. Set it once, it remembers forever.
      </p>
      <div className={styles.cta}>
        <a className={styles.btnPrimary} href={APK_URL} target="_blank" rel="noreferrer">
          Download APK
        </a>
        <a className={styles.btnGhost} href={GITHUB_URL} target="_blank" rel="noreferrer">
          View GitHub
        </a>
      </div>
    </section>
  )
}

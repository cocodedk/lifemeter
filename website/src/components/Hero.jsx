import styles from './Hero.module.css'

const GITHUB_URL = 'https://github.com/cocodedk/lifemeter'

export default function Hero() {
  return (
    <section className={`${styles.hero} reveal`}>
      <p className={styles.kicker}>Android · Works offline · No account needed</p>
      <h1 className={styles.h1}>How long have<br />you been alive?</h1>
      <p className={styles.copy}>
        LifeMeter turns your birth date into a live dashboard of numbers about your life:
        days and seconds alive, an estimate of the food you have eaten, estimated worldwide
        deaths since you were born, your horoscope sign, and a few curiosities. Set your
        birth date once, and the app saves it for your next visit.
      </p>
      <div className={styles.cta}>
        <a className={styles.btnPrimary} href="#install">
          Get the app
        </a>
        <a className={styles.btnGhost} href={GITHUB_URL} target="_blank" rel="noreferrer">
          View GitHub
        </a>
      </div>
    </section>
  )
}

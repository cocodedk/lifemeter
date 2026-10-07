import styles from '../components/Hero.module.css'

const GITHUB_URL = 'https://github.com/cocodedk/lifemeter'

export default function Hero() {
  return (
    <section className={`${styles.hero} reveal`}>
      <p className={styles.kicker}>Android · Lokale data · Ingen konti</p>
      <h1 className={styles.h1}>Hvor længe har<br />du levet?</h1>
      <p className={styles.copy}>
        LifeMeter gør din fødselsdato til et levende overblik med dage i live, sekunder,
        mad spist, dødsfald og fødsler i verden, dit stjernetegn og et par absurde
        kuriositeter. Du vælger datoen én gang, og så husker appen den for altid.
      </p>
      <div className={styles.cta}>
        <a className={styles.btnPrimary} href="#install">
          Hent appen
        </a>
        <a className={styles.btnGhost} href={GITHUB_URL} target="_blank" rel="noreferrer">
          Se GitHub
        </a>
      </div>
    </section>
  )
}

import styles from '../components/Hero.module.css'

const GITHUB_URL = 'https://github.com/cocodedk/lifemeter'

export default function Hero() {
  return (
    <section className={`${styles.hero} reveal`}>
      <p className={styles.kicker}>Android · Virker uden internet · Ingen konto</p>
      <h1 className={styles.h1}>Hvor længe har<br />du levet?</h1>
      <p className={styles.copy}>
        LifeMeter gør din fødselsdato til et levende overblik med tal om dit liv: dage og
        sekunder i live, et skøn over, hvor meget mad du har spist, anslåede dødsfald i
        verden, siden du blev født, dit stjernetegn og et par kuriositeter. Du vælger din
        fødselsdato én gang, og appen gemmer den til næste gang.
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

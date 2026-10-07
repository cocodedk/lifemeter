import styles from '../components/Features.module.css'

const features = [
  { title: 'Levende tal for hele livet', body: 'Dage i live, sekunder i live, kilo mad spist og dødsfald, siden du blev født. Det hele tæller op i realtid.' },
  { title: 'Sessionstæller', body: 'Sekunder på skærmen og dødsfald og fødsler, mens du har kigget med. Nulstilles, hver gang du vender tilbage til appen.' },
  { title: 'Stjernetegn og kuriositeter', body: 'Dit stjernetegn med symbol. Og så et tal mere, som tit får folk til at løfte øjenbryn.' },
  { title: 'Husker din dato', body: 'Vælg den én gang. Oversigten er klar med det samme, hver gang du åbner appen. Du skal ikke taste datoen igen eller trykke på Fortsæt.' },
  { title: 'Træk ned for at skifte', body: 'Stryg ned hvor som helst på skærmen for at åbne datovælgeren. Prøv med en vens fødselsdato.' },
  { title: 'Bliver på telefonen', body: 'Ingen server, ingen konto, ingen analyseværktøjer. Din fødselsdato ligger i Androids SharedPreferences og ingen andre steder.' },
]

export default function Features() {
  return (
    <section id="features" className={`${styles.section} reveal`}>
      <h2 id="how" className={styles.h2}>Det kan appen</h2>
      <div className={styles.grid}>
        {features.map(f => (
          <article key={f.title} className={`${styles.card} glass`}>
            <h3 className={styles.cardTitle}>{f.title}</h3>
            <p className={styles.cardBody}>{f.body}</p>
          </article>
        ))}
      </div>

      <blockquote className={`${styles.quote} glass`}>
        LifeMeter dømmer ikke, sporer ingenting og sender ingenting nogen steder hen. Den tæller bare.
      </blockquote>
    </section>
  )
}

import styles from '../components/Features.module.css'

const features = [
  { title: 'Hele livet i alt', body: 'Se dine dage i live, sekunder i live, anslået mad spist og anslåede dødsfald i verden, siden du blev født. Sekunderne og skønnet over dødsfald bliver regnet ud på ny hvert sekund, mens appen er åben. Store tal bliver afrundet.' },
  { title: 'Denne session', body: 'Se, hvor mange sekunder appen har været åben, og de anslåede fødsler og dødsfald i verden i den tid. Tællerne starter forfra, når du vender tilbage til appen eller vælger en ny fødselsdato.' },
  { title: 'Stjernetegn og kuriositeter', body: 'Se dit stjernetegn og dets symbol og de planeter, solen eller månen, der hører til. Kuriositetskortet skønner også, hvor mange timer du har brugt på sex siden din 16-års fødselsdag, ud fra gennemsnit fra undersøgelser.' },
  { title: 'Gemmer din fødselsdato', body: 'Vælg din fødselsdato én gang. Din oversigt åbner med dine tal, hver gang du starter appen, uden at du skal vælge datoen igen.' },
  { title: 'Skift fødselsdato', body: 'Tryk på Skift fødselsdato, eller rul op til toppen og træk ned, for at vælge en anden dato. Prøv med en vens fødselsdato.' },
  { title: 'Bliver på din telefon', body: 'Ingen konto og ingen analyse, og appen har ingen tilladelse til internettet. Din fødselsdato bliver gemt i appens private lager og kan komme med i din Android-sikkerhedskopi.' },
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
        LifeMeter sporer dig ikke, og appen har ingen tilladelse til internettet. Den regner
        bare.
      </blockquote>
    </section>
  )
}

import { useEffect, useRef } from 'react'
import styles from '../components/Install.module.css'

const steps = [
  { n: 1, title: 'Tillad installation', body: 'Hvis du har hentet installationsfilen (APK) fra GitHub, så åbn den. Når Android spørger, skal du tillade, at din browser eller filhåndtering installerer apps, og så trykke på Installér.' },
  { n: 2, title: 'Åbn appen og vælg din fødselsdato', body: <>Tryk på <em>Vælg fødselsdato</em>, vælg din dato, og din oversigt åbner med det samme.</> },
]

export default function Install() {
  const slot = useRef(null)

  // The install block (F-Droid, APK, Obtainium) is written into da/index.html by the cocode-apps
  // tools, inside <template id="install-block">, so it is not part of this tree: copy it in.
  useEffect(() => {
    const template = document.getElementById('install-block')
    if (template && slot.current) slot.current.replaceChildren(template.content.cloneNode(true))
  }, [])

  return (
    <section id="install" className={`${styles.section} glass reveal`}>
      <h2 className={styles.h2}>Installér på Android</h2>
      <div ref={slot} />
      <div className={styles.steps}>
        {steps.map(s => (
          <div key={s.n} className={styles.step}>
            <span className={styles.num}>{s.n}</span>
            <div>
              <h3 className={styles.stepTitle}>{s.title}</h3>
              <p className={styles.stepBody}>{s.body}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  )
}

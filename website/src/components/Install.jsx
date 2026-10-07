import { useEffect, useRef } from 'react'
import styles from './Install.module.css'

const steps = [
  { n: 1, title: 'Allow installation', body: 'If you install the APK, enable "Install from unknown sources" for your browser or file manager when prompted.' },
  { n: 2, title: 'Open and set your birthdate', body: <>Tap <em>Set birth date</em>, pick your date, and your live dashboard appears instantly.</> },
]

export default function Install() {
  const slot = useRef(null)

  // The install block (F-Droid, APK, Obtainium) is written into index.html by the cocode-apps
  // tools, inside <template id="install-block">, so it is not part of this tree: copy it in.
  useEffect(() => {
    const template = document.getElementById('install-block')
    if (template && slot.current) slot.current.replaceChildren(template.content.cloneNode(true))
  }, [])

  return (
    <section id="install" className={`${styles.section} glass reveal`}>
      <h2 className={styles.h2}>Install on Android</h2>
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

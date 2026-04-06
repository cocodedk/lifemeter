import styles from './Install.module.css'

const APK_URL = 'https://github.com/cocodedk/lifemeter/releases/latest/download/LifeMeter.apk'

const steps = [
  { n: 1, title: 'Download the APK', body: <>Tap the button below or grab it from <a href="https://github.com/cocodedk/lifemeter/releases" target="_blank" rel="noreferrer">GitHub Releases</a>.</> },
  { n: 2, title: 'Allow installation', body: 'Enable "Install from unknown sources" for your browser or file manager when prompted.' },
  { n: 3, title: 'Open and set your birthdate', body: <>Tap <em>Set birth date</em>, pick your date, and your live dashboard appears instantly.</> },
]

export default function Install() {
  return (
    <section id="install" className={`${styles.section} glass reveal`}>
      <h2 className={styles.h2}>Install on Android</h2>
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
      <a className={styles.btn} href={APK_URL} target="_blank" rel="noreferrer">
        Download LifeMeter.apk
      </a>
    </section>
  )
}

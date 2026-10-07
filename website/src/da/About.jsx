import styles from '../components/About.module.css'

export default function About() {
  return (
    <section id="about" className={`${styles.section} glass reveal`}>
      <h2 className={styles.h2}>Om</h2>
      <p className={styles.body}><strong>Skabt af Babak Bandpey.</strong></p>
      <p className={styles.body} style={{ marginTop: 8 }}>
        <strong>Bygget af <a href="https://cocode.dk" target="_blank" rel="noreferrer">Cocode</a>.</strong>
      </p>
      <p className={styles.body} style={{ marginTop: 12 }}>
        LifeMeter og BabakCast udgør en lille værktøjskasse af Android-apps til personlig brug:
        praktiske, private og hurtige.
      </p>
      <div className={styles.links}>
        <a href="https://github.com/cocodedk/lifemeter" target="_blank" rel="noreferrer">Kildekode til LifeMeter</a>
        <a href="https://github.com/cocodedk/BabakCast" target="_blank" rel="noreferrer">BabakCast</a>
        <a href="https://cocode.dk" target="_blank" rel="noreferrer">cocode.dk</a>
      </div>
    </section>
  )
}

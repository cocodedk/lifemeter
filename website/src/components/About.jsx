import styles from './About.module.css'

export default function About() {
  return (
    <section id="about" className={`${styles.section} glass reveal`}>
      <h2 className={styles.h2}>About</h2>
      <p className={styles.body}><strong>Created by Babak Bandpey.</strong></p>
      <p className={styles.body} style={{ marginTop: 8 }}>
        <strong>Built by <a href="https://cocode.dk" target="_blank" rel="noreferrer">Cocode</a>.</strong>
      </p>
      <p className={styles.body} style={{ marginTop: 12 }}>
        LifeMeter and BabakCast form a small toolchain of personal-use Android apps — practical,
        private, and fast.
      </p>
      <div className={styles.links}>
        <a href="https://github.com/cocodedk/lifemeter" target="_blank" rel="noreferrer">LifeMeter Repository</a>
        <a href="https://github.com/cocodedk/BabakCast" target="_blank" rel="noreferrer">BabakCast</a>
        <a href="https://cocode.dk" target="_blank" rel="noreferrer">cocode.dk</a>
      </div>
    </section>
  )
}

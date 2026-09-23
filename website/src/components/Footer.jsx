import styles from './Footer.module.css'

// The old footer's licence/copyright/Cocode line is replaced by cocode-foot below; the Privacy
// Policy link stays, in a slim footer of its own.
export default function Footer() {
  return (
    <>
      <footer className={`${styles.footer} shell`}>
        <p><a href="/privacy.html">Privacy Policy</a></p>
      </footer>

      <cocode-foot repo="cocodedk/lifemeter" dark>
        <a href="https://cocode.dk">cocode.dk</a>
        <a href="https://github.com/cocodedk/lifemeter">GitHub</a>
      </cocode-foot>
    </>
  )
}

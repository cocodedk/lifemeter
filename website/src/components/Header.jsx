import styles from './Header.module.css'

export default function Header() {
  return (
    <header className={`${styles.header} shell`}>
      <div className={styles.brand}>
        <img src="favicon.svg" alt="LifeMeter" width={28} height={28} />
        <span>LifeMeter</span>
      </div>
      <nav className={styles.nav}>
        <a href="#try">Try it</a>
        <a href="#features">Features</a>
        <a href="#install">Install</a>
        <a href="#about">About</a>
      </nav>
    </header>
  )
}

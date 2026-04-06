import styles from './Footer.module.css'

export default function Footer() {
  return (
    <footer className={`${styles.footer} shell`}>
      <p>Apache-2.0 &nbsp;·&nbsp; © 2026 <a href="https://cocode.dk" target="_blank" rel="noreferrer">Cocode</a> &nbsp;·&nbsp; LifeMeter</p>
    </footer>
  )
}

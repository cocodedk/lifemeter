import Hero from './Hero'
import Calculator from './Calculator'
import Features from './Features'
import Install from './Install'
import About from './About'

// The Danish home page. The English components stay as they are; these reuse their styles.
export default function App() {
  return (
    <>
      <div className="bg-grid" />
      <div className="bg-orb orb-a" />
      <div className="bg-orb orb-b" />
      <main id="main" tabIndex={-1} className="shell">
        <Hero />
        <Calculator />
        <Features />
        <Install />
        <About />
      </main>
    </>
  )
}

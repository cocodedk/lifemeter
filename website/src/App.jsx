import Hero from './components/Hero'
import Calculator from './components/Calculator'
import Features from './components/Features'
import Install from './components/Install'
import About from './components/About'

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

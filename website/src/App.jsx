import Header from './components/Header'
import Hero from './components/Hero'
import Calculator from './components/Calculator'
import Features from './components/Features'
import Install from './components/Install'
import About from './components/About'
import Footer from './components/Footer'

export default function App() {
  return (
    <>
      <div className="bg-grid" />
      <div className="bg-orb orb-a" />
      <div className="bg-orb orb-b" />
      <Header />
      <main className="shell">
        <Hero />
        <Calculator />
        <Features />
        <Install />
        <About />
      </main>
      <Footer />
    </>
  )
}

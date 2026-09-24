// The cocode.dk family frame replaces the site's own top bar (brand + in-page nav). Without
// JavaScript its children show instead: a way home and the in-page anchors that would
// otherwise be lost once the old <nav> is gone.
export default function Header() {
  return (
    <cocode-head
      project="LifeMeter"
      accent="#e8882a"
      on-accent="#120a04"
      links="Try it:#try,Features:#features,Install:#install,About:#about"
      dark
    >
      <a href="https://cocode.dk">cocode.dk</a>
    </cocode-head>
  )
}

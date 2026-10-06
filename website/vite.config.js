import { cpSync, existsSync } from 'node:fs'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// The cocode-apps tools write css/cocode-nav.css and the img/ files (app icon, F-Droid badge)
// next to index.html, outside Vite's public/ folder. Copy them into the build so that
// /css/cocode-nav.css and /img/icon.png answer on the deployed site.
const copyFolders = (...folders) => ({
  name: 'copy-cocode-apps-folders',
  closeBundle() {
    for (const folder of folders) {
      if (existsSync(folder)) cpSync(folder, `dist/${folder}`, { recursive: true })
    }
  },
})

export default defineConfig({
  plugins: [react(), copyFolders('css', 'img')],
  base: '/',
  build: {
    rollupOptions: {
      input: { main: 'index.html', privacy: 'privacy/index.html' },
    },
  },
})

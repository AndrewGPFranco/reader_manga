import { defineStore } from 'pinia'
import { darkTheme } from 'naive-ui'

export const useSystemStore = defineStore('system', {
  state: () => ({
    theme: null as typeof darkTheme | null
  }),

  actions: {
    getLatestTheme(): void {
      const themeLocal: string | null = localStorage.getItem('theme')

      if (themeLocal) themeLocal === 'dark' ? (this.theme = darkTheme) : (this.theme = null)
    },
    alterTheme(): void {
      this.theme === null ? (this.theme = darkTheme) : (this.theme = null)
      localStorage.setItem('theme', this.theme === null ? 'light' : 'dark')
    }
  }
})

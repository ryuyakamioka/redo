// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-02-13',
  devtools: { enabled: true },
  modules: ['@pinia/nuxt'],
  app: {
    head: {
      script: [
        { src: 'https://cdn.tailwindcss.com', defer: true }
      ]
    }
  },
  runtimeConfig: {
    public: {
      apiBase: process.env.NUXT_PUBLIC_API_BASE || "/api",
    },
  },
  nitro: {
    devProxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
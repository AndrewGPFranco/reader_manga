import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import RegisterView from '@/views/RegisterView.vue'
import MangaView from '@/views/MangaView.vue'
import MangaViewById from '@/views/MangaViewById.vue'
import ChapterReading from '@/views/ChapterReading.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/register',
      name: 'registerChapter',
      component: RegisterView
    },
    {
      path: '/manga/all',
      name: 'mangaAll',
      component: MangaView
    },
    {
      path: '/manga/:id',
      name: 'mangaId',
      component: MangaViewById
    },
    {
      path: '/manga/:id/chapter/:id',
      name: 'chapterReading',
      component: ChapterReading
    }
  ]
})

export default router
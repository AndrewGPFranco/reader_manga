import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import RegisterView from '@/views/RegisterView.vue'
import MangaList from '@/views/MangaList.vue'
import MangaDetails from '@/views/MangaDetails.vue'
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
      component: MangaList
    },
    {
      path: '/manga/:id',
      name: 'mangaId',
      component: MangaDetails
    },
    {
      path: '/manga/:id/chapter/:id',
      name: 'chapterReading',
      component: ChapterReading
    }
  ]
})

export default router
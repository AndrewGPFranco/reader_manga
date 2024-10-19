import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import RegisterManga from '@/views/RegisterManga.vue'
import RegisterView from '@/views/RegisterView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/register/manga',
      name: 'registerManga',
      component: RegisterManga
    },
    {
      path: '/register/chapter',
      name: 'registerChapter',
      component: RegisterView
    }
  ]
})

export default router
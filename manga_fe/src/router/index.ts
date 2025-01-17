import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import RegisterView from '@/views/RegisterView.vue'
import MangaList from '@/views/MangaList.vue'
import MangaDetails from '@/views/MangaDetails.vue'
import ChapterReading from '@/views/ChapterReading.vue'
import ManagementAdmin from '@/views/ManagementAdmin.vue'
import FavoriteManga from '@/views/FavoriteManga.vue'
import AllMangasList from '@/views/AllMangasList.vue'
import ProfileView from '@/views/ProfileView.vue'
import LoginView from '@/views/LoginView.vue'
import { useAuthStore } from '@/store/AuthStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
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
    },
    {
      path: '/management/admin',
      name: 'managementAdmin',
      component: ManagementAdmin
    },
    {
      path: '/manga/favorites',
      name: 'favoriteManga',
      component: FavoriteManga
    },
    {
      path: '/mangas',
      name: 'mangas',
      component: AllMangasList
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView
    }
  ]
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.isUserAutenticado();

  if (to.name !== 'login' && !isAuthenticated) {
    next({ name: 'login' });
  } else if(to.name === 'login' && isAuthenticated) {
    next({ name: 'home' });
  } else {
    next();
  }
});

export default router
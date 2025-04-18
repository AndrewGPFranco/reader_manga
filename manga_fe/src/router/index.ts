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
import RegisterUser from '@/views/RegisterUser.vue'
import JobExecutor from '@/views/JobExecutor.vue'
import ProgressReadings from '@/views/ProgressReadings.vue'
import EpisodeAnime from '@/views/EpisodeAnime.vue'

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
      path: '/manga/:title',
      name: 'mangaDetails',
      component: MangaDetails
    },
    {
      path: '/manga/:title/chapter/:id/:progress',
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
    },
    {
      path: '/register/user',
      name: 'registerUser',
      component: RegisterUser
    },
    {
      path: '/admin/jobs',
      name: 'jobs',
      component: JobExecutor
    },
    {
      path: '/progress-readings',
      name: 'progress-readings',
      component: ProgressReadings
    },
    {
      path: '/episode',
      name: 'episode',
      component: EpisodeAnime
    }
  ]
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.isUserAutenticado();

  const publicRoutes = ['login', 'registerUser'];
  
  if (to.name && publicRoutes.includes(to.name.toString())) {
    if (isAuthenticated) {
      next({ name: 'home' });
    } else {
      next();
    }
  } else if (!isAuthenticated) {
      next({ name: 'login' });
    } else {
      next();
    }
});

export default router;
<template>
  <NavbarComponent />
  <main>
    <n-card class="anime-card" size="huge">
      <section v-if="!renderEpisode">
        <div class="anime-info">
          <h2 class="anime-title">{{ title }}</h2>
          <p v-if="animeList.episodes" class="episodes-count">{{ animeList.episodes.length }} episódios</p>
        </div>

        <div class="episodes-grid">
          <EpisodeCard v-for="episode in animeList.episodes || []" :key="episode.id" :episode="episode"
            @play="playEpisode" />
        </div>
      </section>

      <section v-else class="container-video">
        <VideoEpisodeAnime :title="title" :id="idEpisode" />
      </section>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { NCard } from 'naive-ui'
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import VideoEpisodeAnime from '@/components/anime/VideoEpisodeAnime.vue'
import EpisodeCard from '@/components/episode/EpisodeCard.vue'
import { useEpisodeStore } from '@/store/EpisodeStore'
import type { AnimeListingVO } from '@/@types/AnimeListingVO'
import type { EpisodeToAnimesVO } from '@/@types/iEpisodeToAnimesVO'

const route = useRoute()
const title = ref<string>('')
const idAnime = ref<string>('')
const idEpisode = ref<string>('')
const renderEpisode = ref<boolean>(false)
const episodeStore = useEpisodeStore()
const animeList = ref<AnimeListingVO>({} as AnimeListingVO)

onMounted(async () => {
  title.value = Array.isArray(route.params.title) ? route.params.title[0] : route.params.title
  idAnime.value = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
  const animeListResponse = await episodeStore.getAllEpisodesByAnime(idAnime.value)
  animeList.value = animeListResponse
})

const playEpisode = (episode: EpisodeToAnimesVO) => {
  idEpisode.value = episode.id.toString()
  renderEpisode.value = true
}
</script>

<style scoped>
main {
  padding: 15px;
  background-color: #f9f9f9;
}

.anime-card {
  border-radius: 12px;
  background-color: #ffffff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(12px);
  min-height: 95vh;
}

.anime-info {
  margin-bottom: 24px;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 12px;
}

.anime-title {
  font-size: 28px;
  font-weight: bold;
  color: #ff5722;
}

.episodes-count {
  color: #757575;
  font-size: 16px;
}

.episodes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
  padding-bottom: 20px;
}

.container-video {
  margin-top: -8px;
}
</style>

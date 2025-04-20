<template>
  <NavbarComponent />
  <main>
    <n-card
      class="flex flex-col anime-card"
      size="huge"
      style="height: 95vh; overflow-y: auto"
    >
      <section v-if="!renderEpisode" class="episodes-container">
        <div class="anime-info">
          <h2 class="anime-title">{{ title }}</h2>
          <p class="episodes-count">{{ allEpisodes.length }} episódios</p>
        </div>

        <div class="episodes-grid">
          <div
            v-for="episode in allEpisodes"
            :key="episode.id"
            class="episode-card"
            @click="playEpisode(episode)"
          >
            <div class="episode-thumbnail">
              <img
                src="https://github.com/AndrewGPFranco.png"
                :alt="episode.title"
                onerror="this.src='/images/default-anime-thumb.jpg'"
              />
              <div class="play-overlay">
                <n-icon size="36">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path fill="currentColor" d="M8 5v14l11-7z" />
                  </svg>
                </n-icon>
              </div>
            </div>
            <div class="episode-info">
              <span class="episode-number">EP {{ episode.id }}</span>
              <h3 class="episode-title">{{ episode.title }}</h3>
            </div>
          </div>
        </div>
      </section>
      <VideoEpisodeAnime v-else :title="title" :id="idEpisode" />
    </n-card>
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import VideoEpisodeAnime from '@/components/animes/VideoEpisodeAnime.vue'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { NCard, NIcon } from 'naive-ui'
import { useEpisodeStore } from '@/store/EpisodeStore'
import type { iEpisode } from '@/@types/iEpisode'

const route = useRoute()

const title = ref<string>('')
const idAnime = ref<string>('')
const episodeStore = useEpisodeStore()
const idEpisode = ref<string>('')
const renderEpisode = ref<boolean>(false)
const allEpisodes = ref<Array<iEpisode>>([])
const selectedEpisode = ref<iEpisode | null>(null)

onMounted(async () => {
  title.value = Array.isArray(route.params.title) ? route.params.title[0] : route.params.title
  idAnime.value = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id

  allEpisodes.value = await episodeStore.getAllEpisodesByAnime(idAnime.value)
})

const playEpisode = (episode: iEpisode) => {
  selectedEpisode.value = episode
  idEpisode.value = episode.id.toString();
  renderEpisode.value = true
}
</script>

<style scoped>
main {
  padding: 15px;
  background-color: #f5f5f5;
}

.anime-card {
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.anime-info {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #e0e0e0;
}

.anime-title {
  font-size: 28px;
  font-weight: 700;
  color: #ff5722;
  margin-bottom: 8px;
}

.episodes-count {
  color: #616161;
  font-size: 16px;
}

.episodes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}

.episode-card {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
  cursor: pointer;
  background-color: #ffffff;
}

.episode-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 16px rgba(0, 0, 0, 0.2);
}

.episode-thumbnail {
  position: relative;
  height: 140px;
  overflow: hidden;
}

.episode-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  color: white;
}

.episode-card:hover .play-overlay {
  opacity: 1;
}

.episode-info {
  padding: 12px;
}

.episode-number {
  display: inline-block;
  background-color: #ff5722;
  color: white;
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 8px;
}

.episode-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  line-height: 1.4;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>

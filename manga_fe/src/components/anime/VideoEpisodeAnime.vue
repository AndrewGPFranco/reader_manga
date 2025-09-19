<template>
  <div class="video-wrapper">
    <div v-if="videoUrl" class="video-container">
      <video :src="videoUrl" controls class="video-player rounded-lg" />
    </div>

    <div v-else class="loading-container">
      <div class="spinner"></div>
      <p class="text-lg text-gray-600">Carregando vídeo...</p>
    </div>

    <div v-if="error" class="mt-4 p-4 text-red-700 rounded-md">
      {{ error }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useEpisodeStore } from '@/store/EpisodeStore'

const episodeStore = useEpisodeStore()
const error = ref<string | null>(null)
const videoUrl = ref<string | null>(null)

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  id: {
    type: String,
    required: true
  }
})

onMounted(async () => {
  document.title = 'Episódio de Anime - Assistindo'

  try {
    const response = await episodeStore.getEpisode(props.title, props.id)
    videoUrl.value = URL.createObjectURL(response)
  } catch (err) {
    console.error('Erro ao carregar o vídeo:', err)
    error.value = 'Não foi possível carregar o vídeo. Por favor, tente novamente.'
  }
})
</script>

<style scoped>
.video-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}

.video-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.video-player {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background-color: #000;
}

.loading-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.spinner {
  animation: spin 1s linear infinite;
  border-top: 2px solid #6366f1;
  border-bottom: 2px solid #6366f1;
  border-radius: 50%;
  height: 48px;
  width: 48px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>

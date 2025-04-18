<template>
  <div>
    <div v-if="videoUrl" class="video-container">
      <video :src="videoUrl" controls class="rounded-lg shadow-lg h-96" />
    </div>

    <div v-else class="flex flex-col items-center justify-center h-64 bg-gray-100 rounded-lg">
      <div
        class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-600 mb-4"
      ></div>
      <p class="text-lg text-gray-600">Carregando vídeo...</p>
    </div>

    <div v-if="error" class="mt-4 p-4 bg-red-100 text-red-700 rounded-md">
      {{ error }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useAnimeStore } from '@/store/AnimeStore'

const error = ref<string | null>(null)
const videoUrl = ref<string | null>(null)

const animeStore = useAnimeStore()

onMounted(async () => {
  document.title = 'Episódio de Anime - Assistindo'

  try {
    const response = await animeStore.getEpisode()
    videoUrl.value = URL.createObjectURL(response)
  } catch (err) {
    console.error('Erro ao carregar o vídeo:', err)
    error.value = 'Não foi possível carregar o vídeo. Por favor, tente novamente.'
  }
})
</script>

<style scoped>
.video-container {
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
}

video {
  width: 73%;
  height: 100%;
  border: 1px solid #000000;
}

video:focus {
  outline: none;
}
</style>

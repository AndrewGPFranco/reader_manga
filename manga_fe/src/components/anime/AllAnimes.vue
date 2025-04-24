<template>
  <section class="p-4">
    <n-card ref="animeCard" title="Animes" class="h-[95vh] overflow-y-auto" size="huge">
      <section class="mt-5 grid grid-cols-[repeat(auto-fill,minmax(200px,1fr))] gap-6">
        <div v-if="!animesArray.length" class="col-span-full text-center text-gray-600">
          <p>Nenhum anime a ser exibido!</p>
        </div>

        <div
          v-else
          v-for="anime in animesArray"
          :key="anime.title"
          class="bg-white rounded-xl shadow-md hover:shadow-lg transition duration-300 overflow-hidden"
        >
          <div class="aspect-[3/2] w-full overflow-hidden">
            <img
              class="w-full h-full object-cover"
              :src="anime.uriImage"
              alt="Capa do Anime"
            />
          </div>

          <div class="p-4">
            <router-link
              :to="`/anime/${anime.title}/${anime.id}`"
              class="block text-lg font-semibold text-gray-800 truncate hover:text-blue-500 transition"
              :title="anime.title"
            >
              {{ anime.title }}
            </router-link>
          </div>
        </div>
      </section>
    </n-card>
  </section>
</template>

<script setup lang="ts">
import { NCard, useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import type { iAnime } from '@/@types/iAnime'
import { useAnimeStore } from '@/store/AnimeStore'

const message = useMessage()
const animeStore = useAnimeStore()
const animesArray = ref<iAnime[]>([])
const animeCard = ref<InstanceType<typeof NCard> | null>(null)

const getAnimes = async () => {
  animesArray.value = await animeStore.findAll()
}

onMounted(async () => {
  document.title = 'Leitor de mangás - Mangás'

  try {
    await getAnimes()
  } catch (error: any) {
    message.error(error.message || 'Erro ao buscar os mangás')
  }
})
</script>

<style scoped>
.n-card ::-webkit-scrollbar {
  width: 6px;
}

.n-card ::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 8px;
}

.n-card ::-webkit-scrollbar-thumb:hover {
  background-color: #555;
}
</style>
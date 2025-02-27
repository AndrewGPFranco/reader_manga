<template>
  <header>
    <NavbarComponent />
  </header>
  <main class="text-black bg-white p-10 shadow-lg max-h-screen overflow-y-auto">
    <n-card>
      <div class="flex items-center space-x-4 mb-6">
        <img :src="manga.image" alt="Mangá" class="w-24 h-24 object-cover rounded-lg shadow-lg" />
        <div>
          <h1 class="text-2xl font-bold">{{ manga.title }}</h1>
          <p class="text-black text-sm">{{ manga.author }}</p>
        </div>
      </div>

      <div class="space-y-2 mb-6">
        <p><span class="font-semibold">Qtde. Capítulos:</span> {{ manga.size }}</p>
        <p><span class="font-semibold">Gênero:</span> {{ manga.gender }}</p>
        <p><span class="font-semibold">Status:</span> {{ manga.status }}</p>
        <p>
          <span class="font-semibold">Data de criação:</span> {{ formatDate(manga.creationDate) }}
        </p>
        <p><span class="font-semibold">Finalizado em:</span> {{ verifyEndDate(manga) }}</p>
        <p><span class="font-semibold">Descrição:</span> {{ manga.description }}</p>
      </div>

      <div>
        <h2 class="text-xl font-semibold border-b border-black mb-4 pb-2">Chapters</h2>
        <ul class="space-y-4">
          <li
            v-for="chapter in manga.chapters"
            :key="chapter.title"
            class="bg-white p-4 rounded-lg shadow-lg border border-gray-200"
          >
            <router-link
              :to="`/manga/${manga.id}/chapter/${chapter.id}`"
              class="font-semibold text-lg"
              >{{ chapter.title }}</router-link
            >
            <p class="text-black">{{ chapter.description }}</p>
            <p><span class="text-black">Páginas:</span> {{ chapter.numberPages }}</p>
          </li>
        </ul>
      </div>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import type iMangaData from '@/interface/Manga'
import { useMangaStore } from '@/store/MangaStore'
import { formatDate } from '@/utils/utils'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const mangaStore = useMangaStore()
const route = useRoute()
let manga = ref<iMangaData>({} as iMangaData)

onMounted(async () => {
  const id: string = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
  manga.value = await mangaStore.getInfoManga(id)
})

function verifyEndDate(str: iMangaData): any {
  return str.endDate != undefined ? formatDate(str.endDate) : 'Still on display.'
}
</script>

<style scoped>
main {
  padding: 15px;
}
</style>

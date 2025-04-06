<template>
  <header>
    <NavbarComponent />
  </header>
  <main id="main-container" class="text-black bg-white p-10 shadow-lg max-h-screen overflow-y-auto">
    <n-card style="height: 95vh; overflow-y: auto">
      <div class="flex items-center space-x-4 mb-6">
        <img :src="manga.image" alt="Mangá" class="w-24 h-24 object-cover rounded-lg shadow-lg" />
        <div>
          <h1 class="text-2xl font-bold">{{ manga.title }}</h1>
        </div>
      </div>

      <div class="space-y-2 mb-6">
        <p><span class="font-semibold">Qtde. Capítulos:</span> {{ manga.size }}</p>
        <p><span class="font-semibold">Gênero:</span> {{ manga.gender }}</p>
        <p><span class="font-semibold">Status:</span> {{ manga.status }}</p>
        <p><span class="font-semibold">Autor:</span> {{ manga.author }}</p>
        <p>
          <span class="font-semibold">Data de criação:</span> {{ formatDate(manga.creationDate) }}
        </p>
        <p><span class="font-semibold">Finalizado em:</span> {{ verifyEndDate(manga) }}</p>
        <p><span class="font-semibold">Descrição:</span> {{ manga.description }}</p>
      </div>

      <div>
        <h2 class="text-xl font-semibold border-b border-black mb-4 pb-2">Capítulos</h2>
        <ul class="space-y-4">
          <li v-for="chapter in chapterOrdenados" :key="chapter.id" :class="[
            'p-4 rounded-lg shadow-lg border border-gray-200',
            chapter.status === StatusType.FINISHED ? 'bg-green-100' : 'bg-white'
          ]">

            <p v-if="chapter.readingProgress != 0" class="font-semibold text-lg cursor-pointer" @click="askContinueReading(chapter)">{{ chapter.title }}</p>
            <router-link :to="`/manga/${manga.title}/chapter/${chapter.id}/1`" v-else class="font-semibold text-lg cursor-router-linkointer">{{ chapter.title }}</router-link>
            <p class="mt-2"><span class="text-black">Páginas:</span> {{ chapter.numberPages }}</p>
            <p>
              <span class="text-black">Progresso:</span>
              {{
                chapter.status === StatusType.FINISHED
                  ? 'Leitura finalizada'
                  : chapter.readingProgress === 0
                    ? 'Leitura não iniciada'
                    : `Pág: ${chapter.readingProgress}`
              }}
            </p>
          </li>
        </ul>
      </div>
    </n-card>
  </main>

  <n-modal v-model:show="isShowDialog">
    <n-card style="width: 600px; max-width: 90%; background: rgba(255, 255, 255, 0.9); border-radius: 12px;"
      title="Deseja continuar de onde parou?" :bordered="false" size="huge" role="dialog" aria-modal="true">
      <div class="flex justify-center gap-4 mt-4">
        <router-link v-if="selectedChapter"
          :to="`/manga/${manga.title}/chapter/${selectedChapter.id}/${selectedChapter.readingProgress}`"
          class="px-6 py-2 rounded-lg text-white font-semibold text-lg transition-all duration-200 ease-in-out bg-green-600 hover:bg-green-700">
          Sim
        </router-link>
        <router-link v-if="selectedChapter" :to="`/manga/${manga.title}/chapter/${selectedChapter.id}/1`"
          class="px-6 py-2 rounded-lg text-white font-semibold text-lg transition-all duration-200 ease-in-out bg-gray-600 hover:bg-gray-700">
          Não
        </router-link>
      </div>
    </n-card>
  </n-modal>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import type iMangaData from '@/@types/Manga'
import { useMangaStore } from '@/store/MangaStore'
import { formatDate } from '@/utils/utils'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import type iChapterData from '@/@types/iChapter'
import { StatusType } from '@/enum/StatusType'

const mangaStore = useMangaStore()
const route = useRoute()

const manga = ref<iMangaData>({} as iMangaData)
const chapterOrdenados = ref<iChapterData[]>([])
const isShowDialog = ref<boolean>(false)
const selectedChapter = ref<iChapterData | null>(null)

onMounted(async () => {
  manga.value = {} as iMangaData;
  
  const title: string = Array.isArray(route.params.title)
  ? route.params.title[0]
  : route.params.title

  document.title = `Leitor de mangás - ${title}`

  manga.value = await mangaStore.getInfoManga(title)

  if (manga.value.chapters) {
    chapterOrdenados.value = [...manga.value.chapters].sort((a, b) =>
      a.title.localeCompare(b.title)
    )
  }
})

const verifyEndDate = (str: iMangaData): string => {
  return str.endDate ? formatDate(str.endDate) : 'Still on display.'
}

const askContinueReading = (chapter: iChapterData) => {
  selectedChapter.value = chapter
  isShowDialog.value = true
}
</script>

<style scoped>
main {
  padding: 15px;
}
</style>

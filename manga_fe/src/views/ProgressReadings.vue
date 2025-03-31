<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <n-card
      class="containerCard"
      title="Leituras em andamento"
      size="huge"
      style="height: 95vh; overflow-y: auto"
    >
      <div>
        <h2 class="text-xl font-semibold border-b border-black mb-4 pb-2">Capítulos</h2>
        <ul class="grid-container">
          <li
            v-for="chapter in chapters"
            :key="chapter.id"
            class="chapter-card"
            tabindex="0"
            @click="askContinueReading(chapter)"
            @keydown.enter="askContinueReading(chapter)"
          >
            <div class="card-content">
              <img :src="chapter.urlImageManga" alt="Capa do Mangá" class="chapter-image" />
              <div class="chapter-info">
                <p class="chapter-title">{{ chapter.title }}</p>
                <p class="chapter-pages">
                  <span class="label">Páginas:</span> {{ chapter.numberPages }}
                </p>
                <p class="chapter-progress">
                  <span class="label">Progresso:</span>
                  {{ chapter.readingProgress }}
                </p>
                <n-modal v-model:show="isShowDialog">
                  <n-card
                    style="
                      width: 600px;
                      max-width: 90%;
                      background: rgba(255, 255, 255, 0.9);
                      border-radius: 12px;
                    "
                    title="Deseja continuar de onde parou?"
                    :bordered="false"
                    size="huge"
                    role="dialog"
                    aria-modal="true"
                  >
                    <div class="flex justify-center gap-4 mt-4">
                      <router-link
                        v-if="selectedChapter"
                        :to="`/manga/${selectedChapter.nameManga}/chapter/${selectedChapter.id}/${selectedChapter.readingProgress}`"
                        class="px-6 py-2 rounded-lg text-white font-semibold text-lg transition-all duration-200 ease-in-out bg-green-600 hover:bg-green-700"
                      >
                        Sim
                      </router-link>
                      <router-link
                        v-if="selectedChapter"
                        :to="`/manga/${selectedChapter.nameManga}/chapter/${selectedChapter.id}/1`"
                        class="px-6 py-2 rounded-lg text-white font-semibold text-lg transition-all duration-200 ease-in-out bg-gray-600 hover:bg-gray-700"
                      >
                        Não
                      </router-link>
                    </div>
                  </n-card>
                </n-modal>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div>
        <n-pagination class="mt-5" v-model:page="page" :page-count="pageTotal" simple />
      </div>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import type iChapterData from '@/@types/iChapter'
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import { useChapterStore } from '@/store/ChapterStore'
import { NCard } from 'naive-ui'
import { onMounted, ref, watch } from 'vue'

const chapterStore = useChapterStore()
const isShowDialog = ref(false)
const selectedChapter = ref<iChapterData | null>(null)
const chapters = ref<iChapterData[]>([])

let page = ref<number>(1)
let pageTotal = ref<number | undefined>(0)

onMounted(async () => {
  document.title = 'Leitor de mangás - Leituras em andamento';

  try {
    const data = await chapterStore.getAllReadingProgress(page.value - 1);
    chapters.value = data;
    pageTotal.value = chapters.value[0].numberPageOfPageable
  } catch (error) {
    console.error('Erro ao carregar os capítulos:', error);
  }
});

const askContinueReading = (chapter: iChapterData) => {
  selectedChapter.value = chapter;
  isShowDialog.value = true;
};

watch(page, async () => {
  const data = await chapterStore.getAllReadingProgress(page.value - 1);
  chapters.value = data;
})
</script>

<style scoped>
main {
  padding: 15px;
}

.grid-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  list-style-type: none;
  padding: 0;
  justify-content: center;
}

.chapter-card {
  width: 250px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: white;
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  border: 1px solid #e2e8f0;
  overflow: hidden;
  flex: 0 0 250px;
}

.chapter-card:hover {
  transform: translateY(-5px);
  box-shadow:
    0 10px 15px -3px rgba(0, 0, 0, 0.1),
    0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chapter-image {
  width: 100%;
  height: 300px;
  object-fit: cover;
}

.chapter-info {
  padding: 16px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.chapter-title {
  font-weight: 600;
  font-size: 1rem;
  margin-bottom: 8px;
  color: #2d3748;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.chapter-pages,
.chapter-progress {
  margin-top: 6px;
  font-size: 0.9rem;
  color: #4a5568;
}

.label {
  font-weight: 600;
  color: #2d3748;
}

@media (max-width: 768px) {
  .grid-container {
    justify-content: space-around;
  }
}

@media (max-width: 560px) {
  .chapter-card {
    width: 100%;
    flex: 0 0 100%;
    max-width: 320px;
  }

  .grid-container {
    justify-content: center;
  }
}
</style>
<template>
  <header>
    <MenuComponent/>
  </header>
  <main>
    <n-card class="containerCard" title="Leituras em andamento" size="huge" style="height: 95vh; overflow-y: auto">
      <div>
        <h2 class="text-xl font-semibold border-b border-black mb-4 pb-2">Capítulos</h2>
        <ul class="grid-container">
          <li v-for="chapter in chapters" :key="chapter.id" class="chapter-card" tabindex="0"
              @click="askContinueReading(chapter)" @keydown.enter="askContinueReading(chapter)">
            <div class="card-content">
              <div class="chapter-cover">
                <img :src="chapter.urlImageManga" alt="Capa do Mangá" class="chapter-image"/>
                <div class="chapter-overlay">
                  <div class="overlay-content">
                    <h3 class="chapter-title-overlay">{{ chapter.title }}</h3>
                    <div class="progress-info">
                      <div class="progress-bar-container">
                        <div class="progress-bar"
                             :style="{ width: `${(chapter.readingProgress / chapter.numberPages) * 100}%` }"></div>
                      </div>
                      <p class="progress-text">
                        {{ chapter.readingProgress }} / {{ chapter.numberPages }} páginas
                      </p>
                    </div>
                    <n-button size="small" class="continue-button" type="primary">
                      <template #icon>
                        <n-icon>
                          <BookOutline/>
                        </n-icon>
                      </template>
                      Continuar leitura
                    </n-button>
                  </div>
                </div>
              </div>
              <div class="chapter-info">
                <p class="chapter-title">{{ chapter.title }}</p>
                <div class="chapter-meta">
                  <p class="chapter-pages">
                    <span class="label">Páginas:</span> {{ chapter.numberPages }}
                  </p>
                  <p class="chapter-progress">
                    <span class="label">Progresso:</span>
                    {{ chapter.readingProgress }} / {{ chapter.numberPages }}
                  </p>
                </div>
                <div class="progress-bar-visible">
                  <div class="progress-bar-fill"
                       :style="{ width: `${(chapter.readingProgress / chapter.numberPages) * 100}%` }">
                  </div>
                </div>
                <n-modal v-model:show="isShowDialog">
                  <n-card class="continue-reading-modal" style="width: 600px; max-width: 90%" title="Continuar Leitura?"
                          :bordered="true" size="large" role="dialog" aria-modal="true">
                    <div class="modal-content">
                      <div v-if="selectedChapter" class="chapter-preview">
                        <img :src="selectedChapter.urlImageManga" :alt="selectedChapter.title" class="preview-image"/>
                        <div class="preview-info">
                          <h4 class="preview-title">{{ selectedChapter.title }}</h4>
                          <div class="preview-progress">
                            <span class="progress-text">
                              {{ selectedChapter.readingProgress }} / {{ selectedChapter.numberPages }} páginas
                            </span>
                          </div>
                          <div class="preview-progress-bar">
                            <div class="preview-progress-fill"
                                 :style="{ width: `${(selectedChapter.readingProgress / selectedChapter.numberPages) * 100}%` }">
                            </div>
                          </div>
                        </div>
                      </div>

                      <div class="modal-actions">
                        <router-link v-if="selectedChapter"
                                     :to="`/manga/${selectedChapter.nameManga}/chapter/${selectedChapter.id}/${selectedChapter.readingProgress}`"
                                     class="action-button continue-button">
                          <n-icon size="18">
                            <PlayOutline/>
                          </n-icon>
                          <span>Continuar de onde parei</span>
                        </router-link>

                        <router-link v-if="selectedChapter"
                                     :to="`/manga/${selectedChapter.nameManga}/chapter/${selectedChapter.id}/1`"
                                     class="action-button restart-button">
                          <n-icon size="18">
                            <RefreshOutline/>
                          </n-icon>
                          <span>Recomeçar do início</span>
                        </router-link>
                      </div>

                      <div class="modal-footer">
                        <n-button type="warning" class="reset-button" @click="resetarProgresso" quaternary size="small">
                          <template #icon>
                            <n-icon>
                              <RefreshOutline/>
                            </n-icon>
                          </template>
                          Resetar progresso
                        </n-button>
                      </div>
                    </div>
                  </n-card>
                </n-modal>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div>
        <n-pagination class="mt-5" v-model:page="page" :page-count="pageTotal" simple/>
      </div>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import type iChapterData from '@/@types/iChapter'
import MenuComponent from '@/components/global/MenuComponent.vue'
import {useChapterStore} from '@/store/ChapterStore'
import {NCard, NIcon, useMessage} from 'naive-ui'
import {onMounted, ref, watch} from 'vue'
import {RefreshOutline, BookOutline, PlayOutline} from '@vicons/ionicons5'

let page = ref<number>(1)
let pageTotal = ref<number | undefined>(0)

const toast = useMessage()
const isShowDialog = ref(false)
const chapterStore = useChapterStore()
const chapters = ref<iChapterData[]>([])
const selectedChapter = ref<iChapterData | null>(null)

onMounted(async () => {
  document.title = 'Leitor de mangás - Leituras em andamento'

  try {
    chapters.value = await chapterStore.getAllReadingProgress(page.value - 1)
    if (chapters.value.length !== 0) pageTotal.value = chapters.value[0].numberPageOfPageable
  } catch (error) {
    console.error('Erro ao carregar os capítulos:', error)
  }
})

const askContinueReading = (chapter: iChapterData) => {
  selectedChapter.value = chapter
  isShowDialog.value = true
}

const resetarProgresso = async () => {
  try {
    if (selectedChapter.value) {
      const result = await chapterStore.progressReset(selectedChapter.value.id)
      isShowDialog.value = false
      toast.success(result)
      chapters.value = []

      chapters.value = await chapterStore.getAllReadingProgress(page.value - 1)
    }
  } catch (error) {
    console.error(error)
    toast.error(String(error))
  }
}

watch(page, async () => {
  chapters.value = await chapterStore.getAllReadingProgress(page.value - 1)
})
</script>

<style scoped>
main {
  padding: 15px;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 2rem;
  list-style-type: none;
  padding: 0;
}

.chapter-card {
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease;
  overflow: hidden;
  background: #fff;
}

:deep(.n-config-provider) .chapter-card {
  background: #fff;
}

section[style*="background-color: rgb(0, 0, 0)"] .chapter-card,
section[style*="background-color:#000"] .chapter-card {
  background: #1a1a1a;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);
}

section[style*="background-color: rgb(0, 0, 0)"] .chapter-card:hover,
section[style*="background-color:#000"] .chapter-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.7);
}

.chapter-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chapter-cover {
  position: relative;
  overflow: hidden;
  height: 280px;
}

.chapter-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  transition: transform 0.5s ease;
}

.chapter-card:hover .chapter-image {
  transform: scale(1.05);
}

.chapter-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.9), transparent);
  padding: 1rem;
  transition: opacity 0.3s ease;
  opacity: 0;
}

.chapter-card:hover .chapter-overlay {
  opacity: 1;
}

.overlay-content {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.chapter-title-overlay {
  color: #ffffff;
  font-size: 1rem;
  font-weight: 700;
  margin: 0;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8), 0 0 8px rgba(0, 0, 0, 0.6);
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.4), transparent);
  padding: 4px 0;
  border-radius: 4px;
}

.progress-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.progress-bar-container {
  width: 100%;
  height: 6px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #18a058, #36ad6a);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-text {
  color: #b3b3b3 !important;
  font-size: 0.85rem;
  margin: 0;
  opacity: 0.9;
}

.continue-button {
  width: 100%;
  margin-top: 0.5rem;
}

.chapter-info {
  padding: 16px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  gap: 8px;
}

.chapter-title {
  font-weight: 600;
  font-size: 1rem;
  margin: 0;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  color: black;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.chapter-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.chapter-pages,
.chapter-progress {
  font-size: 0.875rem;
  color: #666;
  margin: 0;
  transition: color 0.3s ease;
}

.label {
  font-weight: 600;
  color: #333;
  transition: color 0.3s ease;
}

section[style*="background-color: rgb(0, 0, 0)"] .chapter-title,
section[style*="background-color:#000"] .chapter-title {
  color: #e5e5e5;
}

section[style*="background-color: rgb(0, 0, 0)"] .chapter-pages,
section[style*="background-color: rgb(0, 0, 0)"] .chapter-progress,
section[style*="background-color:#000"] .chapter-pages,
section[style*="background-color:#000"] .chapter-progress {
  color: #b3b3b3;
}

section[style*="background-color: rgb(0, 0, 0)"] .label,
section[style*="background-color:#000"] .label {
  color: #e5e5e5;
}

.progress-bar-visible {
  width: 100%;
  height: 6px;
  background: #e5e5e5;
  border-radius: 3px;
  overflow: hidden;
  margin-top: 8px;
  transition: background-color 0.3s ease;
}

section[style*="background-color: rgb(0, 0, 0)"] .progress-bar-visible,
section[style*="background-color:#000"] .progress-bar-visible {
  background: #333;
}

.progress-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #18a058, #36ad6a);
  border-radius: 3px;
  transition: width 0.3s ease;
}

@media (max-width: 768px) {
  .grid-container {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 1.5rem;
  }
}

@media (max-width: 560px) {
  .grid-container {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 1rem;
  }

  .chapter-cover {
    height: 240px;
  }
}

.modal-content {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.chapter-preview {
  display: flex;
  gap: 1rem;
  padding: 0;
}

.preview-image {
  width: 100px;
  height: 140px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
}

.preview-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.preview-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #b3b3b3;
  margin: 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.preview-progress {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.progress-text {
  font-size: 0.875rem;
  color: #666;
  font-weight: 500;
}

.preview-progress-bar {
  width: 100%;
  height: 6px;
  background: #e5e5e5;
  border-radius: 3px;
  overflow: hidden;
}

.preview-progress-fill {
  height: 100%;
  background: #18a058;
  border-radius: 3px;
  transition: width 0.3s ease;
}

.modal-actions {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.action-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 20px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 0.95rem;
  text-decoration: none;
  transition: all 0.2s ease;
  border: 1px solid transparent;
}

.continue-button {
  background: #18a058;
  color: white;
  border-color: #18a058;
}

.continue-button:hover {
  background: #1db366;
  border-color: #1db366;
}

.restart-button {
  background: transparent;
  color: black;
  border-color: #d9d9d9;
}

.restart-button:hover {
  background: #f5f5f5;
  border-color: #bfbfbf;
}

.modal-footer {
  display: flex;
  justify-content: center;
  padding-top: 1rem;
  border-top: 1px solid #e5e5e5;
  margin-top: 0.5rem;
}

section[style*="background-color: rgb(0, 0, 0)"] .preview-title,
section[style*="background-color:#000"] .preview-title {
  color: #e5e5e5;
}

section[style*="background-color: rgb(0, 0, 0)"] .progress-text,
section[style*="background-color:#000"] .progress-text {
  color: #b3b3b3;
}

section[style*="background-color: rgb(0, 0, 0)"] .preview-progress-bar,
section[style*="background-color:#000"] .preview-progress-bar {
  background: #333;
}

section[style*="background-color: rgb(0, 0, 0)"] .restart-button,
section[style*="background-color:#000"] .restart-button {
  color: #e5e5e5;
  border-color: #444;
}

section[style*="background-color: rgb(0, 0, 0)"] .restart-button:hover,
section[style*="background-color:#000"] .restart-button:hover {
  background: #2a2a2a;
  border-color: #555;
}

section[style*="background-color: rgb(0, 0, 0)"] .modal-footer,
section[style*="background-color:#000"] .modal-footer {
  border-top-color: #444;
}

@media (max-width: 640px) {
  .chapter-preview {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .preview-image {
    width: 90px;
    height: 120px;
  }

  .modal-actions {
    gap: 0.5rem;
  }

  .action-button {
    padding: 10px 18px;
    font-size: 0.9rem;
  }
}
</style>

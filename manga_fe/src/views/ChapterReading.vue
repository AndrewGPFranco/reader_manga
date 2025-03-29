<template>
  <header>
    <NavbarComponent />
  </header>

  <main class="manga-viewer__main">
    <n-card ref="chapterCard" v-if="!isLoading" class="manga-viewer__card">
      <template v-if="error">
        <div class="manga-viewer__error">
          <n-alert type="error" :title="error" />
        </div>
      </template>

      <template v-else>
        <n-modal v-model:show="showModalResetReading">
          <n-card style="width: 600px" title="Capítulo finalizado" :bordered="false" size="huge" role="dialog"
            aria-modal="true">
            <template #header-extra> Parabéns!</template>
            XP coletado com sucesso!
          </n-card>
        </n-modal>

        <div class="manga-viewer__content">
          <router-link :to="`/manga/${titleManga}`">
            <ArrowBackOutline class="btn-back" @click="dataReset" />
          </router-link>
          <n-tooltip trigger="hover" v-if="!isTelaCheia">
            <template #trigger>
              <ExpandOutline class="btnExpandir" @click="isTelaCheia = !isTelaCheia" />
            </template>
            Expanda as imagens para melhor experiência de leitura.
          </n-tooltip>
          <n-tooltip trigger="hover" v-if="isTelaCheia">
            <template #trigger>
              <ChevronUpOutline class="btnExpandir" @click="isTelaCheia = !isTelaCheia" />
            </template>
            Minimize as imagens para mais controle da leitura.
          </n-tooltip>

          <img v-if="currentImage" :src="currentImage" :alt="`Manga page ${currentPageNumber}`" :class="{
            'manga-viewer__image': !isTelaCheia,
            'manga-viewer__image_expand': isTelaCheia
          }" @load="handleImageLoad" @error="handleImageError" />

          <n-empty v-else description="No pages available for this chapter" />
        </div>

        <div v-if="currentImage && !isTelaCheia" class="manga-viewer__controls">
          <div class="manga-viewer__info">
            <span>Página {{ currentPageNumber }} de {{ totalPages }}</span>
          </div>

          <div class="manga-viewer__navigation">
            <n-button :disabled="!canNavigatePrevious" @click="previousPage" class="manga-viewer__nav-button"
              @mouseenter="showThumbnails = true" @mouseleave="showThumbnails = false">
              Anterior
            </n-button>

            <div class="manga-viewer__thumbnail-container" v-if="showThumbnails">
              <div class="manga-viewer__thumbnails">
                <img v-for="page in thumbnailPages" :key="page.index" :src="page.src"
                  :alt="`Thumbnail page ${page.index + 1}`" :class="{
                    'manga-viewer__thumbnail': true,
                    active: page.index === currentPageIndex
                  }" @click="jumpToPage(page.index)" />
              </div>
            </div>

            <n-button :disabled="!canNavigateNext" @click="nextPage" class="manga-viewer__nav-button"
              @mouseenter="showThumbnails = true" @mouseleave="showThumbnails = false">
              Próximo
            </n-button>
          </div>
        </div>
      </template>
    </n-card>

    <n-spin v-else size="large" />
  </main>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { NCard, useMessage } from 'naive-ui'
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import { useChapterStore } from '@/store/ChapterStore'
import { ExpandOutline, ChevronUpOutline, ArrowBackOutline } from '@vicons/ionicons5'
import { StatusType } from '@/enum/StatusType'

const route = useRoute()
const message = useMessage()
const chapterStore = useChapterStore()

let currentProgress = ref<number>(1)
let showModalResetReading = ref<boolean>(false)
const showThumbnails = ref<boolean>(false)

const currentChapter = ref<any>({})
const isLoading = ref(true)
const error = ref<string | null>(null)
const currentPageIndex = ref<number>(0)
const imageLoaded = ref<boolean>(false)
const isTelaCheia = ref<boolean>(false)
const chapterCard = ref<InstanceType<typeof NCard> | null>(null)
const idChapter = ref<string>('')
const titleManga = ref<string>('')
const qntdExibicaoModal = ref<number>(0)
const totalPages = ref<number | undefined>(0)
const allImages = ref<string[]>([])
const modalShownForThisChapter = ref<boolean>(false)

const currentPageNumber = computed(() => currentPageIndex.value + 1)
const currentImage = computed(() => allImages.value[currentPageIndex.value] || '')

const canNavigateNext = computed(() => {
  if (totalPages.value !== undefined) return currentPageIndex.value < totalPages.value - 1
  return false
})

const canNavigatePrevious = computed(() => currentPageIndex.value > 0)

const thumbnailPages = computed(() => {
  if (totalPages.value === undefined) return []
  const start = Math.max(0, currentPageIndex.value - 2)
  const end = Math.min(totalPages.value, currentPageIndex.value + 3)
  return Array.from({ length: end - start }, (_, i) => ({
    index: start + i,
    src: allImages.value[start + i] || ''
  }))
})

const resetState = () => {
  showModalResetReading.value = false
  qntdExibicaoModal.value = 0
  modalShownForThisChapter.value = false
  isTelaCheia.value = false
}

const loadAllPages = async (chapterId: string) => {
  try {
    isLoading.value = true
    error.value = null
    allImages.value = []

    resetState()

    const total = await chapterStore.getQuantidade(chapterId)
    totalPages.value = total

    const loadPromises = Array.from({ length: total }, (_, index) =>
      chapterStore
        .getPaginaDoCapitulo(chapterId, index)
        .then((response) => URL.createObjectURL(response))
    )
    allImages.value = await Promise.all(loadPromises)

    currentPageIndex.value = currentProgress.value - 1
    if (chapterCard.value != null) chapterCard.value.$el.scrollTop = 0
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Erro ao carregar o capítulo'
    message.error(error.value)
  } finally {
    isLoading.value = false
  }
}

const nextPage = () => {
  if (canNavigateNext.value) {
    currentPageIndex.value++
    if (chapterCard.value != null) chapterCard.value.$el.scrollTop = 0
  }
}

const previousPage = () => {
  if (canNavigatePrevious.value) {
    currentPageIndex.value--
    if (chapterCard.value != null) chapterCard.value.$el.scrollTop = 0
  }
}

const jumpToPage = (pageIndex: number) => {
  currentPageIndex.value = pageIndex
  if (chapterCard.value != null) chapterCard.value.$el.scrollTop = 0
  showThumbnails.value = false
}

const handleImageLoad = () => {
  imageLoaded.value = true
}

const handleImageError = () => {
  error.value = 'Failed to load image'
  message.error(error.value)
}

const isProcessingKey = ref(false)

const handleKeyPress = async (event: KeyboardEvent) => {
  if (isProcessingKey.value) return
  isProcessingKey.value = true

  if (event.key === 'ArrowRight' || event.key === 'd') {
    nextPage()
  } else if (event.key === 'ArrowLeft' || event.key === 'a') {
    previousPage()
  }

  setTimeout(() => {
    isProcessingKey.value = false
  }, 100)
}

const dataReset = () => {
  resetState()
}

onMounted(async () => {
  document.title = 'Leitor de mangás - Leitura'
  window.addEventListener('keydown', handleKeyPress)

  idChapter.value = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
  titleManga.value = Array.isArray(route.params.title) ? route.params.title[0] : route.params.title
  currentProgress.value = Number(Array.isArray(route.params.progress) ? route.params.progress[0] : route.params.progress)

  if(currentProgress.value === 0) currentProgress.value = 1;

  if (idChapter.value === '') {
    error.value = 'Invalid chapter ID'
    isLoading.value = false
    return
  }

  await chapterStore.updateReadingProgress(idChapter.value, Number(currentProgress.value))
  currentChapter.value = await chapterStore.getReadingProgress(idChapter.value)
  await loadAllPages(idChapter.value)
})

watch(
  () => route.params.id,
  async (newId, oldId) => {
    if (newId && typeof newId === 'string' && newId !== oldId) {
      if (oldId) {
        await atualizaProgresso()
      }

      resetState()

      idChapter.value = newId
      titleManga.value = Array.isArray(route.params.title) ? route.params.title[0] : route.params.title
      currentProgress.value = Number(Array.isArray(route.params.progress) ? route.params.progress[0] : route.params.progress)

      await chapterStore.updateReadingProgress(idChapter.value, Number(currentProgress.value))
      currentChapter.value = await chapterStore.getReadingProgress(idChapter.value)

      await loadAllPages(newId)
    }
  }
)

watch(
  () => currentPageIndex.value,
  async (newVal) => {
    if (newVal >= currentProgress.value) currentProgress.value = newVal + 1

    if (
      currentProgress.value === currentChapter.value.readingProgress &&
      !modalShownForThisChapter.value &&
      qntdExibicaoModal.value === 0 &&
      totalPages.value &&
      newVal === totalPages.value - 1
    ) {
      showModalResetReading.value = true
      qntdExibicaoModal.value++
      modalShownForThisChapter.value = true
    }
  }
)

window.addEventListener('beforeunload', async () => {
  await atualizaProgresso()
})

const atualizaProgresso = async () => {
  if (
    currentChapter.value.status != StatusType.FINISHED &&
    currentProgress.value > currentChapter.value.readingProgress &&
    idChapter.value
  ) {
    await chapterStore.updateReadingProgress(idChapter.value, currentProgress.value)
  }
}

onUnmounted(async () => {
  await atualizaProgresso()
  window.removeEventListener('keydown', handleKeyPress)
  allImages.value.forEach((url) => URL.revokeObjectURL(url))
})
</script>

<style scoped>
.manga-viewer__main {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  padding: 0.5rem;
  overflow: hidden;
}

.manga-viewer__card {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  max-height: 100vh;
  overflow: hidden;
  position: relative;
}

.manga-viewer__content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  padding: 0 1rem 70px;
  transition: all 0.3s ease;
}

.manga-viewer__image {
  max-height: 79vh;
  max-width: 100%;
  object-fit: contain;
  transition: transform 0.3s ease;
}

.manga-viewer__image_expand {
  max-height: 93vh;
  max-width: 100%;
  object-fit: contain;
  transition: transform 0.3s ease;
}

.manga-viewer__image:hover {
  transform: scale(1.02);
}

@media (min-width: 768px) and (max-width: 1024px) {
  .manga-viewer__image {
    max-height: 72vh;
  }
}

@media (min-width: 1400px) {
  .manga-viewer__image {
    max-height: 85vh;
  }

  .manga-viewer__image_expand {
    max-height: 95vh;
  }
}

.manga-viewer__controls {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 0.75rem;
  border-top: 1px solid;
  background-color: rgba(255, 255, 255, 0.9);
  z-index: 5;
  transition: opacity 0.3s ease;
}

.manga-viewer__controls:hover {
  opacity: 1;
}

.manga-viewer__info {
  text-align: center;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.manga-viewer__navigation {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 0 0.5rem;
  position: relative;
}

.manga-viewer__nav-button {
  min-width: 100px;
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

.manga-viewer__nav-button:hover {
  transform: scale(1.05);
}

.manga-viewer__thumbnail-container {
  position: absolute;
  bottom: 60px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  padding: 10px;
  border-radius: 8px;
  z-index: 10;
  animation: fadeIn 0.3s ease;
}

.manga-viewer__thumbnails {
  display: flex;
  gap: 5px;
}

.manga-viewer__thumbnail {
  width: 50px;
  height: 70px;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid transparent;
  transition:
    border-color 0.3s ease,
    transform 0.2s ease;
}

.manga-viewer__thumbnail:hover {
  border-color: #fff;
  transform: scale(1.1);
}

.manga-viewer__thumbnail.active {
  border-color: #ff4500;
}

.manga-viewer__error {
  padding: 2rem;
}

.btnExpandir,
.btn-back {
  position: absolute;
  top: 10px;
  cursor: pointer;
  height: 1.5rem;
  z-index: 10;
  transition: color 0.3s ease;
}

.btnExpandir {
  right: 10px;
}

.btn-back {
  left: 10px;
}

.btnExpandir:hover,
.btn-back:hover {
  color: #ff4500;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}
</style>

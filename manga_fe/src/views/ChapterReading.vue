<template>
  <header>
    <NavbarComponent />
  </header>

  <main class="manga-viewer__main">
    <n-card v-if="!isLoading" class="manga-viewer__card" size="huge" style="height: 95vh; overflow-y: auto;">
      <template v-if="error">
        <div class="manga-viewer__error">
          <n-alert type="error" :title="error" />
        </div>
      </template>

      <template v-else>
        <div class="manga-viewer__content">
          <ExpandOutline v-if="!isTelaCheia"
            class="btnExpandir" @click="isTelaCheia = !isTelaCheia"> /></ExpandOutline>
          <ChevronUpOutline v-if="isTelaCheia"
            class="btnExpandir" @click="isTelaCheia = !isTelaCheia"> /></ChevronUpOutline>

          <img
            v-if="image"
            :src="image"
            :alt="`Manga page `"
            :class="{ 'manga-viewer__image': !isTelaCheia, 'manga-viewer__image_expand': isTelaCheia }"
            @load="handleImageLoad"
            @error="handleImageError"
          />

          <n-empty v-else description="No pages available for this chapter" />
        </div>

        <div v-if="image && !isTelaCheia" class="manga-viewer__controls">
          <div class="manga-viewer__info">
            <span>Página {{ currentPageNumber }} de {{ totalPages }}</span>
          </div>

          <div class="manga-viewer__navigation">
            <n-button
              :disabled="!canNavigatePrevious"
              @click="previousPage"
              class="manga-viewer__nav-button"
            >
              Anterior
            </n-button>

            <n-slider
              v-model:value="currentPageNumber"
              :min="1"
              :max="totalPages"
              :step="1"
              class="manga-viewer__slider"
              @update:value="handleSliderChange"
            />

            <n-button
              :disabled="!canNavigateNext"
              @click="nextPage"
              class="manga-viewer__nav-button"
            >
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
import { useMessage } from 'naive-ui'
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import { useChapterStore } from '@/store/chapterStore'
import { ExpandOutline, ChevronUpOutline } from '@vicons/ionicons5'

const isLoading = ref(true)
const error = ref<string | null>(null)
const currentPageIndex = ref(0)
const imageLoaded = ref(false)
const image = ref<string>('')
const isTelaCheia = ref<boolean>(false)

const route = useRoute()
const message = useMessage()
const chapterStore = useChapterStore()

const totalPages = ref<number | undefined>(0)

const currentPageNumber = computed({
  get: () => currentPageIndex.value + 1,
  set: (value: number) => {
    currentPageIndex.value = value - 1
  }
})

const canNavigateNext = computed(() => currentPageIndex.value < (totalPages.value || 0) - 1)

const canNavigatePrevious = computed(() => currentPageIndex.value > 0)

const loadPage = async (chapterId: string, pageIndex: number) => {
  try {
    imageLoaded.value = false
    error.value = null

    const response = await chapterStore.getPaginaDoCapitulo(chapterId, pageIndex)
    image.value = URL.createObjectURL(response)
  } catch (err) {
    console.error(err)
    error.value = 'Erro ao carregar a imagem'
    message.error(error.value)
  }
}

const loadChapter = async (id: string) => {
  try {
    isLoading.value = true
    error.value = null

    totalPages.value = await chapterStore.getQuantidade(id)

    currentPageIndex.value = 0
    await loadPage(id, currentPageIndex.value)
  } catch (err) {
    console.error(err)
    error.value = err instanceof Error ? err.message : 'Erro ao carregar capítulo'
    message.error(error.value)
  } finally {
    isLoading.value = false
  }
}

const nextPage = async () => {
  if (canNavigateNext.value) {
    currentPageIndex.value++
    const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
    if (id) {
      await loadPage(id, currentPageIndex.value)
    }
  }
}

const previousPage = async () => {
  if (canNavigatePrevious.value) {
    currentPageIndex.value--
    const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
    if (id) {
      await loadPage(id, currentPageIndex.value)
    }
  }
}

const handleSliderChange = async (value: number) => {
  currentPageNumber.value = value
  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
  if (id) {
    await loadPage(id, currentPageIndex.value)
  }
}

const handleImageLoad = () => {
  imageLoaded.value = true
}

const handleImageError = () => {
  error.value = 'Failed to load image'
  message.error(error.value)
}

const handleKeyPress = (event: KeyboardEvent) => {
  if (event.key === 'ArrowRight' || event.key === 'd') {
    nextPage()
  } else if (event.key === 'ArrowLeft' || event.key === 'a') {
    previousPage()
  }
}

onMounted(async () => {
  window.addEventListener('keydown', handleKeyPress)

  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id

  if (!id) {
    error.value = 'Invalid chapter ID'
    isLoading.value = false
    return
  }

  await loadChapter(id)
})

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyPress)

  if (image.value) {
    URL.revokeObjectURL(image.value)
  }
})

watch(
  () => route.params.id,
  async (newId) => {
    if (newId && typeof newId === 'string') {
      await loadChapter(newId)
    }
  }
)

watch(
  () => image.value,
  (newImage, oldImage) => {
    if (oldImage && oldImage !== newImage) {
      URL.revokeObjectURL(oldImage)
    }
  }
)
</script>

<style scoped>
.manga-viewer__main {
  flex: 1;
  padding: 0.5rem;
  display: flex;
  justify-content: center;
}

.manga-viewer__card {
  width: 100%;
  max-width: none;
  min-height: 90vh;
  display: flex;
  flex-direction: column;
}

.manga-viewer__content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  padding: 0 1rem;
}

.manga-viewer__image {
  max-height: 83vh;
  max-width: 100%;
  object-fit: contain;
}

.manga-viewer__image_expand {
  max-height: 100vh;
  max-width: 100%;
  object-fit: contain;
}

.manga-viewer__controls {
  margin-top: auto;
  padding: 0.75rem;
  border-top: 1px solid;
}

.manga-viewer__info {
  text-align: center;
  margin-bottom: 0.5rem;
}

.manga-viewer__navigation {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0 0.5rem;
}

.manga-viewer__slider {
  flex: 1;
}

.manga-viewer__nav-button {
  min-width: 100px;
}

.manga-viewer__error {
  padding: 2rem;
}

.btnExpandir {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  height: 1.5rem;
}

</style>

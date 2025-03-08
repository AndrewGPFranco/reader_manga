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
        <div class="manga-viewer__content">
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
import { NCard, useMessage } from 'naive-ui'
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import { useChapterStore } from '@/store/chapterStore'
import { ExpandOutline, ChevronUpOutline } from '@vicons/ionicons5'

const isLoading = ref(true)
const error = ref<string | null>(null)
const currentPageIndex = ref(0)
const imageLoaded = ref(false)
const image = ref<string>('')
const isTelaCheia = ref<boolean>(false)
const chapterCard = ref<InstanceType<typeof NCard> | null>(null)

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
    if(chapterCard.value != null) chapterCard.value.$el.scrollTop = 0
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
}

.manga-viewer__image {
  max-height: 79vh;
  max-width: 100%;
  object-fit: contain;
}

.manga-viewer__image_expand {
  max-height: 93vh;
  max-width: 100%;
  object-fit: contain;
}

@media(min-width:768px) and (max-width:1024px) {
  .manga-viewer__image {
    max-height: 72vh;
  }
}

@media(min-width:1400px) {
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
  background-color: inherit;
  z-index: 5;
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
  z-index: 10;
}
</style>
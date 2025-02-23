<template>
    <header>
        <NavbarComponent />
    </header>

    <main class="manga-viewer__main">
        <n-card v-if="!isLoading" class="manga-viewer__card" size="huge">
            <template v-if="error">
                <div class="manga-viewer__error">
                    <n-alert type="error" :title="error" />
                </div>
            </template>

            <template v-else>
                <div class="manga-viewer__content">
                    <img v-if="currentPage?.chapterPage" :src="currentPage.chapterPage"
                        :alt="`Manga page ${currentPage.id}`" class="manga-viewer__image" @load="handleImageLoad"
                        @error="handleImageError" />

                    <n-empty v-else description="No pages available for this chapter" />
                </div>

                <div v-if="currentPage" class="manga-viewer__controls">
                    <div class="manga-viewer__info">
                        <span>Página {{ currentPageNumber }} de {{ totalPages }}</span>
                    </div>

                    <div class="manga-viewer__navigation">
                        <n-button :disabled="!canNavigatePrevious" @click="previousPage"
                            class="manga-viewer__nav-button">
                            Anterior
                        </n-button>

                        <n-slider v-model:value="currentPageNumber" :min="1" :max="totalPages" :step="1"
                            class="manga-viewer__slider" @update:value="handleSliderChange" />

                        <n-button :disabled="!canNavigateNext" @click="nextPage" class="manga-viewer__nav-button">
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
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useMessage } from 'naive-ui';
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import { useChapterStore } from '@/store/chapterStore';

const isLoading = ref(true);
const error = ref<string | null>(null);
const chapterData = ref<any[]>([]);
const currentPageIndex = ref(0);
const imageLoaded = ref(false);

const route = useRoute();
const message = useMessage();
const chapterStore = useChapterStore();

const currentPage = computed<any | null>(() =>
    chapterData.value[currentPageIndex.value] || null
);

const totalPages = computed(() => chapterData.value.length);

const currentPageNumber = computed({
    get: () => currentPageIndex.value + 1,
    set: (value: number) => {
        currentPageIndex.value = value - 1;
    }
});

const canNavigateNext = computed(() =>
    currentPageIndex.value < chapterData.value.length - 1
);

const canNavigatePrevious = computed(() =>
    currentPageIndex.value > 0
);

const loadChapter = async (id: string) => {
    try {
        isLoading.value = true;
        error.value = null;

        const data = await chapterStore.getChapterByID(id);

        if (!data || !data.length) {
            throw new Error('No chapter data available');
        }

        chapterData.value = data;
        currentPageIndex.value = 0;
    } catch (err) {
        error.value = err instanceof Error ? err.message : 'Failed to load chapter';
        message.error(error.value);
    } finally {
        isLoading.value = false;
    }
};

const nextPage = () => {
    if (canNavigateNext.value) {
        currentPageIndex.value++;
        imageLoaded.value = false;
    }
};

const previousPage = () => {
    if (canNavigatePrevious.value) {
        currentPageIndex.value--;
        imageLoaded.value = false;
    }
};

const handleSliderChange = (value: number) => {
    currentPageNumber.value = value;
    imageLoaded.value = false;
};

const handleImageLoad = () => {
    imageLoaded.value = true;
};

const handleImageError = () => {
    error.value = 'Failed to load image';
    message.error(error.value);
};

const handleKeyPress = (event: KeyboardEvent) => {
    if (event.key === 'ArrowRight' || event.key === 'd') {
        nextPage();
    } else if (event.key === 'ArrowLeft' || event.key === 'a') {
        previousPage();
    }
};

onMounted(async () => {
    const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;

    if (!id) {
        error.value = 'Invalid chapter ID';
        return;
    }

    await loadChapter(id);
    window.addEventListener('keydown', handleKeyPress);
});

watch(
    () => route.params.id,
    async (newId) => {
        if (newId && typeof newId === 'string') {
            await loadChapter(newId);
        }
    }
);
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

.manga-viewer__controls {
    margin-top: auto;
    padding: 0.75rem;
    border-top: 1px solid var(--border-color);
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
</style>
<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card class="flex flex-col items-center">
            <img v-if="currentPage" :src="currentPage.chapterPage" alt="Página do mangá" :style="{ maxHeight: '85vh', objectFit: 'cover' }"  />
            <h1 v-if="!currentPage">No pages for this chapter yet</h1>
            <aside class="flex justify-between" v-if="currentPage">
                <n-button @click="previousPage" class="mt-2">Previous page</n-button>
                <n-button @click="nextPage" class="mt-2">Next page</n-button>
            </aside>
        </n-card>
    </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import type ChapterData from '@/interface/Chapter';
import type PageData from '@/interface/Page';
import { useChapterStore } from '@/store/ChapterStore';
import { useMessage } from 'naive-ui';
import { onMounted, ref, computed } from 'vue';
import { useRoute } from 'vue-router';

const chapterStore = useChapterStore();
const message = useMessage();
const route = useRoute();
let chapter = ref<ChapterData>({} as ChapterData);
let pageList = computed(() => chapter.value.pages);
let currentPage = ref<PageData>({} as PageData);

const nextPage = () => {
    let nextIndex = (currentPage.value.id + 1) - 1;
    if(nextIndex == undefined || pageList.value[nextIndex] == undefined) {
        message.error("No more pages");
        return;
    } 
    currentPage.value = pageList.value[nextIndex];
}

const previousPage = () => {
    let nextIndex = (currentPage.value.id - 1) - 1;
    if(nextIndex == undefined || pageList.value[nextIndex] == undefined) {
        message.error("No more pages");
        return;
    } 
    currentPage.value = pageList.value[nextIndex];
}

onMounted(async () => {
    const id: string = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    chapter.value = await chapterStore.getChapterByID(id);

    if(pageList.value != undefined) {
        currentPage.value = pageList.value[0];
    }
})
</script>

<style scoped>
    main {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 15px;
    }

    .n-card {
        height: 95vh;
        box-sizing: border-box;
        overflow: scroll;
        overflow-x: hidden;
    }
</style>
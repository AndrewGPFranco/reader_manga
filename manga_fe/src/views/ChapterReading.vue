<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card class="flex flex-col items-center">
        <img
            v-for="page in chapter.pages"
            :key="page.id"  :src="page.page"
            alt="Página do mangá"
            :style="{ maxHeight: '90vh', objectFit: 'cover' }"  />
        </n-card>
    </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import type ChapterData from '@/interface/Chapter';
import { useChapterStore } from '@/store/ChapterStore';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const chapterStore = useChapterStore();
const route = useRoute();
let chapter = ref<ChapterData>({} as ChapterData);

onMounted(async () => {
    const id: string = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    chapter.value = await chapterStore.getChapterByID(id);
})
</script>

<style scoped>
    main {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 15px;
    }
</style>
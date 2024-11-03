<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <p>{{ manga.title }}</p>
        <p>{{ manga.description }}</p>
        <p>{{ manga.size }}</p>
        <p>{{ manga.creationDate }}</p>
        <p>{{ manga.closingDate }}</p>
        <p>{{ manga.status }}</p>
        <p>{{ manga.author }}</p>
        <p>{{ manga.gender }}</p>
        <p>{{ manga.image }}</p>
        <ul>
            <li v-for="chapter in manga.chapters" :key="chapter.title">
                <p>{{ chapter.title }}</p>
                <p>{{ chapter.description }}</p>
                <p>{{ chapter.numberPages }}</p>
            </li>
        </ul>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';
import type { MangaData } from '@/interface/Manga';
import { useMangaStore } from '@/store/MangaStore';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const mangaStore = useMangaStore();
const route = useRoute();
let manga = ref<MangaData>({} as MangaData);

onMounted(async () => {
    const id: string = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    manga.value = await mangaStore.getMangaById(id);
})

</script>

<style scoped>
   main {
      padding: 15px;
   } 
</style>
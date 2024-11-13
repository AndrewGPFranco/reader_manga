<template>
    <header>
        <NavbarComponent />
    </header>
    <main class="text-black bg-white p-10 shadow-lg max-h-screen overflow-y-auto">
        <div class="flex items-center space-x-4 mb-6">
            <img :src="manga.image" alt="Imagem do Mangá" class="w-24 h-24 object-cover rounded-lg shadow-lg">
            <div>
                <h1 class="text-2xl font-bold">{{ manga.title }}</h1>
                <p class="text-black text-sm">{{ manga.author }}</p>
            </div>
        </div>

        <div class="space-y-2 mb-6">
            <p><span class="font-semibold">Size:</span> {{ manga.size }}</p>
            <p><span class="font-semibold">Gender:</span> {{ manga.gender }}</p>
            <p><span class="font-semibold">Status:</span> {{ manga.status }}</p>
            <p><span class="font-semibold">End date:</span> {{ verifyEndDate(manga) }}</p>
            <p><span class="font-semibold">Creation date:</span> {{ formatDate(manga) }}</p>
            <p><span class="font-semibold">Description:</span> {{ manga.description }}</p>
        </div>

        <div>
            <h2 class="text-xl font-semibold border-b border-black mb-4 pb-2">Chapters</h2>
            <ul class="space-y-4">
                <li v-for="chapter in manga.chapters" :key="chapter.title" class="bg-white p-4 rounded-lg shadow-lg border border-gray-200">
                    <router-link :to="`/manga/${manga.id}/chapter/${chapter.id}`" class="font-semibold text-lg">{{ chapter.title }}</router-link>
                    <p class="text-black">{{ chapter.description }}</p>
                    <p><span class="text-black">Páginas:</span> {{ chapter.numberPages }}</p>
                </li>
            </ul>
        </div>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';
import type { MangaData } from '@/interface/Manga';
import { useMangaStore } from '@/store/MangaStore';
import { formatDate } from '@/utils/utils';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const mangaStore = useMangaStore();
const route = useRoute();
let manga = ref<MangaData>({} as MangaData);

onMounted(async () => {
    const id: string = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    manga.value = await mangaStore.getMangaById(id);
})

function verifyEndDate(str: MangaData): any {
    return str.closingDate != undefined ? str.closingDate : "Still on display.";
}

</script>

<style scoped>
   main {
      padding: 15px;
   } 
</style>
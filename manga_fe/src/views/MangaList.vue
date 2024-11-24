<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="My library" size="huge">
            <section class="container flex gap-5 wrap justify-center">
                <div class="max-w-xs rounded overflow-hidden shadow-lg bg-white" v-for="manga in mangasArray" :key="manga.title">
                    <div class="relative">
                        <img class="w-full h-48 object-cover" :src="manga.image" alt="Capa do Manga">
                        <span class="isFavorite" v-if="manga.favorite"><Heart @click="setFavorite(manga, manga.id)" /></span>
                        <span class="isFavorite" v-else><HeartOutline @click="setFavorite(manga, manga.id)" /></span>
                    </div>
                    <div class="p-4">
                        <router-link :to="`/manga/${manga.id}`" class="text-xl font-bold mb-2 text-gray-800">{{ manga.title }}</router-link>
                        <div class="text-gray-700 text-sm mt-2">
                            <p><span class="font-semibold">Number of chapters: </span>{{ manga.size }}</p>
                            <p><span class="font-semibold">Status: </span> {{ manga.status }}</p>
                            <p><span class="font-semibold">Author: </span> {{ manga.author }}</p>
                            <p><span class="font-semibold">Gender: </span> {{ manga.gender }}</p>
                        </div>
                    </div>
                </div>
            </section>
        </n-card>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';
import { onMounted, ref } from 'vue';
import { useMessage } from 'naive-ui';
import { useMangaStore } from '@/store/MangaStore';
import type MangaData from '@/interface/Manga';
import { HeartOutline, Heart } from '@vicons/ionicons5';

const message = useMessage();
const mangasArray = ref<MangaData[]>([]);
const mangaStore = useMangaStore();

const setFavorite = async (manga: MangaData, id: number) => {
    const response = await mangaStore.setFavorite(!manga.favorite, id);
    message.info(String(response.message));
    if(response.statusCode == 200)
        mangasArray.value = await mangaStore.getAllManga();
}

onMounted(async () => {
  try {
    const mangas = await mangaStore.getAllManga();
    mangasArray.value = mangas;
  } catch (error: any) {
    message.error(error.message || 'Erro ao buscar os mangás');
  }
});

</script>

<style scoped>
    main {
        padding: 15px;
    }

    .n-card {
        height: 95vh;
        box-sizing: border-box;
    }

    .relative {
        position: relative;
    }

    .isFavorite {
        position: absolute;
        top: 10px;
        right: 10px;
        z-index: 10;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .isFavorite svg {
        width: 35px;
        height: 35px;
        color: #000000;
        transition: transform 0.2s ease-in-out;
    }

    .isFavorite:hover svg {
        transform: scale(1.2);
    }
</style>

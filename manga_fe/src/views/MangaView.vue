<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="My library" style="height: 95vh;" size="huge">
            <section class="container flex gap-5 wrap justify-center">
                <div class="max-w-xs rounded overflow-hidden shadow-lg bg-white" v-for="manga in mangasArray" :key="manga.title">
                    <img class="w-full h-48 object-cover" :src="manga.image" alt="Capa do Manga">
                    <div class="p-4">
                        <router-link :to="`/manga/${manga.id}`" class="text-xl font-bold mb-2 text-gray-800">{{ manga.title }}</router-link>
                        <div class="text-gray-700 text-sm mb-4">
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
import type { MangaData } from '@/interface/Manga';
import { useMessage } from 'naive-ui';
import { useMangaStore } from '@/store/MangaStore';

const message = useMessage();
const mangasArray = ref<MangaData[]>([]);
const mangaStore = useMangaStore();

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
</style>

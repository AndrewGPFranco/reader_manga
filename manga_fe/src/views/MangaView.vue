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
                        <h2 class="text-xl font-bold mb-2 text-gray-800">{{ manga.title }}</h2>
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
import { api } from '@/network/axiosInstance';
import { onMounted, ref } from 'vue';
import type { MangaData } from '@/interface/Manga';
import { useMessage } from 'naive-ui';

const message = useMessage();
const mangasArray = ref<MangaData[]>([]);

onMounted(async () => {
    try {
        const response = await api.get("/api/v1/manga/all");
        mangasArray.value = response.data;
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

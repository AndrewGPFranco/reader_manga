<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="All mangas" style="height: 95vh;" size="huge">
            <section class="container">
                <div v-for="manga in mangasArray" :key="manga.title">
                    <p>{{ manga.title }}</p>
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

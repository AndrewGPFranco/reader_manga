<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="All Manga" size="huge">
            <section class="container flex flex-wrap gap-5 justify-center">
                <div class="w-72 h-96 rounded overflow-hidden shadow-lg bg-white flex flex-col"
                    v-for="manga in mangasArray" :key="manga.title">
                    <div class="relative">
                        <img class="w-full h-48 object-cover" :src="manga.image" alt="Capa do Manga">
                    </div>
                    <div class="p-4 flex flex-col flex-grow overflow-y-auto">
                        <router-link :to="`/manga/${manga.id}`" class="text-xl font-bold text-gray-800 truncate">
                            {{ manga.title }}
                        </router-link>
                        <div class="text-gray-700 text-sm mt-2 flex-grow overflow-y-auto">
                            <p><span class="font-semibold">Chapters: </span>{{ manga.size }}</p>
                            <p><span class="font-semibold">Status: </span>{{ manga.status }}</p>
                            <p><span class="font-semibold">Author: </span>{{ manga.author }}</p>
                            <p><span class="font-semibold">Genre: </span>{{ manga.gender }}</p>
                        </div>
                        <div class="mt-auto">
                            <n-button v-if="!manga.favorite" @click="adicionaMangaNaListaDoUsuario(manga.id)"
                                type="primary" class="w-full mt-2">
                                Adicionar na lista
                            </n-button>
                            <n-button v-if="manga.favorite" @click="removerMangaDaLista(manga.id)" type="error"
                                class="w-full mt-2">
                                Remover da lista
                            </n-button>
                        </div>
                    </div>
                </div>
            </section>
        </n-card>
    </main>
</template>

<script lang="ts" setup>
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import type MangaData from '@/interface/Manga';
import { useMangaStore } from '@/store/MangaStore';
import { useMessage } from 'naive-ui';
import { onMounted, ref } from 'vue';

const message = useMessage();
const mangasArray = ref<MangaData[]>([]);
const mangaStore = useMangaStore();

const adicionaMangaNaListaDoUsuario = async (idManga: number) => {
    try {
        const result = await mangaStore.adicionaMangaNaListaDoUsuario(idManga);
        mangasArray.value = await mangaStore.getAllManga();
        message.success(result);
    } catch (error: any) {
        message.error(error.message || 'Erro ao adicionar mangá na lista.');
    }
}

const removerMangaDaLista = async (idManga: number) => {
    try {
        const result = await mangaStore.removeDaLista(idManga);
        mangasArray.value = await mangaStore.getAllManga();
        message.success(result);
    } catch (error: any) {
        message.error(error.message || 'Erro ao remover mangá da lista.');
    }
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
    overflow: scroll;
    overflow-x: hidden;
}

.overflow-y-auto {
    overflow-y: auto;
    scrollbar-width: thin;
    scrollbar-color: #888 #f1f1f1;
}

.overflow-y-auto::-webkit-scrollbar {
    width: 6px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: #555;
}
</style>
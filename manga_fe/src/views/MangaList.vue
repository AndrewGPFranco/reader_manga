<!-- Essa é a lista individual do usuário -->
<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="My library" size="huge">
            <section class="container flex gap-5 wrap justify-center"
                v-if="mangasArray != undefined && mangasArray.length > 0">
                <div class="max-w-xs rounded overflow-hidden shadow-lg bg-white" v-for="manga in mangasArray"
                    :key="manga.title">
                    <div class="relative">
                        <img class="w-full h-48 object-cover" :src="manga.image" alt="Capa do Manga">
                        <span class="isFavorite" v-if="manga.favorite">
                            <Heart @click="setFavorite(manga.id)" />
                        </span>
                        <span class="isFavorite" v-else>
                            <HeartOutline @click="setFavorite(manga.id)" />
                        </span>
                    </div>
                    <div class="p-4">
                        <router-link :to="`/manga/${manga.id}`" class="text-xl font-bold mb-2 text-gray-800">{{
                            manga.title }}</router-link>
                        <div class="text-gray-700 text-sm mt-2">
                            <p><span class="font-semibold">Number of chapters: </span>{{ manga.size }}</p>
                            <p><span class="font-semibold">Status: </span> {{ manga.status }}</p>
                            <p><span class="font-semibold">Author: </span> {{ manga.author }}</p>
                            <p><span class="font-semibold">Gender: </span> {{ manga.gender }}</p>
                        </div>
                    </div>
                </div>
            </section>
            <div v-else class="containerWithoutManga">
                <h1>Não há nenhum mangá em sua lista.</h1>
            </div>
        </n-card>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';
import { onMounted, ref } from 'vue';
import { useMessage } from 'naive-ui';
import { useMangaStore } from '@/store/MangaStore';
import { HeartOutline, Heart } from '@vicons/ionicons5';
import { useAuthStore } from '@/store/AuthStore';
import type ResponseListManga from '@/interface/ResponseListManga';
import type MangaData from '@/interface/Manga';

const message = useMessage();
const mangasArray = ref<MangaData[]>();
const mangaStore = useMangaStore();
const userAuth = useAuthStore();

const setFavorite = async (idManga: number) => {
    const response = await mangaStore.setFavorite(idManga);
    message.info(String(response.message));
    if (response.statusCode == 200) {
        const userId = userAuth.getIdUsuario();
        if (userId !== undefined) {
            const mangasDoUsuario = await mangaStore.getListMangaByUser(userId);
            mangasArray.value = mangasDoUsuario.mangaList;
        }
    }
}

onMounted(async () => {
    try {
        const user = userAuth.getUserAutenticado();
        let mangas: ResponseListManga = { mangaList: [] };
        const userId = user.getId();
        if (userId !== undefined)
            mangas = await mangaStore.getListMangaByUser(userId);

        mangasArray.value = mangas.mangaList;
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

.containerWithoutManga {
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>

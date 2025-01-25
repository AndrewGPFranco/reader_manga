<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="All Manga" size="huge">
            <section class="container flex gap-5 wrap justify-center">
                <div class="max-w-xs rounded overflow-hidden shadow-lg bg-white" v-for="manga in mangasArray" :key="manga.title">
                    <div class="relative">
                        <img class="w-full h-48 object-cover" :src="manga.image" alt="Capa do Manga">
                    </div>
                    <div class="p-4">
                        <router-link :to="`/manga/${manga.id}`" class="text-xl font-bold mb-2 text-gray-800">{{ manga.title }}</router-link>
                        <div class="text-gray-700 text-sm mt-2">
                            <p><span class="font-semibold">Number of chapters: </span>{{ manga.size }}</p>
                            <p><span class="font-semibold">Status: </span> {{ manga.status }}</p>
                            <p><span class="font-semibold">Author: </span> {{ manga.author }}</p>
                            <p><span class="font-semibold">Gender: </span> {{ manga.gender }}</p>
                            <n-button v-if="!manga.favorite" id="btnAddLista" @click="adicionaMangaNaListaDoUsuario(manga.id)" type="primary">Adicionar na lista</n-button>
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

    #btnAddLista {
        margin-top: 10px;
    }
</style>
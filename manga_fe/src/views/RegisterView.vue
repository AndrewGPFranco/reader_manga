<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="Gerenciamento de Registros" size="huge">
            <n-tabs style="height: 100%" type="card">
                <n-tab-pane name="Mangá" tab="Mangá Register" style="height: 95%">
                    <section class="container">
                        <FormToMangaRegister :is-edit="false" :manga="mangasArray[0]" />
                    </section>
                </n-tab-pane>
                <n-tab-pane name="Chapter" tab="Chapter Register" style="height: 95%">
                    <section class="container">
                        <FormToChapterRegister :mangas="mangasArray" :is-edit="false" :chapter="chapter" />
                    </section>
                </n-tab-pane>
                <n-tab-pane name="Chapter Pages" tab="Chapter Pages Register" style="height: 95%">
                    <section class="container">
                        <FormToChapterPages :mangas="mangasArray" :is-edit="false" :page="page"/>
                    </section>
                </n-tab-pane>
            </n-tabs>
        </n-card>
    </main>
</template>

<script setup lang="ts">
import { Chapter } from '@/class/Chapter';
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import FormToChapterRegister from '@/components/registerChapter/formToChapterRegister.vue';
import FormToChapterPages from '@/components/registerChapterPages/formToChapterPages.vue';
import FormToMangaRegister from '@/components/registerManga/formToMangaRegister.vue';
import type MangaData from '@/interface/Manga';
import type PageData from '@/interface/Page';
import { useMangaStore } from '@/store/MangaStore';
import { onMounted, ref } from 'vue';

let mangasArray = ref([] as MangaData[])
const chapter = new Chapter(0, "", "", 0, []);
const page = {} as PageData;
const mangaStore = useMangaStore();

onMounted(async () => {
    mangasArray.value = await mangaStore.getAllManga();
})
</script>

<style scoped>
    main {
        padding: 15px;
    }

    .container {
        padding: 10px;
        max-height: 70vh; 
        overflow-y: auto;
    }

    .n-card {
        height: 95vh;
        box-sizing: border-box;
        overflow: scroll;
        overflow-x: hidden;
    }
</style>
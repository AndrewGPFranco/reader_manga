<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="Records management" style="height: 95vh;" size="huge">
            <n-tabs style="height: 100%" type="card">
                <n-tab-pane name="Mangá" tab="Mangá register" style="height: 95%">
                    <section class="container">
                        <FormToMangaRegister />
                    </section>
                </n-tab-pane>
                <n-tab-pane name="Chapter" tab="Chapter register" style="height: 95%">
                    <section class="container">
                        <FormToChapterRegister :mangas="mangasArray" />
                    </section>
                </n-tab-pane>
            </n-tabs>
        </n-card>
    </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import FormToChapterRegister from '@/components/registerChapter/formToChapterRegister.vue';
import FormToMangaRegister from '@/components/registerManga/formToMangaRegister.vue';
import { api } from '@/network/axiosInstance';
import { useMessage } from 'naive-ui';
import { onMounted } from 'vue';

let mangasArray: any[] = [];
const message = useMessage();

async function getAllMangas() {
    api.get("/api/v1/manga/all")
        .then((response) => {
            mangasArray = response.data;
        })
        .catch((error) => {
            message.error(error.message);
        })
}

onMounted(() => {
    getAllMangas();
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
</style>
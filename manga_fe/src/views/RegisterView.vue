<template>
  <header>
    <MenuComponent />
  </header>
  <main>
    <n-card title="Gerenciamento de Registros" size="huge" style="height: 95vh; overflow-y: auto">
      <n-tabs style="height: 100%" type="card">
        <n-tab-pane name="Mangá" tab="Registro de Mangás" style="height: 95%">
          <section class="container">
            <FormToMangaRegister :is-edit="false" :manga="mangasArray[0]" />
          </section>
        </n-tab-pane>
        <n-tab-pane name="Chapter" tab="Registro de Capítulos" style="height: 95%">
          <section class="container">
            <FormToChapterRegister :mangas="mangasArray" :is-edit="false" :chapter="chapter" />
          </section>
        </n-tab-pane>
        <n-tab-pane name="Chapter Pages" tab="Registro de Páginas" style="height: 95%">
          <section class="container">
            <FormToChapterPages :mangas="mangasArray" :is-edit="false" :page="page" />
          </section>
        </n-tab-pane>
        <n-tab-pane name="Episode" tab="Registro de Animes" style="height: 95%">
          <section class="container">
            <FormToAnimeRegister />
          </section>
        </n-tab-pane>
        <n-tab-pane name="Animes" tab="Registro de Episódios" style="height: 95%">
          <section class="container">
            <FormToEpisodeRegister />
          </section>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import { Chapter } from '@/class/Chapter'
import MenuComponent from '@/components/global/MenuComponent.vue'
import FormToMangaRegister from '@/components/registerManga/formToMangaRegister.vue'
import FormToEpisodeRegister from '@/components/registerEpisode/formToEpisodeRegister.vue'
import FormToAnimeRegister from '@/components/registerAnime/formToAnimeRegister.vue'
import FormToChapterRegister from '@/components/registerChapter/formToChapterRegister.vue'
import FormToChapterPages from '@/components/registerChapterPages/formToChapterPages.vue'
import type iMangaData from '@/@types/Manga'
import type iPageData from '@/@types/Pagee'
import { useMangaStore } from '@/store/MangaStore'
import { onMounted, ref } from 'vue'
import { StatusType } from '@/enum/StatusType'

let mangasArray = ref([] as iMangaData[])
const chapter = new Chapter(0, '', 0, [], StatusType.ONGOING, 0)
const page = {} as iPageData
const mangaStore = useMangaStore()

onMounted(async () => {
  document.title = 'Leitor de mangás - Registros'
  mangasArray.value = await mangaStore.getAllManga()
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

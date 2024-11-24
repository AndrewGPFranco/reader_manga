<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <section>
      <n-card title="My favorite mangá" style="height: 95vh;" size="huge">
        <div v-for="manga in favoriteManga" :key="manga.id">
          <p>{{ manga.title }}</p>
        </div>
      </n-card>
    </section>
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import type MangaData from '@/interface/Manga';
import { useMangaStore } from '@/store/MangaStore';
import { onMounted, ref } from 'vue';

const favoriteManga = ref([] as MangaData[]);
const mangaStore = useMangaStore();

onMounted(async () => {
  favoriteManga.value = await mangaStore.getAllFavorites();
})

</script>

<style scoped>
  main {
      padding: 15px;
  }
</style>
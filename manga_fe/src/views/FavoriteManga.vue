<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <section>
      <n-card class="container" title="My favorite mangas" style="height: 95vh;" size="huge">
        <div class="container" v-if="favoriteManga.length > 0">
          <n-card class="mangaCard" :title="manga.title" v-for="manga in favoriteManga" :key="manga.id" bordered>
            <template #cover>
              <img class="cardImg" :src="manga.image">
            </template>
            <router-link :to="`/manga/${manga.id}`" class="text-xl font-bold mb-2 text-gray-800">
              <n-button type="info">
                Access
              </n-button>
            </router-link>
          </n-card>
        </div>
        <div v-else class="containerWithoutManga">
          <h1>You don't have favorite manga</h1>
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

  .container {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

  .cardImg {
    max-height: 150px;
  }

  main {
      padding: 15px;
  }

  .mangaCard {
    max-width: 300px;
  }

  .containerWithoutManga {
    display: flex;
    justify-content: center;
    align-items: center;
  }

</style>
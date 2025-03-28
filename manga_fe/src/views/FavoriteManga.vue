<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <section>
      <n-card class="containerCard" title="Mangás favoritos" size="huge">
        <div class="card-container" v-if="favoriteManga.length > 0">
          <n-card class="mangaCard" v-for="manga in favoriteManga" :key="manga.id" bordered>
            <template #cover>
              <img class="card-image" :src="manga.image" :alt="manga.title" />
            </template>
            <div class="card-content">
              <h3 class="card-title">{{ manga.title }}</h3>
              <router-link :to="`/manga/${manga.title}`" class="full-width">
                <n-button type="info" class="access-btn">Acessar</n-button>
              </router-link>
            </div>
          </n-card>
        </div>
        <div v-else class="containerWithoutManga">
          <h1>Não há nenhum mangá em sua lista de favoritos.</h1>
        </div>
      </n-card>
    </section>
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import type iMangaData from '@/@types/Manga'
import { useMangaStore } from '@/store/MangaStore'
import { onMounted, ref } from 'vue'

const favoriteManga = ref([] as iMangaData[])
const mangaStore = useMangaStore()

onMounted(async () => {
  favoriteManga.value = await mangaStore.getAllFavorites()
})
</script>

<style scoped>
main {
  padding: 15px;
}

.containerCard {
  height: 95vh;
  box-sizing: border-box;
  overflow-y: auto;
}

.card-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 24px;
  padding: 20px;
}

.mangaCard {
  width: 280px;
  max-width: 100%;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.card-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.card-content {
  padding: 10px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.card-title {
  font-size: 1.25rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.full-width {
  width: 100%;
}

.access-btn {
  width: 100%;
  margin-top: auto;
  transition: background 0.3s ease;
}

.access-btn:hover {
  background-color: #007aff;
  color: white;
}

.containerWithoutManga {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  text-align: center;
  font-size: 1.2rem;
  color: #555;
}
</style>

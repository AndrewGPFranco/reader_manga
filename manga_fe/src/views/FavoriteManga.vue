<template>
  <header>
    <MenuComponent/>
  </header>
  <main>
    <section>
      <n-card class="containerCard" title="Mangás favoritos" size="huge">
        <div class="card-container" v-if="favoriteManga.length > 0">
          <div v-for="manga in favoriteManga" :key="manga.id" class="manga-card-item">
            <div class="manga-cover">
              <router-link :to="`/manga/${manga.title}`">
                <img :src="manga.image" :alt="manga.title"/>
              </router-link>
              <div class="manga-overlay">
                <h3 class="manga-title">{{ tratamentoTitulo(manga.title) }}</h3>
                <router-link :to="`/manga/${manga.title}`">
                  <n-button size="small" class="read-button" type="primary">
                    <template #icon>
                      <n-icon>
                        <BookOutline/>
                      </n-icon>
                    </template>
                    Ler agora
                  </n-button>
                </router-link>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="containerWithoutManga">
          <h1>Não há nenhum mangá em sua lista de favoritos.</h1>
        </div>
      </n-card>
    </section>
  </main>
</template>

<script setup lang="ts">
import {NIcon} from 'naive-ui'
import {onMounted, ref} from 'vue'
import type iMangaData from '@/@types/Manga'
import {BookOutline} from '@vicons/ionicons5'
import {useMangaStore} from '@/store/MangaStore'
import MenuComponent from '@/components/global/MenuComponent.vue'

const mangaStore = useMangaStore()
const favoriteManga = ref([] as iMangaData[])

const tratamentoTitulo = (title: string) => {
  return title.length > 15 ? `${title.substring(0, 15)}...` : title
}

onMounted(async () => {
  favoriteManga.value = await mangaStore.getAllFavorites()
  document.title = 'Leitor de mangás - Favoritos'
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
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 2rem;
  padding: 20px;
}

.manga-card-item {
  transition: transform 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.manga-card-item:hover {
  transform: translateY(-5px);
}

.manga-cover {
  position: relative;
  overflow: hidden;
  height: 280px;
}

.manga-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  transition: transform 0.5s ease;
}

.manga-cover:hover img {
  transform: scale(1.05);
}

.manga-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  padding: 1rem;
  transition: opacity 0.3s ease;
  opacity: 0;
}

.manga-cover:hover .manga-overlay {
  opacity: 1;
}

.manga-title {
  color: white;
  font-size: 1rem;
  margin: 0 0 0.5rem 0;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.read-button {
  width: 100%;
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

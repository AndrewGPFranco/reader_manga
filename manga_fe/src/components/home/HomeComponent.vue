<template>
  <div class="home">
    <n-card class="manga-card">
      <div class="header">
        <h1>Descubra Novos Mangás</h1>
        <n-button @click="getMangaRandom" secondary type="primary" size="small">
          <template #icon>
            <RefreshOutline />
          </template>
          Atualizar
        </n-button>
      </div>

      <div class="categories-section">
        <h3>Categorias Populares</h3>
        <div class="category-tags">
          <n-tag v-for="category in categories" :key="category" type="success" class="category-tag">
            {{ category }}
          </n-tag>
        </div>
      </div>

      <div class="section-header">
        <h2 class="section-title">
          Mangás Populares
          <n-tooltip trigger="hover">
            <template #trigger>
              <span class="info"><InformationOutline /></span>
            </template>
            Caso o mangá já esteja cadastrado em nosso sistema, iremos redireciona-lo para a leitura
            interna, caso o contrário, iremos fornecer um link de um excelente site.
          </n-tooltip>
        </h2>
      </div>

      <div v-if="mangas.length === 0" class="loading-container">
        <n-spin size="large" />
      </div>

      <div class="manga-grid" v-else>
        <div v-for="(item, index) in mangas" :key="index" class="manga-card-item">
          <div class="manga-cover">
            <a
              v-if="!item.urlReader.includes(`/manga/${item.titulo}`)"
              :href="item.urlReader"
              target="_blank"
            >
              <img :src="item.urlImage" :alt="item.titulo" />
            </a>
            <router-link v-else :to="item.urlReader">
              <img :src="item.urlImage" :alt="item.titulo" />
            </router-link>
            <div class="manga-overlay">
              <h3 class="manga-title">{{ item.titulo }}</h3>
              <n-button size="small" class="read-button" type="primary">
                <router-link v-if="item.urlReader.includes(`/manga/${item.titulo}`)" :to="item.urlReader">
                  Ler agora
                </router-link>
                <a
                  v-else
                  :href="item.urlReader"
                  target="_blank"
                >
                  Ler agora
                </a>
              </n-button>
            </div>
          </div>
        </div>
      </div>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import type iCoversManga from '@/@types/iCoversManga'
import { useMangaStore } from '@/store/MangaStore'
import { useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { InformationOutline, RefreshOutline } from '@vicons/ionicons5'

const mangas = ref<iCoversManga[]>([])
const message = useMessage()
const mangaStore = useMangaStore()

const categories: Array<string> = [
  'Ação',
  'Aventura',
  'Romance',
  'Fantasia',
  'Sci-Fi',
  'Horror',
  'Comédia',
  'Drama'
]

onMounted(() => getMangaRandom())

const getMangaRandom = async () => {
  try {
    mangas.value = []
    const mangasResult = await mangaStore.getFiveMangaRandom()
    mangasResult.forEach((manga) => {
      if (manga.urlReader === null) {
        manga.urlReader = `/manga/${manga.titulo}`
      }
    })
    mangas.value = mangasResult
  } catch (error) {
    message.error(String(error));
  }
}
</script>

<style scoped>
.home {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.manga-card {
  height: 95vh;
  box-sizing: border-box;
  overflow: auto;
  padding: 2rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.header h1 {
  font-size: 2rem;
  font-weight: bold;
  margin: 0;
  background: linear-gradient(90deg, #4285f4, #34a853, #fbbc05, #ea4335);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.section-header {
  margin-bottom: 1.5rem;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0;
}

.info svg {
  width: 20px;
  height: 20px;
}

.manga-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 2rem;
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

.categories-section {
  margin-bottom: 2rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 1rem;
}

.category-tag {
  cursor: pointer;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
</style>

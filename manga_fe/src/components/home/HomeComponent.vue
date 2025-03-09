<template>
  <div class="home">
    <n-card>
      <div class="banner">
        <img src="../../assets/home/banner.jpg" alt="Banner do site" />
      </div>
      <h2 class="section-title">
        Mangás famosos
        <n-tooltip trigger="hover">
          <template #trigger>
            <span class="info"><InformationOutline /></span>
          </template>
          Caso o mangá já esteja cadastrado em nosso sistema, iremos redireciona-lo para a leitura
          interna, caso o contrário, iremos fornecer um link de um excelente site.
        </n-tooltip>
      </h2>

      <div class="favorites-grid" v-if="mangas.length != 0">
        <div v-for="(item, index) in mangas" :key="index" class="item-card">
          <a
            v-if="!item.urlReader.includes(`/manga/${item.titulo}`)"
            :href="item.urlReader"
            target="_blank"
          >
            <img :src="item.urlImage" alt="Capa de mangá" />
          </a>
          <router-link v-if="item.urlReader.includes(`/manga/${item.titulo}`)" :to="item.urlReader">
            <img :src="item.urlImage" alt="Capa de mangá" />
          </router-link>
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
import { InformationOutline } from '@vicons/ionicons5'

const mangas = ref<iCoversManga[]>([])
const message = useMessage()
const mangaStore = useMangaStore()

onMounted(() => getMangaRandom())

const getMangaRandom = async () => {
  try {
    const mangasResult = await mangaStore.getFiveMangaRandom()
    mangasResult.forEach((manga) => {
      if (manga.urlReader === null) {
        manga.urlReader = `/manga/${manga.titulo}`
      }
    })
    mangas.value = mangasResult
  } catch (error) {
    message.error('Error loading sleeves.')
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

.banner img {
  width: 100%;
  height: 400px;
  border-radius: 5px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 1.2rem;
  font-weight: bold;
  margin-top: 1rem;
}

.favorites-grid {
  display: flex;
  justify-content: center;
  padding: 20px;
  flex-wrap: wrap;
  gap: 20px;
}

.item-card img {
  height: 250px;
  border-radius: 5px;
}

.n-card {
  height: 95vh;
  box-sizing: border-box;
  overflow: scroll;
  overflow-x: hidden;
}

.info svg {
  width: 20px;
  height: 20px;
}
</style>

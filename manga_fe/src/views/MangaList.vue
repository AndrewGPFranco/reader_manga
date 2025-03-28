<!-- Essa é a lista individual do usuário -->
<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <n-card title="Biblioteca" size="huge" style="height: 95vh; overflow-y: auto;">
      <section class="card-container" v-if="mangasArray != undefined && mangasArray.length > 0">
        <div class="card" v-for="manga in mangasArray" :key="manga.title">
          <div class="relative">
            <img class="card-image" :src="manga.image" alt="Capa do Manga" />
            <span class="isFavorite" v-if="manga.favorite">
              <Heart @click="setFavorite(manga.id)" />
            </span>
            <span class="isFavorite" v-else>
              <HeartOutline @click="setFavorite(manga.id)" />
            </span>
          </div>
          <div class="card-content">
            <router-link :to="`/manga/${manga.title}`" class="card-title">{{
              manga.title
            }}</router-link>
            <div class="card-details">
              <p><span class="font-semibold">Number of chapters: </span>{{ manga.size }}</p>
              <p><span class="font-semibold">Status: </span> {{ manga.status }}</p>
              <p><span class="font-semibold">Author: </span> {{ manga.author }}</p>
              <p><span class="font-semibold">Gender: </span> {{ manga.gender }}</p>
            </div>
          </div>
        </div>
      </section>
      <div v-else class="containerWithoutManga">
        <h1>Não há nenhum mangá em sua lista.</h1>
      </div>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import { onMounted, ref } from 'vue'
import { useMessage } from 'naive-ui'
import { useMangaStore } from '@/store/MangaStore'
import { HeartOutline, Heart } from '@vicons/ionicons5'
import { useAuthStore } from '@/store/AuthStore'
import type responseListManga from '@/@types/ResponseListManga'
import type iMangaData from '@/@types/Manga'

const message = useMessage()
const mangasArray = ref<iMangaData[]>()
const mangaStore = useMangaStore()
const userAuth = useAuthStore()

const setFavorite = async (idManga: number) => {
  const response = await mangaStore.setFavorite(idManga)
  message.info(String(response.message))
  if (response.statusCode == 200) {
    const userId = userAuth.getIdUsuario()
    if (userId !== null) {
      const mangasDoUsuario = await mangaStore.getListMangaByUser(userId)
      mangasArray.value = mangasDoUsuario.mangaList
    }
  }
}

onMounted(async () => {
  document.title = 'Leitor de mangás - Biblioteca'

  try {
    const user = userAuth.getUserAutenticado()
    let mangas: responseListManga = { mangaList: [] }
    const userId = user.getId()
    if (userId !== undefined) mangas = await mangaStore.getListMangaByUser(userId)

    mangasArray.value = mangas.mangaList
  } catch (error: any) {
    message.error(error.message || 'Erro ao buscar os mangás')
  }
})
</script>

<style scoped>
main {
  padding: 15px;
}

.card-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  padding: 20px;
}

.card {
  width: 300px;
  background-color: white;
  border-radius: 8px;
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
  padding: 16px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: 1.25rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  text-decoration: none;
}

.card-details {
  font-size: 0.875rem;
  color: #666;
  flex-grow: 1;
}

.relative {
  position: relative;
}

.isFavorite {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 10;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.isFavorite svg {
  width: 35px;
  height: 35px;
  color: #000000;
  transition: transform 0.2s ease-in-out;
}

.isFavorite:hover svg {
  transform: scale(1.2);
}

.containerWithoutManga {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>

<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <n-card
      ref="mangaCard"
      title="Mangás"
      class="flex flex-col"
      size="huge"
      style="height: 95vh; overflow-y: auto"
    >
      <section class="container mt-5 flex flex-wrap gap-5 justify-center">
        <n-input-group class="flex justify-end -mt-7 mb-5">
          <n-button v-if="!isExibindoTodosMangas" type="info" @click="findPage"
            >Exibir todos</n-button
          >
          <n-input
            :style="{ width: '30%' }"
            v-model:value="mangaPesquisado"
            @keydown.enter="procuraMangaEspecifico"
          />
          <n-button type="primary" @click="procuraMangaEspecifico">Procurar</n-button>
        </n-input-group>
        <div v-if="!mangasArray.length">
          <p>Nenhum mangá a ser exibido!</p>
        </div>
        <div
          v-else
          :class="{
            'w-72 h-90 rounded overflow-hidden shadow-lg bg-white flex flex-col':
              isExibindoTodosMangas,
            'w-72 h-80 rounded overflow-hidden shadow-lg bg-white flex flex-col':
              !isExibindoTodosMangas
          }"
          v-for="manga in mangasArray"
          :key="manga.title"
        >
          <div class="relative">
            <img class="w-full h-48 object-cover" :src="manga.image" alt="Capa do Manga" />
          </div>
          <div class="p-4 flex flex-col flex-grow overflow-y-auto">
            <router-link
              :to="`/manga/${manga.title}`"
              class="text-xl font-bold text-gray-800 truncate"
            >
              {{ manga.title }}
            </router-link>
            <div class="text-gray-700 text-sm mt-2 flex-grow overflow-y-auto">
              <p><span class="font-semibold">Chapters: </span>{{ manga.size }}</p>
              <p><span class="font-semibold">Status: </span>{{ manga.status }}</p>
            </div>
            <div class="mt-auto" v-if="isExibindoTodosMangas">
              <n-button
                v-if="!manga.favorite"
                @click="adicionaMangaNaListaDoUsuario(manga.id)"
                type="primary"
                class="w-full mt-2"
              >
                Adicionar na lista
              </n-button>
              <n-button
                v-if="manga.favorite"
                @click="removerMangaDaLista(manga.id)"
                type="error"
                class="w-full mt-2"
              >
                Remover da lista
              </n-button>
            </div>
          </div>
        </div>
      </section>
      <div class="pagination" v-if="mangasArray.length">
        <n-pagination class="mt-5" v-model:page="page" :page-count="pageTotal" simple />
      </div>
    </n-card>
  </main>
</template>

<script lang="ts" setup>
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import type iMangaData from '@/@types/Manga'
import { useMangaStore } from '@/store/MangaStore'
import { NCard, useMessage } from 'naive-ui'
import { onMounted, ref, watch } from 'vue'

const message = useMessage()
const mangasArray = ref<iMangaData[]>([])
const mangaStore = useMangaStore()
const page = ref<number>(1)
const pageTotal = ref<number>(0)
const mangaQuantity = ref<number>(0)
const mangaCard = ref<InstanceType<typeof NCard> | null>(null)

let mangaPesquisado = ref<string>('')
let isExibindoTodosMangas = ref<boolean>(true)

const findPage = async () => {
  isExibindoTodosMangas.value = true
  mangaPesquisado.value = ''
  const data = await mangaStore.getAllMangaPaginado(page.value, 10)
  mangasArray.value = data.content
  page.value = data.number + 1
  pageTotal.value = data.totalPages
  mangaQuantity.value = data.totalElements

  if (mangaCard.value) mangaCard.value.$el.scrollTop = 0
}

const adicionaMangaNaListaDoUsuario = async (idManga: number) => {
  try {
    const result = await mangaStore.adicionaMangaNaListaDoUsuario(idManga)
    mangasArray.value = []
    const data = await mangaStore.getAllMangaPaginado(page.value, 10)
    mangasArray.value = data.content
    page.value = data.number + 1
    pageTotal.value = data.totalPages
    mangaQuantity.value = data.totalElements
    message.success(result)
  } catch (error: any) {
    message.error(error.message || 'Erro ao adicionar mangá na lista.')
  }
}

const removerMangaDaLista = async (idManga: number) => {
  try {
    const result = await mangaStore.removeDaLista(idManga)
    mangasArray.value = []
    const data = await mangaStore.getAllMangaPaginado(page.value, 10)
    mangasArray.value = data.content
    page.value = data.number + 1
    pageTotal.value = data.totalPages
    mangaQuantity.value = data.totalElements
    message.success(result)
  } catch (error: any) {
    message.error(error.message || 'Erro ao remover mangá da lista.')
  }
}

const procuraMangaEspecifico = async () => {
  try {
    if (mangaPesquisado.value === '') {
      isExibindoTodosMangas.value = true
      await findPage()
    } else {
      const data = await mangaStore.getMangaPesquisado(mangaPesquisado.value)
      if (data != undefined) {
        if (data.content.length === 0) {
          message.info('Nenhum mangá encontrado...')
        } else {
          isExibindoTodosMangas.value = false
          mangasArray.value = []
          mangasArray.value = data.content
          page.value = data.number + 1
          pageTotal.value = data.totalPages
          mangaQuantity.value = data.totalElements
        }
      }
    }
  } catch (error: any) {
    message.error('Erro ao buscar os mangá')
  }
}

onMounted(async () => {
  document.title = 'Leitor de mangás - Mangás'

  try {
    await findPage()
  } catch (error: any) {
    message.error(error.message || 'Erro ao buscar os mangás')
  }
})

watch(page, () => {
  findPage()
})
</script>

<style scoped>
main {
  padding: 15px;
}

.overflow-y-auto {
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #888 #f1f1f1;
}

.overflow-y-auto::-webkit-scrollbar {
  width: 6px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>

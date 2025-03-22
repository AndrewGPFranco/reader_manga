<template>
  <n-table :single-line="false">
    <thead>
      <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Status</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="manga in allManga" :key="manga.id">
        <td>{{ manga.id }}</td>
        <td>{{ manga.title }}</td>
        <td>{{ manga.status }}</td>
        <td class="tdButtons">
          <Delete class="buttonDelete" @click="deleteManga(manga.id)" />
          <Edit class="buttonEdit" @click="editManga(manga)" />
        </td>
      </tr>
    </tbody>
  </n-table>
  <div class="pagination">
    <n-pagination class="mt-5" v-model:page="page" :page-count="pageTotal" simple />
  </div>
  <div v-if="isEdit && !finishedEdition" class="containerForm">
    <FormToMangaRegister
      :manga="mangaToBeEdited"
      :isEdit="isEdit"
      @requestResult="handleRequestResult"
      @cancelEdit="cancelEdit"
      @editFinalizada="resetaDados"
    />
  </div>
</template>

<script setup lang="ts">
import type iMangaData from '@/@types/Manga'
import { useMangaStore } from '@/store/MangaStore'
import { onMounted, ref, watch } from 'vue'
import { CreateOutline as Edit, TrashOutline as Delete } from '@vicons/ionicons5'
import { useMessage } from 'naive-ui'
import FormToMangaRegister from '../registerManga/formToMangaRegister.vue'

const isEdit = ref(false)
const message = useMessage()
const mangaStore = useMangaStore()
const allManga = ref<iMangaData[]>([])
const mangaToBeEdited = ref({} as iMangaData)

let page = ref<number>(1)
let pageTotal = ref<number>(0)

let finishedEdition = ref(false)

const resetaDados = () => {
  isEdit.value = false
  mangaToBeEdited.value = {} as iMangaData
  finishedEdition.value = false
}

const deleteManga = async (id: number) => {
  const response = await mangaStore.deleteMangaById(id)
  if (response) allManga.value = allManga.value.filter((manga) => manga.id !== id)

  message.success(String(response))
}

const editManga = (manga: iMangaData) => {
  isEdit.value = true
  mangaToBeEdited.value = manga
}

const mangasPageable = async () => {
  const data = await mangaStore.getAllMangaPaginado(page.value, 10)
  page.value = data.number + 1
  allManga.value = data.content
  pageTotal.value = data.totalPages
}

onMounted(async () => {
  await mangasPageable()
})

const handleRequestResult = async (result: boolean) => {
  finishedEdition.value = result
  if (result) {
    await mangaStore.getAllMangaPaginado(0, 10)
      .then(data => allManga.value = data.content);
  }
}

const cancelEdit = () => {
  isEdit.value = false
  mangaToBeEdited.value = {} as iMangaData
}

watch(page, async () => {
  await mangasPageable()
})
</script>

<style scoped>
.actions {
  display: flex;
  justify-content: space-around;
  align-items: center;
  text-align: center;
}

.tdButtons {
  display: flex;
  justify-content: space-around;
}

.tdButtons svg {
  width: 25px;
  height: 25px;
}

.buttonDelete,
.buttonEdit {
  cursor: pointer;
}

.buttonDelete {
  color: rgba(255, 0, 0, 0.678);
}

.buttonEdit {
  color: rgb(0, 109, 0);
}

.containerForm {
  margin-top: 40px;
}
</style>

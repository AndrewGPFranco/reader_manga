<template>
  <n-table :single-line="false">
    <thead>
      <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="page in allPages" :key="page.id">
        <td>{{ page.id }}</td>
        <td>{{ page.pathPage }}</td>
        <td class="tdButtons">
          <Delete class="buttonDelete" @click="deletePage(page.id)" />
          <Edit class="buttonEdit" @click="editPage(page)" />
        </td>
      </tr>
    </tbody>
  </n-table>
  <div v-if="isEdit && !finishedEdition" class="containerForm">
    <FormToChapterPages
      :mangas="allManga"
      :isEdit="isEdit"
      :page="pageToBeEdited"
      @requestResult="handleRequestResult"
      @cancelEdit="cancelEdit"
    />
  </div>
</template>

<script setup lang="ts">
import { useChapterStore } from '@/store/ChapterStore'
import { TrashOutline as Delete, CreateOutline as Edit } from '@vicons/ionicons5'
import { useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useMangaStore } from '@/store/MangaStore'
import type iPageData from '@/@types/Pagee'
import type iMangaData from '@/@types/Manga'
import type iChapterData from '@/@types/iChapter'
import FormToChapterPages from '../registerChapterPages/formToChapterPages.vue'

const isEdit = ref(false)
const message = useMessage()
const allManga = ref([] as iMangaData[])
const allPages = ref([] as iPageData[])
const allChapter = ref([] as iChapterData[])
const chapterStore = useChapterStore()
const mangaStore = useMangaStore()
const pageToBeEdited = ref({} as iPageData)

let finishedEdition = ref(false)

onMounted(async () => {
  allChapter.value = await chapterStore.getAllChapter(100)
  allManga.value = await mangaStore.getAllManga()
  allPages.value = await chapterStore.getAllPages()
})

const deletePage = async (idPage: number) => {
  const response = await chapterStore.deletePageById(idPage)
  if (response) allPages.value = allPages.value.filter((page) => page.id !== idPage)

  message.success(String(response))
}

const editPage = (page: iPageData) => {
  isEdit.value = true
  pageToBeEdited.value = page
}

const handleRequestResult = async (result: boolean) => {
  finishedEdition.value = result

  if (result) allPages.value = await chapterStore.getAllPages()
}

const cancelEdit = () => {
  isEdit.value = false
}
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

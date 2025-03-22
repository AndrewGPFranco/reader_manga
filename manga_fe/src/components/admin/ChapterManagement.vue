<template>
  <n-table :single-line="false">
    <thead>
      <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Number of Pages</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="chapter in allChapter" :key="chapter.id">
        <td>{{ chapter.id }}</td>
        <td>{{ chapter.title }}</td>
        <td>{{ chapter.numberPages }}</td>
        <td class="tdButtons">
          <Delete class="buttonDelete" @click="deleteChapter(chapter.id)" />
          <Edit class="buttonEdit" @click="editChapter(chapter)" />
        </td>
      </tr>
    </tbody>
  </n-table>
  <div class="pagination">
    <n-pagination class="mt-5" v-model:page="page" :page-count="pageTotal" simple />
  </div>
  <div v-if="isEdit && !finishedEdition" class="containerForm" ref="formEdicao">
    <FormToChapterRegister
      :mangas="allManga"
      :chapter="chapterToBeEdited"
      :isEdit="isEdit"
      @requestResult="handleRequestResult"
      @cancelEdit="cancelEdit"
      @editFinalizada="resetaDados"
    />
  </div>
</template>

<script setup lang="ts">
import type iChapterData from '@/@types/iChapter'
import { useChapterStore } from '@/store/ChapterStore'
import { CreateOutline as Edit, TrashOutline as Delete } from '@vicons/ionicons5'
import { useMessage } from 'naive-ui'
import { onMounted, ref, watch } from 'vue'
import FormToChapterRegister from '../registerChapter/formToChapterRegister.vue'
import type iMangaData from '@/@types/Manga'
import { useMangaStore } from '@/store/MangaStore'

const isEdit = ref(false)
const message = useMessage()
const allManga = ref([] as iMangaData[])
const chapterStore = useChapterStore()
const mangaStore = useMangaStore()
const allChapter = ref([] as iChapterData[])
const chapterToBeEdited = ref({} as iChapterData)
const formEdicao = ref(null)

let finishedEdition = ref(false)
let page = ref<number>(1)
let pageTotal = ref<number>(0)

const resetaDados = () => {
  isEdit.value = false
  chapterToBeEdited.value = {} as iChapterData
  finishedEdition.value = false
}

const chaptersPageable = async () => {
  const data = await chapterStore.getAllChapter(page.value - 1, 10)
  allChapter.value = data.content
  pageTotal.value = data.totalPages
}

onMounted(async () => {
  await chaptersPageable()
  allManga.value = await mangaStore.getAllManga()
})

const deleteChapter = async (id: number) => {
  const response = await chapterStore.deleteChapterById(id)
  if (response) allChapter.value = allChapter.value.filter((chapter) => chapter.id !== id)

  message.success(String(response))
}

const editChapter = (chapter: iChapterData) => {
  isEdit.value = true
  chapterToBeEdited.value = chapter
}

const handleRequestResult = async (result: boolean) => {
  finishedEdition.value = result
  if (result)
    allChapter.value = await chapterStore.getAllChapter(page.value, 10)
}

const cancelEdit = () => {
  isEdit.value = false
  chapterToBeEdited.value = {} as iChapterData
}

watch(page, async () => {
  await chaptersPageable()
})
</script>

<style scoped>
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

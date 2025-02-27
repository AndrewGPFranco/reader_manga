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
    <div v-if="isEdit && !finishedEdition" class="containerForm">
        <FormToChapterRegister :mangas="allManga" :chapter="chapterToBeEdited" :isEdit="isEdit" @requestResult="handleRequestResult" @cancelEdit="cancelEdit"/>
    </div>
</template>

<script setup lang="ts">
import type iChapterData from '@/interface/iChapter';
import { useChapterStore } from '@/store/chapterStore';
import { TrashOutline as Delete, CreateOutline as Edit } from '@vicons/ionicons5'
import { useMessage } from 'naive-ui';
import { onMounted, ref } from 'vue';
import FormToChapterRegister from '../registerChapter/formToChapterRegister.vue';
import type iMangaData from '@/interface/Manga';
import { useMangaStore } from '@/store/MangaStore';

const isEdit = ref(false);
const message = useMessage();
const allManga = ref([] as iMangaData[])
const chapterStore = useChapterStore();
const mangaStore = useMangaStore();
const allChapter = ref([] as iChapterData[]);
const chapterToBeEdited = ref({} as iChapterData);

let finishedEdition = ref(false);

onMounted(async () => {
    const responseChapter = await chapterStore.getAllChapter();
    const responseManga = await mangaStore.getAllManga();
    allChapter.value = responseChapter;
    allManga.value = responseManga;
})

const deleteChapter = async (id: number) => {
    const response = await chapterStore.deleteChapterById(id);
    if(response)
        allChapter.value = allChapter.value.filter(chapter => chapter.id !== id);

    message.success(String(response));
}

const editChapter = (chapter: iChapterData) => {
    isEdit.value = true;
    chapterToBeEdited.value = chapter;
}

const handleRequestResult = async (result: boolean) => {
    finishedEdition.value = result;
    if (result)
        allChapter.value = await chapterStore.getAllChapter();
};

const cancelEdit = () => {
    isEdit.value = false;
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

.buttonDelete, .buttonEdit { cursor: pointer; }

.buttonDelete { color: rgba(255, 0, 0, 0.678); }
.buttonEdit { color: rgb(0, 109, 0); }

.containerForm {
    margin-top: 40px;
}

</style>
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
    <div v-if="isEdit" class="containerForm">
        <FormToMangaRegister :manga="mangaToBeEdited" :isEdit="isEdit"/>
    </div>
</template>

<script setup lang="ts">
import type MangaData from '@/interface/Manga';
import { useMangaStore } from '@/store/MangaStore';
import { onMounted, ref } from 'vue';
import { TrashOutline as Delete, CreateOutline as Edit } from '@vicons/ionicons5'
import { useMessage } from 'naive-ui';
import FormToMangaRegister from '../registerManga/formToMangaRegister.vue';

const isEdit = ref(false);
const message = useMessage();
const mangaStore = useMangaStore();
const allManga = ref<MangaData[]>([]);
const mangaToBeEdited = ref({} as MangaData);

const deleteManga = async (id: number) => {
    const response = await mangaStore.deleteMangaById(id);
    message.success(String(response));
}

const editManga = (manga: MangaData) => {
    isEdit.value = true;
    mangaToBeEdited.value = manga;
}

onMounted(async () => {
    const newLocal = await mangaStore.getAllManga();
    allManga.value = newLocal;
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

.buttonDelete, .buttonEdit { cursor: pointer; }

.buttonDelete { color: rgba(255, 0, 0, 0.678); }
.buttonEdit { color: rgb(0, 109, 0); }

.containerForm {
    margin-top: 40px;
}

</style>
<template>
    <div>
        <n-form ref="formRef" :model="model" :rules="rules" size="medium" label-placement="top">
            <n-grid :span="24" :x-gap="24">
                <n-form-item-gi :span="12" label="Image link" path="page">
                    <n-input v-model:value="model.page" placeholder="Enter the image link" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Manga" path="manga">
                    <n-select v-model:value="model.manga" placeholder="Choose the mangá" :options="generalOptionsManga" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Chapter" path="chapter">
                    <n-select v-model:value="model.chapter" placeholder="Choose the chapter" :options="generalOptionsChapter" />
                </n-form-item-gi>
                <n-gi :span="24">
                    <div style="display: flex; justify-content: flex-end">
                        <n-button round type="primary" @click="handleValidateButtonClick">
                            Register
                        </n-button>
                    </div>
                </n-gi>
            </n-grid>
        </n-form>
    </div>
</template>

<script setup lang="ts">
import { api } from '@/network/axiosInstance';
import { useMessage, type FormInst } from 'naive-ui';
import { computed, ref, watch } from 'vue';

const message = useMessage();
const formRef = ref<FormInst | null>(null);
let mangaSelected = ref();

const props = defineProps({
    mangas: {
        type: Array as () => { title: string; id: string }[],
        required: true
    }
});

const model = ref({
    page: '',
    chapter: '',
    manga: ''
})

const rules = {
    page: {
        required: true,
        message: 'Enter the image link...'
    }
}

const generalOptionsManga = props.mangas.map(v => ({
    label: v.title,
    value: v.id
}));

const pageRegister = () => {
    const data = {
        page: model.value.page,
        chapter_id: model.value.chapter
    }
    api.post("/api/v1/chapter/register/page", data)
        .then(() => message.create("Register successfully"))
        .catch((error) => message.error(error));
};

const generalOptionsChapter = computed(() => {
    if(mangaSelected.value) {
        return mangaSelected.value.map((chapter: { title: any; id: any; }) => ({
            label: chapter.title,
            value: chapter.id
        }));
    }
    return [];
});

const handleValidateButtonClick = (e: MouseEvent) => {
    e.preventDefault();
    formRef.value?.validate((errors) => {
        if (!errors) {
            pageRegister();
        } else {
            message.error('Enter valid data')
        }
    })
}

watch(model.value, () => {
    if(model.value.manga == '') return;
    if(model.value.manga != null || model.value.manga != undefined) {
        api.get(`/api/v1/manga/chapter_id/by-id/${model.value.manga}`)
            .then((response) => {
                mangaSelected.value = response.data
            })
            .catch((error) => {
                message.error(error);
            })
    }
})
</script>
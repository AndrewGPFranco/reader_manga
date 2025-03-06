<template>
    <div>
        <n-form ref="formRef" :model="model" :rules="rules" size="medium" label-placement="top">
            <n-grid :span="24" :x-gap="24">
                <n-form-item-gi :span="12" label="Image link" path="page">
                    <n-input v-model:value="model.page" placeholder="Enter the image link" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Manga" path="manga" v-if="!props.isEdit">
                    <n-select v-model:value="model.manga" placeholder="Choose the mangá" :options="generalOptionsManga" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Chapter" path="chapter" v-if="!props.isEdit">
                    <n-select v-model:value="model.chapter" placeholder="Choose the chapter" :options="generalOptionsChapter" />
                </n-form-item-gi>
                <n-gi :span="24">
                    <div style="display: flex; justify-content: flex-end; gap: 10px;">
                        <n-button round type="info" @click="cancel">
                            Cancel
                        </n-button>
                        <n-button round type="primary" @click="handleValidateButtonClick">
                            {{ action }}
                        </n-button>
                    </div>
                </n-gi>
            </n-grid>
        </n-form>
    </div>
</template>

<script setup lang="ts">
import type iPageData from '@/@types/Pagee';
import { api } from '@/network/axiosInstance';
import { useAuthStore } from '@/store/AuthStore';
import { useChapterStore } from '@/store/chapterStore';
import { useMessage, type FormInst } from 'naive-ui';
import { computed, ref, watch, type PropType } from 'vue';

const props = defineProps({
    mangas: {
        type: Array as () => { title: string; id: number }[],
        required: true
    },
    page: {
        type: Object as PropType<iPageData>,
        required: true
    },
    isEdit: {
        type: Boolean,
        required: true
    }
});

const message = useMessage();
const formRef = ref<FormInst | null>(null);
const chapterStore = useChapterStore();
const action = props.isEdit ? "Edit" : "Register";

let mangaSelected = ref();

const emit = defineEmits<{
  (event: 'requestResult', result: boolean): void;
  (event: 'cancelEdit', result: boolean): void
}>();

const model = ref({
    page: props.page != undefined && props.isEdit ? props.page.pathPage : '',
    chapter: null as string | null,
    manga: null as string | null
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

const pageRegister = async () => {
    const data = {
        page: model.value.page,
        chapter_id: model.value.chapter,
        idChapter: model.value.chapter
    }

    let response = "";
    
    if(!props.isEdit)
        response = await chapterStore.registerPage(data, clearFields);
    else {
        response = await chapterStore.editPage(props.page.id, data, clearFields);
        emit('requestResult', true);
    }

    message.info(response);
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


const clearFields = () => {
    model.value = {
        page: '',
        chapter: null as string | null,
        manga: null as string | null
    }

    formRef.value?.restoreValidation();
}

watch(model.value, () => {
    if(model.value.manga == '') return;
    if(model.value.manga != null || model.value.manga != undefined) {
        const token = localStorage.getItem('token');
        api.get(`/api/v1/manga/chapter_id/by-id/${model.value.manga}`, {
                    headers: {
                        Authorization: `${token}`
                    }
                })
            .then((response) => {
                mangaSelected.value = response.data
                if(mangaSelected.value == undefined || mangaSelected.value == "")
                    model.value.chapter = null as string | null
            })
            .catch((error) => {
                message.error(error);
            })
    }
})

const cancel = () => {
    emit("cancelEdit", true);
}
</script>
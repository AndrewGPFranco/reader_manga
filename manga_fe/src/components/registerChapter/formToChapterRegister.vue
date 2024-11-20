<template>
    <div>
        <n-form ref="formRef" :model="model" :rules="rules" :size="size" label-placement="top">
            <n-grid :span="24" :x-gap="24">
                <n-form-item-gi :span="12" label="Title" path="title">
                    <n-input v-model:value="model.title" placeholder="Enter the title" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Description" path="description">
                    <n-input v-model:value="model.description" placeholder="Enter description" type="textarea"
                        :autosize="{
                            minRows: 1,
                            maxRows: 5,
                        }" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Mangá" path="manga">
                    <n-select v-model:value="model.manga" placeholder="Choose the mangá" :options="generalOptions" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Pages number" path="pagesNumber">
                    <n-input-number v-model:value="model.pagesNumber" />
                </n-form-item-gi>
                <n-gi :span="24">
                    <div style="display: flex; justify-content: flex-end; gap: 10px;">
                        <n-button round type="info" @click="cancel">
                            Cancel
                        </n-button>
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
import { defineProps, ref, type PropType } from 'vue'
import type { FormInst } from 'naive-ui'
import { useMessage } from 'naive-ui'
import { useChapterStore } from '@/store/ChapterStore';
import type ChapterData from '@/interface/Chapter';

const props = defineProps({
    mangas: {
        type: Array as PropType<{ title: string; id: number }[]>,
        required: true
    },
    chapter: {
        type: Object as PropType<ChapterData>,
        required: true
    },
    isEdit: {
        type: Boolean,
        required: true
    }
});

const formRef = ref<FormInst | null>(null)
const message = useMessage();
const chapterStore = useChapterStore();

const emit = defineEmits<{
  (event: 'requestResult', result: boolean): void;
  (event: 'cancelEdit', result: boolean): void
}>();

const size = ref('medium')

const model = ref({
    title: props.chapter.title != undefined ? props.chapter.title : '',
    description: props.chapter.description != undefined ? props.chapter.description : '',
    pagesNumber: props.chapter.numberPages != undefined ? props.chapter.numberPages : null as number | null,
    manga: null as string | null
})

const generalOptions = props.mangas.map(v => ({
    label: v.title,
    value: v.id
}))

const rules = {
    title: {
        required: true,
        message: 'Please enter the title...'
    },
    description: {
        required: true,
        message: 'Please enter description...'
    },
    pagesNumber: {
        type: 'number',
        required: true,
        message: 'Please enter the pages number...'
    },
    manga: {
        required: true,
        message: 'Please select the manga...'
    }
}

const handleValidateButtonClick = (e: MouseEvent) => {
    e.preventDefault()
    formRef.value?.validate((errors) => {
        if (!errors) {
            chapterRegister();
        } else {
            message.error('Enter valid data')
        }
    })
}

const clearFields = () => {
    model.value = {
        title: '',
        description: '',
        pagesNumber: null as number | null,
        manga: null as string | null
    }

    formRef.value?.restoreValidation();
}

const chapterRegister = async () => {
    const { title, description, pagesNumber, manga } = model.value;
    const data = {
        title: title,
        description: description,
        numberPages: pagesNumber,
        mangaId: manga
    }

    let response = "";
    
    if(!props.isEdit)
        response = await chapterStore.registerChapter(data, clearFields);
    else {
        response = await chapterStore.editChapter(props.chapter.id, data, clearFields);
        emit('requestResult', true);
    }

    message.info(response);
}

const cancel = () => {
    emit("cancelEdit", true);
}
</script>
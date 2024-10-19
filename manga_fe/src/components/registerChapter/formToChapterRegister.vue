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
import { defineProps, ref } from 'vue'
import type { FormInst } from 'naive-ui'
import { useMessage } from 'naive-ui'
import { api } from '@/network/axiosInstance';

const props = defineProps({
    mangas: {
        type: Array as () => { title: string, id: string }[],
        required: true
    }
})

const formRef = ref<FormInst | null>(null)
const message = useMessage()

const size = ref('medium')

const model = ref({
    title: '',
    description: '',
    pagesNumber: null as number | null,
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

function handleValidateButtonClick(e: MouseEvent) {
    e.preventDefault()
    formRef.value?.validate((errors) => {
        if (!errors) {
            chapterRegister();
        } else {
            message.error('Enter valid data')
        }
    })
}

function chapterRegister() {
    const { title, description, pagesNumber, manga } = model.value;
    const data = {
        title: title,
        description: description,
        numberPages: pagesNumber,
        mangaId: manga
    }
    
    api.post("/api/v1/chapter/create", data)
        .then(() => {
            message.success("Chapter successfully registered!");
            model.value = {
                title: '',
                description: '',
                pagesNumber: null,
                manga: null
            }

            formRef.value?.restoreValidation();
        })
        .catch(() => {
            message.error("An error occurred while registering, check the data.");
        })
}
</script>
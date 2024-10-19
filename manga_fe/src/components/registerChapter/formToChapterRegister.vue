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
                <n-form-item-gi :span="12" label="Pages number" path="pagesNumber">
                    <n-input-number v-model:value="model.pagesNumber" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Mangá" path="manga">
                    <n-select v-model:value="model.manga" placeholder="Choose the mangá" :options="generalOptions" />
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

<script lang="ts">
import { defineComponent, ref } from 'vue'
import type { FormInst } from 'naive-ui'
import { useMessage } from 'naive-ui'

export default defineComponent({
    props: {
        mangas: {
            type: Array,
            required: true
        }
    },
    setup(props) {
        const formRef = ref<FormInst | null>(null)
        const message = useMessage();
        return {
            formRef,
            size: ref('medium'),
            model: ref({
                title: null,
                description: null,
                pagesNumber: null,
                manga: null
            }),
            generalOptions: props.mangas.map(
                v => ({
                    // @ts-ignore
                    label: v.title,
                    // @ts-ignore
                    value: v.id
                })
            ),
            rules: {
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
                    message: 'Please select the mangá...'
                }
            },
            handleValidateButtonClick(e: MouseEvent) {
                e.preventDefault();
                formRef.value?.validate((errors) => {
                    if (!errors) {
                        message.success('Valid');
                    }
                    else {
                        message.error('Invalid');
                    }
                })
            }
        }
    }
})
</script>
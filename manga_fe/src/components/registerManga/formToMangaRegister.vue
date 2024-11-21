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
                <n-form-item-gi :span="12" label="Status" path="status">
                    <n-select
                        v-model:value="model.status"
                        :options="status"
                    />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Author" path="author">
                    <n-input v-model:value="model.author" placeholder="Enter the author name" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Gender" path="gender">
                    <n-input v-model:value="model.gender" placeholder="Enter the gender" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Image" path="image">
                    <n-input v-model:value="model.image" placeholder="Enter the image" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Creation Date" path="creationDate">
                    <n-date-picker v-model:value="model.creationDate" type="datetime" placeholder="Enter date" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Closing Date" path="closingDate">
                    <n-date-picker v-model:value="model.closingDate" type="datetime" placeholder="Enter date" />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="Size" path="sizeManga">
                    <n-input-number v-model:value="model.sizeManga" placeholder="Enter size" />
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
import { StatusType } from '@/enum/StatusType';
import type MangaData from '@/interface/Manga';
import { useMangaStore } from '@/store/MangaStore';
import { useMessage, type FormInst } from 'naive-ui';
import { ref } from 'vue';

const formRef = ref<FormInst | null>(null);
const mangaStore = useMangaStore();
const message = useMessage();
const size = ref('medium');

const props = defineProps<{
  manga: MangaData;
  isEdit: boolean;
}>();

const action = props.isEdit ? "Edit" : "Register";

const emit = defineEmits<{
  (event: 'requestResult', result: boolean): void;
  (event: 'cancelEdit', result: boolean): void
}>();

const model = ref({
    title: props.manga != undefined && props.isEdit ? props.manga.title : '' as string,
    description: props.manga != undefined && props.isEdit ? props.manga.description : '' as string,
    sizeManga: props.manga != undefined && props.isEdit ? props.manga.size : null as number | null,
    status: props.manga != undefined && props.isEdit ? props.manga.status : StatusType.ONGOING as string,
    author: props.manga != undefined && props.isEdit ? props.manga.author : '' as string,
    gender: props.manga != undefined && props.isEdit ? props.manga.gender : '' as string,
    image: props.manga != undefined && props.isEdit ? props.manga.image : '' as string,
    creationDate: props.manga != undefined && props.isEdit ? props.manga.creationDate : null as Date | null,
    closingDate: props.manga != undefined && props.isEdit ? props.manga.closingDate : null as Date | null,
});

const status = [StatusType.ONGOING, StatusType.FINISHED].map(
    v => ({
        label: v,
        value: v
    })
);

const handleValidateButtonClick = (e: MouseEvent) => {
    e.preventDefault()
    formRef.value?.validate((errors) => {
        if (!errors) {
            mangaRegister();
        } else {
            message.error('Enter valid data')
        }
    })
}

const rules = {
    title: {
        required: true,
        message: 'Please enter the title...'
    },
    description: {
        required: true,
        message: 'Please enter description...'
    },
    status: {
        required: true,
        message: 'Please select the status...'
    },
    author: {
        required: true,
        message: 'Please enter the author name...'
    },
    gender: {
        required: true,
        message: 'Please enter the gender...'
    },
    image: {
        required: true,
        message: 'Please enter the image...'
    },
    creationDate: {
        required: true,
        message: 'Please enter the creation date...'
    },
    closingDate: {
        required: false,
    },
    sizeManga: {
        type: 'number',
        required: true,
        message: 'Please enter the size...'
    }
}

const mangaRegister = async () => {
    const { title, description, sizeManga, creationDate, closingDate, status, author, gender, image } = model.value;
    const data = {
        title: title,
        description: description,
        size: sizeManga,
        creationDate: creationDate != null ? new Date(creationDate) : creationDate,
        closingDate: closingDate != null ? new Date(closingDate) : closingDate,
        status: status,
        author: author,
        gender: gender,
        image: image
    };

    let response = "";
    
    if(!props.isEdit)
        response = await mangaStore.registerManga(data, clearFields);
    else {
        response = await mangaStore.editManga(props.manga.id, data, clearFields);
        emit('requestResult', true);
    }

    message.info(response);
}

const clearFields = () => {
    model.value = {
        title: '',
        description: '',
        sizeManga: null,
        status: StatusType.ONGOING,
        author: '',
        gender: '',
        image: '',
        creationDate: null,
        closingDate: null,
    }

    formRef.value?.restoreValidation();
}

const cancel = () => {
    emit("cancelEdit", true);
}
</script>
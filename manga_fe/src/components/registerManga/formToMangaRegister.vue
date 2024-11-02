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
import { StatusType } from '@/enum/StatusType';
import { api } from '@/network/axiosInstance';
import { useMessage, type FormInst } from 'naive-ui';
import { ref } from 'vue';

const formRef = ref<FormInst | null>(null);
const message = useMessage();
const size = ref('medium');

const model = ref({
    title: '' as string,
    description: '' as string,
    sizeManga: null as number | null,
    status: StatusType.ONGOING as string,
    author: '' as string,
    gender: '' as string,
    image: '' as string,
    creationDate: null as Date | null,
    closingDate: null as Date | null,
});

const status = [StatusType.ONGOING, StatusType.FINISHED].map(
    v => ({
        label: v,
        value: v
    })
);

function handleValidateButtonClick(e: MouseEvent) {
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

function mangaRegister() {
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
    }

    api.post("/api/v1/manga/create", data)
        .then(() => {
            message.success("Mangá successfully registered!");
            clearFields();
        })
        .catch((error) => {
            console.error(error);
            message.error("An error occurred while registering, check the data.");
        })
}

function clearFields() {
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
</script>
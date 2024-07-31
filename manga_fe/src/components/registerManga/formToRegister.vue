<template>
    <div class="flex flex-col p-9 bg-gray-100 min-h-screen">
        <h1 class="text-3xl text-center font-bold underline text-gray-900">Register Manga</h1>

        <div class="containerError">
            <p v-if="error" class="errorMessage text-red-600 font-semibold">{{ error }}</p>
        </div>
        
        <div class="containerSuccess">
            <p v-if="success" class="successMessage text-green-600 font-semibold">{{ success }}</p>
        </div>
        <form @submit.prevent="register" class="flex flex-col space-y-4 m-7 p-6 bg-white shadow-md rounded-md">
            <div class="flex flex-col">
                <label for="title" class="mb-2 font-semibold text-gray-800">Title</label>
                <input type="text" name="title" v-model="title" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <div class="flex flex-col">
                <label for="description" class="mb-2 font-semibold text-gray-800">Description</label>
                <input type="text" name="description" v-model="description" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <div class="flex flex-col">
                <label for="size" class="mb-2 font-semibold text-gray-800">Size</label>
                <input type="text" name="size" v-model="size" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <div class="flex flex-col">
                <label for="creationDate" class="mb-2 font-semibold text-gray-800">Creation Date</label>
                <input type="date" name="creationDate" v-model="creationDate" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <div class="flex flex-col">
                <label for="closingDate" class="mb-2 font-semibold text-gray-800">Closing Date</label>
                <input type="date" name="closingDate" v-model="closingDate" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <div class="flex flex-col">
                <label for="status" class="mb-2 font-semibold text-gray-800">Status</label>
                <select name="status" id="status" v-model="status" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
                    <option v-for="status in Object.values(StatusType)" :key="status">{{ status }}</option>
                </select>
            </div>

            <div class="flex flex-col">
                <label for="author" class="mb-2 font-semibold text-gray-800">Author</label>
                <input type="text" name="author" v-model="author" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <div class="flex flex-col">
                <label for="gender" class="mb-2 font-semibold text-gray-800">Gender</label>
                <input type="text" name="gender" v-model="gender" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <div class="flex flex-col">
                <label for="image" class="mb-2 font-semibold text-gray-800">Image</label>
                <input type="text" name="image" v-model="image" class="p-3 border border-gray-400 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400">
            </div>

            <button type="submit" class="bg-blue-500 text-white p-4 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400">Register</button>
        </form>
    </div>
</template>

<script lang="ts">
    import { ref } from 'vue';
    import { StatusType } from "@/enum/StatusType";
    import { api } from "@/network/axiosInstance";
    import type { MangaData } from "@/interface/Manga.js";
    import { validationFields } from "@/utils/validation";

    export default {
        name: "FormToRegisterManga",
        setup() {
            const title = ref("");
            const description = ref("");
            const size = ref(1);
            const creationDate = ref(new Date());
            const closingDate = ref(new Date());
            const status = ref(StatusType.ONGOING);
            const author = ref("");
            const gender = ref("");
            const image = ref("");
            const error = ref("");
            const success = ref("");

            const clearFields = () => {
                title.value = "";
                description.value = "";
                size.value = 1;
                creationDate.value = new Date();
                closingDate.value = new Date();
                status.value = StatusType.ONGOING;
                author.value = "";
                gender.value = "";
                image.value = "";
            };

            const register = () => {
                const data: MangaData = {
                    title: title.value,
                    description: description.value,
                    size: size.value,
                    creationDate: creationDate.value,
                    closingDate: closingDate.value,
                    status: status.value,
                    author: author.value,
                    gender: gender.value,
                    image: image.value,
                };

                const result = validationFields(data);

                if (typeof result === "string") {
                    error.value = result;
                    setTimeout(() => {
                        error.value = "";
                    }, 5000);
                    return;
                }

                api.post('/api/v1/manga/create', data)
                    .then(() => {
                        clearFields();
                        success.value = "Manga created successfully";
                        setTimeout(() => {
                            success.value = "";
                        }, 5000);
                    })
                    .catch(() => {
                        console.log("Error to register mangá!");
                    });
            };

            return {
                title,
                description,
                size,
                creationDate,
                closingDate,
                status,
                author,
                gender,
                image,
                error,
                success,
                clearFields,
                register,
                StatusType,
            };
        },
    }
</script>

<style scoped>

    .containerError,
    .containerSuccess {
        padding: 10px;
    }

    .errorMessage {
        background-color: #f8d7da;
        color: #721c24; 
        padding: 10px; 
        border: 1px solid #721c24; 
        border-radius: 4px;
        width: 60%;
        margin: 0 auto;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        text-align: center; 
    }

    .successMessage {
        background-color: #d4edda; 
        color: #155724; 
        padding: 10px;
        border: 1px solid #155724;
        border-radius: 4px; 
        width: 60%;
        margin: 0 auto;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); 
        text-align: center; 
    }

</style>
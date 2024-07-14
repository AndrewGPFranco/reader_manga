<template>
    <main>
        <form @submit.prevent="register">

            <label for="title">Title</label>
            <input type="text" name="title" v-model="title">

            <label for="description">Description</label>
            <input type="text" name="description" v-model="description">

            <label for="size">Size</label>
            <input type="text" name="size" v-model="size">

            <label for="creationDate">Creation Date</label>
            <input type="date" name="creationDate" v-model="creationDate">

            <label for="closingDate">Closing Date</label>
            <input type="date" name="closingDate" v-model="closingDate">

            <label for="status">Status</label>
            <select name="status" id="status" v-model="status">
                <option v-for="status in Object.values(statusType)" :key="status">{{ status }}</option>
            </select>

            <label for="author">Author</label>
            <input type="text" name="author" v-model="author">

            <label for="gender">Gender</label>
            <input type="text" name="gender" v-model="gender">

            <label for="image">Image</label>
            <input type="text" name="image" v-model="image">

            <button type="submit">Register</button>

        </form>
    </main>
</template>

<script lang="ts">
    import { StatusType } from "@/enum/StatusType";
    import { api } from "../network/axiosInstance"
    import type { MangaData } from "@/interface/Manga.js";
    import { validationFields } from "@/utils/validation";

    export default {
        name: "RegisterManga",
        data() {
            return {
                title: "",
                description: "",
                size: 1,
                creationDate: new Date,
                closingDate: new Date,
                status: StatusType.ONGOING,
                author: "",
                gender: "",
                image: "",
                statusType: StatusType
            }
        },
        methods: {
            clearFields() {
                this.title = "",
                this.description = "",
                this.size = 1,
                this.creationDate = new Date,
                this.closingDate = new Date,
                this.status = StatusType.ONGOING,
                this.author = "",
                this.gender = "",
                this.image = ""
            },
            register() {
                const data: MangaData = {
                    title: this.title,
                    description: this.description,
                    size: this.size,
                    creationDate: this.creationDate,
                    closingDate: this.closingDate,
                    status: this.status,
                    author: this.author,
                    gender: this.gender,
                    image: this.image,
                };

                const result = validationFields(data);

                if(typeof result === "string") {
                    console.log(result);
                    return;
                }

                api.post('/api/v1/manga/create', data)
                    .then(() => {
                        this.clearFields();
                    })
                    .catch(() => {
                        console.log("Error to register mangá!");
                    })
            }
        },
    }
</script>

<style scoped>

    main {
        padding: 100px;
    }

    form {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
</style>
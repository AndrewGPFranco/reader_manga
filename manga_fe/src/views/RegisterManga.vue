<template>
    <main>

        <h1>Register Manga</h1>

        <div class="containerError">
            <p v-if="error" class="errorMessage">{{ error }}</p>
        </div>

        <div class="containerSuccess">
            <p v-if="success" class="successMessage">{{ success }}</p>
        </div>

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
                statusType: StatusType,
                error: "",
                success: "",
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
                    this.error = result;
                    setTimeout(() => {
                            this.error = "";
                        }, 5000);
                    return;
                }

                api.post('/api/v1/manga/create', data)
                    .then(() => {
                        this.clearFields();
                        this.success = "Manga created successfully";
                        setTimeout(() => {
                            this.success = "";
                        }, 5000);
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
        padding: 0 100px;
    }

    form {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }

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
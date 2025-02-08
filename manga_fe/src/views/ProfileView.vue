<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card>
            <section class="card-content">
                <div class="profile-container">
                    <img src="../assets/home/op.jpeg" alt="Foto de Perfil" class="profile-image" />
                    <h2 class="profile-name">{{ name }}</h2>
                    <p class="profile-info">{{ email }}</p>
                    <p class="profile-info">Nickname: {{ username }}</p>
                    <p class="profile-info">Data de Nascimento: {{ dateBirth }}</p>
                </div>

                <div class="button-group">
                    <button @click="efetuarLogout" class="logout-button">
                        Logout
                    </button>
                    <button @click="acionaFormDeTrocaDeSenha" class="change-password-button">
                        Alterar Senha
                    </button>
                </div>
            </section>

            <section class="containerPasswordRecovery" v-if="isPasswordRecovery">
                <form class="form">
                    <p id="heading">Alteração de senha:</p>
                    <div class="field">
                        <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                            fill="currentColor" viewBox="0 0 16 16">
                            <path
                                d="M13.106 7.222c0-2.967-2.249-5.032-5.482-5.032-3.35 0-5.646 2.318-5.646 5.702 0 3.493 2.235 5.708 5.762 5.708.862 0 1.689-.123 2.304-.335v-.862c-.43.199-1.354.328-2.29.328-2.926 0-4.813-1.88-4.813-4.798 0-2.844 1.921-4.881 4.594-4.881 2.735 0 4.608 1.688 4.608 4.156 0 1.682-.554 2.769-1.416 2.769-.492 0-.772-.28-.772-.76V5.206H8.923v.834h-.11c-.266-.595-.881-.964-1.6-.964-1.4 0-2.378 1.162-2.378 2.823 0 1.737.957 2.906 2.379 2.906.8 0 1.415-.39 1.709-1.087h.11c.081.67.703 1.148 1.503 1.148 1.572 0 2.57-1.415 2.57-3.643zm-7.177.704c0-1.197.54-1.907 1.456-1.907.93 0 1.524.738 1.524 1.907S8.308 9.84 7.371 9.84c-.895 0-1.442-.725-1.442-1.914z">
                            </path>
                        </svg>
                        <input autocomplete="off" placeholder="Username" class="input-field" type="text">
                    </div>
                    <div class="field">
                        <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                            fill="currentColor" viewBox="0 0 16 16">
                            <path
                                d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z">
                            </path>
                        </svg>
                        <input placeholder="Password" class="input-field" type="password">
                    </div>
                    <div class="btn">
                        <button
                            class="button1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Login&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
                        <button class="button2">Sign Up</button>
                    </div>
                </form>
            </section>
        </n-card>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';
import router from '@/router';
import { useAuthStore } from '@/store/AuthStore';
import { onMounted, ref } from 'vue';

const auth = useAuthStore();
const name = ref<string>("");
const email = ref<string>("");
const username = ref<string>("");
const dateBirth = ref<Date | null>(null);
let isPasswordRecovery = ref<boolean>(false);

const efetuarLogout = () => {
    auth.efetuarLogout();
    router.push({ name: 'login' });
}

const acionaFormDeTrocaDeSenha = () => {
    isPasswordRecovery.value = !isPasswordRecovery.value;
};

onMounted(async () => {
    const result = await auth.getUser();
    name.value = result.firstName;
    email.value = result.email;
    dateBirth.value = result.dateBirth;
    username.value = result.username;
});

</script>

<style scoped>
main {
    padding: 15px;
}

.n-card {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 95vh;
    box-sizing: border-box;
    overflow: scroll;
    overflow-x: hidden;
}

.card-content {
    width: 75vw;
    padding: 20px;
    background-color: #010024;
    color: #ffffff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    border-radius: 12px;
}

.profile-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
}

.profile-image {
    width: 128px;
    height: 128px;
    object-fit: cover;
    border-radius: 50%;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.profile-name {
    font-size: 1.75rem;
    font-weight: 600;
    margin-top: 10px;
}

.profile-info {
    color: #d0d0d0;
    margin: 5px 0;
}

.button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}

button {
    padding: 10px 15px;
    border: none;
    border-radius: 8px;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.logout-button {
    background-color: #e74c3c;
}

.logout-button:hover {
    background-color: #c0392b;
}

.change-password-button {
    background-color: #3498db;
}

.change-password-button:hover {
    background-color: #2980b9;
}

.containerPasswordRecovery {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    margin-bottom: 20px;
}
    
.form {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding-left: 2em;
    padding-right: 2em;
    padding-bottom: 0.4em;
    background-color: #171717;
    border-radius: 25px;
    transition: .4s ease-in-out;
    width: 40vw;
}

.form:hover {
    transform: scale(1.05);
    border: 1px solid black;
}

#heading {
    text-align: center;
    margin: 2em;
    color: rgb(255, 255, 255);
    font-size: 1.2em;
}

.field {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5em;
    border-radius: 25px;
    padding: 0.6em;
    border: none;
    outline: none;
    color: white;
    background-color: #171717;
    box-shadow: inset 2px 5px 10px rgb(5, 5, 5);
}

.input-icon {
    height: 1.3em;
    width: 1.3em;
    fill: white;
}

.input-field {
    background: none;
    border: none;
    outline: none;
    width: 100%;
    color: #d3d3d3;
}

.form .btn {
    display: flex;
    justify-content: center;
    flex-direction: row;
    margin-top: 1.8em;
}

.button1 {
    padding: 0.5em;
    padding-left: 1.1em;
    padding-right: 1.1em;
    border-radius: 5px;
    margin-right: 0.5em;
    border: none;
    outline: none;
    transition: .4s ease-in-out;
    background-color: #252525;
    color: white;
    margin-bottom: 1.8em;
}

.button1:hover {
    background-color: black;
    color: white;
}

.button2 {
    padding: 0.5em;
    padding-left: 2.3em;
    padding-right: 2.3em;
    border-radius: 5px;
    border: none;
    outline: none;
    transition: .4s ease-in-out;
    background-color: #252525;
    color: white;
    margin-bottom: 1.8em;
}

.button2:hover {
    background-color: black;
    color: white;
}
</style>
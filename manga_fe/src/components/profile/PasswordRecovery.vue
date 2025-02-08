<template>
    // TODO: implementar funcionalidade de visualizar as senhas que estão sendo digitadas
    <section class="form-container">
        <form class="form">
            <h2 id="heading">Alteração de Senha</h2>
            <div class="field">
                <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                    viewBox="0 0 16 16">
                    <path
                        d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z">
                    </path>
                </svg>
                <input autocomplete="off" placeholder="Senha anterior" class="input-field" type="password" />
            </div>
            <div class="field">
                <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                    viewBox="0 0 16 16">
                    <path
                        d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z">
                    </path>
                </svg>
                <input placeholder="Nova senha" class="input-field" type="password" />
            </div>
            <div class="field">
                <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                    viewBox="0 0 16 16">
                    <path
                        d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z">
                    </path>
                </svg>
                <input placeholder="Repita a nova senha" class="input-field" type="password" />
            </div>
            <div class="btn">
                <button class="button2" @click.prevent="efetuarTrocaSenha">Alterar</button>
            </div>
        </form>
    </section>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const senhaAntiga = ref<string>("");
const novaSenha = ref<string>("");
const novaSenhaRepetida = ref<string>("");

const efetuarTrocaSenha = (e: MouseEvent) => {
    e.preventDefault();

    // TODO: implementar lógica
};

</script>

<style lang="css" scoped>
.form-container {
    display: flex;
    justify-content: center;
    margin: 20px;
    padding: 2em;
}

.form {
    display: flex;
    flex-direction: column;
    gap: 15px;
    padding: 2rem;
    background: linear-gradient(135deg, #f3f4f6, #e5e7eb);
    border-radius: 16px;
    box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
    width: 40vw;
    max-width: 500px;
    transition: transform 0.3s ease;
}

.form:hover {
    transform: translateY(-5px);
}

#heading {
    text-align: center;
    font-size: 1.8rem;
    color: #1f2937;
    font-weight: bold;
    margin-bottom: 1em;
}

.field {
    display: flex;
    align-items: center;
    background-color: #f9fafb;
    border: 1px solid #d1d5db;
    border-radius: 8px;
    padding: 12px;
    box-shadow: inset 2px 4px 8px rgba(0, 0, 0, 0.05);
}

.input-icon {
    height: 20px;
    width: 20px;
    fill: #6b7280;
    margin-right: 10px;
}

.input-field {
    background: transparent;
    border: none;
    outline: none;
    width: 100%;
    color: #374151;
    font-size: 1rem;
}

.field:focus-within {
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.5);
}

.btn {
    display: flex;
    justify-content: center;
    margin-top: 1.5em;
}

.button2 {
    background-color: #2563eb;
    color: white;
    padding: 0.8em 2em;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
}

.button2:hover {
    background-color: #1d4ed8;
    box-shadow: 0 8px 16px rgba(37, 99, 235, 0.3);
}
</style>
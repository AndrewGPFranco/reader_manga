<template>
  <n-table :single-line="false" striped>
    <thead>
    <tr>
      <th>Usuário</th>
      <th class="text-center">Ações</th> <!-- TODO: Melhorar para possibilidade de alterar o tier para qualquer opção! -->
    </tr>
    </thead>

    <tbody>
    <tr v-for="(usuario, index) in usuarios" :key="index">
      <td>{{ usuario }}</td>
      <td class="text-center">
        <n-space justify="center">
          <n-button
              quaternary
              circle
              type="primary"
              size="small"
              @click="tornarAdmin(usuario)"
              aria-label="Tornar administrador"
          >
            <template #icon>
              <Edit/>
            </template>
          </n-button>
        </n-space>
      </td>
    </tr>
    </tbody>
  </n-table>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useMessage} from "naive-ui";
import {useAuthStore} from "@/store/AuthStore";
import {CreateOutline as Edit} from '@vicons/ionicons5'

const authStore = useAuthStore();

const toast = useMessage();
const paginaAtual = ref<number>(0);
const usuarios = ref<string[]>([]);

const getUsuariosLogados = async () => {
  usuarios.value = await authStore.getUsuarios(paginaAtual.value);
}

const tornarAdmin = async (username: string) => {
  const response = await authStore.tornarUsuarioAdmin(username);
  toast.info(response);

  usuarios.value = [];
  await getUsuariosLogados();
}

onMounted(() => getUsuariosLogados());
</script>

<style scoped>
.tdButtons {
  display: flex;
  justify-content: space-around;
}

.tdButtons svg {
  width: 25px;
  height: 25px;
}

.buttonEdit {
  cursor: pointer;
}

.buttonEdit {
  color: rgb(0, 109, 0);
}
</style>
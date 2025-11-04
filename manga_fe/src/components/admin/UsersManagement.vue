<template>
  <n-table :single-line="false" striped>
    <thead>
      <tr>
        <th>Usuário</th>
        <th>Data de Registro</th>
        <th>Role Atual</th>
        <th class="text-center">Alterar Role</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="usuario in usuarios" :key="usuario.username">
        <td>{{ usuario.username }}</td>
        <td>{{ formatDate(usuario.dataIn) }}</td>
        <td>{{ usuario.role }}</td>
        <td class="text-center">
          <n-space>
            <n-select
              :value="novasRoles[usuario.username] || usuario.role"
              @update:value="(value: string) => novasRoles[usuario.username] = value"
              :options="options"
              placeholder="Selecione o tier"
              size="small"
              style="width: 150px"
            />
            <n-button 
              type="primary" 
              size="small" 
              @click="mudarRole(usuario.username, usuario.role, novasRoles[usuario.username] || usuario.role)"
            >
              Alterar Role
            </n-button>
          </n-space>
        </td>
      </tr>
    </tbody>
  </n-table>
  <div class="pagination">
    <n-pagination class="mt-5" v-model:page="page" :page-count="pageTotal" simple />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue'
import { useMessage } from 'naive-ui'
import { formatDate } from '@/utils/utils'
import { useAuthStore } from '@/store/AuthStore'
import type { iUsersManagament } from '@/@types/iUsersManagament'

let page = ref<number>(1)
const toast = useMessage()
let pageTotal = ref<number>(0)
const authStore = useAuthStore()
const paginaAtual = ref<number>(0)
const usuarios = ref<iUsersManagament[]>([])
const novasRoles = reactive<Record<string, string>>({})

const getUsuariosLogados = async () => {
  const data = await authStore.getUsuarios(paginaAtual.value)
  usuarios.value = data.content;
  page.value = data.number + 1
  pageTotal.value = data.totalPages
}

const mudarRole = async (username: string, roleAtual: string, roleNovo: string) => {
  if (roleAtual === roleNovo) {
    toast.info('A role atual e a nova role são iguais')
    return
  }
  
  const response = await authStore.mudarRole(username, roleNovo)
  toast.info(response)
  
  delete novasRoles[username]
  
  usuarios.value = []
  await getUsuariosLogados()
}

const options = [
  {
    label: 'Administrador',
    value: 'ADMIN'
  },
  {
    label: 'Usuário',
    value: 'USER'
  },
  {
    label: 'Moderador',
    value: 'MODERATOR'
  }
]

onMounted(() => getUsuariosLogados())
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
  color: rgb(0, 109, 0);
}
</style>
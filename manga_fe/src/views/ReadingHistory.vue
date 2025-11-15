<template>
  <header>
    <MenuComponent />
  </header>
  <main>
    <n-card class="containerCard" title="Histórico de Leitura" size="huge">
      <TimelineHistory :historico="historico" />
    </n-card>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useChapterStore } from '@/store/ChapterStore'
import type { IHistoricoVO } from '@/@types/IHistoricoVO'
import MenuComponent from '@/components/global/MenuComponent.vue'
import TimelineHistory from '@/components/history/TimelineHistory.vue'

const chapterStore = useChapterStore()
const historico = ref<IHistoricoVO[]>([] as IHistoricoVO[])

async function getAllHistorico() {
  return await chapterStore.getAllHistorico()
}

onMounted(async () => {
  const result = await getAllHistorico()
  historico.value = result.content
})
</script>

<style lang="css" scoped>
main {
  padding: 15px;
}

.containerCard {
  height: 95vh;
  box-sizing: border-box;
  overflow-y: auto;
}
</style>

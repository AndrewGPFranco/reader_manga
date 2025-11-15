<template>
  <div class="history">
    <div
      v-for="group in groupedHistorico"
      :key="`${group.manga}-${group.chapter}`"
      class="timeline-group"
    >
      <div class="manga-info">
        <NH3>{{ group.manga }}</NH3>
        <NP>Capítulo {{ group.chapter }}</NP>
      </div>

      <n-timeline horizontal>
        <n-timeline-item
          v-for="(historico, index) in group.items"
          :key="historico.id"
          :time="formatDate(historico.lastCheck)"
          :type="getTimelineType(index, group.items.length)"
        />
      </n-timeline>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { PropType } from 'vue'
import type { IHistoricoVO } from '@/@types/IHistoricoVO'

const props = defineProps({
  historico: {
    type: Array as PropType<IHistoricoVO[]>,
    required: true
  }
})

interface GroupedHistorico {
  manga: string
  chapter: string
  items: IHistoricoVO[]
}

const groupedHistorico = computed<GroupedHistorico[]>(() => {
  const groups = new Map<string, GroupedHistorico>()

  props.historico.forEach((item) => {
    const numeroCapitulo = item.titleChapter.split('_')[1]
    const key = `${item.titleManga}-${numeroCapitulo}`

    if (!groups.has(key)) {
      groups.set(key, {
        manga: item.titleManga,
        chapter: numeroCapitulo,
        items: []
      })
    }

    groups.get(key)!.items.push(item)
  })

  groups.forEach((group) => {
    group.items.sort((a, b) => new Date(a.lastCheck).getTime() - new Date(b.lastCheck).getTime())
  })

  return Array.from(groups.values())
})

function formatDate(dateInput: string | Date): string {
  const date = typeof dateInput === 'string' ? new Date(dateInput) : dateInput

  const dia = String(date.getDate()).padStart(2, '0')
  const mes = String(date.getMonth() + 1).padStart(2, '0')
  const ano = date.getFullYear()
  const horas = String(date.getHours()).padStart(2, '0')
  const minutos = String(date.getMinutes()).padStart(2, '0')

  return `${dia}/${mes}/${ano} ${horas}:${minutos}`
}

function getTimelineType(index: number, total: number): 'default' | 'success' | 'warning' {
  if (total === 1) return 'success'
  if (index === 0) return 'default'
  if (index === total - 1) return 'success'
  return 'warning'
}
</script>

<style scoped>
.history {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.timeline-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.manga-info {
  margin-bottom: 0.5rem;
}

.manga-info h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
}

.manga-info p {
  margin: 0.25rem 0 0 0;
  font-size: 0.95rem;
  opacity: 0.7;
}
</style>

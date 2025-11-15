<template>
  <div class="history">
    <div class="manga-info">
      <h3>{{ getMangaName() }}</h3>
      <p>{{ getChapterNumber() }}</p>
    </div>

    <n-timeline horizontal>
      <n-timeline-item
        v-for="(historico, index) in props.historico"
        :key="historico.id"
        :time="formatDate(historico.lastCheck)"
        :type="getTimelineType(index)"
      />
    </n-timeline>
  </div>
</template>

<script setup lang="ts">
import type { PropType } from 'vue'
import type { IHistoricoVO } from '@/@types/IHistoricoVO'

const props = defineProps({
  historico: {
    type: Array as PropType<IHistoricoVO[]>,
    required: true
  }
})

function getMangaName(): string {
  if (props.historico.length === 0) return ''
  return props.historico[0].titleManga
}

function getChapterNumber(): string {
  if (props.historico.length === 0) return ''
  const title = props.historico[0].titleChapter
  const numeroCapitulo = title.split('_')[1]
  return `Capítulo ${numeroCapitulo}`
}

function formatDate(dateInput: string | Date): string {
  const date = typeof dateInput === 'string' ? new Date(dateInput) : dateInput

  const dia = String(date.getDate()).padStart(2, '0')
  const mes = String(date.getMonth() + 1).padStart(2, '0')
  const ano = date.getFullYear()
  const horas = String(date.getHours()).padStart(2, '0')
  const minutos = String(date.getMinutes()).padStart(2, '0')

  return `${dia}/${mes}/${ano} ${horas}:${minutos}`
}

function getTimelineType(index: number): 'default' | 'success' | 'warning' {
  const total = props.historico.length

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
  gap: 1rem;
}

.manga-info {
  margin-bottom: 0.5rem;
}

.manga-info h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: #b2f8b2;
}

.manga-info p {
  margin: 0.25rem 0 0 0;
  font-size: 0.95rem;
  opacity: 0.7;
  color: #b2f8b2;
}
</style>

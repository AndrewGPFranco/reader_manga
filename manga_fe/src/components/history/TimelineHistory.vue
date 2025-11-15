<template>
  <div class="history">
    <n-empty
      v-if="groupedHistorico.length === 0"
      description="Nenhum histórico encontrado"
      size="large"
    >
      <template #icon>
        <n-icon size="64">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <path d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </n-icon>
      </template>
    </n-empty>

    <transition-group name="timeline-fade" tag="div" class="timeline-container">
      <n-card
        v-for="(group, groupIndex) in groupedHistorico"
        :key="`${group.manga}-${group.chapter}`"
        class="timeline-card"
        :style="{ '--delay': groupIndex * 0.1 + 's' }"
        hoverable
      >
        <div class="card-header">
          <div class="manga-badge">
            <n-icon size="20" class="manga-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
                <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
              </svg>
            </n-icon>
            <span>Mangá</span>
          </div>

          <div class="manga-info">
            <NH3 class="manga-title">{{ group.manga }}</NH3>
            <div class="chapter-badge">
              <n-icon size="16">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                  <polyline points="14 2 14 8 20 8" />
                </svg>
              </n-icon>
              <NP class="chapter-number">Capítulo {{ group.chapter }}</NP>
            </div>
          </div>
        </div>

        <n-divider class="custom-divider" />

        <div class="timeline-wrapper">
          <div class="timeline-header">
            <n-icon size="18" class="clock-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <circle cx="12" cy="12" r="10" />
                <polyline points="12 6 12 12 16 14" />
              </svg>
            </n-icon>
            <span class="timeline-title">Histórico de Verificações</span>
            <n-tag :bordered="false" size="small" class="count-tag">
              {{ group.items.length }} {{ group.items.length === 1 ? 'vez' : 'vezes' }}
            </n-tag>
          </div>

          <n-timeline horizontal class="custom-timeline">
            <n-timeline-item
              v-for="(historico, index) in group.items"
              :key="historico.id"
              :time="formatDate(historico.lastCheck)"
              :type="getTimelineType(index, group.items.length)"
              :title="getTimelineTitle(index, group.items.length)"
            >
              <template #icon>
                <n-icon size="16">
                  <svg
                    v-if="index === 0"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <circle cx="12" cy="12" r="10" />
                    <polyline points="12 6 12 12 16 14" />
                  </svg>
                  <svg
                    v-else-if="index === group.items.length - 1"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <polyline points="20 6 9 17 4 12" />
                  </svg>
                  <svg
                    v-else
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <circle cx="12" cy="12" r="10" />
                  </svg>
                </n-icon>
              </template>
            </n-timeline-item>
          </n-timeline>
        </div>
      </n-card>
    </transition-group>
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

function getTimelineTitle(index: number, total: number): string {
  if (total === 1) return 'Verificação'
  if (index === 0) return 'Início'
  if (index === total - 1) return 'Última'
  return `${index + 1}ª`
}
</script>

<style scoped>
.history {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding: 0.5rem;
}

.timeline-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.timeline-card {
  animation: slideIn 0.5s ease-out backwards;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.timeline-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.card-header {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.manga-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.4rem 0.8rem;
  background: rgba(99, 226, 183, 0.1);
  border-radius: 8px;
  width: fit-content;
  font-size: 0.85rem;
  font-weight: 500;
  color: rgb(99, 226, 183);
}

.manga-icon {
  color: rgb(99, 226, 183);
}

.manga-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.manga-title {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, rgb(99, 226, 183), rgb(79, 206, 163));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.chapter-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  width: fit-content;
  padding: 0.3rem 0.6rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.chapter-number {
  margin: 0;
  font-size: 0.9rem;
  font-weight: 500;
  opacity: 0.9;
}

.custom-divider {
  margin: 1rem 0;
  opacity: 0.6;
}

.timeline-wrapper {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.timeline-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0;
}

.clock-icon {
  opacity: 0.7;
}

.timeline-title {
  font-size: 0.95rem;
  font-weight: 600;
  opacity: 0.8;
}

.count-tag {
  margin-left: auto;
  background: rgba(99, 226, 183, 0.15);
  color: rgb(99, 226, 183);
  font-weight: 600;
}

.custom-timeline {
  padding: 1rem 0;
}

.timeline-fade-enter-active,
.timeline-fade-leave-active {
  transition: all 0.5s ease;
}

.timeline-fade-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.timeline-fade-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

@media (max-width: 768px) {
  .manga-title {
    font-size: 1.25rem;
  }

  .timeline-card {
    margin: 0 -0.5rem;
  }
}
</style>

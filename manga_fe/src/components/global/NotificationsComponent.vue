<template>
  <div></div>
</template>

<script setup lang="ts">
import {h, onMounted, onUnmounted} from 'vue'
import {NAvatar, useNotification} from 'naive-ui'
import {URL_SSE_NOTIFICATIONS, formatDate} from '@/utils/utils'

const notification = useNotification()

let eventSource: EventSource | null = null

const listenerNotificacoes = () => {
  eventSource = new EventSource(URL_SSE_NOTIFICATIONS)

  eventSource.onmessage = (event) => {
    const data = event.data

    const dadosExibicao = data.split(' / Capa: ')
    const mensagemParaExibicao = dadosExibicao[0]
    const imagemMangaParaAvatar = dadosExibicao[1]

    notification.create({
      title: () =>
          h('div', {style: 'color: #22c55e; font-weight: 700;'}, 'Alerta de capítulo novo!'),
      content: () => h('div', {style: 'color: #22c55e; font-weight: 600;'}, mensagemParaExibicao),
      meta: formatDate(new Date()),
      duration: 15000,
      avatar: () =>
          h(NAvatar, {
            size: 'large',
            src: imagemMangaParaAvatar
          })
    })
  }

  eventSource.onerror = (error) => {
    console.error('Error occurred:', error)
    if (eventSource != null) eventSource.close()
  }
}

const fechaEventSource = () => {
  if (eventSource) {
    eventSource.close()
    eventSource = null
  }
}

onMounted(() => listenerNotificacoes())

onUnmounted(() => fechaEventSource())
</script>

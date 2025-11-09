<template>
  <div></div>
</template>

<script setup lang="ts">
import { NAvatar, useNotification } from 'naive-ui';
import { h, onMounted, onUnmounted } from 'vue';
import { URL_SSE_NOTIFICATIONS, formatDate } from '@/utils/utils';

const notification = useNotification()

let eventSource: EventSource | null = null;

const listenerNotificacoes = () => {
  eventSource = new EventSource(URL_SSE_NOTIFICATIONS);

  eventSource.onmessage = (event) => {
    const data = event.data;

    const dadosExibicao = data.split(' / Capa: ');
    const mensagemParaExibicao = dadosExibicao[0];
    const imagemMangaParaAvatar = dadosExibicao[1];

    notification.create({
      title: 'Alerta de capítulo novo!',
      content: `${mensagemParaExibicao}`,
      meta: `${formatDate(new Date())}`,
      avatar: () =>
        h(NAvatar, {
          size: 'medium',
          src: `${imagemMangaParaAvatar}`
        }),
    })
  };

  eventSource.onerror = (error) => {
    console.error("Error occurred:", error);
    if (eventSource != null)
      eventSource.close();
  };
}

const fechaEventSource = () => {
  if (eventSource) {
    eventSource.close()
    eventSource = null
  }
}

onMounted(() => listenerNotificacoes());

onUnmounted(() => fechaEventSource());
</script>
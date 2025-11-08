<template>
    <div></div>
</template>

<script setup lang="ts">
import { useMessage } from 'naive-ui'
import { onMounted, onUnmounted } from 'vue';
import { URL_SSE_NOTIFICATIONS } from '@/utils/utils';

const toast = useMessage();

let eventSource: EventSource | null = null;

const listenerNotificacoes = () => {
  eventSource = new EventSource(URL_SSE_NOTIFICATIONS);

  eventSource.onmessage = (event) => {
    const data = event.data;
    toast.info(data);
  };

  eventSource.onerror = (error) => {
    console.error("Error occurred:", error);
    if(eventSource != null)
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
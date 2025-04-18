<script setup lang="ts">
import { RouterView } from 'vue-router'
import { useUser } from '@/composables/user'
import { onMounted } from 'vue'

const { setToken } = useUser();

onMounted(() => setToken(localStorage.getItem('token') || undefined));
</script>

<template>
  <n-message-provider>
    <router-view />
  </n-message-provider>
</template>
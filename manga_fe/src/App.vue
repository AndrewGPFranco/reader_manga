<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <n-message-provider>
    <router-view />
  </n-message-provider>
</template>
<script setup lang="ts">
import { RouterView } from 'vue-router'
import { computed, onMounted } from 'vue'
import { useUser } from '@/composables/user'
import { useSystemStore } from '@/store/SystemStore'
import Notifications from '@/components/global/NotificationsComponent.vue'

const { setToken } = useUser()
const systemStore = useSystemStore()
const theme = computed(() => systemStore.theme)

onMounted(() => {
  setToken(localStorage.getItem('token') || undefined)
  systemStore.getLatestTheme()
})
</script>

<template>
  <n-config-provider :theme="theme">
    <n-notification-provider>
      <n-modal-provider>
        <n-message-provider>
          <Notifications />
          <section :style="theme !== null ? { backgroundColor: '#000' } : {}">
            <router-view />
          </section>
        </n-message-provider>
      </n-modal-provider>
    </n-notification-provider>
  </n-config-provider>
</template>
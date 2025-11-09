<template>
  <n-modal
      v-model:show="show"
      preset="card"
      :title="notifications.length > 0 ? `Notificações (${notifications.length})` : 'Notificações'"
      closable
      style="width: 450px; max-height: 80vh;"
      :segmented="{ content: 'soft' }"
  >
    <div class="notifications-container">
      <n-empty
          v-if="notifications.length === 0"
          description="Nenhuma notificação no momento"
          size="large"
      >
        <template #icon>
          <n-icon size="48" :component="BellIcon"/>
        </template>
      </n-empty>

      <n-scrollbar v-else style="max-height: 500px;">
        <div class="notif-list">
          <div
              v-for="not in notifications"
              :key="not.id"
              class="notif-item-wrapper"
          >
            <div class="notif-card">
              <img
                  v-if="not.imagemPath"
                  :src="not.imagemPath"
                  alt="Capa"
                  class="notif-img"
                  loading="lazy"
              />
              <div class="notif-content">
                <p class="notif-text">{{ not.content }}</p>
                <div class="notif-footer">
                  <n-tag
                      v-if="not.origin"
                      size="small"
                      :bordered="false"
                      class="notif-tag"
                  >
                    {{ not.origin }}
                  </n-tag>
                  <span class="notif-time">{{ formatDate(not.dataIn) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </n-scrollbar>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import {NIcon} from 'naive-ui';
import {formatDate} from "@/utils/utils";
import {computed, onMounted, ref, h} from "vue";
import type {iNotification} from "@/@types/iNotification";
import {useNotificationStore} from "@/store/NotificationStore";

const BellIcon = {
  render() {
    return h('svg', {
      xmlns: 'http://www.w3.org/2000/svg',
      viewBox: '0 0 24 24',
      fill: 'currentColor'
    }, [
      h('path', {
        d: 'M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z'
      })
    ]);
  }
};

const notifications = ref<iNotification[]>([]);
const notificationStore = useNotificationStore();

const props = defineProps({
  isShowNotifications: {
    type: Boolean,
    required: true
  }
});

const emit = defineEmits(['update:isShowNotifications']);

const show = computed({
  get: () => props.isShowNotifications,
  set: (value) => emit('update:isShowNotifications', value)
});

onMounted(async () => {
  const response = await notificationStore.getNotifications(0);

  response.content.forEach((item: any) => {
    const dadosExibicao = item.content.split(' / Capa: ');
    const mensagemParaExibicao = dadosExibicao[0];
    const imagemMangaParaAvatar = dadosExibicao[1];

    const data: iNotification = {
      id: item.id,
      content: mensagemParaExibicao,
      imagemPath: imagemMangaParaAvatar,
      dataIn: item.dataIn,
      origin: item.origin
    };

    notifications.value.push(data);
  });
});
</script>

<style scoped>
.notifications-container {
  padding: 4px 0;
}

.notif-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notif-item-wrapper {
  position: relative;
}

.notif-card {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  padding: 14px;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 10px;
  transition: all 0.2s ease;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.notif-card:hover {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(99, 226, 183, 0.2);
}

.notif-img {
  width: 70px;
  height: 70px;
  min-width: 70px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: transform 0.2s ease;
}

.notif-card:hover .notif-img {
  transform: scale(1.05);
}

.notif-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 0;
}

.notif-text {
  margin: 0;
  line-height: 1.5;
  color: rgba(255, 255, 255, 0.9);
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.notif-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.notif-tag {
  font-size: 11px;
  opacity: 0.8;
}

.notif-time {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
  white-space: nowrap;
}

:deep(.n-scrollbar-rail__scrollbar) {
  width: 6px;
}
</style>
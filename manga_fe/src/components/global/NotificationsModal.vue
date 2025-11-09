<template>
  <n-modal
      v-model:show="show"
      preset="card"
      closable
      style="width: 400px;"
  >
    <n-timeline>
      <n-timeline-item
          type="success"
          title="Success"
          content="success content"
          time="2018-04-03 20:46"
      />
    </n-timeline>
  </n-modal>
</template>

<script setup lang="ts">
import {computed} from "vue";

const props = defineProps({
  isShowNotifications: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:isShowNotifications'])

const show = computed({
  get: () => props.isShowNotifications,
  set: (value) => emit('update:isShowNotifications', value)
})
</script>
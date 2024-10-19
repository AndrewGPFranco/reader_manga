<template>
  <n-layout-sider bordered collapse-mode="width" :collapsed-width="64" :width="240" :collapsed="collapsed" show-trigger
    @collapse="collapsed = true" @expand="collapsed = false" position="absolute" top="0" left="0" height="100%" :aria-expanded="!collapsed">
    <n-menu :collapsed="collapsed" :collapsed-width="64" :collapsed-icon-size="30" :options="menuOptions"
      key-field="whateverKey" label-field="whateverLabel" children-field="whateverChildren" />
  </n-layout-sider>
</template>

<script lang="ts">
import type { Component } from 'vue'
import { defineComponent, h, ref } from 'vue'
import { NIcon } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import { ScanCircleOutline as ScanCircle, GridOutline as newManga } from '@vicons/ionicons5'
import { RouterLink } from 'vue-router'

function renderIcon(icon: Component) {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions: MenuOption[] = [
{
    whateverLabel: () =>
      h(RouterLink, { to: '/' }, { default: () => 'Home' }),
    icon: renderIcon(ScanCircle),
    path: '/'
  },
  {
    whateverLabel: () =>
      h(RouterLink, { to: '/register/manga' }, { default: () => 'Mangá Register' }),
    icon: renderIcon(newManga),
    path: '/register/manga'
  }
]

export default defineComponent({
  setup() {
    return {
      collapsed: ref(true),
      menuOptions
    }
  }
})
</script>
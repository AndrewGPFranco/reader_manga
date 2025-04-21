<template>
  <n-layout-sider
    bordered
    collapse-mode="width"
    :collapsed-width="250"
    width="17vw"
    position="absolute"
    top="0"
    left="0"
    height="100%"
    :aria-expanded="true"
  >
    <n-menu
      :collapsed-width="64"
      :collapsed-icon-size="30"
      :options="menuOptions"
      key-field="whateverKey"
      label-field="whateverLabel"
      children-field="whateverChildren"
    />
  </n-layout-sider>
</template>

<script lang="ts">
import type { Component } from 'vue'
import { defineComponent, h } from 'vue'
import { NIcon } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import {
  ScanCircleOutline as ScanCircle,
  BookOutline as Library,
  GridOutline as New,
  SettingsOutline as Management,
  Bookmark as Favorites,
  PersonOutline as Profile,
  NewspaperOutline as Mangas,
  CodeWorkingOutline as Jobs,
  HourglassOutline as ProgressReadings
} from '@vicons/ionicons5'
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/store/AuthStore'
import { VideocamOutline } from '@vicons/ionicons5/lib'

const renderIcon = (icon: Component) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

export default defineComponent({
  setup() {
    const auth = useAuthStore()
    const role = auth.getRoleUser()

    const menuOptions: MenuOption[] = [
      {
        whateverLabel: () => h(RouterLink, { to: '/' }, { default: () => 'Inicio' }),
        icon: renderIcon(ScanCircle),
        path: '/'
      },
      ...(role.includes('ADMIN')
        ? [
            {
              whateverLabel: () => h(RouterLink, { to: '/admin/jobs' }, { default: () => 'Jobs' }),
              icon: renderIcon(Jobs),
              path: '/admin/jobs'
            }
          ]
        : []),
      {
        whateverLabel: () => h(RouterLink, { to: '/anime' }, { default: () => 'Animes' }),
        icon: renderIcon(VideocamOutline),
        path: '/anime'
      },
      {
        whateverLabel: () => h(RouterLink, { to: '/mangas' }, { default: () => 'Mangás' }),
        icon: renderIcon(Mangas),
        path: '/mangas'
      },
      ...(role.includes('ADMIN')
        ? [
            {
              whateverLabel: () =>
                h(RouterLink, { to: '/register' }, { default: () => 'Registros' }),
              icon: renderIcon(New),
              path: '/register'
            }
          ]
        : []),
      {
        whateverLabel: () => h(RouterLink, { to: '/profile' }, { default: () => 'Meu perfil' }),
        icon: renderIcon(Profile),
        path: '/profile'
      },
      ...(role.includes('ADMIN')
        ? [
            {
              whateverLabel: () =>
                h(RouterLink, { to: '/management/admin' }, { default: () => 'Gerenciamento' }),
              icon: renderIcon(Management),
              path: '/management/admin'
            }
          ]
        : []),
      {
        whateverLabel: () =>
          h(RouterLink, { to: '/manga/all' }, { default: () => 'Minha biblioteca' }),
        icon: renderIcon(Library),
        path: '/manga/all'
      },
      {
        whateverLabel: () =>
          h(RouterLink, { to: '/manga/favorites' }, { default: () => 'Mangás favoritos' }),
        icon: renderIcon(Favorites),
        path: '/manga/favorites'
      },
      {
        whateverLabel: () =>
          h(RouterLink, { to: '/progress-readings' }, { default: () => 'Leituras em andamento' }),
        icon: renderIcon(ProgressReadings),
        path: '/progress-readings'
      }
    ]

    return {
      menuOptions
    }
  }
})
</script>

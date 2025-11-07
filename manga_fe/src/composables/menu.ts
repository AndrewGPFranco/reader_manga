import {ref} from 'vue'

const menuCollapsed = ref<boolean>(true)

export function useMenu() {
    const setMenuCollapsed = (collapsed: boolean) => {
        menuCollapsed.value = collapsed
        updateMenuMargin(collapsed)
    }

    const updateMenuMargin = (collapsed: boolean) => {
        const marginLeft = collapsed ? '48px' : '272px'
        document.documentElement.style.setProperty('--menu-margin-left', marginLeft)
    }

    if (typeof document !== 'undefined') {
        updateMenuMargin(menuCollapsed.value)
    }

    return {
        menuCollapsed,
        setMenuCollapsed
    }
}


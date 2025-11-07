import {ref} from 'vue'

const getInitialState = (): boolean => {
    if (typeof localStorage !== 'undefined') {
        const saved = localStorage.getItem('menuCollapsed')
        return saved !== null ? saved === 'true' : true
    }
    return true
}

const menuCollapsed = ref<boolean>(getInitialState())

export function useMenu() {
    const setMenuCollapsed = (collapsed: boolean) => {
        menuCollapsed.value = collapsed
        updateMenuMargin(collapsed)
        if (typeof localStorage !== 'undefined')
            localStorage.setItem('menuCollapsed', String(collapsed))
    }

    const updateMenuMargin = (collapsed: boolean) => {
        const marginLeft = collapsed ? '48px' : '272px'
        document.documentElement.style.setProperty('--menu-margin-left', marginLeft)
    }

    if (typeof document !== 'undefined')
        updateMenuMargin(menuCollapsed.value)

    return {
        menuCollapsed,
        setMenuCollapsed
    }
}


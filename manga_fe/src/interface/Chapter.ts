import type { PageData } from "./Page"

export default interface ChapterData {
    id: number
    title: string
    description: string
    numberPages: number
    pages: PageData[]
}
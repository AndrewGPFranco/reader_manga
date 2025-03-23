import type iPageData from '@/@types/Pagee'
import type { StatusType } from '@/enum/StatusType'

export class Chapter {
  readonly id: number
  readonly title: string
  readonly pages: iPageData[]
  readonly status: StatusType
  readonly numberPages: number
  readonly readingProgress: number

  constructor(
    id: number,
    title: string,
    numberPages: number,
    pages: iPageData[],
    status: StatusType,
    readingProgress: number
  ) {
    this.id = id
    this.title = title
    this.numberPages = numberPages
    this.pages = pages
    this.readingProgress = readingProgress
    this.status = status
  }
}

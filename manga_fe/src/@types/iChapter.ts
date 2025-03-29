import type iPageData from "./Pagee"
import { StatusType } from '@/enum/StatusType'

export default interface iChapterData {
    id: number;
    title: string;
    pages: iPageData[];
    status: StatusType;
    numberPages: number;
    readingProgress: number;
    urlImageManga?: string;
    nameManga?: string;
    numberPageOfPageable?: number;
}
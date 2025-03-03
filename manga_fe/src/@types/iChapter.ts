import type iPageData from "./Pagee"

export default interface iChapterData {
    id: number;
    title: string;
    description: string;
    numberPages: number;
    pages: iPageData[];
}
import { StatusType } from '@/enum/StatusType';
import type iChapterData from './iChapter';

export default interface iMangaData {
    id: number;
    title: string;
    description: string;
    size: number;
    creationDate: Date;
    endDate: Date;
    status: StatusType;
    author: string;
    gender: string;
    image: string;
    chapters: iChapterData[];
    favorite: boolean;
}
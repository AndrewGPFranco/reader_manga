import { StatusType } from '../enum/StatusType';
import type ChapterData from './Chapter';

export interface MangaData {
    id: number;
    title: string;
    description: string;
    size: number;
    creationDate: Date;
    closingDate: Date;
    status: StatusType;
    author: string;
    gender: string;
    image: string;
    chapters: ChapterData[];
}
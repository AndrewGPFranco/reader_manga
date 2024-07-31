import { StatusType } from '../enum/StatusType';

export interface MangaData {
    title: string;
    description: string;
    size: number;
    creationDate: Date;
    closingDate: Date;
    status: StatusType;
    author: string;
    gender: string;
    image: string;
}
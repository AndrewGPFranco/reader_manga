import type PageData from "@/interface/Page";

export class Chapter {

    readonly id: number;
    readonly title: string;
    readonly description: string;
    readonly numberPages: number;
    readonly pages: PageData[];

    constructor(id: number, title: string, description: string, numberPages: number, pages: PageData[]) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.numberPages = numberPages;
        this.pages = pages;
    }

}
import type iPageData from "@/interface/Pagee";

export class Chapter {

    readonly id: number;
    readonly title: string;
    readonly description: string;
    readonly numberPages: number;
    readonly pages: iPageData[];

    constructor(id: number, title: string, description: string, numberPages: number, pages: iPageData[]) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.numberPages = numberPages;
        this.pages = pages;
    }

}
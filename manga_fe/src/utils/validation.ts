import type { MangaData } from "@/interface/Manga";

export function validationFields(data: MangaData): string | boolean{
    if(data.title === "") {
        return "Field title is required";
    }

    if(data.description === "") {
        return "Field description is required";
    }

    if(data.size === 0) {
        return "Field size is required";
    }

    if(data.creationDate === null) {
        return "Field creationDate is required";
    }

    if(data.closingDate === null) {
        return "Field closingDate is required";
    }

    if(data.status === null) {
        return "Field status is required";
    }

    if(data.author === "") {
        return "Field author is required";
    }

    if(data.gender === "") {
        return "Field gender is required";
    }

    if(data.image === "") {
        return "Field image is required";
    }

    return true;
}
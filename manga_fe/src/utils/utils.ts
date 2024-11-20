import type MangaData from "@/interface/Manga";

export function formatDate(date: MangaData): string {
    const creationDate = new Date(date.creationDate);
    return creationDate.toLocaleDateString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
    });
}
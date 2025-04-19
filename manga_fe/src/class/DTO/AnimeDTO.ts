class AnimeDTO {
    readonly file: File;
    readonly id: string;
    readonly title: string;

    constructor(file: File, id: string, title: string) {
        this.id = id;
        this.file = file;
        this.title = title;
    }
}

export default AnimeDTO;
/**
 * Classe responsável por centralizar os dados necessários para diversas ações no sistema envolvendo o usuário da sessão.
 * Não sendo obrigatório o envio de todos os dados, somente o que a operação necessitar.
 */
export class UserData {

    readonly idUser: string;
    readonly idManga: number;
    readonly idChapter: number;
    readonly idPage: number;

    constructor(idUser: string, idManga: number, idChapter: number, idPage: number) {
        this.idUser = idUser;
        this.idManga = idManga;
        this.idChapter = idChapter;
        this.idPage = idPage;
    }

}
export function formatDate(date: string) {
    const creationDate = new Date(date);
    return creationDate.toLocaleDateString('pt-BR');
}
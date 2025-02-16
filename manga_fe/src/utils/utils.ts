export function formatDate(date: Date) {
    const creationDate = new Date(date);
    return creationDate.toLocaleDateString('pt-BR');
}
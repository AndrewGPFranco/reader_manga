export function formatDate(date: Date): string {
    const creationDate: Date = new Date(date);
    return creationDate.toLocaleDateString('pt-BR');
}
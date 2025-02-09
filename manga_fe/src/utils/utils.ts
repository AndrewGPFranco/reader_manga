export function formatDate(date: Date): string {
    const creationDate = new Date(date);
    return creationDate.toLocaleDateString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
    });
}
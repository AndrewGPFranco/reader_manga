export function formatDate(date: Date): string {
  const creationDate: Date = new Date(date)
  return creationDate.toLocaleDateString('pt-BR')
}

export const URL_SSE: string = 'http://localhost:8080/api/v1/job/sse'
export function formatDate(data: Date): string {
  let dataFormatada = new Date(data + 'T00:00:00').toLocaleString('pt-BR', {
    day: 'numeric',
    month: 'short',
    year: 'numeric'
  })

  if (dataFormatada === 'Invalid Date') {
    dataFormatada = new Date(data).toLocaleString('pt-BR', {
      day: 'numeric',
      month: 'short',
      year: 'numeric'
    })
  }

  return dataFormatada;
}

export const URL_SSE: string = 'http://localhost:8080/api/v1/job/sse'
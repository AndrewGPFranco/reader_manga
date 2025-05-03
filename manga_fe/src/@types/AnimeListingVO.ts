import type { EpisodeToAnimesVO } from "./iEpisodeToAnimesVO"

export interface AnimeListingVO {
  idAnime: number
  note: number
  uriImage: string
  titleAnime: string
  launchYear: Date
  isFavorite: boolean
  tags: Array<string>
  episodes: Array<EpisodeToAnimesVO>
}
package com.reader.manga.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.reader.manga.dto.manga.GetMangaDTO;
import com.reader.manga.dto.manga.MangaDTO;
import com.reader.manga.dto.manga.UpdateMangaDTO;
import com.reader.manga.dto.utils.UserData;
import com.reader.manga.exception.CreationErrorException;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.mapper.MangaMapper;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.vo.CoversMangaVO;
import com.reader.manga.vo.MangaCoverVO;
import com.reader.manga.vo.MangaUserVO;
import com.reader.manga.vo.UserMangaVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MangaService {

    private final MangaMapper mangaMapper;
    private final MangaRepository repository;
    private final UserMangaService userMangaService;

    private static final String MANGA_NAO_ENCONTRADO = "Mangá não encontrado.";

    public Map<String, CoversMangaVO> obtemMangasFamosos() {
        List<Manga> todosMangas = repository.findAll();
        Set<String> nomeDosMangasRegistrados = todosMangas.stream()
                .map(m -> m.getTitle().toLowerCase())
                .collect(Collectors.toSet());

        Map<String, CoversMangaVO> coversMangaVOMap = new HashMap<>();

        Map<String, CoversMangaVO> coversManga = new HashMap<>();
        coversManga.put("Naruto", new CoversMangaVO("Naruto", "https://m.media-amazon.com/images/I/91xUwI2UEVL._AC_UF894,1000_QL80_.jpg", "https://mangaplus.shueisha.co.jp/titles/100018"));
        coversManga.put("Demon Slayer: Kimetsu no Yaiba", new CoversMangaVO("Demon Slayer","https://http2.mlstatic.com/D_NQ_NP_942681-MLU50423106087_062022-O.webp", "https://mangaplus.shueisha.co.jp/titles/100009"));
        coversManga.put("JoJo's Bizarre Adventure Parte 1", new CoversMangaVO("Jojo's", "https://m.media-amazon.com/images/I/91XRYa+4cHL._AC_UF1000,1000_QL80_.jpg", "https://mangaplus.shueisha.co.jp/titles/100016"));
        coversManga.put("My Hero Academia", new CoversMangaVO("My Hero Academia", "https://m.media-amazon.com/images/I/71bELfIWTDL._AC_UF1000,1000_QL80_.jpg", "https://mangaplus.shueisha.co.jp/titles/500005"));
        coversManga.put("One Piece", new CoversMangaVO("One Piece", "https://m.media-amazon.com/images/I/716EGgqzyOL._AC_UF1000,1000_QL80_.jpg", "https://mangaplus.shueisha.co.jp/titles/100149"));
        coversManga.put("Hunter x Hunter", new CoversMangaVO("Hunter x Hunter", "https://www.jbchost.com.br/editorajbc/wp-content/uploads/2008/01/hunterxhunter-01-capaaz.jpg", "https://mangaplus.shueisha.co.jp/titles/100015"));
        coversManga.put("Bungo Stray Dogs", new CoversMangaVO("Bungo Stray Dogs", "https://m.media-amazon.com/images/I/81zJTGwXrtL._AC_UF1000,1000_QL80_.jpg", "https://slimeread.com/manga/370/bungou-stray-dogs"));
        coversManga.put("Boruto: Two Blue Vortex", new CoversMangaVO("Boruto", "https://m.media-amazon.com/images/I/81HpeSpReJL._AC_UF1000,1000_QL80_.jpg", "https://mangaplus.shueisha.co.jp/titles/100006"));
        coversManga.put("Tokyo Revengers", new CoversMangaVO("Tokyo Revengers", "https://m.media-amazon.com/images/I/711RqaljbIL.jpg", "https://mangaonline.biz/manga/tokyo-revengers/"));
        coversManga.put("Record of Ragnarok", new CoversMangaVO("Record of Ragnarok", "https://m.media-amazon.com/images/I/91ifr0L+XrL.jpg", "https://mangaonline.biz/manga/shuumatsu-no-walkure/"));
        coversManga.put("Dragon Ball", new CoversMangaVO("Dragon Ball", "https://m.media-amazon.com/images/I/81fHfEpEHTL._AC_UF1000,1000_QL80_.jpg", "https://mangaplus.shueisha.co.jp/titles/100011"));
        coversManga.put("Hellsing", new CoversMangaVO("Hellsing", "https://m.media-amazon.com/images/I/71KIyHsciwL._AC_UF1000,1000_QL80_.jpg", "https://slimeread.com/manga/12166,Hellsing"));
        coversManga.put("Noragami", new CoversMangaVO("Noragami", "https://m.media-amazon.com/images/I/91f63co2jKL._AC_UF1000,1000_QL80_.jpg", "https://slimeread.com/manga/3759/noragami"));
        coversManga.put("The Rising of the Shield Hero", new CoversMangaVO("The Rising of the Shield Hero", "https://m.media-amazon.com/images/I/71szZSLOYGL._AC_UF894,1000_QL80_.jpg", "https://slimeread.com/manga/8417/the-rising-of-the-shield-hero"));

        coversManga.forEach((title, cover) -> coversMangaVOMap.put(title, CoversMangaVO.builder()
                .titulo(title)
                .urlImage(cover.getUrlImage())
                .urlReader(nomeDosMangasRegistrados.contains(title.toLowerCase()) ? null : cover.getUrlReader())
                .build()));

        return coversMangaVOMap;
    }


    public GetMangaDTO createManga(MangaDTO dto) {
        try {
            Manga manga = new Manga(dto.title(), dto.description(), dto.size(), dto.creationDate(), dto.closingDate(), dto.status(), dto.gender(), dto.author(),  dto.image());
            Manga savedManga = repository.save(manga);
            return new GetMangaDTO(savedManga.getId(), savedManga.getTitle(), savedManga.getDescription(), savedManga.getSize(), savedManga.getCreationDate(), savedManga.getClosingDate(), savedManga.getStatus(), savedManga.getGender(), savedManga.getAuthor(), savedManga.getImage());
        }catch (Exception e) {
            throw new CreationErrorException("Error creating Manga. Please try again... " + e.getMessage());
        }
    }

    public void deleteManga(UserData userData) {
        Optional<Manga> mangaById = repository.findById(userData.idManga());
        if(mangaById.isEmpty()){
            throw new NotFoundException("No manga found with the id: " + userData.idManga() + ".");
        }
        userMangaService.deleteAssociacaoUserMangaFavorite(userData.idUser(), userData.idManga());
        userMangaService.deleteAssociacaoUserManga(userData.idUser(), userData.idManga());
        repository.deleteById(userData.idManga());
    }

    public Set<MangaUserVO> readAllMangas(Long idUser) {
        UserMangaVO todosMangasDoUsuario = userMangaService.getTodosMangasDoUsuario(idUser);
        List<Manga> todosMangasDoSistema = repository.findAll();

        Set<MangaUserVO> listaParaRetornar = new HashSet<>();

        for (Manga manga : todosMangasDoSistema) {
            MangaUserVO mangaAtualizado = null;
            for (MangaUserVO mangaUserVO : todosMangasDoUsuario.getMangaList()) {
                if (mangaUserVO.title().equals(manga.getTitle())) {
                    mangaAtualizado = mangaMapper.mangaToMangaUserVO(manga, true);
                    break;
                }
            }

            if (mangaAtualizado != null)
                listaParaRetornar.add(mangaAtualizado);
            else
                listaParaRetornar.add(mangaMapper.mangaToMangaUserVO(manga, false));
        }

        return listaParaRetornar;
    }

    public void updateManga(Long id, UpdateMangaDTO dto) {
        Manga manga = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manga not found"));

        UtilsService.updateField(dto.title(), manga::setTitle);
        UtilsService.updateField(dto.description(), manga::setDescription);
        UtilsService.updateField(dto.size(), manga::setSize);
        UtilsService.updateField(dto.creationDate(), manga::setCreationDate);
        UtilsService.updateField(dto.closingDate(), manga::setClosingDate);
        UtilsService.updateField(dto.status(), manga::setStatus);
        UtilsService.updateField(dto.author(), manga::setAuthor);
        UtilsService.updateField(dto.gender(), manga::setGender);
        UtilsService.updateField(dto.image(), manga::setImage);

        repository.save(manga);
    }

    public List<Manga> getAll() {
        return repository.findAll();
    }

    public Manga findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(MANGA_NAO_ENCONTRADO));
    }

    public Manga findByTitle(String titulo) {
        return repository.findByTitle(titulo).orElseThrow(() -> new NotFoundException(MANGA_NAO_ENCONTRADO));
    }

    public List<Chapter> getChaptersByManga(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(MANGA_NAO_ENCONTRADO)).getChapters();
    }

    public Mono<MangaCoverVO> fetchCoverForTitle(String title, WebClient webClient) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/manga")
                        .queryParam("title", title)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(jsonNode -> {
                    JsonNode data = jsonNode.get("data");
                    if (data != null && !data.isEmpty()) {
                        JsonNode manga = data.get(0);
                        String mangaId = manga.get("id").asText();
                        return fetchCoverImage(mangaId, webClient);
                    }
                    return Mono.empty();
                });
    }

    private Mono<MangaCoverVO> fetchCoverImage(String mangaId, WebClient webClient) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cover")
                        .queryParam("manga[]", mangaId)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> {
                    JsonNode data = jsonNode.get("data");
                    if (data != null && !data.isEmpty()) {
                        JsonNode cover = data.get(0);
                        String fileName = cover.get("attributes").get("fileName").asText();
                        String imageUrl = "https://uploads.mangadex.org/covers/" + mangaId + "/" + fileName;

                        return MangaCoverVO.builder().id(mangaId).imageUrl(imageUrl).build();
                    }
                    return null;
                });
    }

    public List<Map.Entry<String, CoversMangaVO>> getRandomCovers(int max) {
        List<Map.Entry<String, CoversMangaVO>> covers = new ArrayList<>(obtemMangasFamosos().entrySet());
        Collections.shuffle(covers);
        return covers.stream().limit(max).toList();
    }

    public List<String> getApenasNomeDosMangas() {
        List<Manga> todosMangas = repository.findAll();
        List<String> nomeDosMangas = new ArrayList<>(todosMangas.size());

        todosMangas.forEach(manga -> nomeDosMangas.add(manga.getTitle()));

        return nomeDosMangas;
    }

}
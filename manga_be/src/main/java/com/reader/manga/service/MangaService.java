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

@Service
@RequiredArgsConstructor
public class MangaService {

    private final MangaMapper mangaMapper;
    private final MangaRepository repository;
    private final UserMangaService userMangaService;

    static Map<String, CoversMangaVO> coversManga = new HashMap<>();

    static {
        // Manga Covers
        coversManga.put("Naruto", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/91xUwI2UEVL._AC_UF894,1000_QL80_.jpg")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/100018")
                .build());

        coversManga.put("Demon Slayer", CoversMangaVO.builder()
                .urlImage("https://http2.mlstatic.com/D_NQ_NP_942681-MLU50423106087_062022-O.webp")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/100009")
                .build());

        coversManga.put("Jojo's", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/91XRYa+4cHL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/100016")
                .build());

        coversManga.put("My Hero Academia", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/71bELfIWTDL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/500005")
                .build());

        coversManga.put("One Piece", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/716EGgqzyOL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/100149")
                .build());

        coversManga.put("Hunter x Hunter", CoversMangaVO.builder()
                .urlImage("https://www.jbchost.com.br/editorajbc/wp-content/uploads/2008/01/hunterxhunter-01-capaaz.jpg")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/100015")
                .build());

        coversManga.put("Bungo Stray Dogs", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/81zJTGwXrtL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://slimeread.com/manga/370/bungou-stray-dogs")
                .build());

        coversManga.put("Boruto", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/81HpeSpReJL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/100006")
                .build());

        coversManga.put("Tokyo Revengers", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/711RqaljbIL.jpg")
                .urlReader("https://mangaonline.biz/manga/tokyo-revengers/")
                .build());

        coversManga.put("Record of Ragnarok", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/91ifr0L+XrL.jpg")
                .urlReader("https://mangaonline.biz/manga/shuumatsu-no-walkure/")
                .build());

        coversManga.put("Dragon Ball", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/81fHfEpEHTL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://mangaplus.shueisha.co.jp/titles/100011")
                .build());

        coversManga.put("Hellsing", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/71KIyHsciwL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://slimeread.com/manga/12166,Hellsing")
                .build());

        coversManga.put("Noragami", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/91f63co2jKL._AC_UF1000,1000_QL80_.jpg")
                .urlReader("https://slimeread.com/manga/3759/noragami")
                .build());

        coversManga.put("The Rising of the Shield Hero", CoversMangaVO.builder()
                .urlImage("https://m.media-amazon.com/images/I/71szZSLOYGL._AC_UF894,1000_QL80_.jpg")
                .urlReader("https://slimeread.com/manga/8417/the-rising-of-the-shield-hero")
                .build());
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
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Mangá not found"));
    }

    public List<Chapter> getChaptersByManga(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Mangá not found")).getChapters();
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
        List<Map.Entry<String, CoversMangaVO>> covers = new ArrayList<>(coversManga.entrySet());
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
package com.reader.manga.domain.job.manga;

import com.fasterxml.jackson.databind.JsonNode;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.job.base.ColetorBaseFonte;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.ports.repositories.MangaRepository;
import com.reader.manga.domain.valueobjects.jobs.manga.MangaJobVOI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Coletor responsável por obter dados de mangás para o sistema.
 * @author andrewgo
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class ColetorMangaFonteI extends ColetorBaseFonte<MangaJobVOI> {

    private final WebClient webClient;
    private final MangaRepository repository;
    private static final String EN_US = "en_us";
    private static final String EN_JP = "en_jp";

    @Override
    public Mono<MangaJobVOI> executa(Object manga) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/edge/manga")
                        .queryParam("filter[text]", manga)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(jsonNode -> log.error("Resposta da API: {}", jsonNode.get("data").get(0)))
                .flatMap(jsonNode -> {
                    JsonNode dataArray = jsonNode.get("data");
                    if (dataArray != null && dataArray.isArray() && !dataArray.isEmpty()) {
                        JsonNode atributos = dataArray.get(0).get("attributes");
                        if (atributos != null) {
                            MangaJobVOI mangaJobVO = deseralizarObjeto(atributos);
                            salvaDadosNoBanco(mangaJobVO);
                            return Mono.just(mangaJobVO);
                        }
                    }
                    return Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("Erro ao processar a requisição: {}", e.getMessage());
                    return Mono.empty();
                });
    }
    
    @Override
    public void salvaDadosNoBanco(MangaJobVOI vo) {
        Manga manga = new Manga(vo.getTitle(), vo.getDescription(), vo.getSize(), vo.getCreationDate(),
                vo.getClosingDate(), vo.getStatus(), vo.getGender() != null ? vo.getGender() : "Não informado",
                vo.getAuthor() != null ? vo.getAuthor() : "Não informado", vo.getImage());

        repository.save(manga);
    }

    @Override
    public MangaJobVOI deseralizarObjeto(JsonNode atributos) {
        String statusStr = atributos.get("status").asText().toUpperCase();

        String title = getTituloDoManga(atributos);
        String description = atributos.get("description").asText();
        String creationDate = atributos.get("startDate").asText();
        String cloasing = atributos.get("endDate").asText();
        StatusType status = !statusStr.equals("CURRENT") ? StatusType.valueOf(statusStr) : StatusType.ONGOING;
        String image = atributos.get("posterImage").get("small").asText();
        Integer size = atributos.get("chapterCount").asInt() != 0 ? atributos.get("chapterCount").asInt() : 1;

        return MangaJobVOI.builder().title(title).description(description).status(status)
                .creationDate(formataData(creationDate)).closingDate(
                        !cloasing.equals("null") ? formataData(cloasing) : null)
                .image(image).size(size).build();
    }

    private String getTituloDoManga(JsonNode atributos) {
        JsonNode titles = atributos.get("titles");

        if(titles.get("en") != null && !titles.get("en").asText().equals("null"))
            return titles.get("en").asText();
        else if(titles.get(EN_US) != null && !titles.get(EN_US).asText().equals("null"))
            return titles.get(EN_US).asText();
        else if(titles.get(EN_JP) != null && !titles.get(EN_JP).asText().equals("null"))
            return titles.get(EN_JP).asText();

        return "Título indisponível";
    }

}

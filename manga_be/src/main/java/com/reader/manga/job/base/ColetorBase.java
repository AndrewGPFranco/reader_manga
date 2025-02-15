package com.reader.manga.job.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.reader.manga.interfaces.DadosManga;
import com.reader.manga.vo.job.manga.MangaJobVO;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe base para padronizar todos os coletores no sistema.
 * @author andrewgo
 * @param <T> classe que implementa DadosManga.
 */
@Log4j2
public abstract class ColetorBase <T> implements DadosManga {

    protected abstract Mono<MangaJobVO> executa(String manga);

    protected abstract void salvaDadosNoBanco(T dados);

    protected abstract T deseralizarObjeto(JsonNode atributos);

    protected Date formataData(String data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(data);
        } catch (ParseException e) {
            log.error("Houve um erro ao realizar o parse da data: {}", data);
            throw new RuntimeException("Houve um erro ao realizar o parse da data!");
        }
    }

}

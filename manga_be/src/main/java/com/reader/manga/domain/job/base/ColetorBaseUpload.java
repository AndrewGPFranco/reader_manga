package com.reader.manga.domain.job.base;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class ColetorBaseUpload {

    protected abstract void executa(MultipartFile file, String... varargs) throws IOException;

}

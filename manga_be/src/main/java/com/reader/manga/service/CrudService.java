package com.reader.manga.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class CrudService<DTO, GETDTO, ENTITY, REPO extends JpaRepository> {

    protected GETDTO create(DTO dto, ENTITY entidade, String msgError, REPO repository, GETDTO getdto) {
        try {
            ENTITY entity = entidade;
            ENTITY saved = (ENTITY) repository.save(entity);
            return getdto;
        } catch (Exception e) {
            throw new RuntimeException(msgError);
        }
    }

}
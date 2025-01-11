package com.reader.manga.utils;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Classe centralizadora da configuração necessária para rodar testes com um banco real PostgreSQL.
 */
@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/reeadertest",
        "spring.datasource.username=andrew",
        "spring.datasource.password=bibia",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AbstractTests {}

package com.reader.manga;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class MangaApplicationTests {

	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("reeadertest")
			.withUsername("andrew")
			.withPassword("bibia");

	static {
		postgreSQLContainer.start();
		System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
		System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
		System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
	}

	@Test
	void contextLoads() {
	}

}

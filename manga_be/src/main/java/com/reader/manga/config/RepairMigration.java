package com.reader.manga.config;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;

@Configuration
public class RepairMigration {
    protected final Logger log = LoggerFactory.getLogger(RepairMigration.class);

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.repair();
            log.info("Migrations repair");
            flyway.migrate();
            log.info("Migrations migrate");
        };
    }
}
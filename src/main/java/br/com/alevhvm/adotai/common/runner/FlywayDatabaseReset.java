package br.com.alevhvm.adotai.common.runner;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

@Component
public class FlywayDatabaseReset {


    private final Flyway flyway;

    public FlywayDatabaseReset(Flyway flyway) {
        this.flyway = flyway;
    }

    @PostConstruct
    public void resetDatabase() {
        System.out.println("Limpando e recriando o banco de dados via Flyway");
        flyway.clean();
        flyway.migrate();
    }
}

package net.nemerosa.ontrack.neo4j.migration;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;

@SpringBootApplication
public class Migration extends Neo4jConfiguration {

    @Autowired
    private MigrationProperties migrationProperties;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Migration.class);
        application.run(args);
    }

    @Override
    public Neo4jServer neo4jServer() {
        return new RemoteServer(
                migrationProperties.getNeo4j().getUrl(),
                migrationProperties.getNeo4j().getUsername(),
                migrationProperties.getNeo4j().getPassword()
        );
    }

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory("net.nemerosa.ontrack.neo4j.domain");
    }

}

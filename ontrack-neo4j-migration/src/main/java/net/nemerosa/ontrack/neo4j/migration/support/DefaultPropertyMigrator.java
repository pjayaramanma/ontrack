package net.nemerosa.ontrack.neo4j.migration.support;

import com.fasterxml.jackson.databind.JsonNode;
import net.nemerosa.ontrack.json.JsonUtils;
import net.nemerosa.ontrack.neo4j.migration.CypherUtils;
import net.nemerosa.ontrack.neo4j.migration.Entity;
import net.nemerosa.ontrack.neo4j.migration.PropertyMigrator;
import org.springframework.data.neo4j.template.Neo4jOperations;

import java.io.IOException;
import java.util.Map;

public class DefaultPropertyMigrator implements PropertyMigrator {

    public static final PropertyMigrator INSTANCE = new DefaultPropertyMigrator();

    @Override
    public void migrate(String type, JsonNode data, Entity entity, Neo4jOperations template) throws IOException {
        Map<String, ?> params = JsonUtils.toMap(data);
        String cypherQuery = String.format(
                " MATCH (e: `%s` {id: %d}) " +
                        "CREATE (e)-[:HAS_PROPERTY]->(p: `%s` {%s})",
                entity.getType().getNodeName(),
                entity.getId(),
                type,
                CypherUtils.getCypherParameters(params)
        );
        template.query(
                cypherQuery,
                params
        );
    }
}
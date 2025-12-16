package ps.demo.zglj.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
public class DatabaseInitializer {

    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    @Transactional
    public void initializeDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            // Execute schema scripts
            executeScript(connection, "sql/schema/shedlock.ddl");
            //executeScript(connection, "sql/schema/02_create_indexes.sql");

        } catch (SQLException e) {
            log.error("Database initialization failed", e);
            throw new RuntimeException("Database initialization error", e);
        }
    }

    private void executeScript(Connection connection, String scriptPath) {
        try {
            log.info("Executing SQL script: {}", scriptPath);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptPath));
            log.info("Successfully executed: {}", scriptPath);
        } catch (Exception e) {
            log.error("Failed to execute script: {}", scriptPath, e);
            // Handle based on requirements - throw or continue
        }
    }
}

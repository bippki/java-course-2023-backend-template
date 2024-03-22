package edu.java;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class SimpleIntegrationTest extends IntegrationTest {
    @Test
    public void assertThatTablesExists() {
        try (Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );
             Statement statement = connection.createStatement()) {
            ResultSet tableTelegramChatExists = statement.executeQuery(
                "SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'telegram_chat');");
            tableTelegramChatExists.next();
            assertTrue(tableTelegramChatExists.getBoolean(1));

            ResultSet tableLinkExists = statement.executeQuery(
                "SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'link');");
            tableLinkExists.next();
            assertTrue(tableLinkExists.getBoolean(1));

            ResultSet tableAssignmentExists = statement.executeQuery(
                "SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'assignment');");
            tableAssignmentExists.next();
            assertTrue(tableAssignmentExists.getBoolean(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

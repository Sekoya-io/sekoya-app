package test.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Override de la version fournie par Igloo pour forcer à considérer l'image comme compatible pour
 * la fourniture du service postgres ({@link DockerImageName#asCompatibleSubstituteFor(String)}).
 */
@Configuration
public class PostgisTestContainerConfiguration {

  @Value("${testContainer.database.name}")
  String databaseName;

  @Value("${testContainer.database.userName}")
  String username;

  @Value("${testContainer.database.password}")
  String password;

  @Value("${testContainer.database.exposedPorts}")
  String exposedPorts;

  @Value("${testContainer.database.dockerImageName}")
  String dockerImageName;

  @Bean
  @ServiceConnection
  PostgreSQLContainer postgreSQLContainer() {
    try (PostgreSQLContainer container =
        new PostgreSQLContainer(
            DockerImageName.parse(dockerImageName).asCompatibleSubstituteFor("postgres"))) {
      return container
          .withDatabaseName(databaseName)
          .withUsername(username)
          .withPassword(password)
          .withExposedPorts(Integer.parseInt(exposedPorts))
          .withReuse(true);
    }
  }
}

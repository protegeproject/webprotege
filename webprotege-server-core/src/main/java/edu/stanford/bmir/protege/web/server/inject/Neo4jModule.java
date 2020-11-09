package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoCredential;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.neo4j.driver.Driver;

import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName.NEO4J_AUTH_PASSWORD;
import static edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName.NEO4J_AUTH_USERNAME;
import static edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName.NEO4J_HOST;
import static edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName.NEO4J_PORT;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Module
public class Neo4jModule {

  @Provides
  @Neo4jHost
  public String provideNeo4jHost(WebProtegeProperties webProtegeProperties) {
    return webProtegeProperties.getNeo4jHomeDir().orElse(NEO4J_HOST.getDefaultValue().get());
  }

  @Provides
  @Neo4jPort
  public int provideNeo4jPortNumber(WebProtegeProperties webProtegeProperties) {
    return webProtegeProperties.getNeo4jPort().orElse(Integer.parseInt(NEO4J_PORT.getDefaultValue().get()));
  }

  @Provides
  @Neo4jUsername
  public String provideNeo4jUserName(WebProtegeProperties webProtegeProperties) {
    return webProtegeProperties.getNeo4jUserName().orElse(NEO4J_AUTH_USERNAME.getDefaultValue().get());
  }

  @Provides
  @Neo4jPassword
  public String provideNeo4jPassword(WebProtegeProperties webProtegeProperties) {
    return webProtegeProperties.getNeo4jPassword().orElse(NEO4J_AUTH_PASSWORD.getDefaultValue().get());
  }

  @Provides
  @ApplicationSingleton
  public Driver provideNeo4jDriver(Neo4jDriverProvider provider) {
    return provider.get();
  }
}

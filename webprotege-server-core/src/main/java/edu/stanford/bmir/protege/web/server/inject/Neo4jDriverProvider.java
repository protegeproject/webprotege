package edu.stanford.bmir.protege.web.server.inject;

import javax.inject.Inject;
import javax.inject.Provider;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@ApplicationSingleton
public class Neo4jDriverProvider implements Provider<Driver> {

  @Nonnull
  private final String hostname;

  @Nonnull
  private final int portNumber;

  @Nonnull
  private final String username;

  @Nonnull
  private final String password;

  @Inject
  public Neo4jDriverProvider(@Nonnull @Neo4jHost String hostname,
                             @Neo4jPort int portNumber,
                             @Nonnull @Neo4jUsername String username,
                             @Nonnull @Neo4jPassword String password) {
    this.hostname = checkNotNull(hostname);
    this.portNumber = checkNotNull(portNumber);
    this.username = checkNotNull(username);
    this.password = checkNotNull(password);
  }

  @Nonnull
  @Override
  public Driver get() {
    return GraphDatabase.driver("bolt://" + hostname + ":" + portNumber,
        AuthTokens.basic(username, password));
  }
}

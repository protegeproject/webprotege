package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
public class MongoCredentialProvider implements Provider<Optional<MongoCredential>> {

    private static final Logger logger = LoggerFactory.getLogger(MongoCredentialProvider.class);

    @Nonnull
    private final String dbUserName;

    @Nonnull
    private final String authenticationDatabase;

    @Nonnull
    private final char [] dbPassword;

    @Inject
    public MongoCredentialProvider(@Nonnull @DbUsername String dbUserName,
                                   @Nonnull @DbAuthenticationSource String authenticationDatabase,
                                   @Nonnull @DbPassword char[] dbPassword) {
        this.dbUserName = checkNotNull(dbUserName);
        this.authenticationDatabase = checkNotNull(authenticationDatabase);
        this.dbPassword = Arrays.copyOf(checkNotNull(dbPassword), dbPassword.length);
    }

    @Override
    public Optional<MongoCredential> get() {
        if(dbUserName.isEmpty()) {
            return Optional.empty();
        }
        if(dbPassword.length == 0) {
            return Optional.empty();
        }
        if(authenticationDatabase.isEmpty()) {
            logger.info("No authentication source (database) has been supplied for the MongoDB client connection.  Will attempt to connect without authentication.");
            return Optional.empty();
        }
        logger.info("A username, password and authentication source database name have been supplied for MongoDB client connection authentication.");
        var credential = MongoCredential.createCredential(dbUserName, authenticationDatabase, dbPassword);
        return Optional.of(credential);
    }
}

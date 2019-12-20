package edu.stanford.bmir.protege.web.server.cmdline;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.api.ApiKeyHasher;
import edu.stanford.bmir.protege.web.server.api.ApiKeyManager;
import edu.stanford.bmir.protege.web.server.api.UserApiKeyStoreImpl;
import edu.stanford.bmir.protege.web.server.collection.CollectionIdConverter;
import edu.stanford.bmir.protege.web.server.color.ColorConverter;
import edu.stanford.bmir.protege.web.server.form.FormIdConverter;
import edu.stanford.bmir.protege.web.server.persistence.*;
import edu.stanford.bmir.protege.web.server.tag.TagIdConverter;
import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.io.Console;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class GenerateApiKeyCmd extends Cmd {

    public GenerateApiKeyCmd() {
        super("generate-api-key", "Generates an API key for a user");
    }

    @Override
    public void run(@Nonnull List<String> args) {
        Console console = System.console();
        MongoClient mongoClient = WebProtegeCli.getMongoClient();
        MorphiaProvider morphiaProvider = new MorphiaProvider(
                new UserIdConverter(),
                new OWLEntityConverter(new OWLDataFactoryImpl()),
                new ProjectIdConverter(),
                new ThreadIdConverter(),
                new CommentIdConverter(),
                new CollectionIdConverter(), new FormIdConverter(), new TagIdConverter(), new ColorConverter());
        Morphia morphia = morphiaProvider.get();
        Datastore datastore = morphia.createDatastore(mongoClient, "webprotege");
        ApiKeyManager keyManager = new ApiKeyManager(new ApiKeyHasher(), new UserApiKeyStoreImpl(datastore));

        console.printf("Please enter the user name that they key will be generated from\n");
        String userName = console.readLine();
        console.printf("Please enter a purpose for the API key\n");
        String purpose = console.readLine();

        console.printf("Generating api key...");

        ApiKey key = keyManager.generateApiKeyForUser(UserId.getUserId(userName.trim()), purpose.trim());
        mongoClient.close();

        console.printf("\n");
        console.printf("Generated API key:\n\n");
        console.printf("    %s\n\n", key.getKey());
        console.printf("Please keep this API key safe.  Treat it as a password.\n");
        console.printf("You should NOT distribute it or store it in version control repositories.\n");
        console.printf("This API key cannot be recovered.  You will need to generate a new key if you lose this one.\n");
    }
}

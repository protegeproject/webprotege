package edu.stanford.bmir.protege.web.server.cmdline;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.access.*;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.server.inject.MongoCredentialProvider;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.color.ColorConverter;
import edu.stanford.bmir.protege.web.server.tag.TagIdConverter;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.server.collection.CollectionIdConverter;
import edu.stanford.bmir.protege.web.server.filemanager.ConfigDirectorySupplier;
import edu.stanford.bmir.protege.web.server.filemanager.ConfigInputStreamSupplier;
import edu.stanford.bmir.protege.web.server.form.FormIdConverter;
import edu.stanford.bmir.protege.web.server.inject.MongoClientProvider;
import edu.stanford.bmir.protege.web.server.inject.WebProtegePropertiesProvider;
import edu.stanford.bmir.protege.web.server.persistence.*;
import edu.stanford.bmir.protege.web.server.user.UserRecord;
import edu.stanford.bmir.protege.web.server.user.UserRecordConverter;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import edu.stanford.bmir.protege.web.shared.access.BuiltInRole;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.cmdline.WebProtegeCli.getMongoClient;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2017
 */
public class SetupTools {

    private static final String DB_NAME = "webprotege";

    @Nonnull
    private final UserRecordRepository userRecordRepository;

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final Console console;

    public SetupTools(@Nonnull UserRecordRepository userRecordRepository,
                      @Nonnull AccessManager accessManager,
                      @Nonnull Console console) {
        this.userRecordRepository = checkNotNull(userRecordRepository);
        this.accessManager = checkNotNull(accessManager);
        this.console = checkNotNull(console);
    }

    public void createAdministratorAccount() throws IOException {
        console.printf("Please enter a user name for the administrator:\n");
        String userName = console.readLine();
        console.printf("Please enter an email address for the administrator:\n");
        String emailAddress = console.readLine();
        String password = getPassword(console);
        createAdministratorAccount(userName, emailAddress, password);

        accessManager.setAssignedRoles(Subject.forUser(userName),
                                       ApplicationResource.get(),
                                       Collections.singleton(BuiltInRole.SYSTEM_ADMIN.getRoleId()));

        console.printf("You have successfully set up the administrator account.\n");
    }

    private void createAdministratorAccount(@Nonnull String userName,
                                            @Nonnull String emailAddress,
                                            @Nonnull String password) {
        Salt salt = getFreshSalt();
        PasswordDigestAlgorithm digestAlgorithm = new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider());
        SaltedPasswordDigest digest = digestAlgorithm.getDigestOfSaltedPassword(password, salt);
        userRecordRepository.save(new UserRecord(
                UserId.getUserId(userName),
                userName,
                emailAddress,
                "",
                salt,
                digest
        ));
    }

    static Salt getFreshSalt() {
        return new SaltProvider().get();
    }

    private String getPassword(Console console) {
        while (true) {
            console.printf("Please enter a password for the administrator account:\n");
            char [] pwd = console.readPassword();
            console.printf("Please confirm the password:\n");
            char [] confPwd = console.readPassword();
            if(Arrays.equals(pwd, confPwd)) {
                return String.valueOf(pwd);
            }
            else {
                System.out.println("Passwords do not match.  Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            Morphia morphia = getMorphia();
            MongoClient mongoClient = getMongoClient();
            Datastore datastore = morphia.createDatastore(mongoClient, DB_NAME);
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            UserRecordRepository userRecordRepository = new UserRecordRepository(database, new UserRecordConverter());
            SetupTools tools = new SetupTools(userRecordRepository,
                                              new AccessManagerImpl(RoleOracleImpl.get(),
                                                                    datastore),
                                              System.console());
            tools.createAdministratorAccount();
        } catch (IOException e) {
            System.out.printf("An error occurred: %s %s\n", e.getClass().getSimpleName(), e.getMessage());
        }
    }


    private static Morphia getMorphia() {
        return new MorphiaProvider(
                new UserIdConverter(),
                new OWLEntityConverter(new OWLDataFactoryImpl()),
                new ProjectIdConverter(),
                new ThreadIdConverter(),
                new CommentIdConverter(),
                new CollectionIdConverter(), new FormIdConverter(), new TagIdConverter(), new ColorConverter()).get();
    }
}

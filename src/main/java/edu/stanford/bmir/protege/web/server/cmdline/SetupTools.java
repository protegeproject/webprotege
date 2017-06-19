package edu.stanford.bmir.protege.web.server.cmdline;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.access.*;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.inject.MongoClientProvider;
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
import java.io.BufferedInputStream;
import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

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
        console.printf("Please log into WebProtege using the user name and password that you specified.\n");
        console.printf("You should complete your WebProtege setup by viewing the #admin page of your installation.  " +
                               "If you have WebProtege running at https://my.domain.com/webprotege/WebProtege.jsp then the admin " +
                               "page can be found at https://my.domain.com/webprotege/WebProtege.jsp#admin\n");
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

    public static void main(String[] args) throws IOException {
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
    }

    private static MongoClient getMongoClient() throws IOException {
        WebProtegeProperties properties = getWebProtegeProperties();
        String dbHost = properties.getDBHost().orElse("localhost");
        int dbPort = Integer.parseInt(properties.getDBPort().orElse(WebProtegePropertyName.MONGO_DB_PORT.toString()));
        return new MongoClientProvider(dbHost, dbPort).get();
    }

    private static Morphia getMorphia() {
        return new MorphiaProvider(
                    new UserIdConverter(),
                    new OWLEntityConverter(new OWLDataFactoryImpl()),
                    new ProjectIdConverter(),
                    new ThreadIdConverter(),
                    new CommentIdConverter()
            ).get();
    }

    @Nonnull
    private static WebProtegeProperties getWebProtegeProperties() throws IOException {
        Properties properties = new Properties(System.getProperties());
        properties.load(new BufferedInputStream(SetupTools.class.getClass().getResourceAsStream("/webprotege.properties")));
        return new WebProtegeProperties(properties);
    }
}

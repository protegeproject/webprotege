package edu.stanford.bmir.protege.web.server.cmdline;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import edu.stanford.bmir.protege.web.server.access.*;
import edu.stanford.bmir.protege.web.server.collection.CollectionIdConverter;
import edu.stanford.bmir.protege.web.server.color.ColorConverter;
import edu.stanford.bmir.protege.web.server.form.FormIdConverter;
import edu.stanford.bmir.protege.web.server.persistence.*;
import edu.stanford.bmir.protege.web.server.tag.TagIdConverter;
import edu.stanford.bmir.protege.web.shared.access.BuiltInRole;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.checkerframework.checker.nullness.Opt;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.io.Console;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-05-02
 */
public class SetPermissions {


    @Nonnull
    private final Console console;

    public SetPermissions(@Nonnull Console console) {
        this.console = console;
    }

    public void run() {
        console.printf("Set User Role\n");
        try(MongoClient mongoClient = WebProtegeCli.getMongoClient()) {
            var accessManager = getAccessManager(mongoClient);

            var projectId = getProjectId();
            var userId = getUserId();

            var subject = Subject.forUser(userId);
            var resource = ProjectResource.forProject(projectId);

            var role = getRole();
            role.ifPresent(r -> accessManager.setAssignedRoles(subject, resource, Collections.singleton(r.getRoleId())));
        }
    }

    private AccessManager getAccessManager(MongoClient mongoClient) {
        Datastore datastore = getDatatstore(mongoClient);
        return new AccessManagerImpl(RoleOracleImpl.get(), datastore);
    }

    private String getUserId() {
        console.printf("Please enter the User Id of the user that you wish to set the role for\n");
        return console.readLine();
    }

    @Nonnull
    private ProjectId getProjectId() {
        while(true) {
            console.printf("Please enter the Project Id that you want to change the permissions for\n");
            var enteredProjectId = console.readLine();
            if(ProjectId.isWelFormedProjectId(enteredProjectId)) {
                return ProjectId.get(enteredProjectId);
            }
            else {
                console.printf("Malformed Project Id\n");
            }
        }
    }

    private Optional<BuiltInRole> getRole() {
        console.printf("Please enter the number of role that you wish to assign\n");
        console.printf("1) Manage\n");
        console.printf("2) Edit\n");
        console.printf("3) Comment\n");
        console.printf("4) View\n");
        String roleIndex = console.readLine();
        switch(roleIndex) {
            case "1":
                return Optional.of(BuiltInRole.CAN_MANAGE);
            case "2":
                return Optional.of(BuiltInRole.CAN_EDIT);
            case "3":
                return Optional.of(BuiltInRole.CAN_COMMENT);
            case "4":
                return Optional.of(BuiltInRole.CAN_VIEW);
            default:
                console.printf("Invalid role number");
                return Optional.empty();
        }
    }

    private static Datastore getDatatstore(MongoClient mongoClient) {
        MorphiaProvider morphiaProvider = new MorphiaProvider(
                new UserIdConverter(),
                new OWLEntityConverter(new OWLDataFactoryImpl()),
                new ProjectIdConverter(),
                new ThreadIdConverter(),
                new CommentIdConverter(),
                new CollectionIdConverter(), new FormIdConverter(), new TagIdConverter(), new ColorConverter());
        Morphia morphia = morphiaProvider.get();
        return morphia.createDatastore(mongoClient, "webprotege");
    }

    public static void main(String[] args) {
        SetPermissions setPermissions = new SetPermissions(System.console());
        setPermissions.run();
    }

}

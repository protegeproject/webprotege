package edu.stanford.bmir.protege.web.server.issues;

import com.github.mustachejava.DefaultMustacheFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentNotificationEmailGenerator_TestCase {

    public static final String TEMPLATE_FILE_CONTENTS = "{{project.displayName}} {{comment.createdBy.userName}}";

    public static final String USER_NAME = "John Smith";

    public static final String THE_FILE_NAME = "TheFileName";

    private CommentNotificationEmailGenerator generator;

    @Mock
    private FileContents templateFile;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private EntityDiscussionThread thread;

    private OWLEntity entity = OWLFunctionalSyntaxFactory.createClass();

    @Mock
    private OWLEntityData entityData;

    @Mock
    private Comment comment;

    @Mock
    private UserId creator;

    @Mock
    private File file;

    @Mock
    private PlaceUrl placeUrl;

    @Mock
    private ApplicationNameSupplier applicationNameSupplier;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() throws Exception {
        when(templateFile.getContents()).thenReturn(TEMPLATE_FILE_CONTENTS);
        when(applicationNameSupplier.get())
                .thenReturn("TheAppName");
        when(placeUrl.getProjectUrl(projectId))
                .thenReturn("TheProjectUrl");
        when(placeUrl.getEntityUrl(projectId, entity))
                .thenReturn("TheEntityUrl");
        when(entityData.getEntity())
                .thenReturn(entity);

        when(thread.getProjectId())
                .thenReturn(projectId);
        when(thread.getEntity())
                .thenReturn(entity);

        when(comment.getCreatedBy())
                .thenReturn(creator);
        when(creator.getUserName())
                .thenReturn(USER_NAME);

        when(entityData.getBrowserText()).thenReturn("TheBrowserText");
        templateEngine = new TemplateEngine(DefaultMustacheFactory::new);
        generator = new CommentNotificationEmailGenerator(templateFile,
                                                          templateEngine,
                                                          applicationNameSupplier,
                                                          placeUrl
        );
    }

    @Test
    public void shouldPopulateTemplate() {
        String populated = populateTemplate();
        assertThat(populated, is("MyProject John Smith"));
    }

    private String populateTemplate() {
        return generator.generateEmailBody("MyProject", entityData, thread, comment);
    }
}

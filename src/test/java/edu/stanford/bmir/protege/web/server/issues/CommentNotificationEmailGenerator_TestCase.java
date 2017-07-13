package edu.stanford.bmir.protege.web.server.issues;

import com.github.mustachejava.DefaultMustacheFactory;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.place.PlaceUrl;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
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

    @Mock
    private OWLEntity entity;

    @Mock
    private OWLEntityData entityData;

    @Mock
    private Comment comment;

    @Mock
    private WebProtegeLogger logger;

    @Mock
    private UserId creator;

    @Mock
    private File file;

    @Mock
    private PlaceUrl placeUrl;

    @Mock
    private ApplicationNameSupplier applicationNameSupplier;

    @Before
    public void setUp() throws Exception {
        when(templateFile.getContents()).thenReturn(TEMPLATE_FILE_CONTENTS);
        when(templateFile.getFile()).thenReturn(file);
        when(file.getName()).thenReturn(THE_FILE_NAME);
        when(comment.getCreatedBy()).thenReturn(creator);
        when(creator.getUserName()).thenReturn(USER_NAME);
        when(applicationNameSupplier.get()).thenReturn("TheAppName");
        when(placeUrl.getProjectUrl(any())).thenReturn("TheProjectUrl");
        when(placeUrl.getEntityUrl(any(), any())).thenReturn("TheEntityUrl");
        when(entityData.getEntity()).thenReturn(entity);
        when(entityData.getBrowserText()).thenReturn("TheBrowserText");
        templateEngine = new TemplateEngine(DefaultMustacheFactory::new);
        generator = new CommentNotificationEmailGenerator(templateFile,
                                                          templateEngine,
                                                          applicationNameSupplier,
                                                          placeUrl, logger
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

package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.app.ApplicationHostSupplier;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageIdGenerator_TestCase {

    private static final String THE_APPLICATION_HOST = "the.application.host";

    private static final String THE_OBJECT_CATEGORY = "TheObjectCategory";

    private final String projectIdString = UUID.randomUUID().toString();

    private final ProjectId projectId = ProjectId.get(projectIdString);

    private MessageIdGenerator generator;

    @Mock
    private ApplicationHostSupplier hostSupplier;

    @Before
    public void setUp() throws Exception {
        when(hostSupplier.get()).thenReturn(THE_APPLICATION_HOST);
        generator = new MessageIdGenerator(hostSupplier);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
        generator.generateProjectMessageId(null, THE_OBJECT_CATEGORY, UUID.randomUUID().toString());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ObjectCategory_IsNull() {
        generator.generateProjectMessageId(projectId, null, UUID.randomUUID().toString());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ObjectId_IsNull() {
        generator.generateProjectMessageId(projectId, THE_OBJECT_CATEGORY, null);
    }

    @Test
    public void shouldGenerateMessageId() {
        String objectId = UUID.randomUUID().toString();
        MessageId msgId = generator.generateProjectMessageId(projectId, THE_OBJECT_CATEGORY, objectId);
        assertThat(msgId.getId(), is("<projects/" + projectIdString + "/" + THE_OBJECT_CATEGORY + "/" + objectId + "@" + THE_APPLICATION_HOST + ">"));
    }
}

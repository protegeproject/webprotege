package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.shared.issues.CommentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentMessageIdGenerator_TestCase {

    private static final String COMMENT_ID_STRING = "comment.id.string";

    private static final String PROJECT_ID_STRING = "project.id.string";

    private CommentMessageIdGenerator generator;

    @Mock
    private MessageIdGenerator messageIdGenerator;

    @Mock
    private ProjectId projectId;

    @Mock
    private CommentId commentId;

    @Before
    public void setUp() {
        when(commentId.getId()).thenReturn(COMMENT_ID_STRING);
        when(projectId.getId()).thenReturn(PROJECT_ID_STRING);
        generator = new CommentMessageIdGenerator(messageIdGenerator);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
        generator.generateCommentMessageId(null, commentId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_CommentId_IsNull() {
        generator.generateCommentMessageId(projectId, null);
    }

    @Test
    public void shouldGenerateMessageIdForComment() {
        generator.generateCommentMessageId(projectId, commentId);
        verify(messageIdGenerator, times(1)).generateProjectMessageId(projectId, "comments", COMMENT_ID_STRING);
    }
}

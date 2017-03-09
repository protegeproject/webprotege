package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import edu.stanford.bmir.protege.web.shared.issues.mention.UserIdMention;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentParticipantsExtractor_TestCase {

    public static final String USER_COMMENT = "User Comment";

    private CommentParticipantsExtractor extractor;

    @Mock
    private MentionParser mentionParser;

    private List<ParsedMention> parsedMentions;

    @Mock
    private ParsedMention parsedMention;

    @Mock
    private UserIdMention userMention;

    @Mock
    private UserId creatorId, mentionedUserId;

    @Mock
    private Comment comment;

    @Before
    public void setUp() throws Exception {
        when(userMention.getMentionedUserId()).thenReturn(Optional.of(mentionedUserId));
        when(parsedMention.getParsedMention()).thenReturn(userMention);
        parsedMentions = Collections.singletonList(parsedMention);
        when(mentionParser.parseMentions(anyString())).thenReturn(parsedMentions);
        when(comment.getCreatedBy()).thenReturn(creatorId);
        extractor = new CommentParticipantsExtractor(mentionParser);
    }

    @Test
    public void shouldExtractCreator() {
        assertThat(extractor.extractParticipants(comment), hasItem(creatorId));
    }

    @Test
    public void shouldExtractMentionedUser() {
        assertThat(extractor.extractParticipants(comment), hasItem(mentionedUserId));
    }

}

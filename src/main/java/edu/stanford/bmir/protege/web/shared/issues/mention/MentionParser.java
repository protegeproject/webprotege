package edu.stanford.bmir.protege.web.shared.issues.mention;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
public class MentionParser {

    private static final int START_BOUNDARY_GROUP = 1;

    private static final int ISSUE_MENTION_MATCH_GROUP = 3;

    private static final int USER_ID_MATCH_GROUP = 5;

    private static final int REVISION_MATCH_GROUP = 9;

    private static final int ENTITY_MATCH_GROUP = 11;

    // @formatter:off
    private final RegExp pattern = RegExp.compile(
            // Start of string or some kind of boundary
            "(^|[\\s\\.\\,\\(\\)\\[\\]\\{\\}])" +
                    // Issue e.g. #33 for issue 33
                    "((\\#(\\d+))" +
                    "|" +
                    // UserUd e.g. @Matthew or @{Matthew Horridge}
                    "(\\@(([^\\{\\s,]+)|\\{([^\\}]+)\\}))" +
                    "|" +
                    // Revision e.g. R33 for Revision 33
                    "(R(\\d+))" +
                    "|" +
                    // Entity e.g. Class(<http:/my.ontology/ClassA>)
                    "((Class|ObjectProperty|DataProperty|AnnotationProperty|NamedIndividual|Datatype)\\(<([^>]+)>\\)))",
            // Global and Multiple
            "gm");
    // @formatter:on

    /**
     * Parses a list of Mentions from the given text.
     * @param text The text
     * @return The list of parsed Mentions
     */
    @Nonnull
    public List<ParsedMention> parseMentions(@Nonnull String text) {
        checkNotNull(text);
        List<ParsedMention> parsedMentions = new ArrayList<>();
        MatchResult matchResult = pattern.exec(text);
        while (matchResult != null) {
            ParsedMention mention = parseMentionFromMatch(matchResult);
            parsedMentions.add(mention);
            pattern.setLastIndex(matchResult.getIndex() + 1);
            matchResult = pattern.exec(text);
        }
        return parsedMentions;
    }

    private static ParsedMention parseMentionFromMatch(MatchResult matchResult) {
        String issueMatchGroup = matchResult.getGroup(ISSUE_MENTION_MATCH_GROUP);
        if (issueMatchGroup != null) {
            return parseIssueMention(matchResult);
        }
        String userIdMatchGroup = matchResult.getGroup(USER_ID_MATCH_GROUP);
        if (userIdMatchGroup != null) {
            return parseUserIdMention(matchResult);
        }
        String revisionMatchGroup = matchResult.getGroup(REVISION_MATCH_GROUP);
        if (revisionMatchGroup != null) {
            return parseRevisionMention(matchResult);
        }
        String entityMatchGroup = matchResult.getGroup(ENTITY_MATCH_GROUP);
        if (entityMatchGroup != null) {
            return parseEntityMention(matchResult);
        }
        throw new RuntimeException("MentionParseError:  Match does not correspond to any mention patterns");
    }


    private static ParsedMention parseIssueMention(MatchResult matchResult) {
        String issueMatchGroup = matchResult.getGroup(ISSUE_MENTION_MATCH_GROUP);
        if (issueMatchGroup == null) {
            throw new IllegalStateException("Revision match group is not present in input: " + matchResult.getInput());
        }
        int startIndex = matchResult.getIndex() + matchResult.getGroup(START_BOUNDARY_GROUP).length();
        int endIndex = startIndex + issueMatchGroup.length();
        String issueNumberGroup = matchResult.getGroup(ISSUE_MENTION_MATCH_GROUP + 1);
        return new ParsedMention(
                new IssueMention(Integer.parseInt(issueNumberGroup)),
                startIndex,
                endIndex);
    }

    private static ParsedMention parseRevisionMention(MatchResult matchResult) {
        String revisionMatchGroup = matchResult.getGroup(REVISION_MATCH_GROUP);
        if (revisionMatchGroup == null) {
            throw new IllegalStateException("Revision match group is not present in input: " + matchResult.getInput());
        }
        int startIndex = matchResult.getIndex() + matchResult.getGroup(START_BOUNDARY_GROUP).length();
        int endIndex = startIndex + revisionMatchGroup.length();
        String revisionNumberGroup = matchResult.getGroup(REVISION_MATCH_GROUP + 1);
        return new ParsedMention(
                new RevisionMention(
                        Long.parseLong(revisionNumberGroup)),
                startIndex,
                endIndex);
    }


    private static ParsedMention parseUserIdMention(MatchResult matchResult) {
        String userIdMatchGroup = matchResult.getGroup(USER_ID_MATCH_GROUP);
        if (userIdMatchGroup == null) {
            throw new IllegalStateException("UserId match group is not present in input: " + matchResult.getInput());
        }
        int startIndex = matchResult.getIndex() + matchResult.getGroup(START_BOUNDARY_GROUP).length();
        int endIndex = startIndex + userIdMatchGroup.length();
        String plainUserIdMatchGroup = matchResult.getGroup(USER_ID_MATCH_GROUP + 2);
        if (plainUserIdMatchGroup != null) {
            return new ParsedMention(
                    new UserIdMention(
                            UserId.getUserId(plainUserIdMatchGroup)),
                    startIndex,
                    startIndex + userIdMatchGroup.length());
        }
        else {
            String bracketedUserIdMatchGroup = matchResult.getGroup(USER_ID_MATCH_GROUP + 3);
            if (bracketedUserIdMatchGroup != null) {
                return new ParsedMention(
                        new UserIdMention(
                                UserId.getUserId(bracketedUserIdMatchGroup)),
                        startIndex,
                        endIndex
                );

            }
            else {
                throw new IllegalStateException("UserId not present in input " + matchResult.getInput());
            }
        }
    }


    private static ParsedMention parseEntityMention(MatchResult matchResult) {
        String entityMatchGroup = matchResult.getGroup(ENTITY_MATCH_GROUP);
        if (entityMatchGroup == null) {
            throw new IllegalStateException("Entity match group is not present in input: " + matchResult.getInput());
        }
        String entityTypeGroup = matchResult.getGroup(ENTITY_MATCH_GROUP + 1);
        if (entityTypeGroup == null) {
            throw new IllegalStateException("EntityType group is not present in input: " + matchResult.getInput());
        }
        String entityIriGroup = matchResult.getGroup(ENTITY_MATCH_GROUP + 2);
        if (entityIriGroup == null) {
            throw new IllegalStateException("Entity IRI group is not present in input: " + matchResult.getInput());
        }
        EntityType<?> entityType = null;
        for (EntityType<?> type : EntityType.values()) {
            if (type.getName().equals(entityTypeGroup)) {
                entityType = type;
                break;
            }
        }
        if (entityType == null) {
            throw new IllegalStateException("Illegal entity type: " + entityTypeGroup);
        }
        IRI entityIri = IRI.create(entityIriGroup);
        int startIndex = matchResult.getIndex() + matchResult.getGroup(START_BOUNDARY_GROUP).length();
        int endIndex = startIndex + entityMatchGroup.length();
        return new ParsedMention(
                new EntityMention(
                        DataFactory.getOWLEntity(entityType, entityIri)
                ),
                startIndex,
                endIndex
        );

    }
}

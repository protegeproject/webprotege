package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionCallback;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionChoice;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.EntityLookupRequest;
import edu.stanford.bmir.protege.web.shared.entity.EntityLookupResult;
import edu.stanford.bmir.protege.web.shared.entity.LookupEntitiesAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2017
 */
public class CommentAutoCompleter {

    private static final int NAME_GROUP = 1;

    private final RegExp nameRegExp = RegExp.compile("@([^ ]*)$");

    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectId projectId;

    @Inject
    public CommentAutoCompleter(@Nonnull ProjectId projectId,
                                @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void handleAutocomplete(@Nonnull String query,
                                   @Nonnull EditorPosition caretPosition,
                                   int caretPos,
                                   @Nonnull AutoCompletionCallback callback) {
        String upToCarent = query.substring(0, caretPos);
        MatchResult result = nameRegExp.exec(upToCarent);
        if (result == null) {
            handleAttemptAtEntityCompletions(upToCarent, caretPosition, callback);
        }
        else {
            String matchedPartialName = result.getGroup(NAME_GROUP);

            EditorPosition replaceTextFrom = new EditorPosition(
                    caretPosition.getLineNumber(),
                    caretPosition.getColumnNumber() - result
                            .getGroup(0)
                            .length());

            dispatchServiceManager.execute(new GetUserIdCompletionsAction(matchedPartialName),
                                           userIdsResult -> {
                                               List<AutoCompletionChoice> suggestions = new ArrayList<>();
                                               List<UserId> userIds = userIdsResult.getPossibleItemCompletions();
                                               for (UserId userId : userIds) {
                                                   String userName = userId.getUserName();
                                                   String replacement = getReplacementStringFromUserName(userName);
                                                   EditorPosition replaceTextTo = new EditorPosition(
                                                           caretPosition.getLineNumber(),
                                                           caretPosition.getColumnNumber()
                                                   );
                                                   suggestions.add(
                                                           new AutoCompletionChoice(replacement,
                                                                                    userName,
                                                                                    "" ,
                                                                                    replaceTextFrom,
                                                                                    replaceTextTo)
                                                   );
                                               }
                                               callback.completionsReady(new AutoCompletionResult(suggestions,
                                                                                                  replaceTextFrom));
                                           });
        }

    }

    /**
     * Gets a replacement string that will generate a valid @mention for a user name.
     *
     * @param userName The user name.
     * @return The replacement string.
     */
    private static String getReplacementStringFromUserName(@Nonnull String userName) {
        if (userName.contains(" ")) {
            return "@{" + userName + "}";
        }
        else {
            return "@" + userName;
        }
    }


    private void handleAttemptAtEntityCompletions(String queryUpToCaret,
                                                  EditorPosition caretPos,
                                                  AutoCompletionCallback callback) {
        // Last index of space or 0 if there are not spaces
        int wordStart = queryUpToCaret.lastIndexOf(" ") + 1;
        int wordFragmentLen = queryUpToCaret.length() - wordStart;
        String wordFragment = queryUpToCaret.substring(wordStart);
        if(wordFragment.isEmpty()) {
            callback.completionsReady(new AutoCompletionResult());
            return;
        }
        dispatchServiceManager.execute(
                new LookupEntitiesAction(projectId,
                                         lookUpEntities(wordFragment)),
                result -> {
                    List<AutoCompletionChoice> choices = new ArrayList<>();
                    EditorPosition pos = new EditorPosition(caretPos.getLineNumber(),
                                                            caretPos.getColumnNumber() - wordFragmentLen);
                    for (final EntityLookupResult entity : result.getEntityLookupResults()) {
                        choices.add(new AutoCompletionChoice(
                                formatReplacementTest(entity),
                                entity.getOWLEntityData().getBrowserText(),
                                "",
                                pos,
                                caretPos
                        ));
                    }
                    AutoCompletionResult autoCompletionResult = new AutoCompletionResult(choices, pos);
                    callback.completionsReady(autoCompletionResult);
                });
    }

    private static EntityLookupRequest lookUpEntities(String lastWord) {
        EntityLookupRequest req = new EntityLookupRequest(lastWord, SearchType.getDefault());
        GWT.log("[CommentAutoCompleter] Request: " + req);
        return req;
    }

    private static String formatReplacementTest(EntityLookupResult result) {
        String directLink = result.getDirectLink().replace("(", "%28").replace(")", "%29");
        return "[" + result.getOWLEntityData().getBrowserText() + "](" + directLink + ")";
    }
}

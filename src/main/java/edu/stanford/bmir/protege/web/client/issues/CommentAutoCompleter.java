package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionCallback;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionChoice;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsAction;
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

    private final RegExp nameRegExp = RegExp.compile("@([^ ]*)$" );

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public CommentAutoCompleter(@Nonnull DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void handleAutocomplete(@Nonnull String query,
                                   @Nonnull EditorPosition caretPosition,
                                   int caretPos,
                                   @Nonnull AutoCompletionCallback callback) {
        String upToCarent = query.substring(0, caretPos);
        MatchResult result = nameRegExp.exec(upToCarent);
        if (result == null) {
            callback.completionsReady(new AutoCompletionResult());
            return;
        }
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

    /**
     * Gets a replacement string that will generate a valid @mention for a user name.
     * @param userName The user name.
     * @return The replacement string.
     */
    private static String getReplacementStringFromUserName(@Nonnull String userName) {
        if (userName.contains(" " )) {
            return  "@{" + userName + "}";
        }
        else {
            return  "@" + userName;
        }
    }
}

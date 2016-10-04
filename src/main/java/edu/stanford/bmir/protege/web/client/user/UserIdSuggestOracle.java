package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.user.client.ui.SuggestOracle;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/03/16
 */
public class UserIdSuggestOracle extends SuggestOracle {

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public UserIdSuggestOracle(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        dispatchServiceManager.execute(new GetUserIdCompletionsAction(request.getQuery()), new DispatchServiceCallback<GetPossibleItemCompletionsResult<UserId>>() {
            @Override
            public void handleSuccess(GetPossibleItemCompletionsResult<UserId> result) {
                Collection<Suggestion> suggestions = new ArrayList<>();
                for(final UserId userId : result.getPossibleItemCompletions()) {
                    suggestions.add(new Suggestion() {
                        @Override
                        public String getDisplayString() {
                            String userName = userId.getUserName();
                            String query = request.getQuery();
                            int queryIndex = userName.toLowerCase().indexOf(query.toLowerCase());
                            if(queryIndex != -1) {
                                return userName.substring(0, queryIndex)
                                        + "<span style='font-weight: bold;'>"
                                        + userName.substring(queryIndex, queryIndex + query.length())
                                        + "</span>"
                                        + userName.substring(queryIndex + query.length());
                            }
                            else {
                                return userName;
                            }
                        }

                        @Override
                        public String getReplacementString() {
                            return userId.getUserName();
                        }
                    });
                }
                callback.onSuggestionsReady(request, new Response(suggestions));
            }
        });
    }
}

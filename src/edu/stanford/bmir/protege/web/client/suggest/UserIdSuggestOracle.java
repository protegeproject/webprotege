package edu.stanford.bmir.protege.web.client.suggest;

import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class UserIdSuggestOracle extends SuggestOracle {

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        String query = request.getQuery();

    }
}

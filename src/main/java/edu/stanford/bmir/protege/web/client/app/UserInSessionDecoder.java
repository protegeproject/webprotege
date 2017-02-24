package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.HashSet;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.app.UserInSessionEncoding.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/12/14
 */
public class UserInSessionDecoder implements ClientObjectDecoder<UserInSession> {

    @Override
    public UserInSession decode(JSONValue json) {
        JSONObject object = json.isObject();
        if(object == null) {
            throw new RuntimeException("Expected json object");
        }
        JSONValue userNameValue = object.get(USER_NAME);
        if(userNameValue == null) {
            throw new RuntimeException("Expected userName attribute");
        }
        JSONString userNameStringValue = userNameValue.isString();
        if(userNameStringValue == null) {
            throw new RuntimeException("Expected userName value to be string");
        }
        String displayName = object.get(DISPLAY_NAME).isString().stringValue();
        String userEmail = object.get(USER_EMAIL).isString().stringValue();
        JSONArray actionArray = object.get(APPLICATION_ACTIONS).isArray();
        Set<ActionId> allowedActions = new HashSet<>();
        if(actionArray != null) {
            for(int i = 0; i < actionArray.size(); i++) {
                ActionId actionId = new ActionId(actionArray.get(i).isString().stringValue());
                allowedActions.add(actionId);
            }
        }
        UserId userId = UserId.getUserId(userNameStringValue.stringValue());
        UserDetails userDetails;
        if(userId.isGuest()) {
            userDetails = UserDetails.getGuestUserDetails();
        }
        else {
            userDetails = UserDetails.getUserDetails(userId, displayName, userEmail);
        }
        return new UserInSession(
                userDetails,
                allowedActions
        );
    }
}

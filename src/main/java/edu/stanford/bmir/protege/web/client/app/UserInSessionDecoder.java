package edu.stanford.bmir.protege.web.client.app;

import com.google.common.collect.ImmutableList;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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
        JSONValue userNameValue = object.get("userName");
        if(userNameValue == null) {
            throw new RuntimeException("Expected userName attribute");
        }
        JSONString userNameStringValue = userNameValue.isString();
        if(userNameStringValue == null) {
            throw new RuntimeException("Expected userName value to be string");
        }
        String displayName = object.get("displayName").isString().stringValue();
        String userEmail = object.get("userEmail").isString().stringValue();

        JSONArray groupsArray = object.get("groups").isArray();
        ImmutableList.Builder<GroupId> groupIdBuilder = ImmutableList.builder();
        for(int i = 0; i < groupsArray.size(); i++) {
            JSONValue value = groupsArray.get(i);
            String groupId = value.isString().stringValue();
            groupIdBuilder.add(GroupId.get(groupId));
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
                groupIdBuilder.build()
        );
    }
}

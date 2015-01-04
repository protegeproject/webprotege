package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/12/14
 */
public class UserInSessionEncoder implements ClientObjectEncoder<UserInSession> {

    @Override
    public JsonObject encode(UserInSession object) {
        UserDetails userDetails = object.getUserDetails();
        JsonArrayBuilder groupsBuilder = Json.createArrayBuilder();
        for (GroupId groupId : object.getGroups()) {
            groupsBuilder.add(groupId.getGroupName());
        }

        return Json.createObjectBuilder()
                .add("userName", userDetails.getUserId().getUserName())
                .add("displayName", userDetails.getDisplayName())
                .add("userEmail", userDetails.getEmailAddress().or(""))
                .add("groups", groupsBuilder.build())
                .build();
    }
}

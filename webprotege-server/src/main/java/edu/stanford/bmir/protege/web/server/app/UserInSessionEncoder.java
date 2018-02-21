package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import static edu.stanford.bmir.protege.web.shared.app.UserInSessionEncoding.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/12/14
 */
public class UserInSessionEncoder implements ClientObjectEncoder<UserInSession> {

    @Override
    public JsonObject encode(UserInSession object) {
        UserDetails userDetails = object.getUserDetails();
        JsonArrayBuilder actionArray = Json.createArrayBuilder();
        object.getAllowedApplicationActions().stream().map(a -> a.getId()).forEach(a -> actionArray.add(a));
        return Json.createObjectBuilder()
                   .add(USER_NAME, userDetails.getUserId().getUserName())
                   .add(DISPLAY_NAME, userDetails.getDisplayName())
                   .add(USER_EMAIL, userDetails.getEmailAddress().orElse(""))
                   .add(APPLICATION_ACTIONS, actionArray)
                .build();
    }
}

package edu.stanford.bmir.protege.web.client.app;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;
import edu.stanford.bmir.protege.web.client.TestResources;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Optional;
import java.util.Set;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/12/14
 */
public class GwtTest_UserInSessionDecoder extends GWTTestCase {

    public static final UserId EXPECTED_USER_ID = UserId.getUserId("JohnSmith");

    public static final String EXPECTED_DISPLAY_NAME = "John Smith";

    public static final String EXPECTED_EMAIL_ADDRESS = "john.smith@gmail.com";

    public static final Set<ActionId> EXPECTED_ACTIONS = ImmutableSet.of(new ActionId("MyAction"));

    public static final UserInSession EXPECTED_SESSION_DATA = new UserInSession(
            UserDetails.getUserDetails(
                    EXPECTED_USER_ID,
                    EXPECTED_DISPLAY_NAME,
                    Optional.of(EXPECTED_EMAIL_ADDRESS)),
            EXPECTED_ACTIONS
    );
    private String userInSessionJson;

    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeJUnit";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        delayTestFinish(10000);
        userInSessionJson = TestResources.INSTANCE.userInSessionJson().getText();
    }

    public void test_Decode() {
        UserInSessionDecoder decoder = new UserInSessionDecoder();
        UserInSession userInSession = decoder.decode(new JSONObject(
                JsonUtils.safeEval(userInSessionJson)
        ));
        assertEquals(userInSession, EXPECTED_SESSION_DATA);
        finishTest();
    }
}

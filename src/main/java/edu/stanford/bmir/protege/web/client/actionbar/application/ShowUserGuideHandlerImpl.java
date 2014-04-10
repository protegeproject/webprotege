package edu.stanford.bmir.protege.web.client.actionbar.application;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/09/2013
 */
public class ShowUserGuideHandlerImpl implements ShowUserGuideHandler {

    private static final String USER_GUIDE_URL = "http://protegewiki.stanford.edu/wiki/WebProtegeUsersGuide";

    @Override
    public void handleShowUserGuide() {
        com.google.gwt.user.client.Window.open(USER_GUIDE_URL, "_blank", "");
    }
}

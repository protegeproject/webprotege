package edu.stanford.bmir.protege.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/12/14
 */
public interface TestResources extends ClientBundle {

    TestResources INSTANCE = GWT.create(TestResources.class);

    @ClientBundle.Source("GwtTest_UserInSession.json")
    TextResource userInSessionJson();

}

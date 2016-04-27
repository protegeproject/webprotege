package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import edu.stanford.bmir.protege.web.client.WebProtegeInitializer;
import edu.stanford.bmir.protege.web.client.place.WebProtegeActivityManager;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactoryModuleGenerated;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorClientModule;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationPresenter;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/12/15
 */
@GinModules(
        value = {
                ApplicationClientModule.class,
                PrimitiveDataEditorClientModule.class,
                PortletFactoryModuleGenerated.class})
public interface ApplicationClientInjector extends Ginjector {

    public WebProtegeInitializer getWebProtegeInitializer();

    public ApplicationPresenter getApplicationPresenter();

    public ClientApplicationProperties getClientApplicationProperties();

    WebProtegeActivityManager getActivityManager();

    PlaceHistoryHandler getPlaceHistoryHandler();
}

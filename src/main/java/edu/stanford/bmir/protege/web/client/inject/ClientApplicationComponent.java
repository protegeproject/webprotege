package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.place.shared.PlaceHistoryHandler;
import dagger.Component;
import edu.stanford.bmir.protege.web.client.WebProtegeInitializer;
import edu.stanford.bmir.protege.web.client.place.WebProtegeActivityManager;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactoryModuleGenerated;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationPresenter;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/12/15
 */
@Component(
        modules = {
                ClientApplicationModule.class,
                PortletFactoryModuleGenerated.class})
@Singleton
public interface ClientApplicationComponent {

    WebProtegeInitializer getWebProtegeInitializer();

    ApplicationPresenter getApplicationPresenter();

    ClientApplicationProperties getClientApplicationProperties();

    WebProtegeActivityManager getActivityManager();

    PlaceHistoryHandler getPlaceHistoryHandler();

    ClientProjectComponent getClientProjectComponent(ClientProjectModule projectModule);
}

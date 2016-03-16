package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import edu.stanford.bmir.protege.web.client.WebProtegeInitializer;
import edu.stanford.bmir.protege.web.client.place.WebProtegeActivityManager;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationPresenter;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public class WebProtegeClientInjector {

    private static ApplicationClientInjector injector = GWT.create(ApplicationClientInjector.class);

    public static ApplicationPresenter getApplicationPresenter() {
        return injector.getApplicationPresenter();
    }

    public static WebProtegeInitializer getWebProtegeInitializer() {
        return injector.getWebProtegeInitializer();
    }

    public static UIFactory getUiFactory(ProjectId projectId) {
        ProjectIdProvider.setProjectId(projectId);
        return injector.getUiFactory();
    }

    public static PrimitiveDataEditorImpl getPrimitiveDataEditor(ProjectId projectId) {
        ProjectIdProvider.setProjectId(projectId);
        return injector.getPrimitiveDataEditor();
    }

    public static ClientApplicationProperties getClientApplicationProperties() {
        return injector.getClientApplicationProperties();
    }

    public static WebProtegeActivityManager getActivityManager() {
        return injector.getActivityManager();
    }

    public static PlaceHistoryHandler getPlaceHistoryHandler() {
        return injector.getPlaceHistoryHandler();
    }
}

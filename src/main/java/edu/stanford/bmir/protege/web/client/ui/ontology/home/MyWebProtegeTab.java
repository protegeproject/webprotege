package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.Panel;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerPresenter;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerView;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MyWebProtegeTab extends Panel {

    private ProjectManagerPresenter projectManagerPresenter;

    private static final String DEFAULT_TAB_NAME = "WebProt\u00E9g\u00E9";

    public MyWebProtegeTab(LoadProjectRequestHandler loadProjectRequestHandler, EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        projectManagerPresenter = new ProjectManagerPresenter(loadProjectRequestHandler, eventBus, dispatchServiceManager);
        ProjectManagerView projectManagerView = projectManagerPresenter.getProjectManagerView();
        add(projectManagerView.getWidget());
        setMonitorResize(true);

        Optional<String> appName = Application.get().getClientApplicationProperty(WebProtegePropertyName.APPLICATION_NAME);
        setTitle(appName.or(DEFAULT_TAB_NAME));
    }

}

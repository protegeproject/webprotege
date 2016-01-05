package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.google.common.base.Optional;
import com.gwtext.client.widgets.Panel;
import edu.stanford.bmir.protege.web.client.HasClientApplicationProperties;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerPresenter;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerView;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

import javax.inject.Inject;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MyWebProtegeTab extends Panel {

    private final ProjectManagerPresenter projectManagerPresenter;

    private final HasClientApplicationProperties hasClientApplicationProperties;

    private static final String DEFAULT_TAB_NAME = "WebProt\u00E9g\u00E9";

    @Inject
    public MyWebProtegeTab(ProjectManagerPresenter projectManagerPresenter, HasClientApplicationProperties hasClientApplicationProperties) {
        this.projectManagerPresenter = projectManagerPresenter;
        this.hasClientApplicationProperties = hasClientApplicationProperties;

        ProjectManagerView projectManagerView = projectManagerPresenter.getProjectManagerView();
        add(projectManagerView.getWidget());
        setMonitorResize(true);

        Optional<String> appName = hasClientApplicationProperties.getClientApplicationProperty(WebProtegePropertyName.APPLICATION_NAME);
        setTitle(appName.or(DEFAULT_TAB_NAME));
    }
}

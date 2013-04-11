package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.gwtext.client.widgets.Panel;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerPresenter;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerView;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MyWebProtegeTab extends Panel {

    private ProjectManagerPresenter projectManagerPresenter;


    public MyWebProtegeTab(LoadProjectRequestHandler loadProjectRequestHandler) {
        projectManagerPresenter = new ProjectManagerPresenter(loadProjectRequestHandler);
        ProjectManagerView projectManagerView = projectManagerPresenter.getProjectManagerView();
        add(projectManagerView.getWidget());
        setMonitorResize(true);
	}

	public String getLabel() {
		return "My WebProt\u00E9g\u00E9";
	}
}

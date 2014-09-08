package edu.stanford.bmir.protege.web.client.reasoning;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class DLQueryPortlet extends AbstractOWLEntityPortlet {

    private DLQueryPresenter presenter;

    public DLQueryPortlet(Project project) {
        super(project);
        setHeight(500);
    }

    @Override
    public void reload() {

    }

    @Override
    public void initialize() {
        DLQueryViewImpl dlQueryView = new DLQueryViewImpl();
        presenter = new DLQueryPresenter(
                getProjectId(),
                DispatchServiceManager.get(), dlQueryView
        );
        setTitle("DL Query");
        dlQueryView.setWidth("100%");
        dlQueryView.setHeight("100%");
        add(presenter.getView().asWidget());
    }
}

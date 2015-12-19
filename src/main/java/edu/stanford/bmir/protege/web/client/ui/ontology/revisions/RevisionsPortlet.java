package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class RevisionsPortlet extends AbstractOWLEntityPortlet {

    public static final int INITIAL_HEIGHT = 400;

    private RevisionsListViewPresenter presenter;

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    public RevisionsPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, Project project) {
        super(selectionModel, eventBus, project);
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());
        setHeight(INITIAL_HEIGHT);
        presenter = new RevisionsListViewPresenter(getProjectId(), eventBus,  new RevisionsListViewImpl(), dispatchServiceManager);
        presenter.reload();
        add(presenter.getWidget());
        setTitle("Revisions");
        presenter.reload();
    }

    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }


}

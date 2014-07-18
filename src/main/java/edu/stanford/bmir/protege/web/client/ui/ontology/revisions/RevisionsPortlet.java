package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class RevisionsPortlet extends AbstractOWLEntityPortlet {

    public static final int INITIAL_HEIGHT = 400;

    private RevisionsListViewPresenter presenter;

    public RevisionsPortlet(Project project) {
        super(project);
    }

    @Override
    public void reload() {
        presenter.reload();
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());
        setHeight(INITIAL_HEIGHT);
        presenter = new RevisionsListViewPresenter(getProjectId(), new RevisionsListViewImpl());
        presenter.reload();
        add(presenter.getWidget());
        setTitle("Revisions");
    }

    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }


}

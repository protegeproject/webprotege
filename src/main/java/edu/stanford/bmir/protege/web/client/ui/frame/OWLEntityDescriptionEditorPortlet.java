package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class OWLEntityDescriptionEditorPortlet extends AbstractOWLEntityPortlet {

    private final ManchesterSyntaxFrameEditorPresenter presenter;

    @Inject
    public OWLEntityDescriptionEditorPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider, ManchesterSyntaxFrameEditorPresenter presenter) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.presenter = presenter;
    }

    @Override
    public void initialize() {
        ManchesterSyntaxFrameEditorImpl editor = new ManchesterSyntaxFrameEditorImpl();
        presenter.attach(this);
        add(editor);
        setHeight(500);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        Optional<OWLEntityData> selectedEntityData = getSelectedEntityData();
        if (selectedEntityData.isPresent()) {
            presenter.setSubject(entityData.get().getEntity());
            setTitle("Description for " + entityData.get().getBrowserText());
        }
        else {
            presenter.clearSubject();
        }
    }

    @Override
    protected void onRefresh() {
        presenter.refresh();
    }
}

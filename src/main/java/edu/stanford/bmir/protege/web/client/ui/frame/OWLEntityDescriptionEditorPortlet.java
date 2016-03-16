package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

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
        presenter.attach(this);
        getContentHolder().setWidget(presenter.getView());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();
        if (selectedEntity.isPresent()) {
            presenter.setSubject(selectedEntity.get());
            setTitle(entity.get().getEntityType().getPrintName() + " Description");
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

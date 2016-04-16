package edu.stanford.bmir.protege.web.client.change;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

public class EntityChangesPortlet extends AbstractWebProtegePortlet {

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final ChangeListViewPresenter presenter;

    @Inject
	public EntityChangesPortlet(SelectionModel selectionModel,
                                EventBus eventBus,
                                ProjectId projectId,
                                LoggedInUserProvider loggedInUserProvider,
                                ChangeListViewPresenter presenter) {
		super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.presenter = presenter;
        getContentHolder().setWidget(presenter.getView().asWidget());

        addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                EntityChangesPortlet.this.handleProjectChanged(event);
            }
        });
        addApplicationEventHandler(PermissionsChangedEvent.TYPE, new PermissionsChangedHandler() {
            @Override
            public void handlePersmissionsChanged(PermissionsChangedEvent event) {
                updateDisplayForSelectedEntity();
            }
        });
        setTitle("Changes");
	}

    private void handleProjectChanged(ProjectChangedEvent event) {
        if(lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        for(OWLEntityData entityData : event.getSubjects()) {
            if(getSelectionModel().getSelection().equals(Optional.of(entityData.getEntity()))) {
                updateDisplayForSelectedEntity();
                return;
            }
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        updateDisplayForSelectedEntity();
    }

    private void updateDisplayForSelectedEntity() {
        ProjectId projectId = getProjectId();
		if (getSelectedEntity().isPresent()) {
			presenter.setChangesForEntity(projectId, getSelectedEntity().get());

        }
	}

}

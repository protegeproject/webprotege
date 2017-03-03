package edu.stanford.bmir.protege.web.client.change;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

@Portlet(id = "portlets.ChangesByEntity",
        title = "Entity Changes",
        tooltip = "Displays a list of project changes for the selected entity.")
public class EntityChangesPortlet extends AbstractWebProtegePortlet {

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final ChangeListViewPresenter presenter;

    @Inject
	public EntityChangesPortlet(SelectionModel selectionModel,
                                EventBus eventBus,
                                ProjectId projectId,
                                LoggedInUserProvider loggedInUserProvider,
                                ChangeListViewPresenter presenter) {
		super(selectionModel, eventBus, projectId);
        this.presenter = presenter;
        setWidget(presenter.getView().asWidget());

        addProjectEventHandler(ProjectChangedEvent.TYPE, event -> EntityChangesPortlet.this.handleProjectChanged(event));
        addApplicationEventHandler(ON_PERMISSIONS_CHANGED, event -> updateDisplayForSelectedEntity());
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

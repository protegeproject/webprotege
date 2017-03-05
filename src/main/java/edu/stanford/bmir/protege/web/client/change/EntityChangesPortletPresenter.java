package edu.stanford.bmir.protege.web.client.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
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
public class EntityChangesPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final String FORBIDDEN_MESSAGE = "You do not have permission to view changes for this project";

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final ChangeListViewPresenter presenter;

    @Inject
	public EntityChangesPortletPresenter(SelectionModel selectionModel,
                                         ProjectId projectId,
                                         ChangeListViewPresenter presenter) {
		super(selectionModel, projectId);
        this.presenter = presenter;
	}

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(getProjectId(),
                                        ProjectChangedEvent.TYPE, event -> handleProjectChanged(event));
        eventBus.addApplicationEventHandler(ON_PERMISSIONS_CHANGED, event -> updateDisplayForSelectedEntity());
        portletUi.setWidget(presenter.getView().asWidget());
        portletUi.setForbiddenMessage(FORBIDDEN_MESSAGE);
        portletUi.setViewTitle("Changes");
        updateDisplayForSelectedEntity();
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
    protected void handleAfterSetEntity(java.util.Optional<OWLEntity> entity) {
        updateDisplayForSelectedEntity();
    }

    private void updateDisplayForSelectedEntity() {
        ProjectId projectId = getProjectId();
		if (getSelectedEntity().isPresent()) {
			presenter.setChangesForEntity(projectId, getSelectedEntity().get());
        }
        presenter.handlePermissionsChanged();
	}

}

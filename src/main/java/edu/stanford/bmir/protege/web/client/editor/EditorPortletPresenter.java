package edu.stanford.bmir.protege.web.client.editor;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@Portlet(id = "portlets.EntityEditor",
        title = "Entity Editor",
        tooltip = "Displays a simple property-value oriented description of the selected class, property or individual for viewing and editing.")
public class EditorPortletPresenter extends AbstractWebProtegePortletPresenter {

    private EditorPresenter editorPresenter;

    private final Widget editorView;

    @Inject
    public EditorPortletPresenter(
            SelectionModel selectionModel,
            final ProjectId projectId,
            EditorPresenter editorPresenter) {
        super(selectionModel, projectId);
        this.editorPresenter = editorPresenter;
        editorView = editorPresenter.getView();
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setViewTitle("Nothing selected");
        portletUi.setWidget(editorView);
        editorPresenter.updatePermissionBasedItems();
        eventBus.addProjectEventHandler(getProjectId(),
                                        PermissionsChangedEvent.ON_PERMISSIONS_CHANGED,
                                        event -> editorPresenter.updatePermissionBasedItems());
        eventBus.addApplicationEventHandler(UserLoggedInEvent.ON_USER_LOGGED_IN,
                                            event -> editorPresenter.updatePermissionBasedItems());
        eventBus.addApplicationEventHandler(UserLoggedOutEvent.ON_USER_LOGGED_OUT,
                                            event -> editorPresenter.updatePermissionBasedItems());
        handleAfterSetEntity(getSelectedEntity());

    }

    @Override
    protected void handleAfterSetEntity(java.util.Optional<OWLEntity> entity) {
        if(!entity.isPresent()) {
            setViewTitle("Nothing selected");
            return;
        }
        setViewTitle(entity.get().getEntityType().getPrintName() + " description");
        final Optional<OWLEntityContext> editorContext = getEditorContext(entity, getProjectId());
        editorPresenter.setEditorContext(editorContext, this);
    }

    public static Optional<OWLEntityContext> getEditorContext(java.util.Optional<OWLEntity> sel, ProjectId projectId) {
        if(!sel.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new OWLEntityContext(projectId, sel.get()));
    }

    @Override
    public void dispose() {
        editorPresenter.dispose();
        super.dispose();
    }
}

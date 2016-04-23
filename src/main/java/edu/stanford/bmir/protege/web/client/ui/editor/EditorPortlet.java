package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.portlet.Portlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@Portlet(id = "edu.stanford.bmir.protege.web.client.ui.editor.EditorPortlet", displayName = "Editor")
public class EditorPortlet extends AbstractWebProtegePortlet {

    private EditorPresenter editorPresenter;

    @Inject
    public EditorPortlet(
            SelectionModel selectionModel,
            final EventBus eventBus,
            final ProjectId projectId,
            LoggedInUserProvider loggedInUserProvider,
            EditorPresenter editorPresenter) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.editorPresenter = editorPresenter;
        setTitle("Nothing selected");
        final Widget editorView = editorPresenter.getView();
        setWidget(editorView);
    }

    @Override
    public void handleActivated() {
        editorPresenter.updatePermissionBasedItems();
        handleAfterSetEntity(getSelectedEntity());
    }

    @Override
    public void handlePermissionsChanged() {
        editorPresenter.updatePermissionBasedItems();
    }

    @Override
    protected void handleLogin(UserId userId) {
        editorPresenter.updatePermissionBasedItems();
    }

    @Override
    protected void handleLogout(UserId userId) {
        editorPresenter.updatePermissionBasedItems();
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if(!entity.isPresent()) {
            setTitle("Nothing selected");
            return;
        }
        setTitle(entity.get().getEntityType().getPrintName() + " description");
        final Optional<EditorCtx> editorContext = getEditorContext(entity, getProjectId());
        editorPresenter.setEditorContext(editorContext);
    }

    public static Optional<EditorCtx> getEditorContext(Optional<OWLEntity> sel, ProjectId projectId) {
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

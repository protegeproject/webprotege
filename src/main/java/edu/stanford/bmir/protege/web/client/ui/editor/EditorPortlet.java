package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class EditorPortlet extends AbstractOWLEntityPortlet {

    private EditorPresenter editorPresenter;

    @Inject
    public EditorPortlet(
            SelectionModel selectionModel,
                         EventBus eventBus,
                         ProjectId projectId,
                         LoggedInUserProvider loggedInUserProvider,
            EditorPresenter editorPresenter) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.editorPresenter = editorPresenter;
        setTitle("Nothing selected");
        editorPresenter.addEditorContextChangedHandler(new EditorContextChangedHandler() {
            @Override
            public void handleEditorContextChanged(EditorContextChangedEvent editorContextChangedEvent) {
                setTitle(editorContextChangedEvent.getContextDescription());
            }
        });

        final Widget editorHolder = editorPresenter.getEditorHolder();
        editorHolder.setSize("100%", "100%");
        ScrollPanel sp = new ScrollPanel(editorHolder);
        getContentHolder().setWidget(sp);
    }

    @Override
    public void handleActivated() {
        editorPresenter.updatePermissionBasedItems();
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if(!entity.isPresent()) {
            // TODO: Show nothing selected
            return;
        }
        final Optional<EditorCtx> editorContext = getEditorContext(entity);
        editorPresenter.setEditorContext(editorContext);
    }

    public Optional<EditorCtx> getEditorContext(Optional<OWLEntity> sel) {
        if(!sel.isPresent()) {
            return Optional.absent();
        }
        return Optional.<EditorCtx>of(new OWLEntityDataContext(getProjectId(), sel.get()));
    }

//    @Override
    protected void onDestroy() {
        editorPresenter.dispose();
    }
}

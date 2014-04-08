package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class EditorPortlet extends AbstractOWLEntityPortlet {

    private EditorPresenter editorPresenter;

    public EditorPortlet(Project project) {
        super(project);
    }

    public EditorPortlet(Project project, boolean initialize) {
        super(project, initialize);
    }

    @Override
    public void reload() {
        Optional<OWLEntityData> sel = getSelectedEntityData();
        if(!sel.isPresent()) {
            // TODO: Show nothing selected
            return;
        }
        final Optional<EditorCtx> editorContext = getEditorContext();
        editorPresenter.setEditorContext(editorContext);

    }



    @Override
    public void initialize() {
        setTitle("Nothing selected");
        EditorContextMapper contextMapper = new EditorContextMapper();
        contextMapper.registerSelector(new EntityDataContextSelector());

        editorPresenter = new EditorPresenter(getProjectId(), contextMapper);
        editorPresenter.addEditorContextChangedHandler(new EditorContextChangedHandler() {
            @Override
            public void handleEditorContextChanged(EditorContextChangedEvent editorContextChangedEvent) {
                setTitle(editorContextChangedEvent.getContextDescription());
            }
        });

        final Widget editorHolder = editorPresenter.getEditorHolder();
//        editorHolder.setSize("100%", "100%");
        ScrollPanel sp = new ScrollPanel(editorHolder);
        sp.setSize("100%", "100%");
        add(sp);
    }

    @Override
    public Collection<EntityData> getSelection() {
        return null;
    }

    public Optional<EditorCtx> getEditorContext() {
        Optional<OWLEntityData> sel = getSelectedEntityData();
        if(!sel.isPresent()) {
            return Optional.absent();
        }
        return Optional.<EditorCtx>of(new OWLEntityDataContext(getProjectId(), sel.get()));
    }

    @Override
    protected void onDestroy() {
        editorPresenter.dispose();
    }
}

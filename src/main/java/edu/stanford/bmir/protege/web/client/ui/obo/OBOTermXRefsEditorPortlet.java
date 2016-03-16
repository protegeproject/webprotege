package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2012
 */
public class OBOTermXRefsEditorPortlet extends AbstractOBOTermPortlet {

    private final XRefListEditor editor;
    

    @Inject
    public OBOTermXRefsEditorPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        editor = new XRefListEditor();
        getContentHolder().setWidget(editor);
        editor.setEnabled(true);
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        if(!editor.getValue().isPresent()) {
            return;
        }
        getService().setXRefs(getProjectId(), entity, editor.getValue().get(), new OBOTermEditorApplyChangesAsyncCallback("Your changes to the term XRefs have not been applied"));
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        getService().getXRefs(getProjectId(), entity, new AbstractWebProtegeAsyncCallback<List<OBOXRef>>() {
            public void onSuccess(List<OBOXRef> result) {
                editor.setValue(result);
            }
        });
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected String getTitlePrefix() {
        return "XRefs";
    }
}

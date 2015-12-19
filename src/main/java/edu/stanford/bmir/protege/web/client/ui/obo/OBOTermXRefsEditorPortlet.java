package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2012
 */
public class OBOTermXRefsEditorPortlet extends AbstractOBOTermPortlet {

    private XRefListEditor editor;
    
    
    public OBOTermXRefsEditorPortlet(SelectionModel selectionModel, EventBus eventBus, Project project) {
        super(selectionModel, eventBus, project);

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

    @Override
    public void initialize() {
        editor = new XRefListEditor();
        add(editor);
        editor.setEnabled(true);
    }
}

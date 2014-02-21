package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2012
 */
public class OBOTermXRefsEditorPortlet extends AbstractOBOTermPortlet {

    private XRefListEditor editor;
    
    
    public OBOTermXRefsEditorPortlet(Project project) {
        super(project);

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
        getService().getXRefs(getProjectId(), entity, new AsyncCallback<List<OBOXRef>>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert(caught.getMessage());
                GWT.log(caught.getMessage(), caught);
            }

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
    protected void updateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("XRefs");
        EntityData entityData = getEntity();
        if(entityData != null) {
            sb.append(" for ");
            sb.append(entityData.getBrowserText());
        }
        setTitle(sb.toString());
    }

    @Override
    public void initialize() {
        editor = new XRefListEditor();
        add(editor);
    }

    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}

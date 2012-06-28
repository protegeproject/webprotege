package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Cls;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermRelationshipPortlet extends AbstractOBOTermPortlet {
    
    private OBOTermRelationshipEditor editor;

    public OBOTermRelationshipPortlet(Project project) {
        super(project);
    }


    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(Entity entity) {
        if(!(entity instanceof Cls)) {
            return;
        }
        OBOTermRelationships relationships = editor.getValue();
        getService().setRelationships(getProjectId(), (Cls) entity, relationships, new OBOTermEditorApplyChangesAsyncCallback("Your changes to the term relationships have not been applied"));
    }

    @Override
    protected void displayEntity(Entity entity) {
        Entity current = getCurrentEntity();
        if(!(current instanceof Cls)) {
            editor.clearValue();
            return;
        }
        getService().getRelationships(getProjectId(),  (Cls) current, new AsyncCallback<OBOTermRelationships>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert(caught.getMessage());
            }

            public void onSuccess(OBOTermRelationships result) {
                editor.setValue(result);
            }
        });
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    protected void updateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Relationships");
        EntityData currentEntity = getEntity();
        if(currentEntity != null) {
            sb.append(" for ");
            sb.append(currentEntity.getBrowserText());
        }
        setTitle(sb.toString());
    }

    @Override
    public void initialize() {
        editor = new OBOTermRelationshipEditor(getProjectId());
        OBOTermEditorView view = new OBOTermEditorView(editor);
        add(view);
    }
    

    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}

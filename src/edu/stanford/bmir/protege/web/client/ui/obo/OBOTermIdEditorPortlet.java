package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBONamespace;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermId;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTermIdEditorPortlet extends AbstractOBOTermPortlet {

    private OBOTermIdEditor editor;

    public OBOTermIdEditorPortlet(Project project) {
        super(project);
        setAutoScroll(false);
        setHeight("auto");
    }

    @Override
    public void initialize() {
        editor = new OBOTermIdEditor();
        add(new OBOTermEditorView(editor));
        getService().getNamespaces(getProjectId(), new AsyncCallback<Set<OBONamespace>>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert(caught.getMessage());
            }

            public void onSuccess(Set<OBONamespace> result) {
                editor.setAvailableNamespaces(result);
            }
        });
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void displayEntity(Entity entity) {
        OBOTextEditorServiceAsync service = getService();
        service.getTermId(getProjectId(), entity, new AsyncCallback<OBOTermId>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert(caught.getMessage());
            }

            public void onSuccess(OBOTermId result) {
                editor.setValue(result);
            }
        });

    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(Entity entity) {
        OBOTermId editedTermId = editor.getValue();
        getService().setTermId(getProjectId(), entity, editedTermId, new OBOTermEditorApplyChangesAsyncCallback("Your changes to the term Id have not been applied."));

    }

    protected void updateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Term ID Information");
        EntityData entityData = getEntity();
        if (entityData != null) {
            sb.append(" for ");
            sb.append(entityData.getBrowserText());
        }
        setTitle(sb.toString());
    }


    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}

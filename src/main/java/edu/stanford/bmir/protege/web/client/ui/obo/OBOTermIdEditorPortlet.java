package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.obo.OBONamespace;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermId;
import org.semanticweb.owlapi.model.OWLEntity;

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
        editor = new OBOTermIdEditorImpl();
        add(editor.asWidget());
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
    protected void displayEntity(OWLEntity entity) {
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
    protected void commitChangesForEntity(OWLEntity entity) {
        Optional<OBOTermId> editedTermId = editor.getValue();
        if (editedTermId.isPresent()) {
            getService().setTermId(getProjectId(), entity, editedTermId.get(), new OBOTermEditorApplyChangesAsyncCallback("Your changes to the term Id have not been applied."));
        }

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
}

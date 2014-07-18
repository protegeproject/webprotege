package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermCrossProduct;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermCrossProductPortlet extends AbstractOBOTermPortlet {

    private OBOTermCrossProductEditor editor;
    
    public OBOTermCrossProductPortlet(Project project) {
        super(project);
    }


    @Override
    public void initialize() {
        editor = new OBOTermCrossProductEditorImpl();
        add(editor.asWidget());
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        if(!(entity.isOWLClass())) {
            editor.clearValue();
        }
        else {
            getService().getCrossProduct(getProjectId(), (OWLClass) entity, new AsyncCallback<OBOTermCrossProduct>() {
                public void onFailure(Throwable caught) {
                    MessageBox.alert(caught.getMessage());
                    GWT.log(caught.getMessage(), caught);
                }

                public void onSuccess(OBOTermCrossProduct result) {
                    editor.setValue(result);
                }
            });
        }
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        if(!(entity.isOWLClass())) {
            return;
        }
        if(!editor.getValue().isPresent()) {
            return;
        }
        OBOTermCrossProduct crossProduct = editor.getValue().get();
        getService().setCrossProduct(getProjectId(), (OWLClass) entity, crossProduct, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                if(caught instanceof NotSignedInException) {
                    MessageBox.alert("You are not signed in.  Changes not saved.  You must be signed in for your changes to be saved.");
                }
                else {
                    MessageBox.alert(caught.getMessage());
                    GWT.log(caught.getMessage(), caught);
                }
            }

            public void onSuccess(Void result) {
            }
        });
    }

    
    protected void updateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cross product");
        EntityData entityData = getEntity();
        if(entityData != null) {
            sb.append(" for ");
            sb.append(entityData.getBrowserText());
        }
        setTitle(sb.toString());
    }
}

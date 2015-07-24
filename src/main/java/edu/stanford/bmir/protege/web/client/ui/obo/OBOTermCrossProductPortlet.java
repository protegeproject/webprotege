package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermCrossProductPortlet extends AbstractOBOTermPortlet {

    private OBOTermCrossProductEditor editor;

    private Optional<OBOTermCrossProduct> pristineValue = Optional.absent();
    
    public OBOTermCrossProductPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
    }


    @Override
    public void initialize() {
        editor = new OBOTermCrossProductEditorImpl();
        add(editor.asWidget());
    }

    @Override
    protected void clearDisplay() {
        pristineValue = Optional.absent();
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
                    pristineValue = Optional.of(result);
                    editor.setValue(result);
                }
            });
        }
    }

    @Override
    protected boolean isDirty() {
        return !pristineValue.equals(editor.getValue());
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

    @Override
    protected String getTitlePrefix() {
        return "Cross product";
    }
}

package edu.stanford.bmir.protege.web.client.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.NotSignedInException;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
@Portlet(id = "portlets.obo.TermCrossProduct", title = "OBO Term Cross Product")
public class OBOTermCrossProductPortlet extends AbstractOBOTermPortlet {

    private OBOTermCrossProductEditor editor;

    private Optional<OBOTermCrossProduct> pristineValue = Optional.absent();

    @Inject
    public OBOTermCrossProductPortlet(OBOTermCrossProductEditor editor,
                                      SelectionModel selectionModel,
                                      ProjectId projectId) {
        super(selectionModel, projectId);
        this.editor = editor;
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
    }

    @Override
    protected void clearDisplay() {
        pristineValue = Optional.absent();
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        if (!(entity.isOWLClass())) {
            editor.clearValue();
        }
        else {
            getService().getCrossProduct(getProjectId(), (OWLClass) entity, new AsyncCallback<OBOTermCrossProduct>() {
                public void onFailure(Throwable caught) {
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
        if (!(entity.isOWLClass())) {
            return;
        }
        if (!editor.getValue().isPresent()) {
            return;
        }
        OBOTermCrossProduct crossProduct = editor.getValue().get();
        getService().setCrossProduct(getProjectId(), (OWLClass) entity, crossProduct, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                if (caught instanceof NotSignedInException) {
                    MessageBox.showMessage(
                            "You are not signed in.  Changes not saved.  You must be signed in for your changes to be saved.");
                }
                else {
                    MessageBox.showMessage(caught.getMessage());
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

package edu.stanford.bmir.protege.web.client.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermDefinition;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.NotSignedInException;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
@Portlet(id = "portlets.obo.TermDefinition", title = "OBO Term Definition")
public class OBOTermDefinitionPortletPresenter extends AbstractOBOTermPortletPresenter {

    private OBOTermDefinitionEditor editor;

    @Inject
    public OBOTermDefinitionPortletPresenter(ProjectId projectId, SelectionModel selectionModel) {
        super(selectionModel, projectId);
        editor = new OBOTermDefinitionEditorImpl();
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        if (!editor.getValue().isPresent()) {
            return;
        }
        getService().setDefinition(getProjectId(), entity, editor.getValue().get(), new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                if (caught instanceof NotSignedInException) {
                    MessageBox.showMessage(
                            "Your changes to the term definition have not been saved.  You must be signed in to make changes.");
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
    protected void displayEntity(OWLEntity entity) {
        getService().getDefinition(getProjectId(), entity, new AsyncCallback<OBOTermDefinition>() {
            public void onFailure(Throwable caught) {
                MessageBox.showMessage(caught.getMessage());
            }

            public void onSuccess(OBOTermDefinition result) {
                if (result == null) {
                    editor.clearValue();
                }
                else {
                    editor.setValue(result);
                }
                updateTitle();
            }
        });
    }

    @Override
    protected String getTitlePrefix() {
        return "Definition";
    }

}

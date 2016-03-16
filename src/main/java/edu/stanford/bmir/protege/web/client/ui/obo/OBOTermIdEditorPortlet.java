package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorServiceAsync;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.obo.OBONamespace;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTermIdEditorPortlet extends AbstractOBOTermPortlet {

    private OBOTermIdEditor editor;

    @Inject
    public OBOTermIdEditorPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
//        setAutoScroll(false);
        editor = new OBOTermIdEditorImpl();
        getContentHolder().setWidget(editor.asWidget());
        getService().getNamespaces(getProjectId(), new AsyncCallback<Set<OBONamespace>>() {
            public void onFailure(Throwable caught) {
                MessageBox.showMessage(caught.getMessage());
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
                MessageBox.showMessage(caught.getMessage());
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

    @Override
    protected String getTitlePrefix() {
        return "Term ID Information";
    }
}

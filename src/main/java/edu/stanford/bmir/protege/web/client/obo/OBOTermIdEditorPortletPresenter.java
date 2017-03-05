package edu.stanford.bmir.protege.web.client.obo;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorServiceAsync;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.OBONamespace;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
@Portlet(id = "portlets.obo.TermId", title = "OBO Term Id")
public class OBOTermIdEditorPortletPresenter extends AbstractOBOTermPortletPresenter {

    private OBOTermIdEditor editor;

    @Inject
    public OBOTermIdEditorPortletPresenter(SelectionModel selectionModel, ProjectId projectId) {
        super(selectionModel, projectId);
//        setAutoScroll(false);
        editor = new OBOTermIdEditorImpl();
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
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
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

package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2012
 */
@Portlet(id = "portlets.obo.TermXRefs", title = "OBO Term XRefs")
public class OBOTermXRefsEditorPortletPresenter extends AbstractOBOTermPortletPresenter {

    private final XRefListEditor editor;
    

    @Inject
    public OBOTermXRefsEditorPortletPresenter(SelectionModel selectionModel, ProjectId projectId) {
        super(selectionModel, projectId);
        editor = new XRefListEditor();
        editor.setEnabled(true);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor);
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
        getService().getXRefs(getProjectId(), entity, new AbstractWebProtegeAsyncCallback<List<OBOXRef>>() {
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
    protected String getTitlePrefix() {
        return "XRefs";
    }
}

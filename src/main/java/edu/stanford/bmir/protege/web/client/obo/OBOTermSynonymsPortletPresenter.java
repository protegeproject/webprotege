package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
@Portlet(id = "portlets.obo.TermSynonyms", title = "OBO Term Synonyms")
public class OBOTermSynonymsPortletPresenter extends AbstractOBOTermPortletPresenter {

    private OBOTermSynonymListEditor editor;

    @Inject
    public OBOTermSynonymsPortletPresenter(SelectionModel selectionModel, ProjectId projectId) {
        super(selectionModel, projectId);
        editor = new OBOTermSynonymListEditor();
        editor.setEnabled(true);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor);
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        getService().getSynonyms(getProjectId(), entity, new AbstractWebProtegeAsyncCallback<Collection<OBOTermSynonym>>() {
            public void onSuccess(Collection<OBOTermSynonym> result) {
                editor.setValue(new ArrayList<>(result));
            }
        });
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        Optional<List<OBOTermSynonym>> synonyms = editor.getValue();
        if(!synonyms.isPresent()) {
            return;
        }
        getService().setSynonyms(getProjectId(), entity, synonyms.get(), new OBOTermEditorApplyChangesAsyncCallback("Your changes to the term synonyms have not been applied."));
    }

    @Override
    protected String getTitlePrefix() {
        return "Synonyms";
    }

}

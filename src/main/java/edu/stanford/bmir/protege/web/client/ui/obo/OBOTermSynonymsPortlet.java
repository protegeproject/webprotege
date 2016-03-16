package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTermSynonymsPortlet extends AbstractOBOTermPortlet {

    private OBOTermSynonymListEditor editor;

    @Inject
    public OBOTermSynonymsPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        editor = new OBOTermSynonymListEditor();
        editor.setEnabled(true);
        getContentHolder().setWidget(editor);
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

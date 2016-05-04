package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

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
    
    public OBOTermSynonymsPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
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

    @Override
    public void initialize() {
        editor = new OBOTermSynonymListEditor();
        editor.setEnabled(true);
        add(editor);
    }

}

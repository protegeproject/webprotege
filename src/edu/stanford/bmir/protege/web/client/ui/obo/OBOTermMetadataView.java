package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorService;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class OBOTermMetadataView extends FlowPanel {

    private final OBOTermIdEditor idEditor;

    private final OBOTermDefinitionEditor definitionEditor;

    private final OBOTermSynonymListEditor synonymListEditor;
    
    private final OBOTermRelationshipEditor relationshipEditor;
    
    private final OBOTermCrossProductEditor crossProductEditor;
    
    private final OBOTermEditorView view;

    private final OBOTextEditorServiceAsync service =  GWT.create(OBOTextEditorService.class);

    public OBOTermMetadataView(ProjectId projectId) {
        List<OBOTermEditorGroup> editorGroups = new ArrayList<OBOTermEditorGroup>();
        List<OBOTermEditor> metadataEditors = new ArrayList<OBOTermEditor>();

        idEditor = new OBOTermIdEditor();
        metadataEditors.add(idEditor);
        definitionEditor = new OBOTermDefinitionEditor();
        metadataEditors.add(definitionEditor);
        relationshipEditor = new OBOTermRelationshipEditor(projectId);
        metadataEditors.add(relationshipEditor);
        crossProductEditor = new OBOTermCrossProductEditor(projectId);
        metadataEditors.add(crossProductEditor);


        
        synonymListEditor = new OBOTermSynonymListEditor();
        
        

        
        OBOTermEditorGroup metadataGroup = new OBOTermEditorGroup(metadataEditors);
        editorGroups.add(metadataGroup);
        
        editorGroups.add(synonymListEditor);
        view = new OBOTermEditorView(editorGroups);
        add(view);
    }
    
    public void reload(ProjectId projectId, OWLEntity subject) {
        if (subject != null) {
            service.getTermId(projectId, subject, new AsyncCallback<OBOTermId>() {
                public void onFailure(Throwable caught) {
                }

                public void onSuccess(OBOTermId result) {
                    idEditor.setValue(result);
                    view.rebuild();
                }
            });

            service.getDefinition(projectId, subject, new AsyncCallback<OBOTermDefinition>() {
                public void onFailure(Throwable caught) {
                }

                public void onSuccess(OBOTermDefinition result) {
                    definitionEditor.setValue(result);
                    view.rebuild();
                }
            });

            service.getRelationships(projectId, (OWLClass) subject, new AsyncCallback<OBOTermRelationships>() {
                public void onFailure(Throwable caught) {
                }

                public void onSuccess(OBOTermRelationships result) {
                    relationshipEditor.setValue(result);
                    view.rebuild();
                }
            });

            service.getCrossProduct(projectId, (OWLClass) subject, new AsyncCallback<OBOTermCrossProduct>() {
                public void onFailure(Throwable caught) {
                }

                public void onSuccess(OBOTermCrossProduct result) {
                    crossProductEditor.setValue(result);
                    view.rebuild();
                }
            });
            service.getSynonyms(projectId, subject, new AsyncCallback<Collection<OBOTermSynonym>>() {
                public void onFailure(Throwable caught) {
                }

                public void onSuccess(Collection<OBOTermSynonym> result) {
                    synonymListEditor.setValues(new ArrayList<OBOTermSynonym>(result));
                    view.rebuild();
                }
            });
        }
        else {
            view.rebuild();
        }
    }

    public boolean isDirty() {
        return idEditor.isDirty() || definitionEditor.isDirty() || relationshipEditor.isDirty() || crossProductEditor.isDirty() || synonymListEditor.isDirty();
    }

    public void commit(ProjectId projectId, OWLEntity subject) {
        if(idEditor.isDirty()) {
            service.setTermId(projectId, subject, idEditor.getValue(), new OBOTermEditorApplyChangesAsyncCallback());
        }
        if(definitionEditor.isDirty()) {
            service.setDefinition(projectId, subject, definitionEditor.getValue(), new OBOTermEditorApplyChangesAsyncCallback());
        }
        if(relationshipEditor.isDirty()) {
            service.setRelationships(projectId, (OWLClass) subject, relationshipEditor.getValue(), new OBOTermEditorApplyChangesAsyncCallback());
        }
        if(crossProductEditor.isDirty()) {
            service.setCrossProduct(projectId, (OWLClass) subject, crossProductEditor.getValue(), new OBOTermEditorApplyChangesAsyncCallback());
        }
        if(synonymListEditor.isDirty()) {
            service.setSynonyms(projectId, subject, synonymListEditor.getValues(), new OBOTermEditorApplyChangesAsyncCallback());
        }
    }
}

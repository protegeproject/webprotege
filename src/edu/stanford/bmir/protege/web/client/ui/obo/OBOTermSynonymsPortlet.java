package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermSynonym;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTermSynonymsPortlet extends AbstractOBOTermPortlet {

    private OBOTermSynonymListEditor editor;
    
    private OBOTermEditorView editorView;

    public OBOTermSynonymsPortlet(Project project) {
        super(project);
    }

    @Override
    protected void clearDisplay() {
        editor.setValues(Collections.<OBOTermSynonym>emptyList());
        editorView.rebuild();
    }

    @Override
    protected void displayEntity(Entity entity) {
        getService().getSynonyms(getProjectId(), entity, new AsyncCallback<Collection<OBOTermSynonym>>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Collection<OBOTermSynonym> result) {
                editor.setValues(new ArrayList<OBOTermSynonym>(result));
                editorView.rebuild();
            }
        });
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(Entity entity) {
        List<OBOTermSynonym> synonyms = editor.getValues();
        getService().setSynonyms(getProjectId(), entity, synonyms, new OBOTermEditorApplyChangesAsyncCallback("Your changes to the term synonyms have not been applied."));
    }

    @Override
    protected void updateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Synonyms");
        EntityData entityData = getEntity();
        if(entityData != null) {
            sb.append(" for ");
            sb.append(entityData.getBrowserText());
        }
        setTitle(sb.toString());
    }



    @Override
    public void initialize() {
        editor = new OBOTermSynonymListEditor();
        editorView = new OBOTermEditorView(editor);
        add(editorView);
    }

//    private void addAddButton() {
//        add(new Button("Add synonym", new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                handleAdd();
//            }
//        }));
//    }

//    private void handleAdd() {
//        List<OBOTermSynonym> synonymList = new ArrayList<OBOTermSynonym>(editor.getValues());
//        synonymList.add(new OBOTermSynonym());
//        editor.setValues(synonymList);
//        editorView.rebuild();
//    }

    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}

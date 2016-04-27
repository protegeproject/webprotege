package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
@Portlet(id = "portlets.obo.TermRelationships", title = "OBO Term Relationships")
public class OBOTermRelationshipPortlet extends AbstractOBOTermPortlet {
    
    private final OBOTermRelationshipEditor editor;

    private Optional<List<OBORelationship>> pristineValue = Optional.absent();

    @Inject
    public OBOTermRelationshipPortlet(OBOTermRelationshipEditor editor, ProjectId projectId, EventBus eventBus, SelectionModel selectionModel, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.editor = editor;
        this.editor.setEnabled(true);
        setWidget(this.editor.getWidget());
    }


    @Override
    protected boolean isDirty() {
        boolean dirty = !editor.getValue().equals(pristineValue);
        GWT.log("OBO Term Relationship Portlet: isDirty = " + dirty);
        return dirty;
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        if(!(entity instanceof OWLClass)) {
            return;
        }
        List<OBORelationship> relationships = editor.getValue().or(Collections.<OBORelationship>emptyList());
        getService().setRelationships(getProjectId(), (OWLClass) entity, new OBOTermRelationships(new HashSet<OBORelationship>(relationships)), new OBOTermEditorApplyChangesAsyncCallback("Your changes to the term relationships have not been applied"));
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        com.google.common.base.Optional<OWLEntity> current = getSelectedEntity();
        if(!current.isPresent()) {
            editor.clearValue();
            pristineValue = Optional.absent();
            return;
        }
        if(!(current.get() instanceof OWLClass)) {
            editor.clearValue();
            pristineValue = Optional.absent();
            return;
        }
        getService().getRelationships(getProjectId(),  (OWLClass) current.get(), new AsyncCallback<OBOTermRelationships>() {
            public void onFailure(Throwable caught) {
                MessageBox.showMessage(caught.getMessage());
            }

            public void onSuccess(OBOTermRelationships result) {
                Set<OBORelationship> relationships = result.getRelationships();
                List<OBORelationship> listOfRels = new ArrayList<OBORelationship>(relationships);
                pristineValue = Optional.of(listOfRels);
                editor.setValue(listOfRels);
            }
        });
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
        pristineValue = Optional.absent();
    }

    @Override
    protected String getTitlePrefix() {
        return "Relationships";
    }

}

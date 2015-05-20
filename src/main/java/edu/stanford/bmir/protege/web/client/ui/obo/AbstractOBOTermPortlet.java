package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorService;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public abstract class AbstractOBOTermPortlet extends AbstractOWLEntityPortlet {

    private static final OBOTextEditorServiceAsync SERVICE = GWT.create(OBOTextEditorService.class);

    protected AbstractOBOTermPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
        addStyleName("web-protege-laf");
        setHeight("300px");
        setAutoScroll(true);
    }

    public OBOTextEditorServiceAsync getService() {
        return SERVICE;
    }

    @Override
    protected void handleBeforeSetEntity(Optional<OWLEntityData> existingEntity) {
        if(existingEntity.isPresent() && isDirty()) {
            commitChangesForEntity(existingEntity.get().getEntity());
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        if(entityData.isPresent()) {
            displayEntity(entityData.get().getEntity());
        }
        else {
//            clearDisplay();
        }
        updateTitle();
    }

    /**
     * Called to determined whether or not any edits have been made to the current entity via the UI
     * @return <code>true</code> if edits have been made, otherwise <code>false</code>.
     */
    protected abstract boolean isDirty();

    /**
     * Called to commit any changes that have been made.  This method will only be called by the system if the
     * {@link #isDirty()} method returns <code>true</code>.
     * @param entity The entity being edited not <code>null</code>.
     */
    protected abstract void commitChangesForEntity(OWLEntity entity);

    /**
     * Called to update the display so that it displays a given entity.
     * @param entity The entity to be displayed. Not <code>null</code>.
     */
    protected abstract void displayEntity(OWLEntity entity);

    /**
     * Called to clear the display if no entity is should be displayed.
     */
    protected abstract void clearDisplay();

    protected abstract String getTitlePrefix();

    /**
     * Called to update the title.  Implementations should compute a title and then set it using the {@link #setTitle(String)}
     * method.
     */
    final protected void updateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTitlePrefix());
        Optional<OWLEntityData> currentEntity = getSelectedEntityData();
        if(currentEntity.isPresent()) {
            sb.append(" for ");
            sb.append(currentEntity.get().getBrowserText());
        }
        setTitle(sb.toString());
    }

}

package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.WebProtege;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorService;
import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public abstract class AbstractOBOTermPortlet extends AbstractEntityPortlet {

    private static final OBOTextEditorServiceAsync SERVICE = GWT.create(OBOTextEditorService.class);

    protected AbstractOBOTermPortlet(Project project) {
        super(project);
        addStyleName("web-protege-laf");
        setHeight("300px");
        setAutoScroll(true);
    }

    public OBOTextEditorServiceAsync getService() {
        return SERVICE;
    }
    
    public ProjectId getProjectId() {
        return new ProjectId(getProject().getProjectName());
    }

    @Override
    final public void setEntity(EntityData newEntityData) {
        try {
            if(_currentEntity == newEntityData && newEntityData != null) {
                return;
            }
            Entity entity = getCurrentEntity();
            if (entity != null && isDirty()) {
                commitChangesForEntity(entity);
            }
            _currentEntity = newEntityData;
            if (_currentEntity != null) {
                Entity newEntity = getCurrentEntity();
                if (newEntity != null) {
                    displayEntity(newEntity);
                }
            }
            else {
                clearDisplay();
            }
            updateTitle();
        }
        catch (RuntimeException e) {
            GWT.log(e.getMessage(), e);
        }
    }

    @Override
    final public void reload() {

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
    protected abstract void commitChangesForEntity(Entity entity);

    /**
     * Called to update the display so that it displays a given entity.
     * @param entity The entity to be displayed. Not <code>null</code>.
     */
    protected abstract void displayEntity(Entity entity);

    /**
     * Called to clear the display if no entity is should be displayed.
     */
    protected abstract void clearDisplay();

    /**
     * Called to update the title.  Implementations should compute a title and then set it using the {@link #setTitle(String)}
     * method.
     */
    protected abstract void updateTitle();


    /**
     * Gets the current entity which should be displayed.
     * @return The current entity.  May be <code>null</code>.
     */
    public Entity getCurrentEntity() {
        EntityData entityData = getEntity();
        if(entityData == null) {
            return null;
        }
        WebProtegeIRI iri = new WebProtegeIRI(entityData.getName());
        if(entityData.getValueType() == ValueType.Cls) {
            return new Cls(iri);
        }
        else if(entityData.getValueType() == ValueType.Property) {
            return new ObjectProperty(iri);
        }
        else if(entityData.getValueType() == ValueType.Instance) {
            return new NamedIndividual(iri);
        }
        else {
            return null;
        }
    }
}

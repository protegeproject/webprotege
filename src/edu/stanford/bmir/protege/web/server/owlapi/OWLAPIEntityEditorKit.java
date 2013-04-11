package edu.stanford.bmir.protege.web.server.owlapi;


import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/05/2012
 * <p>
 *     In Web-Protege, and other tools, the primary mechanism for referring to entities in the UI is browser text.  The
 *     entity editor kit provides an interface for getting and setting this browser text.
 * </p>
 */
public abstract class OWLAPIEntityEditorKit {

    private OWLAPIProject project;

    protected OWLAPIEntityEditorKit(OWLAPIProject project) {
        this.project = project;
    }

    
    public OWLAPIProject getProject() {
        return project;
    }

    public abstract boolean isBrowserTextEditable(OWLEntity entity);

    /**
     * Gets the browser text for an entity.
     * @param entity The entity
     * @return The short form.  Not null.  All entities have a short form.
     */
    public abstract String getEntityBrowserText(OWLEntity entity);

    public abstract OWLEntityBrowserTextChangeSet setEntityBrowserText(OWLEntity entity, String browserText);


    public abstract OWLEntityCreatorFactory getEntityCreatorFactory();


    public abstract List<OWLEntityBrowserTextChangeSet> getChangedEntities(List<OWLOntologyChangeRecord> ontologyChanges);


    public abstract ShortFormProvider getShortFormProvider();


    public abstract void dispose();
}


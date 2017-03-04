package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public abstract class OWLOntologyChangeFactory {

    private String changeDescription;

    private OWLAPIProject project;

    private UserId userId;

    /**
     * Constructs a change enactor with the specified high level change description.
     * @param project The project which the changes are applied to.
     * @param userId The id of the user applying the change.
     * @param changeDescription The change description
     */
    protected OWLOntologyChangeFactory(OWLAPIProject project, UserId userId, String changeDescription) {
        this.project = project;
        this.userId = userId;
        this.changeDescription = changeDescription;
    }

    public OWLAPIProject getProject() {
        return project;
    }

    public UserId getUserId() {
        return userId;
    }

    public OWLOntology getRootOntology() {
        return project.getRootOntology();
    }

    public OWLOntologyManager getRootOntologyManager() {
        return getRootOntology().getOWLOntologyManager();
    }

    public OWLDataFactory getDataFactory() {
        return getRootOntologyManager().getOWLDataFactory();
    }

    public RenderingManager getRenderingManager() {
        return project.getRenderingManager();
    }

    protected IRI getIRIFromName(String name) {
        IRI basicIRI = IRI.create(name);
        if(basicIRI.isAbsolute()) {
            return basicIRI;
        }
        else {
            return IRI.create(getRootOntology().getOntologyID().getOntologyIRI() + "#" + name);
        }
    }


    public String getChangeDescription() {
        return changeDescription;
    }

    /**
     * Creates the necessary ontology changes to make the appropriate changes.  These changes will appended to the
     * specified list.
     * @param changeListToFill The list which the changes will be appended to.
     * @return The change object e.g. EntityData etc.
     */
    public abstract void createChanges(List<OWLOntologyChange> changeListToFill);


}

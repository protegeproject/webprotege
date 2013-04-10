package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableResult;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassesResult extends RenderableResult {

    private ObjectPath<OWLClass> superClassPathToRoot;

    private Set<OWLClass> createdClasses = new HashSet<OWLClass>();

    private CreateClassesResult() {
    }

    public CreateClassesResult(ObjectPath<OWLClass> superClassPathToRoot, Set<OWLClass> createdClasses, BrowserTextMap browserTextMap) {
        super(browserTextMap);
        this.superClassPathToRoot = superClassPathToRoot;
        this.createdClasses = createdClasses;
    }

    public ObjectPath<OWLClass> getSuperClassPathToRoot() {
        return superClassPathToRoot;
    }

    public Set<OWLClass> getCreatedClasses() {
        return new HashSet<OWLClass>(createdClasses);
    }
}

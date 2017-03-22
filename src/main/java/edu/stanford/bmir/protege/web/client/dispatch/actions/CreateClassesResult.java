package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class CreateClassesResult implements Result {

    private ObjectPath<OWLClass> superClassPathToRoot;

    private Set<OWLClassData> createdClasses = new HashSet<>();

    private CreateClassesResult() {
    }

    public CreateClassesResult(ObjectPath<OWLClass> superClassPathToRoot, Set<OWLClassData> createdClasses) {
        this.superClassPathToRoot = superClassPathToRoot;
        this.createdClasses = new HashSet<>(createdClasses);
    }

    public ObjectPath<OWLClass> getSuperClassPathToRoot() {
        return superClassPathToRoot;
    }

    public Set<OWLClassData> getCreatedClasses() {
        return new HashSet<>(createdClasses);
    }
}

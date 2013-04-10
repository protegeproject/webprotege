package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLClass;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class ClassEntry extends ClassFrameEntry implements Serializable {

    private OWLClass cls;

    private ClassEntry() {
    }

    public ClassEntry(OWLClass cls) {
        this.cls = cls;
    }

    public OWLClass getOWLClass() {
        return cls;
    }

    @Override
    public int hashCode() {
        return "OWLClassEntry".hashCode() + cls.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ClassEntry)) {
            return false;
        }
        ClassEntry other = (ClassEntry) obj;
        return this.cls.equals(other.cls);
    }
}

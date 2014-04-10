package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/12/2012
 */
public class AddClassChange extends ClassFrameChange {

    private OWLClass cls;

    public AddClassChange(OWLClass subject, OWLClass cls) {
        super(subject);
        this.cls = cls;
    }

    public OWLClass getOWLClass() {
        return cls;
    }

    @Override
    public int hashCode() {
        return "AddClassChange".hashCode() + getSubject().hashCode() + cls.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AddClassChange)) {
            return false;
        }
        AddClassChange other = (AddClassChange) obj;
        return this.cls.equals(other.cls) && this.getSubject().equals(other.getSubject());
    }
}

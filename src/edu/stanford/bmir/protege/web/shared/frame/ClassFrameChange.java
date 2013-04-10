package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/12/2012
 */
public abstract class ClassFrameChange {

    private OWLClass subject;

    protected ClassFrameChange(OWLClass subject) {
        this.subject = subject;
    }

    public OWLClass getSubject() {
        return subject;
    }
}

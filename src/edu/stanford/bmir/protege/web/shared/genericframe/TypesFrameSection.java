package edu.stanford.bmir.protege.web.shared.genericframe;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class TypesFrameSection extends FrameSection<OWLClass> {

    public TypesFrameSection() {
        super(new FrameSectionId("simple-types"), "Types", FrameSectionType.CLASS);
    }
}

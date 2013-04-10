package edu.stanford.bmir.protege.web.shared.genericframe;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class SubClassOfFrameSection extends FrameSection<OWLClass> {


    public SubClassOfFrameSection() {
        super(new FrameSectionId("SubClassOf"), "SubClassOf", FrameSectionType.CLASS);
    }
}

package edu.stanford.bmir.protege.web.shared.genericframe;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class AnnotationPropertiesFrameSection extends FrameSection<OWLAnnotationProperty> {

    public static final FrameSectionId ID = new FrameSectionId("annotation-properties");

    public static final String DISPLAY_NAME = "Annotation properties";

    public AnnotationPropertiesFrameSection() {
        super(ID, DISPLAY_NAME, FrameSectionType.ANNOTATION_PROPERTY);
    }
}

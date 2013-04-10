package edu.stanford.bmir.protege.web.shared.genericframe;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class TestFrames {

    public static void main(String[] args) {
        Frame<?> f = new Frame();
        AnnotationPropertiesFrameSection sec = new AnnotationPropertiesFrameSection();
        List<OWLAnnotationProperty> props = f.getFrameSectionValues(sec);

        f.addFrameSectionValue(sec, null);

        TypesFrameSection typesSection = new TypesFrameSection();
        f.addFrameSectionValue(typesSection, null);
        List<OWLClass> cls = f.getFrameSectionValues(typesSection);

        f.addFrameSectionValue(new SubClassOfFrameSection(), null);
        List<OWLClass> clses = f.getFrameSectionValues(new SubClassOfFrameSection());


    }
}

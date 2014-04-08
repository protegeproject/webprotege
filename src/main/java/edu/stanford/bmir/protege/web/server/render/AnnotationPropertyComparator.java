package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.util.ShortFormProvider;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public interface AnnotationPropertyComparator {

    public int compareTo(OWLAnnotationProperty property1, OWLAnnotationProperty property2, ShortFormProvider shortFormProvider);
}

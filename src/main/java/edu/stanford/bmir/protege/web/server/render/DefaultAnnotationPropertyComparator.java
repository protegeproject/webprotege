package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;

import java.util.Comparator;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class DefaultAnnotationPropertyComparator implements Comparator<OWLAnnotationProperty> {

    private ShortFormProvider shortFormProvider;

    private static final AnnotationPropertyIndexProvider indexProvider = new AnnotationPropertyIndexProvider();

    public DefaultAnnotationPropertyComparator(ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
    }

    @Override
    public int compare(OWLAnnotationProperty property1, OWLAnnotationProperty property2) {
        int index1 = indexProvider.getIndex(property1);
        int index2 = indexProvider.getIndex(property2);
        if(index1 != index2) {
            return index1 - index2;
        }

        String sf1 = shortFormProvider.getShortForm(property1);
        String sf2 = shortFormProvider.getShortForm(property2);
        // Some other label
        boolean sf1containsLabel = sf1.toLowerCase().contains("label");
        boolean sf2containsLabel = sf2.toLowerCase().contains("label");
        if(sf1containsLabel && !sf2containsLabel) {
            return -1;
        }
        if(sf2containsLabel && !sf1containsLabel) {
            return 1;
        }
        if(sf1.endsWith("definition")) {
            return -1;
        }
        if(sf2.endsWith("definition")) {
            return 1;
        }
        return sf1.compareToIgnoreCase(sf2);
    }

}

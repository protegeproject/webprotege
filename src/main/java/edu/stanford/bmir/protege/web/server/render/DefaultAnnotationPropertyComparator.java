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

    public DefaultAnnotationPropertyComparator(ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
    }

    @Override
    public int compare(OWLAnnotationProperty property1, OWLAnnotationProperty property2) {
        if(property1.isLabel()) {
            return -1;
        }
        if(property2.isLabel()) {
            return 1;
        }
        String sf1 = shortFormProvider.getShortForm(property1);
        String sf2 = shortFormProvider.getShortForm(property2);
        if(sf1.toLowerCase().contains("label")) {
            return -1;
        }
        if(sf2.toLowerCase().contains("label")) {
            return 1;
        }

        if(property1.getIRI().equals(DublinCoreVocabulary.TITLE.getIRI())) {
            return -1;
        }
        if(property2.getIRI().equals(DublinCoreVocabulary.TITLE.getIRI())) {
            return 1;
        }
        if(sf1.endsWith("definition")) {
            return -1;
        }
        if(sf2.endsWith("definition")) {
            return 1;
        }
        if(property1.getIRI().equals(DublinCoreVocabulary.DESCRIPTION.getIRI())) {
            return 1;
        }
        if(property2.getIRI().equals(DublinCoreVocabulary.DESCRIPTION.getIRI())) {
            return -1;
        }
        return sf1.compareTo(sf2);
    }

}

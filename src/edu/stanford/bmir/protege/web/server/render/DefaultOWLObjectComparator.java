package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.Comparator;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class DefaultOWLObjectComparator implements Comparator<OWLObject> {

    private ShortFormProvider shortFormProvider;

    @Override
    public int compare(OWLObject owlObject, OWLObject owlObject2) {
        return owlObject.compareTo(owlObject2);
    }
}

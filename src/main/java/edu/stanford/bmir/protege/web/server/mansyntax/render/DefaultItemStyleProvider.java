package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class DefaultItemStyleProvider implements ItemStyleProvider {

    @Override
    public Optional<String> getItemStyle(Object item) {
        if(item instanceof OWLClassExpression) {
            return Optional.of("ms-item-ce");
        }
        if(item instanceof OWLProperty) {
            return Optional.of("ms-item-prop");
        }
        if(item instanceof OWLAnnotationProperty) {
            return Optional.of("ms-item-anno");
        }
        if(item instanceof OWLIndividual) {
            return Optional.of("ms-item-ind");
        }
        if(item instanceof OWLDataRange) {
            return Optional.of("ms-item-dr");
        }
        return Optional.absent();

    }
}

package edu.stanford.bmir.protege.web.shared.object;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.inject.Inject;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class OWLIndividualSelector extends OWLEntitySelector<OWLIndividual, OWLNamedIndividual> {

    @Inject
    public OWLIndividualSelector(Comparator<? super OWLNamedIndividual> entityComparator) {
        super(EntityType.NAMED_INDIVIDUAL, entityComparator);
    }

    @Override
    protected OWLIndividual toType(OWLNamedIndividual entity) {
        return entity;
    }
}

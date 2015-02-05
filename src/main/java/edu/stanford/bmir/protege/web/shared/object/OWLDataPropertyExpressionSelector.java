package edu.stanford.bmir.protege.web.shared.object;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;

import javax.inject.Inject;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class OWLDataPropertyExpressionSelector extends OWLEntitySelector<OWLDataPropertyExpression, OWLDataProperty> {

    @Inject
    public OWLDataPropertyExpressionSelector( Comparator<? super OWLDataProperty> entityComparator) {
        super(EntityType.DATA_PROPERTY, entityComparator);
    }

    @Override
    protected OWLDataPropertyExpression toType(OWLDataProperty entity) {
        return entity;
    }
}

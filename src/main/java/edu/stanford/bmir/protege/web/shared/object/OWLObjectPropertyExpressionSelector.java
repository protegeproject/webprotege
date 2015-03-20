package edu.stanford.bmir.protege.web.shared.object;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import javax.inject.Inject;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class OWLObjectPropertyExpressionSelector extends OWLEntitySelector<OWLObjectPropertyExpression, OWLObjectProperty> {

    @Inject
    public OWLObjectPropertyExpressionSelector(Comparator<? super OWLObjectProperty> entityComparator) {
        super(EntityType.OBJECT_PROPERTY, entityComparator);
    }

    @Override
    protected OWLObjectPropertyExpression toType(OWLObjectProperty entity) {
        return entity;
    }
}

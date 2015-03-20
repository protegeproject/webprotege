package edu.stanford.bmir.protege.web.shared.object;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;

import javax.inject.Inject;
import java.util.Comparator;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class OWLClassExpressionSelector extends OWLEntitySelector<OWLClassExpression, OWLClass> {

    @Inject
    public OWLClassExpressionSelector(Comparator<? super OWLClass> entityComparator) {
        super(EntityType.CLASS, entityComparator);
    }

    @Override
    protected OWLClassExpression toType(OWLClass entity) {
        return entity;
    }
}

package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class AnnotationValuesAreNotDisjointMatcher implements Matcher<OWLEntity> {

    @Nonnull
    private final HasAnnotationAssertionAxioms axioms;

    @Nonnull
    private final Matcher<OWLAnnotationProperty> propertyA;

    @Nonnull
    private final Matcher<OWLAnnotationProperty> propertyB;

    public AnnotationValuesAreNotDisjointMatcher(@Nonnull HasAnnotationAssertionAxioms axioms,
                                                 @Nonnull Matcher<OWLAnnotationProperty> propertyA,
                                                 @Nonnull Matcher<OWLAnnotationProperty> propertyB) {
        this.axioms = checkNotNull(axioms);
        this.propertyA = checkNotNull(propertyA);
        this.propertyB = checkNotNull(propertyB);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        Set<OWLAnnotationAssertionAxiom> assertions = axioms.getAnnotationAssertionAxioms(value.getIRI());
        Set<OWLAnnotationValue> valuesA = new HashSet<>(assertions.size());
        Set<OWLAnnotationValue> valuesB = new HashSet<>(assertions.size());
        for(OWLAnnotationAssertionAxiom ax : assertions) {
            OWLAnnotationValue val = ax.getValue();
            if(propertyA.matches(ax.getProperty())) {
                valuesA.add(val);
                if(valuesB.contains(val)) {
                    return true;
                }
            }
            else if(propertyB.matches(ax.getProperty())) {
                valuesB.add(val);
                if(valuesA.contains(val)) {
                    return true;
                }
            }
        }
        return false;
    }
}

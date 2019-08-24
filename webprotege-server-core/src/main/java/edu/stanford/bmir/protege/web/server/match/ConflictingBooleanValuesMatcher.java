package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@AutoFactory
public class ConflictingBooleanValuesMatcher implements Matcher<OWLEntity> {

    private final AnnotationAssertionAxiomsIndex axioms;

    @Inject
    public ConflictingBooleanValuesMatcher(@Provided AnnotationAssertionAxiomsIndex axioms) {
        this.axioms = checkNotNull(axioms);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        Multimap<OWLAnnotationProperty, Boolean> map = HashMultimap.create(10, 2);
        for (OWLAnnotationAssertionAxiom ax : axioms.getAnnotationAssertionAxioms(value.getIRI()).collect(toList())) {
            if (ax.getValue() instanceof OWLLiteral) {
                OWLLiteral literal = (OWLLiteral) ax.getValue();
                if (literal.isBoolean() && isInBooleanLexicalSpace(literal)) {
                    if (map.put(ax.getProperty(), normalise(literal))) {
                        if (map.get(ax.getProperty()).size() > 1) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    private boolean normalise(OWLLiteral literal) {
        return literal.getLiteral().equals("true")
                || literal.getLiteral().equals("1");
    }

    private boolean isInBooleanLexicalSpace(OWLLiteral literal) {
        String lit = literal.getLiteral();
        switch (lit) {
            case "true":
            case "false":
            case "1":
            case "0":
                return true;
            default:
                return false;
        }
    }
}

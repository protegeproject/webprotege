package edu.stanford.bmir.protege.web.server.frame.translator;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyLiteralValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
public class DataPropertyAssertionAxiom2PropertyValuesTranslator {

    @Inject
    public DataPropertyAssertionAxiom2PropertyValuesTranslator() {
    }

    @Nonnull
    public Set<PlainPropertyValue> translate(@Nonnull OWLDataPropertyAssertionAxiom axiom,
                                             @Nonnull OWLEntity subject,
                                             @Nonnull State initialState) {
        if(!axiom.getSubject()
                 .equals(subject)) {
            return Collections.emptySet();
        }
        OWLDataProperty property = axiom.getProperty()
                                        .asOWLDataProperty();
        return ImmutableSet.of(PlainPropertyLiteralValue.get(property,
                                                             axiom.getObject(),
                                                             initialState));
    }
}

package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
public class NonUniqueLangTagsMatcher implements EntityFrameMatcher {

    private HasAnnotationAssertionAxioms hasAnnotationAssertionAxioms;

    private Matcher<OWLAnnotationProperty> propertyMatcher;

    public NonUniqueLangTagsMatcher(@Nonnull HasAnnotationAssertionAxioms hasAnnotationAssertionAxioms,
                                    @Nonnull Matcher<OWLAnnotationProperty> propertyMatcher) {
        this.hasAnnotationAssertionAxioms = checkNotNull(hasAnnotationAssertionAxioms);
        this.propertyMatcher = checkNotNull(propertyMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity entity) {
        // Count languages by property
        Map<OWLAnnotationProperty, Multiset<String>> prop2Langs = new HashMap<>();
        hasAnnotationAssertionAxioms.getAnnotationAssertionAxioms(entity.getIRI()).stream()
                                    .filter(ax -> propertyMatcher.matches(ax.getProperty()))
                                    .filter(ax -> ax.getValue() instanceof OWLLiteral)
                                    .forEach(ax -> {
                                        Multiset<String> langs = prop2Langs.computeIfAbsent(ax.getProperty(), p -> HashMultiset.create());
                                        String lang = ((OWLLiteral) ax.getValue()).getLang();
                                        langs.add(lang);
                                    });
        return prop2Langs.values().stream()
                         .anyMatch(langsForProp -> langsForProp.stream().anyMatch(lang -> langsForProp.count(lang) > 1));
    }
}

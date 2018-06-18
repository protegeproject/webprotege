package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
@AutoFactory
public class NonUniqueLangTagsMatcher implements EntityFrameMatcher {

    private AnnotationAssertionAxiomsIndex axiomsIndex;

    private Matcher<OWLAnnotationProperty> propertyMatcher;

    @Inject
    public NonUniqueLangTagsMatcher(@Nonnull @Provided  AnnotationAssertionAxiomsIndex axiomsIndex,
                                    @Nonnull Matcher<OWLAnnotationProperty> propertyMatcher) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
        this.propertyMatcher = checkNotNull(propertyMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity entity) {
        // Count languages by property
        Map<OWLAnnotationProperty, Multiset<String>> prop2Langs = new HashMap<>();
        axiomsIndex.getAnnotationAssertionAxioms(entity.getIRI())
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

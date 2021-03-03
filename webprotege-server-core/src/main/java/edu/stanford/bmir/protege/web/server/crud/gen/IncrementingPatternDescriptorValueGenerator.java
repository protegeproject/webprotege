package edu.stanford.bmir.protege.web.server.crud.gen;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsByValueIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.crud.gen.IncrementingPatternDescriptor;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-02
 */
@ProjectSingleton
public class IncrementingPatternDescriptorValueGenerator {

    private Map<CacheKey, Integer> cache = new HashMap<>();

    @Nonnull
    private final OWLDataFactory dataFactory;


    @Nonnull
    private final AnnotationAssertionAxiomsByValueIndex index;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public IncrementingPatternDescriptorValueGenerator(@Nonnull OWLDataFactory dataFactory,
                                                       @Nonnull AnnotationAssertionAxiomsByValueIndex index,
                                                       @Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.dataFactory = checkNotNull(dataFactory);
        this.index = checkNotNull(index);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    public synchronized OWLLiteral generateNextValue(@Nonnull OWLAnnotationProperty property,
                                                     @Nonnull IncrementingPatternDescriptor descriptor) {
        checkFormat(descriptor);
        var cacheKey = CacheKey.get(property, descriptor);
        return searchForNextValue(cacheKey);
    }

    @Nonnull
    private OWLLiteral searchForNextValue(@Nonnull CacheKey cacheKey) {
        var descriptor = cacheKey.getDescriptor();
        var currentValue = getSearchStartingPoint(cacheKey);
        for (int i = currentValue; i < Integer.MAX_VALUE; i++) {
            var format = descriptor.getFormat();
            String formattedValue;
            OWL2Datatype datatype;
            if (format.isEmpty()) {
                formattedValue = Integer.toString(i);
                datatype = OWL2Datatype.XSD_INTEGER;
            }
            else {
                formattedValue = String.format(format, i);
                datatype = OWL2Datatype.XSD_STRING;
            }
            var literal = dataFactory.getOWLLiteral(formattedValue, datatype);
            var property = cacheKey.getProperty();
            var exists = projectOntologiesIndex.getOntologyIds()
                                               .flatMap(ontId -> index.getAxiomsByValue(literal, ontId))
                                               .anyMatch(ax -> ax.getProperty().equals(property));
            if (!exists) {
                cache.put(cacheKey, i);
                return literal;
            }
        }
        throw new RuntimeException("Maxium possible value reached (" + Integer.MAX_VALUE + ")");
    }

    private int getSearchStartingPoint(@Nonnull CacheKey cacheKey) {
        var currentValue = cache.get(cacheKey);
        if (currentValue == null) {

            currentValue = cacheKey.getDescriptor().getStartingValue();
        }
        else {
            currentValue = currentValue + 1;
        }
        return currentValue;
    }


    @AutoValue
    static abstract class CacheKey {

        public static CacheKey get(@Nonnull OWLAnnotationProperty property,
                                   @Nonnull IncrementingPatternDescriptor descriptor) {
            return new AutoValue_IncrementingPatternDescriptorValueGenerator_CacheKey(property, descriptor);
        }

        public abstract OWLAnnotationProperty getProperty();

        public abstract IncrementingPatternDescriptor getDescriptor();
    }

    /** @noinspection ResultOfMethodCallIgnored*/
    private void checkFormat(IncrementingPatternDescriptor descriptor) {
        var format = descriptor.getFormat();
        if(format.isEmpty()) {
            return;
        }
        String.format(format, 1);
    }
}

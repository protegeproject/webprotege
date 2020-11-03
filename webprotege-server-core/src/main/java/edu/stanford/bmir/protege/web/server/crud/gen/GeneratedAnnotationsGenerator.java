package edu.stanford.bmir.protege.web.server.crud.gen;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.EntityIriPrefixCriteriaRewriter;
import edu.stanford.bmir.protege.web.server.match.MatcherFactory;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedValueDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedValueDescriptorVisitor;
import edu.stanford.bmir.protege.web.shared.crud.gen.IncrementingPatternDescriptor;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-02
 */
public class GeneratedAnnotationsGenerator {

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Nonnull
    private final EntityIriPrefixCriteriaRewriter rewriter;

    @Nonnull
    private final IncrementingPatternDescriptorValueGenerator incrementingPatternDescriptorValueGenerator;

    @Nonnull
    private OWLDataFactory dataFactory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public GeneratedAnnotationsGenerator(@Nonnull MatcherFactory matcherFactory,
                                         @Nonnull EntityIriPrefixCriteriaRewriter rewriter,
                                         @Nonnull IncrementingPatternDescriptorValueGenerator incrementingPatternDescriptorValueGenerator,
                                         @Nonnull OWLDataFactory dataFactory,
                                         @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.matcherFactory = checkNotNull(matcherFactory);
        this.rewriter = checkNotNull(rewriter);
        this.incrementingPatternDescriptorValueGenerator = checkNotNull(incrementingPatternDescriptorValueGenerator);
        this.dataFactory = checkNotNull(dataFactory);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }


    public <E> void generateAnnotations(@Nonnull OWLEntity entity,
                                    @Nonnull EntityCrudKitSettings<?> crudKitSettings,
                                    @Nonnull OntologyChangeList.Builder<E> changeListBuilder) {

        var generatedAnnotationSettings = crudKitSettings.getGeneratedAnnotationsSettings();

        for(var descriptor : generatedAnnotationSettings.getDescriptors()) {
            if(isActivatedForEntity(entity, descriptor)) {
                var chg = generateNextValueAndOntologyChange(entity, descriptor);
                changeListBuilder.add(chg);
            }
        }
    }

    @Nonnull
    private AddAxiomChange generateNextValueAndOntologyChange(@Nonnull OWLEntity entity,
                                                              GeneratedAnnotationDescriptor descriptor) {
        var annotationProperty = descriptor.getProperty();
        var generatedValueDescriptor = descriptor.getValueDescriptor();
        var value = getValue(annotationProperty, generatedValueDescriptor);
        var ax = dataFactory.getOWLAnnotationAssertionAxiom(annotationProperty,
                                                   entity.getIRI(),
                                                   value);
        var ontId = defaultOntologyIdManager.getDefaultOntologyId();
        return AddAxiomChange.of(ontId, ax);
    }

    private boolean isActivatedForEntity(@Nonnull OWLEntity entity, GeneratedAnnotationDescriptor descriptor) {
        return descriptor.getActivatedBy()
                  .map(criteria -> {
                      var rewrittenCriteria = rewriter.rewriteCriteria(criteria);
                      var matcher = matcherFactory.getMatcher(rewrittenCriteria);
                      return matcher.matches(entity);
                  })
                  .orElse(false);
    }

    private OWLAnnotationValue getValue(OWLAnnotationProperty annotationProperty, GeneratedValueDescriptor generatedValueDescriptor) {
        return generatedValueDescriptor.accept(new GeneratedValueDescriptorVisitor<OWLLiteral>() {
            @Override
            public OWLLiteral visit(@Nonnull IncrementingPatternDescriptor descriptor) {
                return incrementingPatternDescriptorValueGenerator.generateNextValue(annotationProperty,
                                                                                     descriptor);

            }
        });
    }
}

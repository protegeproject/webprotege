package edu.stanford.bmir.protege.web.server.frame.translator;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueMinimiser;
import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.match.RelationshipMatcherFactory;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrameTranslationOptions;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipCriteria;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.server.index.ClassFrameAxiomsIndex.AnnotationsTreatment.EXCLUDE_ANNOTATIONS;
import static edu.stanford.bmir.protege.web.server.index.ClassFrameAxiomsIndex.AnnotationsTreatment.INCLUDE_ANNOTATIONS;
import static edu.stanford.bmir.protege.web.shared.frame.ClassFrameTranslationOptions.AncestorsTreatment.INCLUDE_ANCESTORS;
import static edu.stanford.bmir.protege.web.shared.frame.RelationshipTranslationOptions.RelationshipMinification.MINIMIZED_RELATIONSHIPS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 * <p>
 * A translator that converts sets of axioms to class frames and vice-versa.
 * </p>
 */
public class Class2ClassFrameTranslator {

    @Nonnull
    private final ClassFrameAxiomsIndex classFrameAxiomsIndex;

    @Nonnull
    private final HasGetAncestors<OWLClass> ancestorsProvider;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final AxiomPropertyValueTranslator axiomPropertyValueTranslator;

    @Nonnull
    private final ClassFrameTranslationOptions options;

    @Nonnull
    private final RelationshipMatcherFactory matcherFactory;

    @AutoFactory
    public Class2ClassFrameTranslator(@Provided @Nonnull ClassFrameAxiomsIndex classFrameAxiomsIndex,
                                      @Provided @Nonnull HasGetAncestors<OWLClass> ancestorsProvider,
                                      @Provided @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                      @Provided @Nonnull AxiomPropertyValueTranslator axiomPropertyValueTranslator,
                                      @Nonnull @Provided RelationshipMatcherFactory matcherFactory,
                                      @Nonnull ClassFrameTranslationOptions options) {
        this.classFrameAxiomsIndex = checkNotNull(classFrameAxiomsIndex);
        this.ancestorsProvider = checkNotNull(ancestorsProvider);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.axiomPropertyValueTranslator = checkNotNull(axiomPropertyValueTranslator);
        this.options = checkNotNull(options);
        this.matcherFactory = checkNotNull(matcherFactory);
    }

    /**
     * Translate the specified class to a class frame, in accordance with the specified
     * translation options in this translator.
     */
    @Nonnull
    public PlainClassFrame getFrame(@Nonnull OWLClass subject) {
        var frameAxioms = classFrameAxiomsIndex.getFrameAxioms(subject, INCLUDE_ANNOTATIONS);
        var propertyValues = new ArrayList<>(translateAxiomsToPropertyValues(subject,
                                                                             frameAxioms,
                                                                             State.ASSERTED));
        if(options.getAncestorsTreatment() == INCLUDE_ANCESTORS) {
            for(OWLClass ancestor : ancestorsProvider.getAncestors(subject)) {
                if(!ancestor.equals(subject)) {
                    var ancestorFrameAxioms = classFrameAxiomsIndex.getFrameAxioms(ancestor, EXCLUDE_ANNOTATIONS);
                    propertyValues.addAll(translateAxiomsToPropertyValues(ancestor,
                                                                          ancestorFrameAxioms,
                                                                          State.DERIVED));
                }
            }
        }

        var parents = frameAxioms
                .stream()
                .filter(OWLSubClassOfAxiom.class::isInstance)
                .map(OWLSubClassOfAxiom.class::cast)
                .map(OWLSubClassOfAxiom::getSuperClass)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .distinct()
                .collect(toImmutableSet());

        var propertyValuesMin = ImmutableSet.copyOf(propertyValues);

        if(options.getRelationshipTranslationOptions()
                  .getRelationshipMinification() == MINIMIZED_RELATIONSHIPS) {
            propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues)
                                                      .collect(toImmutableSet());
        }

        return PlainClassFrame.get(subject,
                                   parents,
                                   propertyValuesMin);
    }

    private List<PlainPropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                     Set<OWLAxiom> relevantAxioms,
                                                                     State initialState) {
        var relationshipOptions = options.getRelationshipTranslationOptions();
        Optional<RelationshipCriteria> outgoingRelationshipCriteria = relationshipOptions.getOutgoingRelationshipCriteria();
        if(outgoingRelationshipCriteria.isEmpty()) {
            return ImmutableList.of();
        }
        var relationshipMatcher = matcherFactory.getRelationshipMatcher(outgoingRelationshipCriteria.get());
        return relevantAxioms.stream()
                             .flatMap(axiom -> axiomPropertyValueTranslator.getPropertyValues(subject,
                                                                                              axiom,
                                                                                              initialState)
                                                                           .stream())
                             .filter(relationshipMatcher::matches)
                             .collect(Collectors.toList());
    }
}

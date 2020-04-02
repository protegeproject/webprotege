package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.frame.translator.AxiomPropertyValueTranslator;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class EntityRelationshipMatcher implements EntityFrameMatcher {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final RelationshipPresence relationshipPresence;

    @Nonnull
    private final PropertyValueMatcher propertyValueMatcher;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex;

    @Nonnull
    private final PropertyAssertionAxiomsBySubjectIndex propertyAssertionAxiomsBySubjectIndex;

    @Nonnull
    private final AxiomPropertyValueTranslator translator;

    @AutoFactory
    public EntityRelationshipMatcher(@Nonnull @Provided ProjectOntologiesIndex projectOntologiesIndex,
                                     @Nonnull RelationshipPresence relationshipPresence,
                                     @Nonnull PropertyValueMatcher propertyValueMatcher,
                                     @Nonnull @Provided SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex,
                                     @Nonnull @Provided PropertyAssertionAxiomsBySubjectIndex propertyAssertionAxiomsBySubjectIndex,
                                     @Nonnull @Provided AxiomPropertyValueTranslator axiomTranslator) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.relationshipPresence = relationshipPresence;
        this.propertyValueMatcher = propertyValueMatcher;
        this.subClassOfAxiomsBySubClassIndex = subClassOfAxiomsBySubClassIndex;
        this.propertyAssertionAxiomsBySubjectIndex = propertyAssertionAxiomsBySubjectIndex;
        this.translator = axiomTranslator;
    }

    @Override
    public boolean matches(@Nonnull OWLEntity entity) {

        Stream<? extends OWLAxiom> axiomStream;
        if(entity.isOWLClass()) {
            axiomStream = projectOntologiesIndex.getOntologyIds()
                    .flatMap(ontId -> {
                        OWLClass cls = entity.asOWLClass();
                        return subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(cls, ontId);
                    });
        }
        else if(entity.isOWLNamedIndividual()) {
            axiomStream = projectOntologiesIndex.getOntologyIds()
                                                .flatMap(ontId -> {
                                                    OWLNamedIndividual individual = entity.asOWLNamedIndividual();
                                                    return propertyAssertionAxiomsBySubjectIndex.getPropertyAssertions(
                                                            individual, ontId);
                                                });
        }
        else {
            axiomStream = Stream.empty();
        }

        Stream<PlainPropertyValue> propertyValueStream = axiomStream.flatMap(ax -> translator.getPropertyValues(entity, ax, State.ASSERTED).stream());


        if (relationshipPresence == RelationshipPresence.AT_LEAST_ONE) {
            return propertyValueStream.anyMatch(propertyValueMatcher::matches);
        }
        else if(relationshipPresence == RelationshipPresence.AT_MOST_ONE) {
            return propertyValueStream.filter(propertyValueMatcher::matches)
                                        .limit(2)
                                        .count() <= 1;
        }
        else {
            return propertyValueStream.noneMatch(propertyValueMatcher::matches);
        }
    }
}

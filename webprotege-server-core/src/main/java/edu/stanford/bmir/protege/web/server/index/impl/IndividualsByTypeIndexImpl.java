package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.individuals.IndividualRendering;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode.ALL_INSTANCES;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
public class IndividualsByTypeIndexImpl implements IndividualsByTypeIndex, DependentIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ProjectSignatureByTypeIndex projectSignatureByTypeIndex;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividual;

    @Nonnull
    private final ClassAssertionAxiomsByClassIndex classAssertionAxiomsByClass;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final OWLDataFactory dataFactory;


    @Inject
    public IndividualsByTypeIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                      @Nonnull ProjectSignatureByTypeIndex projectSignatureByTypeIndex,
                                      @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividual,
                                      @Nonnull ClassAssertionAxiomsByClassIndex classAssertionAxiomsByClass,
                                      @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                      @Nonnull DictionaryManager dictionaryManager,
                                      @Nonnull OWLDataFactory dataFactory) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.projectSignatureByTypeIndex = projectSignatureByTypeIndex;
        this.classAssertionAxiomsByIndividual = classAssertionAxiomsByIndividual;
        this.classAssertionAxiomsByClass = classAssertionAxiomsByClass;
        this.classHierarchyProvider = classHierarchyProvider;
        this.dictionaryManager = dictionaryManager;
        this.dataFactory = dataFactory;
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex, projectSignatureByTypeIndex, classAssertionAxiomsByClass, classAssertionAxiomsByIndividual);
    }

    @Nonnull
    @Override
    public Stream<OWLNamedIndividual> getIndividualsByType(@Nonnull OWLClass type,
                                                           @Nonnull InstanceRetrievalMode mode) {
        if(type.isOWLThing()) {
            if(mode == ALL_INSTANCES) {
                // Signature
                return projectSignatureByTypeIndex.getSignature(EntityType.NAMED_INDIVIDUAL).distinct();
            }
            else {
                return projectSignatureByTypeIndex.getSignature(EntityType.NAMED_INDIVIDUAL).distinct()
                                                  .filter(this::isDirectInstanceOfOWLThing);
            }
        }
        Stream<OWLClass> direct = Stream.of(type);
        Stream<OWLClass> ancestors;
        if(mode == ALL_INSTANCES) {
            // This potentially needs caching
            ancestors = classHierarchyProvider.getDescendants(type)
                                              .stream();
        }
        else {
            ancestors = Stream.empty();
        }
        Stream<OWLClass> types = Stream.concat(direct, ancestors);
        Stream<OWLNamedIndividual> individuals =
                types.flatMap(t -> projectOntologiesIndex.getOntologyIds()
                                                         .flatMap(ontId -> classAssertionAxiomsByClass.getClassAssertionAxioms(
                                                                 t,
                                                                 ontId))
                                                         .map(OWLClassAssertionAxiom::getIndividual)
                                                         .filter(OWLIndividual::isNamed)
                                                         .map(OWLIndividual::asOWLNamedIndividual));
        return individuals
                .distinct()
                .map(this::toIndividualRendering)
                .sorted()
                .map(IndividualRendering::getIndividual);
    }

    private IndividualRendering toIndividualRendering(OWLNamedIndividual ind) {
        return new IndividualRendering(ind, dictionaryManager.getShortForm(ind)
                                                                             .toLowerCase());
    }

    private boolean isDirectInstanceOfOWLThing(OWLNamedIndividual i) {
        var types = projectOntologiesIndex.getOntologyIds()
                                          .flatMap(ontId -> classAssertionAxiomsByIndividual.getClassAssertionAxioms(i, ontId))
                                          .map(OWLClassAssertionAxiom::getClassExpression)
                                          .collect(toSet());
        return types.isEmpty() || types.contains(dataFactory.getOWLThing());
    }
}

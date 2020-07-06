package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.individuals.IndividualRendering;
import edu.stanford.bmir.protege.web.server.index.IndividualsQueryResult;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.Scanner;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.pagination.PageCollector.toPage;
import static edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode.ALL_INSTANCES;
import static edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode.DIRECT_INSTANCES;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Sep 2018
 */
public class IndividualsIndexImpl implements IndividualsIndex, DependentIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividual;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final IndividualsByTypeIndex individualsByTypeIndex;

    @Inject
    public IndividualsIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividual,
                                @Nonnull DictionaryManager dictionaryManager,
                                @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                @Nonnull OWLDataFactory dataFactory,
                                @Nonnull IndividualsByTypeIndex individualsByTypeIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.classAssertionAxiomsByIndividual = checkNotNull(classAssertionAxiomsByIndividual);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.dataFactory = checkNotNull(dataFactory);
        this.individualsByTypeIndex = checkNotNull(individualsByTypeIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex, classAssertionAxiomsByIndividual);
    }

    @Nonnull
    @Override
    public IndividualsQueryResult getIndividuals(@Nonnull String searchString,
                                                 @Nonnull PageRequest pageRequest) {
        return getIndividuals(DataFactory.getOWLThing(),
                              ALL_INSTANCES,
                              searchString,
                              pageRequest);
    }

    @Nonnull
    @Override
    public IndividualsQueryResult getIndividuals(@Nonnull OWLClass type,
                                                 @Nonnull InstanceRetrievalMode mode,
                                                 @Nonnull String search,
                                                 @Nonnull PageRequest pageRequest) {
        List<SearchString> searchStrings = SearchString.parseMultiWordSearchString(search);
        Counter counter = new Counter();
        Optional<Page<OWLNamedIndividual>> page = getIndividualsMatching(type, mode)
                .peek(ind -> counter.increment())
                .filter(ind -> matchesSearchStrings(ind, searchStrings))
                .map(this::toIndividualRendering)
                .sorted()
                .map(IndividualRendering::getIndividual)
                .collect(toPage(pageRequest.getPageNumber(),
                                pageRequest.getPageSize()));

        return IndividualsQueryResult.get(page.orElse(Page.emptyPage()),
                                          counter.getCounter(),
                                          type,
                                          mode);
    }

    /**
     * Returns a stream of distinct individuals that match the specified parameters.
     */
    private Stream<OWLNamedIndividual> getIndividualsMatching(@Nonnull OWLClass type,
                                                              @Nonnull InstanceRetrievalMode mode) {
        return individualsByTypeIndex.getIndividualsByType(type, mode);
    }

    private boolean matchesSearchStrings(@Nonnull OWLNamedIndividual i,
                                         @Nonnull List<SearchString> searchStrings) {
        if(searchStrings.isEmpty()) {
            return true;
        }
        String shortForm = dictionaryManager.getShortForm(i);
        Scanner scanner = new Scanner(shortForm, shortForm.toLowerCase());
        for(SearchString searchString : searchStrings) {
            int index = scanner.indexOf(searchString, 0);
            if(index == -1) {
                return false;
            }
        }
        return true;
    }

    private IndividualRendering toIndividualRendering(OWLNamedIndividual ind) {
        return new IndividualRendering(ind,
                                       dictionaryManager.getShortForm(ind)
                                                        .toLowerCase());
    }

    @Nonnull
    @Override
    public IndividualsQueryResult getIndividualsPageContaining(@Nonnull OWLNamedIndividual individual,
                                                               @Nonnull Optional<OWLClass> preferredType,
                                                               @Nonnull InstanceRetrievalMode preferredMode,
                                                               int pageSize) {

        OWLClass actualType = null;
        OWLClass matchingDirectType = null;
        OWLClass matchingIndirectType = null;
        if(preferredType.isPresent()) {
            // Search through class assertion axioms in order to find a matching preferred type
            OWLClass thePreferredType = preferredType.get();
            if(!thePreferredType.isOWLThing()) {
                var types = projectOntologiesIndex.getOntologyIds()
                                                  .flatMap(ontId -> classAssertionAxiomsByIndividual.getClassAssertionAxioms(
                                                          individual,
                                                          ontId))
                                                  .map(OWLClassAssertionAxiom::getClassExpression)
                                                  .filter(OWLClassExpression::isNamed)
                                                  .map(OWLClassExpression::asOWLClass)
                                                  .distinct()
                                                  .collect(Collectors.toList());
                for(OWLClass type : types) {
                    // Found a direct type that matches the preferred type
                    if(type.equals(thePreferredType)) {
                        matchingDirectType = type;
                    }
                    else if(classHierarchyProvider.getAncestors(thePreferredType)
                                                  .contains(type)) {
                        // Found an indirect type of the preferred type
                        matchingIndirectType = type;
                    }
                }
            }
            if(matchingDirectType != null) {
                actualType = matchingDirectType;
            }
            else if(matchingIndirectType != null) {
                actualType = matchingIndirectType;
            }
        }
        if(actualType == null) {
            // No preferred type or preferred type not found
            // Try for a specific type
            actualType = projectOntologiesIndex.getOntologyIds()
                                               .flatMap(ontId -> classAssertionAxiomsByIndividual.getClassAssertionAxioms(
                                                       individual,
                                                       ontId))
                                               .map(OWLClassAssertionAxiom::getClassExpression)
                                               .filter(OWLClassExpression::isNamed)
                                               .map(OWLClassExpression::asOWLClass)
                                               .findFirst()
                                               .orElse(dataFactory.getOWLThing());
        }
        InstanceRetrievalMode actualMode;
        if(preferredMode == ALL_INSTANCES) {
            actualMode = ALL_INSTANCES;
        }
        else {
            if(matchingDirectType != null) {
                actualMode = DIRECT_INSTANCES;
            }
            else {
                actualMode = ALL_INSTANCES;
            }
        }

        List<OWLNamedIndividual> individuals = getIndividualsMatching(actualType, actualMode).collect(toList());
        int individualIndex = individuals.indexOf(individual);
        Page<OWLNamedIndividual> page;
        if(individualIndex == -1) {
            page = Page.emptyPage();
        }
        else {
            int pageNumber = (individualIndex / pageSize) + 1;
            int pageCount = (individuals.size() / pageSize) + 1;
            int pageStartIndex = (individualIndex / pageSize) * pageSize;
            int pageEndIndex = Math.min(pageStartIndex + pageSize, individuals.size());
            page = new Page<>(pageNumber,
                              pageCount,
                              individuals.subList(pageStartIndex,
                                                  pageEndIndex),
                              individuals.size());
        }
        return IndividualsQueryResult.get(page,
                                          individuals.size(),
                                          actualType,
                                          actualMode);
    }

    @Nonnull
    @Override
    public Stream<OWLClass> getTypes(@Nonnull OWLNamedIndividual individual) {
        return projectOntologiesIndex.getOntologyIds()
                              .flatMap(ontId -> classAssertionAxiomsByIndividual.getClassAssertionAxioms(individual, ontId))
                              .map(OWLClassAssertionAxiom::getClassExpression)
                              .filter(OWLClassExpression::isNamed)
                              .map(OWLClassExpression::asOWLClass);
    }

}

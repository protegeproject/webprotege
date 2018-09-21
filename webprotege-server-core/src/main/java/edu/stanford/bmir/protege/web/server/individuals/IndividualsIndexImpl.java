package edu.stanford.bmir.protege.web.server.individuals;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.Scanner;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.bmir.protege.web.server.util.AlphaNumericStringComparator;
import edu.stanford.bmir.protege.web.server.util.ClassExpression;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.server.util.ProtegeStreams;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
public class IndividualsIndexImpl implements IndividualsIndex {

    private final Logger logger = LoggerFactory.getLogger(IndividualsIndexImpl.class);

    private final Lock lock = new ReentrantLock();

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final OWLDataFactory dataFactory;

    private final SetMultimap<OWLClass, OWLClass> classAssertionClassesByAncestors = HashMultimap.create();

    private final SetMultimap<OWLClass, OWLClass> ancestorsByClassAssertionClasses = HashMultimap.create();

    private boolean builtIndex = false;

    private boolean bound = false;

    @Inject
    public IndividualsIndexImpl(@Nonnull ProjectId projectId, @Nonnull OWLOntology rootOntology, @Nonnull DictionaryManager dictionaryManager, @Nonnull ClassHierarchyProvider classHierarchyProvider, @Nonnull OWLDataFactory dataFactory) {
        this.projectId = checkNotNull(projectId);
        this.rootOntology = checkNotNull(rootOntology);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.dataFactory = checkNotNull(dataFactory);
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

    @Nonnull
    @Override
    public IndividualsQueryResult getIndividualsPageContaining(@Nonnull OWLNamedIndividual individual,
                                                                 @Nonnull Optional<OWLClass> preferredType,
                                                                 @Nonnull InstanceRetrievalMode preferredMode,
                                                                 int pageSize) {

        OWLClass actualType = null;
        OWLClass matchingDirectType = null;
        OWLClass matchingIndirectType = null;
        if (preferredType.isPresent()) {
            // Search through class assertion axioms in order to find a matching preferred type
            OWLClass thePreferredType = preferredType.get();
            if (!thePreferredType.isOWLThing()) {
                for(OWLOntology ontology : rootOntology.getImportsClosure()) {
                    for(OWLClassAssertionAxiom ax : rootOntology.getClassAssertionAxioms(individual)) {
                        if (!ax.getClassExpression().isAnonymous()) {
                            //
                            OWLClass typeCls = ax.getClassExpression().asOWLClass();
                            if(ax.getClassExpression().equals(thePreferredType)) {
                                // Found a direct type that matches the preferred type
                                matchingDirectType = typeCls;
                            }
                            else if(classHierarchyProvider.getAncestors(thePreferredType).contains(typeCls)) {
                                // Found an indirect type of the preferred type
                                matchingIndirectType = typeCls;
                            }
                        }
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
            // Try for a specific type
            actualType = EntitySearcher.getTypes(individual, rootOntology.getImportsClosure())
                    .stream()
                    .filter(ClassExpression::isOWLClass)
                    .map(ClassExpression::asOWLClass)
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

    /**
     * Returns a stream of distinct individuals that match the specified parameters.
     */
    private Stream<OWLNamedIndividual> getIndividualsMatching(@Nonnull OWLClass type,
                                                              @Nonnull InstanceRetrievalMode mode) {
        if(type.isOWLThing()) {
            if (mode == ALL_INSTANCES) {
                // Signature
                return getIndividualsInSignature()
                        .distinct();
            }
            else {
                return getIndividualsInSignature()
                        .filter(this::isDirectInstanceOfOWLThing)
                        .distinct();
            }
        }
        Stream<OWLClass> direct = Stream.of(type);
        Stream<OWLClass> ancestors;
        if (mode == ALL_INSTANCES) {
            // This potentially needs caching
            ancestors = classHierarchyProvider.getDescendants(type).stream();
        }
        else {
            ancestors = Stream.empty();
        }
        Stream<OWLClass> types = Stream.concat(direct, ancestors);
        Stream<OWLNamedIndividual> individuals =
                types.flatMap(t -> rootOntology.getImportsClosure()
                        .stream()
                        .flatMap(o -> o.getClassAssertionAxioms(t).stream()))
                        .map(OWLClassAssertionAxiom::getIndividual)
                        .filter(OWLIndividual::isNamed)
                        .map(OWLIndividual::asOWLNamedIndividual);
        return individuals
                .distinct()
                .map(this::toIndividualRendering)
                .sorted()
                .map(IndividualRendering::getIndividual);
    }

    private boolean isDirectInstanceOfOWLThing(OWLNamedIndividual i) {
        Collection<OWLClassExpression> types = EntitySearcher.getTypes(i, rootOntology.getImportsClosure());
        return types.isEmpty() || types.contains(dataFactory.getOWLThing());
    }

    private Stream<OWLNamedIndividual> getIndividualsInSignature() {
        return rootOntology.getIndividualsInSignature(Imports.INCLUDED).stream();
    }

    private static boolean isNamed(@Nonnull OWLClassExpression ce) {
        return !ce.isAnonymous();
    }

    @Nonnull
    @Override
    public Stream<OWLClass> getTypes(@Nonnull OWLNamedIndividual individual) {
        return rootOntology.getImportsClosure()
                .stream()
                .flatMap(o -> o.getImportsClosure().stream())
                .flatMap(o -> o.getClassAssertionAxioms(individual).stream())
                .map(OWLClassAssertionAxiom::getClassExpression)
                .filter(ce -> !ce.isAnonymous())
                .map(OWLClassExpression::asOWLClass);
    }

    private IndividualRendering toIndividualRendering(OWLNamedIndividual ind) {
        return new IndividualRendering(ind, dictionaryManager.getShortForm(ind).toLowerCase());
    }

    private boolean matchesSearchStrings(@Nonnull OWLNamedIndividual i,
                                         @Nonnull List<SearchString> searchStrings) {
        if (searchStrings.isEmpty()) {
            return true;
        }
        String shortForm = dictionaryManager.getShortForm(i);
        Scanner scanner = new Scanner(shortForm, shortForm.toLowerCase());
        for (SearchString searchString : searchStrings) {
            int index = scanner.indexOf(searchString, 0);
            if (index == -1) {
                return false;
            }
        }
        return true;
    }

    private static class IndividualRendering implements Comparable<IndividualRendering> {

        private final OWLNamedIndividual individual;

        private final String rendering;

        private final AlphaNumericStringComparator comparator = new AlphaNumericStringComparator();

        public IndividualRendering(OWLNamedIndividual individual, String rendering) {
            this.individual = individual;
            this.rendering = rendering;
        }

        public OWLNamedIndividual getIndividual() {
            return individual;
        }

        public String getRendering() {
            return rendering;
        }

        @Override
        public int compareTo(IndividualRendering o) {
            return comparator.compare(this.rendering, o.rendering);
        }
    }
}

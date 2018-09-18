package edu.stanford.bmir.protege.web.server.individuals;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.Scanner;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.bmir.protege.web.server.util.AlphaNumericStringComparator;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
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

    private final SetMultimap<OWLClass, OWLClass> classAssertionClassesByAncestors = HashMultimap.create();

    private final SetMultimap<OWLClass, OWLClass> ancestorsByClassAssertionClasses = HashMultimap.create();

    private boolean builtIndex = false;

    private boolean bound = false;

    @Inject
    public IndividualsIndexImpl(@Nonnull ProjectId projectId, @Nonnull OWLOntology rootOntology, @Nonnull DictionaryManager dictionaryManager, @Nonnull ClassHierarchyProvider classHierarchyProvider) {
        this.projectId = checkNotNull(projectId);
        this.rootOntology = checkNotNull(rootOntology);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
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
        Counter counter = new Counter();
        Optional<Page<OWLNamedIndividual>> page = individuals
                .peek(ind -> counter.increment())
                .filter(ind -> matchesSearchStrings(ind, searchStrings))
                .distinct()
                .map(this::toIndividualRendering)
                .sorted()
                .map(IndividualRendering::getIndividual)
                .collect(toPage(pageRequest.getPageNumber(),
                                pageRequest.getPageSize()));

        return new IndividualsQueryResult(page.orElse(Page.emptyPage()),
                                          counter.getCounter());
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

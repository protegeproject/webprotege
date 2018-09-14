package edu.stanford.bmir.protege.web.server.individuals;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.Scanner;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.pagination.PageCollector.toPage;
import static edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode.ALL_INSTANCES;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

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

    private final SetMultimap<OWLClass, OWLNamedIndividual> individualsByClass = HashMultimap.create();

    private final SetMultimap<OWLClass, OWLClass> clsesBySubClass = HashMultimap.create();

    private boolean builtIndex = false;

    @Inject
    public IndividualsIndexImpl(@Nonnull ProjectId projectId, @Nonnull OWLOntology rootOntology, @Nonnull DictionaryManager dictionaryManager, @Nonnull ClassHierarchyProvider classHierarchyProvider) {
        this.projectId = checkNotNull(projectId);
        this.rootOntology = checkNotNull(rootOntology);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
    }

    private void ensureIndex() {
        try {
            lock.lock();
            if (builtIndex) {
                return;
            }
            Stopwatch stopwatch = Stopwatch.createStarted();
            logger.info("{} Building individuals indexes", projectId);
            individualsByClass.clear();
            clsesBySubClass.clear();
            for (OWLOntology ontology : rootOntology.getImportsClosure()) {
                for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
                    if (!individual.isAnonymous()) {
                        Set<OWLClassAssertionAxiom> axioms = ontology.getClassAssertionAxioms(individual);
                        if (!axioms.isEmpty()) {
                            axioms
                                    .stream()
                                    .filter(ax -> !ax.getClassExpression().isAnonymous())
                                    .forEach(ax -> individualsByClass.put(ax.getClassExpression().asOWLClass(),
                                                                          ax.getIndividual().asOWLNamedIndividual()));
                        }
                        else {
                            individualsByClass.put(DataFactory.getOWLThing(),
                                                   individual);
                        }
                    }
                }
            }
            for (OWLClass cls : individualsByClass.keySet()) {
                classHierarchyProvider.getParents(cls)
                                      .forEach(par -> clsesBySubClass.put(par, cls));
            }
            stopwatch.stop();
            logger.info("{} Built individuals indexes in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
            builtIndex = true;
        } finally {
            lock.unlock();
        }
    }


    @Nonnull
    @Override
    public IndividualsQueryResult getIndividuals(@Nonnull String searchString,
                                                 @Nonnull PageRequest pageRequest) {
        ensureIndex();
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
        ensureIndex();
        List<SearchString> searchStrings = SearchString.parseMultiWordSearchString(search);
        Stream<OWLNamedIndividual> individuals;
        Stream<OWLNamedIndividual> direct = individualsByClass.get(type).stream();
        if (mode == ALL_INSTANCES) {
            Stream<OWLNamedIndividual> indirect = clsesBySubClass.get(type)
                                                                 .stream()
                                                                 .flatMap(cls -> individualsByClass.get(cls).stream());

            individuals = Streams.concat(direct, indirect);
        }
        else {
            individuals = direct;
        }
        Counter counter = new Counter();
        Optional<Page<OWLNamedIndividual>> page = individuals.peek(i -> counter.increment()).filter(i -> {
            if (searchStrings.isEmpty()) {
                return true;
            }
            String shortForm = dictionaryManager.getShortForm(i);
            Scanner scanner = new Scanner(shortForm, shortForm);
            for (SearchString searchString : searchStrings) {
                int index = scanner.indexOf(searchString, 0);
                if (index == -1) {
                    return false;
                }
            }
            return true;
        })
                                                             .distinct()
                                                             .map(i -> new IndividualRendering(i, dictionaryManager.getShortForm(i)
                                                                                                                   .toLowerCase()))
                                                             .sorted()
                                                             .map(IndividualRendering::getIndividual)
                                                             .collect(toPage(pageRequest.getPageNumber(),
                                                                             pageRequest.getPageSize()));

        return new IndividualsQueryResult(page.orElse(Page.emptyPage()),
                                          counter.getCounter());
    }


    private static class IndividualRendering implements Comparable<IndividualRendering> {

        private final OWLNamedIndividual individual;

        private final String rendering;

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
            return this.rendering.compareTo(o.rendering);
        }
    }
}

package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.project.chg.OntologyChangeTranslator;
import edu.stanford.bmir.protege.web.server.project.chg.OntologyChangeTranslatorVisitor;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
@ProjectSingleton
public class ProjectOntologyManagerLoader {

    public static final String GENERATED_ONTOLOGY_IRI_PREFIX = "http://webprotege.stanford.edu/project/";

    private static final Logger logger = LoggerFactory.getLogger(ProjectOntologyManagerLoader.class);

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public ProjectOntologyManagerLoader(ProjectId projectId,
                                        RevisionManager revisionManager) {
        this.projectId = checkNotNull(projectId);
        this.revisionManager = checkNotNull(revisionManager);
    }

    /**
     * Creates a set of ontologies in the specified manager that represent the set of
     * project ontologies for the latest revision.
     *
     * @param manager The manager.
     * @return An ontology that represents the project root ontologies
     */
    @Nonnull
    public OWLOntology createProjectOntologiesInManager(@Nonnull OWLOntologyManager manager) throws OWLOntologyCreationException {
        logger.info("{} Loading project", projectId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        ImmutableList<Revision> revisions = revisionManager.getRevisions();
        logger.info("{} Processing {} revisions", projectId, String.format("%,d", revisions.size()));
        if(revisions.isEmpty()) {
            var ontologyIri = createUniqueOntologyIRI();
            return manager.createOntology(ontologyIri);
        }
        var changeTranslator = new OntologyChangeTranslator(new OntologyChangeTranslatorVisitor(manager));
        var owlOntologyChanges = revisions
                .stream()
                .flatMap(rev -> rev.getChanges()
                                   .stream())
                .map(chg -> {
                    createOntologyIfNecessary(chg, manager);
                    return changeTranslator.toOwlOntologyChange(chg);
                })
                .collect(toImmutableList());
        logger.info("{} Applying {} ontology changes", projectId, String.format("%,d", owlOntologyChanges.size()));
        long t0 = stopwatch.elapsed(MILLISECONDS);
        manager.applyChanges(owlOntologyChanges);
        long t1 = stopwatch.elapsed(MILLISECONDS);
        logger.info("{} Applied {} ontology changes in {} ms",
                    projectId,
                    String.format("%,d", owlOntologyChanges.size()),
                    (t1 - t0));
        Set<OWLOntology> rootOnts = new HashSet<>(manager.getOntologies());
        logger.info("{} Loaded {} ontologies in {} ms", projectId, rootOnts.size(), stopwatch.elapsed(MILLISECONDS));
        MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
        memoryMonitor.monitorMemoryUsage();
        if(rootOnts.size() == 1) {
            return rootOnts.iterator()
                           .next();
        }
        manager.getOntologies()
               .stream()
               .flatMap(ont -> ont.getImports()
                                  .stream())
               .forEach(rootOnts::remove);
        return rootOnts.stream()
                       .sorted()
                       .findFirst()
                       .orElseThrow();
    }

    private static IRI createUniqueOntologyIRI() {
        String ontologyName = IdUtil.getBase62UUID();
        return IRI.create(GENERATED_ONTOLOGY_IRI_PREFIX + ontologyName);
    }

    private void createOntologyIfNecessary(OntologyChange chg,
                                                  OWLOntologyManager man) {
        var ontologyId = chg.getOntologyId();
        if(!man.contains(ontologyId)) {
            try {
                man.createOntology(ontologyId);
            } catch(OWLOntologyCreationException e) {
                logger.error("{} Could not create ontology with Id: {}", projectId, ontologyId, e);
            }
        }
    }
}

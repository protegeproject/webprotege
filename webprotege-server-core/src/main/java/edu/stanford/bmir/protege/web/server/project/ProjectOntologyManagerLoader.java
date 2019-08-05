package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
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

    private final RevisionManager revisionManager;

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
        List<OWLOntologyChange> changes = revisions
                .stream()
                .flatMap(rev -> rev.getChanges().stream())
                .peek(chg -> createOntologyIfNecessary(chg, manager))
                .map(chg -> chg.createOntologyChange(manager))
                .collect(Collectors.toList());
        logger.info("{} Applying {} ontology changes", projectId, String.format("%,d", changes.size()));
        long t0 = stopwatch.elapsed(MILLISECONDS);
        manager.applyChanges(changes);
        long t1 = stopwatch.elapsed(MILLISECONDS);
        logger.info("{} Applied {} ontology changes in {} ms", projectId, String.format("%,d", changes.size()), (t1 - t0));
        Set<OWLOntology> rootOnts = new HashSet<>(manager.getOntologies());
        logger.info("{} Loaded {} ontologies in {} ms", projectId, rootOnts.size(), stopwatch.elapsed(MILLISECONDS));
        MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
        memoryMonitor.monitorMemoryUsage();
        if(rootOnts.size() == 1) {
            return rootOnts.iterator().next();
        }
        manager.getOntologies().stream().flatMap(ont -> ont.getImports().stream()).forEach(rootOnts::remove);
        return rootOnts.stream().sorted().findFirst().orElseThrow();
    }

    private static IRI createUniqueOntologyIRI() {
        String ontologyName = IdUtil.getBase62UUID();
        return IRI.create(GENERATED_ONTOLOGY_IRI_PREFIX + ontologyName);
    }

    private void createOntologyIfNecessary(OWLOntologyChangeRecord chg,
                                           OWLOntologyManager man) {
        var ontologyId = chg.getOntologyID();
        try {
            if(man.contains(ontologyId)) {
                return;
            }
            man.createOntology(ontologyId);
        } catch(OWLOntologyCreationException e) {
            logger.error("{} Could not create ontology with Id: {}", projectId, ontologyId, e);
        }
    }
}

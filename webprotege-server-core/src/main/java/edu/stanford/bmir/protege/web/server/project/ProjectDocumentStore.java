package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntologyDocument;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
@ProjectSingleton
public class ProjectDocumentStore {

    public static final String GENERATED_ONTOLOGY_IRI_PREFIX = "http://webprotege.stanford.edu/project/";

    private static final Logger logger = LoggerFactory.getLogger(ProjectDocumentStore.class);

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final RevisionManager revisionManager;

    private ProjectId projectId;


    @Inject
    public ProjectDocumentStore(ProjectId projectId,
                                RevisionManager revisionManager) {
        this.projectId = checkNotNull(projectId);
        this.revisionManager = checkNotNull(revisionManager);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLOntology initialiseOntologyManagerWithProject(OWLOntologyManager manager) throws OWLOntologyCreationException, OWLOntologyStorageException {
        try {
            logger.info("{} Loading project", projectId);
            writeLock.lock();
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
            manager.getOntologies()
                    .stream()
                    .flatMap(ont -> ont.getImports().stream())
                    .forEach(rootOnts::remove);
            return rootOnts.stream()
                    .sorted()
                    .findFirst()
                    .orElseThrow();
        } finally {
            writeLock.unlock();
        }
    }

    private void createOntologyIfNecessary(OWLOntologyChangeRecord chg, OWLOntologyManager man) {
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

    private static IRI createUniqueOntologyIRI() {
        String ontologyName = IdUtil.getBase62UUID();
        return IRI.create(GENERATED_ONTOLOGY_IRI_PREFIX + ontologyName);
    }
}

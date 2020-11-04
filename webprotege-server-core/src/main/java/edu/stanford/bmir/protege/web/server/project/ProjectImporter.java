package edu.stanford.bmir.protege.web.server.project;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionStoreFactory;
import edu.stanford.bmir.protege.web.server.upload.CsvDocumentResolver;
import edu.stanford.bmir.protege.web.server.upload.DocumentResolver;
import edu.stanford.bmir.protege.web.server.upload.UploadedOntologiesProcessor;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.BranchId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.owl2lpg.exporter.csv.OntologyCsvExporter;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/06/15
 */
public class ProjectImporter {

    private static final Logger logger = LoggerFactory.getLogger(ProjectImporter.class);

    @Nonnull
    private final UploadedOntologiesProcessor uploadedOntologiesProcessor;

    @Nonnull
    private final DocumentResolver documentResolver;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final BranchId branchId;

    @Nonnull
    private final RevisionStoreFactory revisionStoreFactory;

    @Nonnull
//    private final OntologyCsvExporter ontologyCsvExporter;

    @AutoFactory
    @Inject
    public ProjectImporter(@Nonnull ProjectId projectId,
                           @Nonnull BranchId branchId,
                           @Provided @Nonnull UploadedOntologiesProcessor uploadedOntologiesProcessor,
                           @Provided @Nonnull DocumentResolver documentResolver,
                           @Provided @Nonnull RevisionStoreFactory revisionStoreFactory,
//                           @Provided @Nonnull OntologyCsvExporter ontologyCsvExporter,
                           @Provided @Nonnull CsvDocumentResolver csvDocumentResolver) {
        this.projectId = checkNotNull(projectId);
        this.branchId = checkNotNull(branchId);
        this.uploadedOntologiesProcessor = checkNotNull(uploadedOntologiesProcessor);
        this.documentResolver = checkNotNull(documentResolver);
        this.revisionStoreFactory = checkNotNull(revisionStoreFactory);
//        this.ontologyCsvExporter = checkNotNull(ontologyCsvExporter);
    }


    public void createProjectFromSources(DocumentId sourcesId,
                                         UserId owner) throws IOException, OWLOntologyCreationException {
        logger.info("{} Creating project from sources", projectId);
        var stopwatch = Stopwatch.createStarted();
        var uploadedOntologies = uploadedOntologiesProcessor.getUploadedOntologies(sourcesId);
        logger.info("{} Loaded sources in {} ms", projectId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        var memoryMonitor = new MemoryMonitor(logger);
        memoryMonitor.logMemoryUsage();
        logger.info("{} Converting ontologies to CSV", projectId);
        convertOntologiesToCsv(uploadedOntologies);
        logger.info("{} Loading data to Neo4j", projectId);
        loadCsvToNeo4j();
//        deleteCsvFiles();
        logger.info("{} Writing change log", projectId);
        generateInitialChanges(owner, uploadedOntologies);
        deleteSourceFile(sourcesId);

        logger.info("{} Project creation from sources complete in {} ms", projectId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        memoryMonitor.logMemoryUsage();

    }

    private void convertOntologiesToCsv(Collection<Ontology> ontologies) {
//        ontologies.forEach(ontology -> ontologyCsvExporter.export(ontology.getOntologyID(),
//            ontology.getAnnotations(),
//            ontology.getAxioms(),
//            ontology.getImportsDeclarations(),
//            projectId,
//            branchId,
//            ontology.getOntologyDocumentId()));
    }

    private void loadCsvToNeo4j() {
    }

    private void generateInitialChanges(UserId owner, Collection<Ontology> uploadedOntologies) {
        ImmutableList<OntologyChange> changeRecords = getInitialChangeRecords(uploadedOntologies);
        logger.info("{} Writing initial revision containing {} change records", projectId, changeRecords.size());
        Stopwatch stopwatch = Stopwatch.createStarted();
        var revisionStore = revisionStoreFactory.createRevisionStore(projectId);
        revisionStore.addRevision(
                new Revision(
                        owner,
                        RevisionNumber.getRevisionNumber(1),
                        changeRecords,
                        System.currentTimeMillis(),
                        "Initial import"));
        logger.info("{} Initial revision written in {} ms", projectId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private ImmutableList<OntologyChange> getInitialChangeRecords(Collection<Ontology> ontologies) {
        ImmutableList.Builder<OntologyChange> changeRecordList = ImmutableList.builder();
        for (var ont : ontologies) {
            var axioms = ont.getAxioms();
            logger.info("{} Processing ontology source ({} axioms)", projectId, axioms.size());
            for (var axiom : ont.getAxioms()) {
                changeRecordList.add(AddAxiomChange.of(ont.getOntologyDocumentId(), axiom));
            }
            for (var annotation : ont.getAnnotations()) {
                changeRecordList.add(AddOntologyAnnotationChange.of(ont.getOntologyDocumentId(), annotation));
            }
            for (var importsDeclaration : ont.getImportsDeclarations()) {
                changeRecordList.add(AddImportChange.of(ont.getOntologyDocumentId(), importsDeclaration));
            }
        }
        return changeRecordList.build();
    }

    private void deleteSourceFile(DocumentId sourceFileId) {
        var sourceFilePath = documentResolver.resolve(sourceFileId);
        try {
            Files.deleteIfExists(sourceFilePath);
        } catch(IOException e) {
            logger.info("Could not delete uploaded file: {} Cause: {}", sourceFilePath, e.getMessage());
        }
    }
}

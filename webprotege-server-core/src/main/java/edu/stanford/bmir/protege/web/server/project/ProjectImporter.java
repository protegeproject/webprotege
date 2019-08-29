package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.inject.DataDirectory;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.server.inject.project.ChangeHistoryFileProvider;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryFactory;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryProvider;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionStoreImpl;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.apache.commons.io.FileUtils;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/06/15
 */
public class ProjectImporter {

    private static final Logger logger = LoggerFactory.getLogger(ProjectImporter.class);

    private final File uploadsDirectory;

    private final ProjectId projectId;

    private final RevisionStoreImpl revisionStore;

    private final UploadedProjectSourcesExtractor uploadedProjectSourcesExtractor;

    @Inject
    public ProjectImporter(ProjectId projectId,
                           @Nonnull @UploadsDirectory File uploadsDirectory,
                           @Nonnull @DataDirectory File dataDirectory,
                           UploadedProjectSourcesExtractor uploadedProjectSourcesExtractor) {
        this.projectId = projectId;
        this.uploadsDirectory = checkNotNull(uploadsDirectory);
        File projectDirectory = new ProjectDirectoryProvider(
                new ProjectDirectoryFactory(dataDirectory), projectId).get();
        this.revisionStore = new RevisionStoreImpl(projectId,
                                                   new ChangeHistoryFileProvider(projectDirectory).get(),
                                                   new OWLDataFactoryImpl(),
                                                   new OntologyChangeRecordTranslatorImpl());
        this.uploadedProjectSourcesExtractor = uploadedProjectSourcesExtractor;
        this.revisionStore.load();
    }


    public void createProjectFromSources(DocumentId sourcesId,
                                         UserId owner) throws IOException, OWLOntologyCreationException {
        var uploadedFile = new File(uploadsDirectory, sourcesId.getDocumentId());
        if(!uploadedFile.exists()) {
            throw new FileNotFoundException(uploadedFile.getAbsolutePath());
        }
        var rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
        var projectSources = uploadedProjectSourcesExtractor.extractProjectSources(uploadedFile);
        var loaderConfig = new OWLOntologyLoaderConfiguration()
                .setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);

        logger.info("{} Creating project from sources", projectId);
        var stopwatch = Stopwatch.createStarted();
        var rawProjectSourcesImporter = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
        rawProjectSourcesImporter.importRawProjectSources(projectSources);
        logger.info("{} Loaded sources in {} ms", projectId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        var memoryMonitor = new MemoryMonitor(logger);
        memoryMonitor.logMemoryUsage();
        logger.info("{} Writing change log", projectId);
        generateInitialChanges(owner, rootOntologyManager);
        deleteSourceFile(uploadedFile);
        logger.info("{} Project creation from sources complete in {} ms", projectId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        memoryMonitor.logMemoryUsage();

    }

    private void generateInitialChanges(UserId owner, OWLOntologyManager rootOntologyManager) {
        ImmutableList<OntologyChange> changeRecords = getInitialChangeRecords(rootOntologyManager);
        logger.info("{} Writing initial revision containing {} change records", projectId, changeRecords.size());
        Stopwatch stopwatch = Stopwatch.createStarted();
        revisionStore.addRevision(
                new Revision(
                        owner,
                        RevisionNumber.getRevisionNumber(1),
                        changeRecords,
                        System.currentTimeMillis(),
                        "Initial import"));
        logger.info("{} Initial revision written in {} ms", projectId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private ImmutableList<OntologyChange> getInitialChangeRecords(OWLOntologyManager rootOntologyManager) {
        ImmutableList.Builder<OntologyChange> changeRecordList = ImmutableList.builder();
        for (OWLOntology ont : rootOntologyManager.getOntologies()) {
            logger.info("{} Processing ontology source ({} axioms)", projectId, ont.getAxiomCount());
            rootOntologyManager.setOntologyFormat(ont, new BinaryOWLOntologyDocumentFormat());
            for (OWLAxiom axiom : ont.getAxioms()) {
                changeRecordList.add(AddAxiomChange.of(ont.getOntologyID(), axiom));
            }
            for (OWLAnnotation annotation : ont.getAnnotations()) {
                changeRecordList.add(AddOntologyAnnotationChange.of(ont.getOntologyID(), annotation));
            }
            for (OWLImportsDeclaration importsDeclaration : ont.getImportsDeclarations()) {
                changeRecordList.add(AddImportChange.of(ont.getOntologyID(),
                                                        importsDeclaration));
            }
        }
        return changeRecordList.build();
    }

    private void deleteSourceFile(File sourceFile) {
        FileUtils.deleteQuietly(sourceFile);
    }
}

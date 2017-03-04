package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.inject.DataDirectory;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.server.inject.project.*;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionStoreImpl;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.apache.commons.io.FileUtils;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.AddImportData;
import org.semanticweb.owlapi.change.AddOntologyAnnotationData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/06/15
 */
public class ProjectImporter {

    private final File uploadsDirectory;

    private final File rootOntologyDocument;

    private final ProjectId projectId;

    private final RevisionStoreImpl revisionStore;

    private final WebProtegeLogger logger;

    private final UploadedProjectSourcesExtractor uploadedProjectSourcesExtractor;

    private final File dataDirectory;

    @Inject
    public ProjectImporter(ProjectId projectId,
                           @Nonnull @UploadsDirectory File uploadsDirectory,
                           @Nonnull @DataDirectory File dataDirectory,
                           WebProtegeLogger logger,
                           UploadedProjectSourcesExtractor uploadedProjectSourcesExtractor) {
        this.projectId = projectId;
        this.dataDirectory = checkNotNull(dataDirectory);
        this.uploadsDirectory = checkNotNull(uploadsDirectory);
        File projectDirectory = new ProjectDirectoryProvider(
                new ProjectDirectoryFactory(dataDirectory), projectId).get();
        rootOntologyDocument = new RootOntologyDocumentProvider(projectDirectory).get();
        this.revisionStore = new RevisionStoreImpl(projectId,
                                                   new OWLDataFactoryImpl(),
                                                   new ChangeHistoryFileProvider(projectDirectory).get(),
                                                   logger);
        this.logger = logger;
        this.uploadedProjectSourcesExtractor = uploadedProjectSourcesExtractor;
        this.revisionStore.load();
    }


    public void createProjectFromSources(DocumentId sourcesId,
                                         UserId owner) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
        File uploadedFile = new File(uploadsDirectory, sourcesId.getDocumentId());
        if (uploadedFile.exists()) {
            OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
            RawProjectSources projectSources = uploadedProjectSourcesExtractor.extractProjectSources(uploadedFile);
            OWLOntologyLoaderConfiguration loaderConfig = new OWLOntologyLoaderConfiguration()
                    .setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
            RawProjectSourcesImporter importer = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
            OWLOntology ontology = importer.importRawProjectSources(projectSources);

            generateInitialChanges(owner, rootOntologyManager);
            writeNewProject(rootOntologyManager, ontology);
            deleteSourceFile(uploadedFile);
        }
        else {
            throw new FileNotFoundException(uploadedFile.getAbsolutePath());
        }

    }

    private void generateInitialChanges(UserId owner, OWLOntologyManager rootOntologyManager) {
        ImmutableList<OWLOntologyChangeRecord> changeRecords = getInitialChangeRecords(rootOntologyManager);
        revisionStore.addRevision(
                new Revision(
                        owner,
                        RevisionNumber.getRevisionNumber(1),
                        changeRecords,
                        System.currentTimeMillis(),
                        "Initial import"));
    }

    private ImmutableList<OWLOntologyChangeRecord> getInitialChangeRecords(OWLOntologyManager rootOntologyManager) {
        // TODO:  Separate change generator
        ImmutableList.Builder<OWLOntologyChangeRecord> changeRecordList = ImmutableList.builder();
        for (OWLOntology ont : rootOntologyManager.getOntologies()) {
            rootOntologyManager.setOntologyFormat(ont, new BinaryOWLOntologyDocumentFormat());
            for (OWLAxiom axiom : ont.getAxioms()) {
                changeRecordList.add(new OWLOntologyChangeRecord(ont.getOntologyID(), new AddAxiomData(axiom)));
            }
            for (OWLAnnotation annotation : ont.getAnnotations()) {
                changeRecordList.add(new OWLOntologyChangeRecord(ont.getOntologyID(),
                                                                 new AddOntologyAnnotationData(annotation)));
            }
            for (OWLImportsDeclaration importsDeclaration : ont.getImportsDeclarations()) {
                changeRecordList.add(new OWLOntologyChangeRecord(ont.getOntologyID(),
                                                                 new AddImportData(importsDeclaration)));
            }
        }
        return changeRecordList.build();
    }

    private void deleteSourceFile(File sourceFile) {
        FileUtils.deleteQuietly(sourceFile);
    }

    private void writeNewProject(OWLOntologyManager rootOntologyManager,
                                 OWLOntology ontology) throws OWLOntologyStorageException {
        rootOntologyDocument.getParentFile().mkdirs();
        rootOntologyManager.saveOntology(ontology, new BinaryOWLOntologyDocumentFormat(), IRI.create(rootOntologyDocument));
        ImportsCacheManager importsCacheManager = new ImportsCacheManager(
                projectId,
                new ImportsCacheDirectoryProvider(
                        new ProjectDirectoryProvider(
                                new ProjectDirectoryFactory(dataDirectory),
                                projectId)),
                logger
        );
        importsCacheManager.cacheImports(ontology);
    }

}

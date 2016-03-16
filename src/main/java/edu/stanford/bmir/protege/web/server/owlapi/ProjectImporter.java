package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.inject.DataDirectoryProvider;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectoryProvider;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.inject.project.ChangeHistoryFileProvider;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryFactory;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryProvider;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntologyDocumentProvider;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.change.Revision;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionStoreImpl;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    public ProjectImporter(ProjectId projectId) {
        // THIS WILL BE THROWN OUT
        this.projectId = projectId;
        WebProtegeProperties properties = WebProtegeInjector.get().getInstance(WebProtegeProperties.class);
        DataDirectoryProvider dataDirectoryProvider = new DataDirectoryProvider(properties);
        this.uploadsDirectory = new UploadsDirectoryProvider(dataDirectoryProvider.get()).get();
        File projectDirectory = new ProjectDirectoryProvider(new ProjectDirectoryFactory(dataDirectoryProvider.get()), projectId).get();
        rootOntologyDocument = new RootOntologyDocumentProvider(projectDirectory).get();
        WebProtegeLogger logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);
        this.revisionStore = new RevisionStoreImpl(projectId,
                new OWLDataFactoryImpl(false, false),
                new ChangeHistoryFileProvider(projectDirectory).get(),
                logger);
        this.revisionStore.load();
    }


    public void createProjectFromSources(DocumentId sourcesId, UserId owner) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
        File uploadedFile = new File(uploadsDirectory, sourcesId.getDocumentId());
        UploadedProjectSourcesExtractor extractor = WebProtegeInjector.get().getInstance(UploadedProjectSourcesExtractor.class);
        if (uploadedFile.exists()) {
            OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
            RawProjectSources projectSources = extractor.extractProjectSources(uploadedFile);
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
            for(OWLAxiom axiom : ont.getAxioms()) {
                changeRecordList.add(new OWLOntologyChangeRecord(ont.getOntologyID(), new AddAxiomData(axiom)));
            }
            for(OWLAnnotation annotation : ont.getAnnotations()) {
                changeRecordList.add(new OWLOntologyChangeRecord(ont.getOntologyID(), new AddOntologyAnnotationData(annotation)));
            }
            for(OWLImportsDeclaration importsDeclaration : ont.getImportsDeclarations()) {
                changeRecordList.add(new OWLOntologyChangeRecord(ont.getOntologyID(), new AddImportData(importsDeclaration)));
            }
        }
        return changeRecordList.build();
    }

    private void deleteSourceFile(File sourceFile) {
        FileUtils.deleteQuietly(sourceFile);
    }

    private void writeNewProject(
            OWLOntologyManager rootOntologyManager,
            OWLOntology ontology) throws
            OWLOntologyStorageException {
        rootOntologyDocument.getParentFile().mkdirs();
        rootOntologyManager.saveOntology(ontology, new BinaryOWLOntologyDocumentFormat(),
                IRI.create(rootOntologyDocument));
        ImportsCacheManager cacheManager = new ImportsCacheManager(projectId);
        cacheManager.cacheImports(ontology);
    }

}

package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.project.ImportedOntologyMetadata;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentHandlerAdapter;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.owlapi.OWLOntologyWrapper;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.SetOntologyIDData;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/04/2013
 */
public class ImportsCacheManager {



    private static final String META_DATA_TIME_STAMP_ATTR = "timestamp";

    private static final String METADATA_DOCUMENT_IRI_ATTR = "documentIRI";




    private final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    private final Lock READ_LOCK = READ_WRITE_LOCK.readLock();

    private final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();








    private final WebProtegeLogger logger;

    private Map<OWLOntologyID, ImportedOntologyMetadata> metadataMap = new HashMap<OWLOntologyID, ImportedOntologyMetadata>();

    private Set<OWLOntologyID> ontologyIDs = new HashSet<OWLOntologyID>();

    private Map<IRI, IRI> iri2Document = new HashMap<IRI, IRI>();


    private final ProjectId projectId;

    private final OWLAPIProjectFileStore projectFileStore;

    public ImportsCacheManager(ProjectId projectId) {
        this.logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);
        this.projectId = projectId;
        projectFileStore = WebProtegeInjector.get().getInstance(OWLAPIProjectFileStoreFactory.class).get(projectId);
    }

    public OWLOntologyIRIMapper getIRIMapper() {
        try {
            WRITE_LOCK.lock();
            readCachedImportsFromDisk();
            return new ImportsCacheIRIMapper(iri2Document);
        }
        finally {
            WRITE_LOCK.lock();
        }
    }

    public void cacheImports(OWLOntology rootOntology) {
        try {
            WRITE_LOCK.lock();
            ontologyIDs.clear();
            readCachedImportsFromDisk();
            projectFileStore.getImportsCacheDataDirectory().mkdirs();
            for(OWLOntology ont : rootOntology.getImportsClosure()) {
                // TODO: Don't cache project ontologies!
                if(!ont.equals(rootOntology)) {
                    cacheOntologyIfNotAlreadyCached(ont);
                }
            }
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    private void cacheOntologyIfNotAlreadyCached(OWLOntology ont) {
        try {
            WRITE_LOCK.lock();
            if(ontologyIDs.contains(ont.getOntologyID())) {
                return;
            }
            DataOutputStream os = null;
            try {

                final long timestamp = System.currentTimeMillis();
                IRI documentIRI = ont.getOWLOntologyManager().getOntologyDocumentIRI(ont);
                final ImportedOntologyMetadata value = new ImportedOntologyMetadata(ont.getOntologyID(), documentIRI, timestamp);


                BinaryOWLMetadata metadata = toBinaryOWLMetadata(value);
                BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
                final File file = getFreshImportCacheFile();
                os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                serializer.write(new OWLOntologyWrapper(ont), os, metadata);

                ontologyIDs.add(ont.getOntologyID());
                metadataMap.put(ont.getOntologyID(), value);
                logger.info("Cached imported ontology: " + ont.getOntologyID() + " in " + file.getName());
            }

            catch (IOException e) {
                logger.severe(e);
            }
            finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                }
                catch (IOException e) {
                    logger.severe(e);
                }
            }
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }



    /**
     * Gets a fresh file for caching an import.
     * @return A file for caching the import.  Not {@code null}.
     */
    private File getFreshImportCacheFile() {
        return new File(projectFileStore.getImportsCacheDataDirectory(), UUID.randomUUID() + ".binary");
    }


    /**
     * Translates an {@link ImportedOntologyMetadata} object to {@link BinaryOWLMetadata}.
     * @param value The value to be translated.
     * @return The translation.  Not {@code null}.
     */
    private BinaryOWLMetadata toBinaryOWLMetadata(ImportedOntologyMetadata value) {
        BinaryOWLMetadata metadata = new BinaryOWLMetadata();
        metadata.setLongAttribute(META_DATA_TIME_STAMP_ATTR, value.getAccessTimestamp());
        metadata.setStringAttribute(METADATA_DOCUMENT_IRI_ATTR, value.getOriginalDocumentLocation().toString());
        return metadata;
    }


    private void readCachedImportsFromDisk() {
        try {
            WRITE_LOCK.lock();
            File importsCacheDataDirectory = projectFileStore.getImportsCacheDataDirectory();
            final File[] cachedDocuments = importsCacheDataDirectory.listFiles();
            if(cachedDocuments == null) {
                return;
            }
            for(File ontologyDocument : cachedDocuments) {
                if (!ontologyDocument.isHidden() && !ontologyDocument.isDirectory()) {
                    parseOntologyDocument(ontologyDocument);
                }
            }
            for(OWLOntologyID id : ontologyIDs) {
                logger.info("Cached import: " + id);
            }
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    private void parseOntologyDocument(File ontologyDocument) {
        try {
            WRITE_LOCK.lock();
            InputStream is = null;

            try {
                BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
                is = new BufferedInputStream(new FileInputStream(ontologyDocument));
                final Handler handler = new Handler();
                serializer.read(is, handler, new OWLDataFactoryImpl());
                OWLOntologyID id = handler.getOntologyID();
                if(id.getOntologyIRI().isPresent()) {
                    ontologyIDs.add(id);
                    iri2Document.put(id.getOntologyIRI().get(), IRI.create(ontologyDocument.toURI()));
                    if(id.getVersionIRI().isPresent()) {
                        iri2Document.put(id.getVersionIRI().get(), IRI.create(ontologyDocument));
                    }
                    metadataMap.put(id, new ImportedOntologyMetadata(id, handler.getDocumentIRI(), handler.getTimestamp()));
                }
            }
            catch (IOException e) {
                logger.severe(e);
            }
            catch (BinaryOWLParseException e) {
                logger.severe(e);
            }
            catch (UnloadableImportException e) {
                logger.severe(e);
            }
            finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                }
                catch (IOException e) {
                    logger.severe(e);
                }
            }
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }




//    public void refreshCachedOntology(OWLOntologyID ontologyID) {
//
//    }



    private static class ImportsCacheIRIMapper implements OWLOntologyIRIMapper {

        private Map<IRI, IRI> map = new HashMap<IRI, IRI>();

        private ImportsCacheIRIMapper(Map<IRI, IRI> map) {
            this.map = map;
        }

        @Override
        public IRI getDocumentIRI(IRI ontologyIRI) {
            return map.get(ontologyIRI);
        }
    }


    private static class Handler extends BinaryOWLOntologyDocumentHandlerAdapter<OWLRuntimeException> {

        private OWLOntologyID ontologyID;

        private long timestamp;

        private IRI documentIRI;

        @Override
        public void handleDocumentMetaData(BinaryOWLMetadata metadata) throws OWLRuntimeException {
            timestamp = metadata.getLongAttribute(META_DATA_TIME_STAMP_ATTR, 0l);
            documentIRI = IRI.create(metadata.getStringAttribute(METADATA_DOCUMENT_IRI_ATTR, ""));
        }

        @Override
        public void handleOntologyID(OWLOntologyID ontologyID) throws OWLRuntimeException {
            this.ontologyID = ontologyID;
        }

        @Override
        public void handleChanges(OntologyChangeDataList changesList) {
            for(OWLOntologyChangeData changeData : changesList) {
                if(changeData instanceof SetOntologyIDData) {
                    ontologyID = ((SetOntologyIDData) changeData).getNewId();
                }
            }
        }

        public OWLOntologyID getOntologyID() {
            return ontologyID;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public IRI getDocumentIRI() {
            return documentIRI;
        }
    }
}

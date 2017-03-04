package edu.stanford.bmir.protege.web.server.project;


import edu.stanford.bmir.protege.web.server.owlapi.ExplodedOWLAPIProjectSource;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/05/2012
 */
public class OWLAPIProjectImporter {

    public static final char ZIP_FILE_MAGIC_NUMBER_BYTE_0 = 'P';

    public static final char ZIP_FILE_MAGIC_NUMBER_BYTE_1 = 'K';

    private File sourceFile;
    
    private boolean isZipFile;
    

    private OWLAPIProjectImporter(File sourceFile) throws IOException {
        if(!sourceFile.exists()) {
            throw new FileNotFoundException(sourceFile.getName());
        }
        this.sourceFile = sourceFile;
        isZipFile = isZipFile(sourceFile);
    }


    /**
     * Creates a project importer for a given source file, which may be a vanilla OWL compatible ontology document (that
     * the OWL API can parse directly) or a zip file in the project upload format:  /project   /project/ontologies
     * /project/ontologies/root-ontology*  /project/ontologies/imports/*
     * @param sourceDocumentFile The source document file
     * @return An importer for the source document
     * @throws FileNotFoundException if sourceDocumentFile does not exist
     * @throws IOException if there was a problem scanning sourceDocumentFile
     */
    public static OWLAPIProjectImporter create(File sourceDocumentFile) throws IOException {
        return new OWLAPIProjectImporter(sourceDocumentFile);
    }

    public boolean isZipFile() {
        return isZipFile;
    }

    public OWLOntology loadRootOntologyAndImportsClosure() throws OWLOntologyCreationException, IOException {
        if(isZipFile()) {
            return loadRootOntologyAndImportsClosureFromZipFile();
        }
        else {
            return loadRootOntologyAndImportsClosureFromOntologyDocument();
        }
    }
    
    
    private OWLOntology loadRootOntologyAndImportsClosureFromZipFile() throws IOException, OWLOntologyCreationException {
        File tempDir = extractZipFile(sourceFile);
        File projectDir = new File(tempDir, ExplodedOWLAPIProjectSource.PROJECT_DIRECTORY_NAME);
        ExplodedOWLAPIProjectSource explodedSource = new ExplodedOWLAPIProjectSource(projectDir);
        if(!explodedSource.isWellFormed()) {

        }
        OWLOntologyManager manager = createOntologyManager();
        return explodedSource.loadRootOntologyImportsClosure(manager);

    }
    
    private OWLOntology loadRootOntologyAndImportsClosureFromOntologyDocument() throws IOException, OWLOntologyCreationException {
        OWLOntologyManager manager = createOntologyManager();
        return manager.loadOntologyFromOntologyDocument(sourceFile);
    }

    private OWLOntologyManager createOntologyManager() {
        OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
        manager.clearIRIMappers();
        return manager;
    }



    private static File extractZipFile(File sourceFile) throws IOException {
        File tempDir = File.createTempFile("owlapi-project", "");
        tempDir.mkdir();
        ZipFile zipFile = new ZipFile(sourceFile);
        
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if(zipEntry.isDirectory()) {
                File dir = new File(tempDir, zipEntry.getName());
                dir.mkdirs();
            }
            else {
                InputStream is = zipFile.getInputStream(zipEntry);
                int bufferSize = 10 * 1024 * 1024;
                BufferedInputStream bis = new BufferedInputStream(is, bufferSize);
                File entryFile = new File(tempDir, zipEntry.getName());
                entryFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(entryFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte [] buffer = new byte[bufferSize];
                while(true) {
                    int read = bis.read(buffer);
                    if(read == -1) {
                        break;
                    }
                    bos.write(buffer, 0, read);
                    if(read < buffer.length) {
                        break;
                    }
                }
                bis.close();
                bos.close();
            }   
        }
        return tempDir;
    }

    public static boolean isZipFile(File sourceDocumentFile) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(sourceDocumentFile), 5));
        char magicNumberByte0 = (char) dis.readByte();
        char magicNumberByte1 = (char) dis.readByte();
        dis.close();
        return magicNumberByte0 == ZIP_FILE_MAGIC_NUMBER_BYTE_0 && magicNumberByte1 == ZIP_FILE_MAGIC_NUMBER_BYTE_1;
    }


}

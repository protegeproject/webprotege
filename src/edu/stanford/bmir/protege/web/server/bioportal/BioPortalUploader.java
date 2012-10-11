package edu.stanford.bmir.protege.web.server.bioportal;

import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalOntologyId;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalUploadInfo;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalUserId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalUploader {


    private File ontologyDocument;

    private BioPortalUploadInfo uploadInfo;

    public BioPortalUploader(File ontologyDocument, BioPortalUploadInfo bioPortalUploadInfo) {
        this.ontologyDocument = ontologyDocument;
        this.uploadInfo = bioPortalUploadInfo;
    }

    public BioPortalUploader(ProjectId projectId, RevisionNumber revisionNumber, BioPortalUploadInfo uploadInfo) throws IOException, OWLOntologyStorageException {
        this.uploadInfo = uploadInfo;
        ontologyDocument = getOntologyDocumentFromProjectAndRevision(projectId, revisionNumber);
    }
    
    private File getOntologyDocumentFromProjectAndRevision(ProjectId projectId, RevisionNumber revisionNumber) throws IOException, OWLOntologyStorageException {
        OWLAPIProjectDocumentStore store = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
        File ontologyDocument = File.createTempFile("BioPortalOntology", ".zip");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ontologyDocument));
        store.exportProjectRevision(revisionNumber, bos, new RDFXMLOntologyFormat());
        return ontologyDocument;
    }

    public void uploadToBioPortal(String bioportalRestAPIBase) throws IOException {
        if(bioportalRestAPIBase == null) {
            throw new NullPointerException("bioportalRestAPIBase must not be null");
        }
        uploadToBioPortal(bioportalRestAPIBase, ontologyDocument);
        ontologyDocument.delete();
    }
    
    private void setParameter(String name, Integer value, MultipartEntity entity) throws UnsupportedEncodingException {
        if(value == null) {
            return;
        }
        setParameter(name, Integer.toString(value), entity);
    }
    
    private void setParameter(String name, String value, MultipartEntity entity) throws UnsupportedEncodingException {
        if(value == null) {
            return;
        }
        if(value.isEmpty()) {
            return;
        }
        entity.addPart(name, new StringBody(value));
    }

    private void uploadToBioPortal(String bioportalRestAPIBase, File ontologyDocument) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(bioportalRestAPIBase + "ontologies/");

        FileBody bin = new FileBody(ontologyDocument, "text/plain");
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("filePath", bin);
        
        setParameter(BioPortalConstants.API_KEY_PROPERTY, BioPortalConstants.DEFAULT_API_KEY, reqEntity);

        setParameter("displayLabel", uploadInfo.getDisplayLabel(), reqEntity);
        setParameter("userId", uploadInfo.getUserId().getIntValue(), reqEntity);
        setParameter("description", uploadInfo.getOntologyDescription(), reqEntity);

        setParameter("format", BioPortalOntologyFormat.OWL.getFormatName(), reqEntity);

        BioPortalReleaseDate releaseDate = BioPortalReleaseDate.createWithToday();
        setParameter("dateReleased", releaseDate.formatForRestService(), reqEntity);

        setParameter("contactName", uploadInfo.getContactName(), reqEntity);
        setParameter("contactEmail", uploadInfo.getContactEmailAddress(), reqEntity);

        setParameter("abbreviation", uploadInfo.getOntologyAbbreviation(), reqEntity);
        setParameter("versionNumber", uploadInfo.getVersionNumber(), reqEntity);


        setParameter("synonymsSlot", OWLRDFVocabulary.RDFS_LABEL.getIRI().toString(), reqEntity);

        BioPortalOntologyId bioPortalOntologyId = uploadInfo.getBioPortalOntologyId();
        if (!bioPortalOntologyId.isNull()) {
            setParameter("ontologyId", bioPortalOntologyId.getIntValue(), reqEntity);
        }
        setParameter("isRemote", "0", reqEntity);

        setParameter("publication", uploadInfo.getPublicationLink(), reqEntity);
        setParameter("homepage", uploadInfo.getHomepageLink(), reqEntity);
        setParameter("documentation", uploadInfo.getDocumentationLink(), reqEntity);


        httppost.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        System.out.println(resEntity);
        InputStream is = resEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        try {
            File ontologyDocument = new File("/tmp/root-ontology.owl");

            BioPortalUploadInfo uploadInfo = new BioPortalUploadInfo(
                    BioPortalUserId.createFromId(38871),
                    BioPortalOntologyId.getId(3142),
                    "Test Ontology 34",
                    "TONT",
                    "An ontology for testing the bioportal upload",
                    "Matthew Horridge",
                    "matthew.horridge@stanford.edu",
                    "1.0.2");
            BioPortalUploader uploader = new BioPortalUploader(ontologyDocument, uploadInfo);
            uploader.uploadToBioPortal("http://stagerest.bioontology.org/bioportal/", ontologyDocument);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

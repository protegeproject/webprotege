package edu.stanford.bmir.protege.web.server.bioportal;

import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioportalProposalsService;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.BioPortalNoteConstants;
import edu.stanford.bmir.protege.web.server.URLUtil;
import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import org.ncbo.stanford.util.BioPortalServerConstants;

public class BioPortalProposalsServiceImpl extends WebProtegeRemoteServiceServlet implements BioportalProposalsService {

    private static final long serialVersionUID = -4997960524274542340L;


    public String getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId) {
        return getBioPortalNotesXML(bpRestBase, ontologyVersionId, null);
    }

    public String getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId, String entityURI) {
        return getBioPortalNotesXML(bpRestBase, ontologyVersionId, entityURI);
    }

    //TODO: move to BP lib
    private static String getBioPortalNotesXML(String bpRestBase, String ontologyVersionId, String entityURI) {
        entityURI = URLUtil.encode(entityURI);
        if (entityURI == null) { return null; }

        String url = bpRestBase == null ? BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL : bpRestBase;
        url = url + "notes/" + ontologyVersionId;
        url = url + "?";
        url = url + "conceptid=" + entityURI;
        //TODO: make configurable
        url = url + "&threaded=true";
        url = url + "&" + BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX;

        return URLUtil.getURLContent(url);
    }

    public String getBioPortalUsers(String bpRestBase) {
        String url = bpRestBase == null ? BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL : bpRestBase;
        url = url + "users";
        url = url + "?" + BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX;
        return URLUtil.getURLContent(url);
    }

    public String changeNoteStatus(String projectName, String bpRestBase, String ontologyVirtuaId, String noteId, String newStatus) {
        String url = bpRestBase == null ? BioPortalServerConstants.BP_REST_BASE : bpRestBase;
        url = url + "virtual/notes/";
        url = url + ontologyVirtuaId + "?noteid=" + noteId + "&status=" + URLUtil.encode(newStatus);
        url = url + "&" + BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX;

        URLUtil.httpPut(url);

        return null;
    }

    public void createNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteType,
            String appliesToId, String appliesToType,
            String subject, String content, String author, String email) {

        noteType = noteType == null ? BioPortalNoteConstants.NOTE_TYPE_COMMENT : noteType;
        noteType= URLUtil.encode(noteType);
        appliesToId = URLUtil.encode(appliesToId);
        subject = URLUtil.encode(subject);
        content = URLUtil.encode(content);
        author = URLUtil.encode(author);
        email = URLUtil.encode(email);

        StringBuffer params = new StringBuffer();
        addIfNotNull(params, "type", noteType, true);
        addIfNotNull(params, "appliesto", appliesToId, true);
        addIfNotNull(params, "appliestotype", appliesToType, true);
        addIfNotNull(params, "subject", subject, true);
        addIfNotNull(params, "content", content, true);
        addIfNotNull(params, "author", author, true);
        addIfNotNull(params, "email", email, true);

        params.append(BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX);

        StringBuffer url = new StringBuffer(bpRestBase == null ? BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL : bpRestBase);
        if (isVirtual) {
            url.append("virtual/");
        }
        url.append("notes/");
        url.append(ontologyId);

        URLUtil.httpPost(url.toString(), params.toString());
    }

    public void deleteNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId) {
        noteId = URLUtil.encodeNN(noteId);

        StringBuffer params = new StringBuffer();
        params.append("noteid="); params.append(noteId); params.append("&");
        params.append(BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX);

        StringBuffer url = new StringBuffer(bpRestBase == null ? BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL : bpRestBase);
        if (isVirtual) {
            url.append("virtual/");
        }
        url.append("notes/");
        url.append(ontologyId);

        url.append("?");
        url.append(params);

        URLUtil.httpDelete(url.toString());
    }

    public void updateNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId,
            Boolean archive, Boolean archivethread,
            Boolean unarchive, Boolean unarchivethread,
            String noteType,
            String appliesToId, String appliesToType,
            String subject, String content,
            String author, String email,
            String status) {

        noteId = URLUtil.encode(noteId);
        noteType= URLUtil.encode(noteType);
        appliesToId = URLUtil.encode(appliesToId);
        subject = URLUtil.encode(subject);
        content = URLUtil.encode(content);
        author = URLUtil.encode(author);
        status = URLUtil.encode(status);

        StringBuffer params = new StringBuffer();
        addIfNotNull(params, "noteid", noteId, true);

        addIfNotNull(params, "archive", archive, true);
        addIfNotNull(params, "archivethread", archivethread, true);

        addIfNotNull(params, "unarchive", unarchive, true);
        addIfNotNull(params, "unarchivethread", unarchivethread, true);

        addIfNotNull(params, "type", noteType, true);

        addIfNotNull(params, "appliesto", appliesToId, true);
        addIfNotNull(params, "appliestotype", appliesToType, true);

        addIfNotNull(params, "subject", subject, true);
        addIfNotNull(params, "content", content, true);

        addIfNotNull(params, "author", author, true);
        addIfNotNull(params, "email", email, true);

        addIfNotNull(params, "status", status, true);

        params.append(BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX);

        StringBuffer url = new StringBuffer(bpRestBase == null ? BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL : bpRestBase);
        if (isVirtual) {
            url.append("virtual/");
        }
        url.append("notes/");
        url.append(ontologyId);
        url.append("?");

        url.append(params);

        URLUtil.httpPut(url.toString());
    }

    private void addIfNotNull(StringBuffer buffer, String param, Object value, boolean addAnd) {
        if (value == null) {
            return;
        }
        buffer.append(param);
        buffer.append("=");
        buffer.append(value.toString());
        if (addAnd) {
            buffer.append("&");
        }
    }

    public static void main(String[] args) {
        BioPortalProposalsServiceImpl inst = new BioPortalProposalsServiceImpl();
        inst.deleteNote(null, null, "1104", true, "Note_695f5d33-daaa-4ebe-a4cb-c2bedb46da60");
    }

}

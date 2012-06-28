package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BioportalProposalsAsync {

    void getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId, AsyncCallback<String> callback);

    void getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId, String entityURI, AsyncCallback<String> callback);

    void getBioPortalUsers(String bpRestBase, AsyncCallback<String> callback);

    void changeNoteStatus(String projectName, String bpRestBase, String ontologyVirtualId, String noteId, String newStatus, AsyncCallback<String> callback);

    void createNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteType,
            String appliesToId, String appliesToType, String subject, String content, String author, String email,
            AsyncCallback<Void> callback);

    void deleteNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId,
            AsyncCallback<Void> callback);

    void updateNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId,
            Boolean archive, Boolean archivethread, Boolean unarchive, Boolean unarchivethread, String noteType,
            String appliesToId, String appliesToType, String subject, String content, String author, String email,
            String status, AsyncCallback<Void> callback);

}

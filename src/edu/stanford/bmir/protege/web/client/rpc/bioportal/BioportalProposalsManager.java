package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BioportalProposalsManager {

    private static BioportalProposalsAsync proxy;
    private static BioportalProposalsManager instance;

    private BioportalProposalsManager() {
        proxy = (BioportalProposalsAsync) GWT.create(BioportalProposals.class);
    }

    public static BioportalProposalsManager getBioportalProposalsManager() {
        if (instance == null) {
            instance = new BioportalProposalsManager();
        }
        return instance;
    }

    public  void getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId, AsyncCallback<String> callback) {
        proxy.getBioportalProposals(projectName, bpRestBase, ontologyVersionId, callback);
    }

    public  void getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId, String entityURI, AsyncCallback<String> callback) {
        proxy.getBioportalProposals(projectName, bpRestBase, ontologyVersionId, entityURI, callback);
    }

    public void getBioPortalUsers(String bpRestBase, AsyncCallback<String> callback) {
        proxy.getBioPortalUsers(bpRestBase, callback);
    }

    public  void changeNoteStatus(String projectName, String bpRestBase, String ontologyVirtualId, String noteId, String newStatus, AsyncCallback<String> callback) {
        proxy.changeNoteStatus(projectName, bpRestBase, ontologyVirtualId, noteId, newStatus, callback);
    }

    public void createNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteType,
            String appliesToId, String appliesToType, String subject, String content, String author, String email,
            AsyncCallback<Void> callback) {
        proxy.createNote(projectName, bpRestBase, ontologyId, isVirtual, noteType, appliesToId, appliesToType, subject, content, author, email, callback);
    }

    public void deleteNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId,
            AsyncCallback<Void> callback) {
        proxy.deleteNote(projectName, bpRestBase, ontologyId, isVirtual, noteId, callback);
    }

    public void updateNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId,
            Boolean archive, Boolean archivethread, Boolean unarchive, Boolean unarchivethread, String noteType,
            String appliesToId, String appliesToType, String subject, String content, String author, String email,
            String status, AsyncCallback<Void> callback) {
        proxy.updateNote(projectName, bpRestBase, ontologyId, isVirtual, noteId, archive, archivethread, unarchive, unarchivethread,
                noteType, appliesToId, appliesToType, subject, content, author, email, status, callback);
    }

}

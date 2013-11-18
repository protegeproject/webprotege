package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bpProposals")
public interface BioportalProposals extends RemoteService {

    String getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId);

    String getBioportalProposals(String projectName, String bpRestBase, String ontologyVersionId, String entityURI);

    String getBioPortalUsers(String bpRestBase);

    void createNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteType,
            String appliesToId, String appliesToType,
            String subject, String content, String author, String email);

    String changeNoteStatus(String projectName, String bpRestBase, String ontologyVersionId, String noteId, String newStatus);

    void deleteNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId);

    public void updateNote(String projectName, String bpRestBase, String ontologyId, boolean isVirtual, String noteId,
            Boolean archive, Boolean archivethread,
            Boolean unarchive, Boolean unarchivethread,
            String noteType,
            String appliesToId, String appliesToType,
            String subject, String content,
            String author, String email,
            String status) ;

}

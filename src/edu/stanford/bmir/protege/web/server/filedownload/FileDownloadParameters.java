package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;

import javax.servlet.http.HttpServletRequest;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 * <p>
 *     Wraps a HttpServletRequest to enable information about a download request to be extracted.
 * </p>
 */
public class FileDownloadParameters {

    private HttpServletRequest request;

    public FileDownloadParameters(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Determines if this is a request for a project download.
     * @return <code>true</code> if this is a request for a project download, otherwise <code>false</code>
     */
    public boolean isProjectDownload() {
        return getRawProjectNameParameter() != null;
    }


    public ProjectId getProjectId() {
        String projectName = getRawProjectNameParameter();
        if(projectName == null) {
            throw new UnsupportedOperationException("getProjectId can only be called if the request is for a project download (isProjectDownload() returns true)");
        }
        return ProjectId.get(projectName);
    }

    /**
     * Gets the requested revision number from the request parameters.
     * @return The requested revision.  If no revision in particular has been requested then the revision number
     * that denotes the head revision will be returned.  Also, if the revision number is malformed in the request then
     * the RevisionNumber corresponding the head revision will be returned.
     */
    public RevisionNumber getRequestedRevision() {
        String revisionString = getRawRevisionParameter();
        if(revisionString == null) {
            return RevisionNumber.getHeadRevisionNumber();
        }
        else {
            try {
                long rev = Long.parseLong(revisionString);
                return RevisionNumber.getRevisionNumber(rev);
            }
            catch (NumberFormatException e) {
                // TODO: Log!
                return RevisionNumber.getHeadRevisionNumber();
            }
        }
    }


    private String getRawRevisionParameter() {
        return request.getParameter(FileDownloadConstants.REVISION);
    }

    /**
     * Gets the raw project name request parameter.
     * @return The parameter or <code>null</code> if the parameter was not specified in the request.
     */
    private String getRawProjectNameParameter() {
        return request.getParameter(FileDownloadConstants.PROJECT_NAME_PARAMETER);
    }
}

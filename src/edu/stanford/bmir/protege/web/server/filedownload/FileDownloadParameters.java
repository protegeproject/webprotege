package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;

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
        return new ProjectId(projectName);
    }


    /**
     * Gets the raw project name request parameter.
     * @return The parameter or <code>null</code> if the parameter was not specified in the request.
     */
    private String getRawProjectNameParameter() {
        return request.getParameter(FileDownloadConstants.PROJECT_NAME_PARAMETER);
    }
}

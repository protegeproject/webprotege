package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectIdFormatException;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.MoreObjects.toStringHelper;

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
     * @return <code>true</code> if this is a request for a project download and the projectId is specified and the
     * id is well-formed, otherwise <code>false</code>.
     */
    public boolean isProjectDownload() {
        String rawProjectNameParameter = getRawProjectNameParameter();
        boolean projectParamPresent = rawProjectNameParameter != null;
        if(!projectParamPresent) {
            return false;
        }
        try {
            ProjectId.get(rawProjectNameParameter);
            return true;
        } catch (ProjectIdFormatException e) {
            return false;
        }
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

    /**
     * Gets the requested format
     * @return The ontology format.  Not {@code null}.
     */
    public DownloadFormat getFormat() {
        String format = getRawFormatParameter();
        return DownloadFormat.getDownloadFormatFromParameterName(format);
    }

    private String getRawFormatParameter() {
        return request.getParameter(FileDownloadConstants.FORMAT);
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


    @Override
    public String toString() {
        return toStringHelper("FileDownloadParameters" )
                .add("projectId", getProjectId())
                .add("revision", getRequestedRevision())
                .add("format", getFormat())
                .toString();
    }
}

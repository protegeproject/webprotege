package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class FileDownloadParametersTestCase {

    @Mock
    protected HttpServletRequest servletRequest;

    private FileDownloadParameters parameters;

    @Before
    public void setUp() {
        parameters = new FileDownloadParameters(servletRequest);
    }

    @Test
    public void shouldParseRevision() {
        when(servletRequest.getParameter(FileDownloadConstants.REVISION)).thenReturn("3");
        RevisionNumber revisionNumber = parameters.getRequestedRevision();
        assertThat(revisionNumber.getValue(), is(equalTo(3l)));
    }

    @Test
    public void shouldParseNoRevisionIntoHeadRevision() {
        RevisionNumber revisionNumber = parameters.getRequestedRevision();
        assertThat(revisionNumber.isHead(), is(true));
    }

    @Test
    public void shouldParseMalformedRevisionIntoHeadRevision() {
        when(servletRequest.getParameter(FileDownloadConstants.REVISION)).thenReturn("x");
        RevisionNumber revisionNumber = parameters.getRequestedRevision();
        assertThat(revisionNumber.isHead(), is(true));
    }

    @Test
    public void shouldParseLongMaxIntoHeadRevision() {
        String rev = Long.toString(Long.MAX_VALUE);
        when(servletRequest.getParameter(FileDownloadConstants.REVISION)).thenReturn(rev);
        RevisionNumber revisionNumber = parameters.getRequestedRevision();
        assertThat(revisionNumber.isHead(), is(true));
    }

    @Test
    public void shouldReturnFalseForIsDownloadIfProjectIdIsNotSpecified() {
        assertThat(parameters.isProjectDownload(), is(false));
    }

    @Test
    public void shouldReturnFalseForIsDownloadIfProjectIdIsMalformed() {
        when(servletRequest.getParameter(FileDownloadConstants.PROJECT_NAME_PARAMETER)).thenReturn("x");
        assertThat(parameters.isProjectDownload(), is(false));
    }


    @Test
    public void shouldParseOntologyId() {
        ProjectId projectId = ProjectId.get("00000000-0000-0000-0000-000000000000");
        when(servletRequest.getParameter(FileDownloadConstants.PROJECT_NAME_PARAMETER)).thenReturn(projectId.getId());
        assertThat(parameters.getProjectId(), is(equalTo(projectId)));
    }

    @Test
    public void shouldParseNoFormatAsRDFXML() {
        assertThat(parameters.getFormat(), is(equalTo(DownloadFormat.RDF_XML)));
    }

    @Test
    public void shouldParseJunkAsRDFXMLFormat() {
        when(servletRequest.getParameter(FileDownloadConstants.FORMAT)).thenReturn("junk");
        assertThat(parameters.getFormat(), is(equalTo(DownloadFormat.RDF_XML)));
    }

    @Test
    public void shouldParseRDFXMLFormat() {
        when(servletRequest.getParameter(FileDownloadConstants.FORMAT)).thenReturn("owl");
        assertThat(parameters.getFormat(), is(equalTo(DownloadFormat.RDF_XML)));
    }


    @Test
    public void shouldParseRDFTurtleFormat() {
        when(servletRequest.getParameter(FileDownloadConstants.FORMAT)).thenReturn("ttl");
        assertThat(parameters.getFormat(), is(equalTo(DownloadFormat.RDF_TURLE)));
    }


    @Test
    public void shouldParseOWLXMLFormat() {
        when(servletRequest.getParameter(FileDownloadConstants.FORMAT)).thenReturn("owx");
        assertThat(parameters.getFormat(), is(equalTo(DownloadFormat.OWL_XML)));
    }


    @Test
    public void shouldParseManchesterSyntaxFormat() {
        when(servletRequest.getParameter(FileDownloadConstants.FORMAT)).thenReturn("omn");
        assertThat(parameters.getFormat(), is(equalTo(DownloadFormat.MANCHESTER)));
    }


    @Test
    public void shouldParseFunctionalSyntaxFormat() {
        when(servletRequest.getParameter(FileDownloadConstants.FORMAT)).thenReturn("ofn");
        assertThat(parameters.getFormat(), is(equalTo(DownloadFormat.FUNCTIONAL_SYNTAX)));
    }
}

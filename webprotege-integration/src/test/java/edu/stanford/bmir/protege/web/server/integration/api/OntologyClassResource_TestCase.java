package edu.stanford.bmir.protege.web.server.integration.api;

import edu.stanford.bmir.protege.web.server.integration.dto.OntologyClassData;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyClassListResponse;
import edu.stanford.bmir.protege.web.server.integration.service.OntologyClassService;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OntologyClassResource_TestCase {

    private static final String PROJECT_ID = "00000000-0000-0000-0000-000000000001";
    private static final String CLASS_IRI = "http://example.org/Sensor";

    @Mock
    private OntologyClassService service;

    private OntologyClassResource resource;

    private UserId userId;

    @Before
    public void setUp() {
        resource = new OntologyClassResource(service);
        userId = UserId.getUserId("api-user");
    }

    @Test
    public void shouldRejectGuestOnGet() {
        Response response = resource.getClassData(PROJECT_ID, CLASS_IRI, UserId.getGuest());

        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        assertThat(((OntologyClassResource.ErrorBody) response.getEntity()).getMessage(),
                   is("Authentication required"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldGetSingleClass() {
        OntologyClassData data = sampleClass();
        when(service.getClassData(PROJECT_ID, CLASS_IRI, userId)).thenReturn(data);

        Response response = resource.getClassData(PROJECT_ID, CLASS_IRI, userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), is(data));
        verify(service).getClassData(PROJECT_ID, CLASS_IRI, userId);
        verify(service, never()).listClassData(any(), any());
    }

    @Test
    public void shouldListClassesWhenClassIriOmitted() {
        when(service.listClassData(PROJECT_ID, userId)).thenReturn(Collections.singletonList(sampleClass()));

        Response response = resource.getClassData(PROJECT_ID, null, userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        OntologyClassListResponse body = (OntologyClassListResponse) response.getEntity();
        assertThat(body.getCount(), is(1));
        verify(service).listClassData(PROJECT_ID, userId);
    }

    @Test
    public void shouldListClassesWhenClassIriBlank() {
        when(service.listClassData(PROJECT_ID, userId)).thenReturn(Collections.emptyList());

        Response response = resource.getClassData(PROJECT_ID, "   ", userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        verify(service).listClassData(PROJECT_ID, userId);
    }

    @Test
    public void shouldMapIllegalArgumentToBadRequest() {
        when(service.getClassData(PROJECT_ID, CLASS_IRI, userId))
                .thenThrow(new IllegalArgumentException("classIri must not be blank"));

        Response response = resource.getClassData(PROJECT_ID, CLASS_IRI, userId);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((OntologyClassResource.ErrorBody) response.getEntity()).getMessage(),
                   is("classIri must not be blank"));
    }

    private static OntologyClassData sampleClass() {
        return new OntologyClassData(
                PROJECT_ID,
                CLASS_IRI,
                Collections.singletonList("http://www.w3.org/2002/07/owl#Thing"),
                Collections.singletonMap("http://www.w3.org/2000/01/rdf-schema#label", "Sensor")
        );
    }
}

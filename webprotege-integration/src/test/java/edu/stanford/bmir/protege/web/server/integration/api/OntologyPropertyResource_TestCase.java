package edu.stanford.bmir.protege.web.server.integration.api;

import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyData;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyKind;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyListResponse;
import edu.stanford.bmir.protege.web.server.integration.service.OntologyPropertyService;
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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OntologyPropertyResource_TestCase {

    private static final String PROJECT_ID = "00000000-0000-0000-0000-000000000001";
    private static final String PROPERTY_IRI = "http://example.org/hasStatus";

    @Mock
    private OntologyPropertyService service;

    private OntologyPropertyResource resource;

    private UserId userId;

    @Before
    public void setUp() {
        resource = new OntologyPropertyResource(service);
        userId = UserId.getUserId("api-user");
    }

    @Test
    public void shouldRejectGuestOnGet() {
        Response response = resource.getPropertyData(PROJECT_ID, PROPERTY_IRI, "DATA", UserId.getGuest());

        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldGetSingleProperty() {
        OntologyPropertyData data = sampleProperty();
        when(service.getPropertyData(PROJECT_ID, PROPERTY_IRI, OntologyPropertyKind.DATA, userId)).thenReturn(data);

        Response response = resource.getPropertyData(PROJECT_ID, PROPERTY_IRI, "DATA", userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), is(data));
        verify(service).getPropertyData(PROJECT_ID, PROPERTY_IRI, OntologyPropertyKind.DATA, userId);
        verify(service, never()).listPropertyData(any(), any(), any());
    }

    @Test
    public void shouldAcceptLowerCaseKind() {
        when(service.getPropertyData(eq(PROJECT_ID), eq(PROPERTY_IRI), eq(OntologyPropertyKind.OBJECT), eq(userId)))
                .thenReturn(sampleProperty());

        Response response = resource.getPropertyData(PROJECT_ID, PROPERTY_IRI, "object", userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        verify(service).getPropertyData(PROJECT_ID, PROPERTY_IRI, OntologyPropertyKind.OBJECT, userId);
    }

    @Test
    public void shouldRequireKindWhenPropertyIriProvided() {
        Response response = resource.getPropertyData(PROJECT_ID, PROPERTY_IRI, null, userId);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((OntologyPropertyResource.ErrorBody) response.getEntity()).getMessage(),
                   containsString("kind' is required"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldRejectIllegalKind() {
        Response response = resource.getPropertyData(PROJECT_ID, PROPERTY_IRI, "DATATYPE", userId);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((OntologyPropertyResource.ErrorBody) response.getEntity()).getMessage(),
                   containsString("must be OBJECT, DATA, or ANNOTATION"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldListAllKindsWhenIriAndKindOmitted() {
        when(service.listPropertyData(PROJECT_ID, null, userId))
                .thenReturn(Collections.singletonList(sampleProperty()));

        Response response = resource.getPropertyData(PROJECT_ID, null, null, userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        OntologyPropertyListResponse body = (OntologyPropertyListResponse) response.getEntity();
        assertThat(body.getCount(), is(1));
        verify(service).listPropertyData(PROJECT_ID, null, userId);
    }

    @Test
    public void shouldListFilteredKind() {
        when(service.listPropertyData(PROJECT_ID, OntologyPropertyKind.DATA, userId))
                .thenReturn(Collections.emptyList());

        Response response = resource.getPropertyData(PROJECT_ID, null, "DATA", userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        verify(service).listPropertyData(PROJECT_ID, OntologyPropertyKind.DATA, userId);
    }

    private static OntologyPropertyData sampleProperty() {
        return new OntologyPropertyData(
                PROJECT_ID,
                PROPERTY_IRI,
                OntologyPropertyKind.DATA,
                Collections.singletonList("http://example.org/Sensor"),
                Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyMap()
        );
    }
}

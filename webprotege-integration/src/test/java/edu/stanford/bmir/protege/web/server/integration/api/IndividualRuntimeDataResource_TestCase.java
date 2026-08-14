package edu.stanford.bmir.protege.web.server.integration.api;

import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeData;
import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeDataListResponse;
import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeDataRequest;
import edu.stanford.bmir.protege.web.server.integration.service.IndividualRuntimeDataService;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link IndividualRuntimeDataResource} REST contract.
 * Discovered by Surefire ({@code mvn test -pl webprotege-integration}); no production callers.
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualRuntimeDataResource_TestCase {

    private static final String PROJECT_ID = "00000000-0000-0000-0000-000000000001";
    private static final String INDIVIDUAL_IRI = "http://example.org/Sensor1";
    private static final String STATUS_PROPERTY = "http://example.org/hasStatus";

    @Mock
    private IndividualRuntimeDataService service;

    private IndividualRuntimeDataResource resource;

    private UserId userId;

    @Before
    public void setUp() {
        resource = new IndividualRuntimeDataResource(service);
        userId = UserId.getUserId("api-user");
    }

    @Test
    public void shouldRejectGuestOnGet() {
        Response response = resource.getRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, UserId.getGuest());

        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        assertThat(((IndividualRuntimeDataResource.ErrorBody) response.getEntity()).getMessage(),
                   is("Authentication required"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldRejectNullUserOnGet() {
        Response response = resource.getRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, null);

        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldGetSingleIndividualRuntimeData() {
        IndividualRuntimeData data = sampleData();
        when(service.getRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId)).thenReturn(data);

        Response response = resource.getRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), is(data));
        verify(service).getRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId);
        verify(service, never()).listRuntimeData(any(), any());
    }

    @Test
    public void shouldListRuntimeDataWhenIndividualIriOmitted() {
        List<IndividualRuntimeData> items = Collections.singletonList(sampleData());
        when(service.listRuntimeData(PROJECT_ID, userId)).thenReturn(items);

        Response response = resource.getRuntimeData(PROJECT_ID, null, userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        IndividualRuntimeDataListResponse body = (IndividualRuntimeDataListResponse) response.getEntity();
        assertThat(body.getCount(), is(1));
        assertThat(body.getItems(), is(items));
        verify(service).listRuntimeData(PROJECT_ID, userId);
    }

    @Test
    public void shouldListRuntimeDataWhenIndividualIriBlank() {
        when(service.listRuntimeData(PROJECT_ID, userId)).thenReturn(Collections.emptyList());

        Response response = resource.getRuntimeData(PROJECT_ID, "   ", userId);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        verify(service).listRuntimeData(PROJECT_ID, userId);
    }

    @Test
    public void shouldMapIllegalArgumentToBadRequestOnGet() {
        when(service.getRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId))
                .thenThrow(new IllegalArgumentException("individualIri must not be blank"));

        Response response = resource.getRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((IndividualRuntimeDataResource.ErrorBody) response.getEntity()).getMessage(),
                   is("individualIri must not be blank"));
    }

    @Test
    public void shouldPutUsingQueryIndividualIri() {
        IndividualRuntimeDataRequest request = new IndividualRuntimeDataRequest(
                null,
                Collections.singletonMap(STATUS_PROPERTY, "RUNNING")
        );
        IndividualRuntimeData saved = sampleData();
        when(service.putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), any(), eq(userId)))
                .thenReturn(saved);

        Response response = resource.putRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId, request);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), is(saved));
        ArgumentCaptor<IndividualRuntimeDataRequest> requestCaptor =
                ArgumentCaptor.forClass(IndividualRuntimeDataRequest.class);
        verify(service).putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), requestCaptor.capture(), eq(userId));
        assertThat(requestCaptor.getValue().getProperties(), hasEntry(STATUS_PROPERTY, "RUNNING"));
    }

    @Test
    public void shouldPutUsingBodyIndividualIri() {
        IndividualRuntimeDataRequest request = new IndividualRuntimeDataRequest(
                INDIVIDUAL_IRI,
                Collections.singletonMap(STATUS_PROPERTY, "IDLE")
        );
        when(service.putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), any(), eq(userId)))
                .thenReturn(sampleData());

        Response response = resource.putRuntimeData(PROJECT_ID, null, userId, request);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        verify(service).putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), eq(request), eq(userId));
    }

    @Test
    public void shouldPutWithNullBodyUsingQueryIri() {
        when(service.putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), any(), eq(userId)))
                .thenReturn(sampleData());

        Response response = resource.putRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId, null);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        ArgumentCaptor<IndividualRuntimeDataRequest> requestCaptor =
                ArgumentCaptor.forClass(IndividualRuntimeDataRequest.class);
        verify(service).putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), requestCaptor.capture(), eq(userId));
        assertThat(requestCaptor.getValue().getIndividualIri(), is(INDIVIDUAL_IRI));
        assertThat(requestCaptor.getValue().getProperties().isEmpty(), is(true));
    }

    @Test
    public void shouldRejectPutWhenQueryAndBodyIriMismatch() {
        IndividualRuntimeDataRequest request = new IndividualRuntimeDataRequest(
                "http://example.org/Other",
                Collections.singletonMap(STATUS_PROPERTY, "RUNNING")
        );

        Response response = resource.putRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId, request);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((IndividualRuntimeDataResource.ErrorBody) response.getEntity()).getMessage(),
                   containsString("must match"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldRejectPutWhenIndividualIriMissing() {
        IndividualRuntimeDataRequest request = new IndividualRuntimeDataRequest(
                null,
                Collections.singletonMap(STATUS_PROPERTY, "RUNNING")
        );

        Response response = resource.putRuntimeData(PROJECT_ID, null, userId, request);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((IndividualRuntimeDataResource.ErrorBody) response.getEntity()).getMessage(),
                   containsString("individualIri must be provided"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldRejectGuestOnPut() {
        Response response = resource.putRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, UserId.getGuest(), null);

        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldPatchRuntimeData() {
        IndividualRuntimeDataRequest request = new IndividualRuntimeDataRequest(
                INDIVIDUAL_IRI,
                Collections.singletonMap(STATUS_PROPERTY, "RUNNING")
        );
        IndividualRuntimeData saved = sampleData();
        when(service.patchRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, request, userId)).thenReturn(saved);

        Response response = resource.patchRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId, request);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        assertThat(response.getEntity(), is(saved));
        verify(service).patchRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, request, userId);
    }

    @Test
    public void shouldRejectNullBodyOnPatch() {
        Response response = resource.patchRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId, null);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((IndividualRuntimeDataResource.ErrorBody) response.getEntity()).getMessage(),
                   is("Request body is required"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldRejectGuestOnPatch() {
        IndividualRuntimeDataRequest request = new IndividualRuntimeDataRequest(
                INDIVIDUAL_IRI,
                Collections.singletonMap(STATUS_PROPERTY, "RUNNING")
        );

        Response response = resource.patchRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, UserId.getGuest(), request);

        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldDeleteRuntimeDataWithNoContent() {
        when(service.deleteRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId)).thenReturn(true);

        Response response = resource.deleteRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId);

        assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        assertThat(response.getEntity(), nullValue());
        verify(service).deleteRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId);
    }

    @Test
    public void shouldReturnNotFoundWhenNothingToDelete() {
        when(service.deleteRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId)).thenReturn(false);

        Response response = resource.deleteRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, userId);

        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
        assertThat(((IndividualRuntimeDataResource.ErrorBody) response.getEntity()).getMessage(),
                   containsString("No asserted property values"));
    }

    @Test
    public void shouldRequireIndividualIriOnDelete() {
        Response response = resource.deleteRuntimeData(PROJECT_ID, null, userId);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        assertThat(((IndividualRuntimeDataResource.ErrorBody) response.getEntity()).getMessage(),
                   containsString("individualIri' is required"));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldRejectBlankIndividualIriOnDelete() {
        Response response = resource.deleteRuntimeData(PROJECT_ID, "  ", userId);

        assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldRejectGuestOnDelete() {
        Response response = resource.deleteRuntimeData(PROJECT_ID, INDIVIDUAL_IRI, UserId.getGuest());

        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        verifyZeroInteractions(service);
    }

    @Test
    public void shouldTrimIndividualIriFromQueryAndBody() {
        IndividualRuntimeDataRequest request = new IndividualRuntimeDataRequest(
                "  " + INDIVIDUAL_IRI + "  ",
                Collections.singletonMap(STATUS_PROPERTY, "RUNNING")
        );
        when(service.putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), any(), eq(userId)))
                .thenReturn(sampleData());

        Response response = resource.putRuntimeData(PROJECT_ID, "  " + INDIVIDUAL_IRI + "  ", userId, request);

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        verify(service).putRuntimeData(eq(PROJECT_ID), eq(INDIVIDUAL_IRI), any(), eq(userId));
    }

    private static IndividualRuntimeData sampleData() {
        return new IndividualRuntimeData(
                PROJECT_ID,
                INDIVIDUAL_IRI,
                Collections.singletonMap(STATUS_PROPERTY, "RUNNING"),
                1L,
                "api-user"
        );
    }
}

package edu.stanford.bmir.protege.web.server.integration.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.integration.dispatch.ActionDispatch;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyData;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyKind;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OntologyPropertyService_TestCase {

    private static final String PROJECT_ID = "00000000-0000-0000-0000-000000000001";
    private static final String OBJECT_IRI = "http://example.org/hasStatus";
    private static final String DATA_IRI = "http://example.org/hasTemp";
    private static final String DOMAIN = "http://example.org/Sensor";
    private static final String RANGE = "http://example.org/StatusValue";
    private static final String INVERSE = "http://example.org/isStatusOf";
    private static final String XSD_STRING = "http://www.w3.org/2001/XMLSchema#string";

    @Mock
    private ActionDispatch actionDispatch;

    private OntologyPropertyService service;

    private UserId userId;

    @Before
    public void setUp() {
        service = new OntologyPropertyService(actionDispatch);
        userId = UserId.getUserId("api-user");
    }

    @Test
    public void shouldGetObjectPropertyFrame() {
        when(actionDispatch.execute(any(GetObjectPropertyFrameAction.class), eq(userId)))
                .thenReturn(new GetObjectPropertyFrameResult(objectFrame()));

        OntologyPropertyData data = service.getPropertyData(
                PROJECT_ID, OBJECT_IRI, OntologyPropertyKind.OBJECT, userId);

        assertThat(data.getKind(), is(OntologyPropertyKind.OBJECT));
        assertThat(data.getPropertyIri(), is(OBJECT_IRI));
        assertThat(data.getDomains(), contains(DOMAIN));
        assertThat(data.getRanges(), contains(RANGE));
        assertThat(data.getCharacteristics(), contains("Functional"));
        assertThat(data.getInverses(), contains(INVERSE));
        verify(actionDispatch, never()).execute(any(GetDataPropertyFrameAction.class), eq(userId));
    }

    @Test
    public void shouldGetDataPropertyFrameWithFunctionalCharacteristic() {
        when(actionDispatch.execute(any(GetDataPropertyFrameAction.class), eq(userId)))
                .thenReturn(new GetDataPropertyFrameResult(dataFrame(true)));

        OntologyPropertyData data = service.getPropertyData(
                PROJECT_ID, DATA_IRI, OntologyPropertyKind.DATA, userId);

        assertThat(data.getKind(), is(OntologyPropertyKind.DATA));
        assertThat(data.getDomains(), contains(DOMAIN));
        assertThat(data.getRanges(), contains(XSD_STRING));
        assertThat(data.getCharacteristics(), contains("Functional"));
        assertThat(data.getInverses(), empty());
    }

    @Test
    public void shouldLeaveDataPropertyCharacteristicsEmptyWhenNotFunctional() {
        when(actionDispatch.execute(any(GetDataPropertyFrameAction.class), eq(userId)))
                .thenReturn(new GetDataPropertyFrameResult(dataFrame(false)));

        OntologyPropertyData data = service.getPropertyData(
                PROJECT_ID, DATA_IRI, OntologyPropertyKind.DATA, userId);

        assertThat(data.getCharacteristics(), empty());
    }

    @Test
    public void shouldListDataPropertiesUsingMatchingThenFrame() {
        EntityNode node = EntityNode.getFromEntityData(
                OWLDataPropertyData.get(DataFactory.getOWLDataProperty(DATA_IRI), ImmutableMap.of()));
        when(actionDispatch.execute(any(GetMatchingEntitiesAction.class), eq(userId)))
                .thenReturn(GetMatchingEntitiesResult.get(
                        new Page<>(1, 1, Collections.singletonList(node), 1)));
        when(actionDispatch.execute(any(GetDataPropertyFrameAction.class), eq(userId)))
                .thenReturn(new GetDataPropertyFrameResult(dataFrame(false)));

        List<OntologyPropertyData> listed = service.listPropertyData(
                PROJECT_ID, OntologyPropertyKind.DATA, userId);

        assertThat(listed, hasSize(1));
        assertThat(listed.get(0).getKind(), is(OntologyPropertyKind.DATA));
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(actionDispatch, times(2)).execute(
                (edu.stanford.bmir.protege.web.shared.dispatch.Action) captor.capture(),
                eq(userId));
        assertThat(captor.getAllValues().get(0), instanceOf(GetMatchingEntitiesAction.class));
        assertThat(captor.getAllValues().get(1), instanceOf(GetDataPropertyFrameAction.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectBlankPropertyIri() {
        service.getPropertyData(PROJECT_ID, " ", OntologyPropertyKind.OBJECT, userId);
    }

    private static ObjectPropertyFrame objectFrame() {
        return ObjectPropertyFrame.get(
                OWLObjectPropertyData.get(DataFactory.getOWLObjectProperty(OBJECT_IRI), ImmutableMap.of()),
                ImmutableSet.of(),
                ImmutableSet.of(OWLClassData.get(DataFactory.getOWLClass(DOMAIN), ImmutableMap.of())),
                ImmutableSet.of(OWLClassData.get(DataFactory.getOWLClass(RANGE), ImmutableMap.of())),
                ImmutableSet.of(OWLObjectPropertyData.get(DataFactory.getOWLObjectProperty(INVERSE), ImmutableMap.of())),
                ImmutableSet.of(ObjectPropertyCharacteristic.FUNCTIONAL)
        );
    }

    private static DataPropertyFrame dataFrame(boolean functional) {
        return DataPropertyFrame.get(
                OWLDataPropertyData.get(DataFactory.getOWLDataProperty(DATA_IRI), ImmutableMap.of()),
                ImmutableSet.of(),
                ImmutableSet.of(OWLClassData.get(DataFactory.getOWLClass(DOMAIN), ImmutableMap.of())),
                ImmutableSet.of(OWLDatatypeData.get(DataFactory.getOWLDatatype(XSD_STRING), ImmutableMap.of())),
                functional
        );
    }
}

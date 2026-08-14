package edu.stanford.bmir.protege.web.server.integration.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.integration.dispatch.ActionDispatch;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyClassData;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.PropertyLiteralValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
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
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OntologyClassService_TestCase {

    private static final String PROJECT_ID = "00000000-0000-0000-0000-000000000001";
    private static final String CLASS_IRI = "http://example.org/Sensor";
    private static final String LABEL_PROPERTY = "http://www.w3.org/2000/01/rdf-schema#label";

    @Mock
    private ActionDispatch actionDispatch;

    private OntologyClassService service;

    private UserId userId;

    @Before
    public void setUp() {
        service = new OntologyClassService(actionDispatch);
        userId = UserId.getUserId("api-user");
    }

    @Test
    public void shouldGetClassFrame() {
        when(actionDispatch.execute(any(GetClassFrameAction.class), eq(userId)))
                .thenReturn(new GetClassFrameResult(classFrame(CLASS_IRI, "Sensor")));

        OntologyClassData data = service.getClassData(PROJECT_ID, CLASS_IRI, userId);

        assertThat(data.getProjectId(), is(PROJECT_ID));
        assertThat(data.getClassIri(), is(CLASS_IRI));
        assertThat(data.getParents(), contains(DataFactory.getOWLThing().getIRI().toString()));
        assertThat(data.getProperties(), hasEntry(LABEL_PROPERTY, "Sensor"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectBlankClassIri() {
        service.getClassData(PROJECT_ID, "  ", userId);
    }

    @Test
    public void shouldListClassesAcrossPages() {
        String iri1 = "http://example.org/Sensor";
        String iri2 = "http://example.org/Actuator";
        when(actionDispatch.execute(any(GetMatchingEntitiesAction.class), eq(userId)))
                .thenReturn(GetMatchingEntitiesResult.get(
                        new Page<>(1, 2, Collections.singletonList(classNode(iri1)), 2)))
                .thenReturn(GetMatchingEntitiesResult.get(
                        new Page<>(2, 2, Collections.singletonList(classNode(iri2)), 2)));
        when(actionDispatch.execute(any(GetClassFrameAction.class), eq(userId)))
                .thenReturn(new GetClassFrameResult(classFrame(iri1, "Sensor")))
                .thenReturn(new GetClassFrameResult(classFrame(iri2, "Actuator")));

        List<OntologyClassData> listed = service.listClassData(PROJECT_ID, userId);

        assertThat(listed, hasSize(2));
        assertThat(listed.get(0).getClassIri(), is(iri1));
        assertThat(listed.get(1).getClassIri(), is(iri2));

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(actionDispatch, times(4)).execute(
                (edu.stanford.bmir.protege.web.shared.dispatch.Action) captor.capture(),
                eq(userId));
        List<GetMatchingEntitiesAction> listActions = captor.getAllValues().stream()
                .filter(GetMatchingEntitiesAction.class::isInstance)
                .map(GetMatchingEntitiesAction.class::cast)
                .collect(Collectors.toList());
        assertThat(listActions, hasSize(2));
        assertThat(listActions.get(0).getPageRequest().getPageNumber(), is(1));
        assertThat(listActions.get(1).getPageRequest().getPageNumber(), is(2));
    }

    private static EntityNode classNode(String classIri) {
        return EntityNode.getFromEntityData(
                OWLClassData.get(DataFactory.getOWLClass(classIri), ImmutableMap.of()));
    }

    private static ClassFrame classFrame(String classIri, String label) {
        return ClassFrame.get(
                OWLClassData.get(DataFactory.getOWLClass(classIri), ImmutableMap.of()),
                ImmutableSet.of(OWLClassData.get(DataFactory.getOWLThing(), ImmutableMap.of())),
                ImmutableSet.of(
                        PropertyLiteralValue.get(
                                OWLDataPropertyData.get(DataFactory.getOWLDataProperty(LABEL_PROPERTY), ImmutableMap.of()),
                                OWLLiteralData.get(DataFactory.getOWLLiteral(label)),
                                State.ASSERTED)
                )
        );
    }
}

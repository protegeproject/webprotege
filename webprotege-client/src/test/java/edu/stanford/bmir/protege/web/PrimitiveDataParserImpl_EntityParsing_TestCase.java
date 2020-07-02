package edu.stanford.bmir.protege.web;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.primitive.EntityDataLookupHandler;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataParserCallback;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataParserImpl;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 11/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class PrimitiveDataParserImpl_EntityParsing_TestCase {

    @Mock
    protected EntityDataLookupHandler lookupHandler;

    @Mock
    protected PrimitiveDataParserCallback primitiveDataParserCallback;


    protected Set<PrimitiveType> primitiveTypes;

    private PrimitiveDataParserImpl parser;

    private Map<String, OWLEntityData> lookupMap = Maps.newHashMap();

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        primitiveTypes = Sets.newHashSet();
        lookupMap = Maps.newHashMap();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                AsyncCallback<Optional<OWLEntityData>> callback = (AsyncCallback<Optional<OWLEntityData>>) invocationOnMock.getArguments()[2];
                callback.onSuccess(Optional.of(lookupMap.get(invocationOnMock.getArguments()[0])));
                return null;
            }
        }).when(lookupHandler).lookupEntity(any(String.class), any(Set.class), any(CompositeRootCriteria.class), any(AsyncCallback.class));
        parser = new PrimitiveDataParserImpl(lookupHandler);
    }

    @Test
    public void shouldParseNameAsClass() {
        primitiveTypes.add(PrimitiveType.CLASS);
        parseEntityData(OWLClassData.get(new OWLClassImpl(IRI.create("http://ont.com/cls")), ImmutableMap.of()));
    }

    @Test
    public void shouldParseNameAsObjectProperty() {
        primitiveTypes.add(PrimitiveType.OBJECT_PROPERTY);
        parseEntityData(OWLObjectPropertyData.get(new OWLObjectPropertyImpl(IRI.create("http://ont.com/prop")), ImmutableMap.of()));
    }

    @Test
    public void shouldParseNameAsDataProperty() {
        primitiveTypes.add(PrimitiveType.DATA_PROPERTY);
        parseEntityData(OWLDataPropertyData.get(new OWLDataPropertyImpl(IRI.create("http://ont.com/prop")), ImmutableMap.of()));
    }

    @Test
    public void shouldParseNameAsAnnotationProperty() {
        primitiveTypes.add(PrimitiveType.DATA_PROPERTY);
        parseEntityData(OWLAnnotationPropertyData.get(new OWLAnnotationPropertyImpl(IRI.create("http://ont.com/prop")), ImmutableMap.of()));
    }

    @Test
    public void shouldParseNameAsIndividual() {
        primitiveTypes.add(PrimitiveType.NAMED_INDIVIDUAL);
        parseEntityData(OWLNamedIndividualData.get(new OWLNamedIndividualImpl(IRI.create("http://ont.com/ind")), ImmutableMap.of()));
    }

    private void parseEntityData(OWLEntityData entityData) {
        lookupMap.put(entityData.getBrowserText(), entityData);
        parser.parsePrimitiveData(entityData.getBrowserText(),
                                  Optional.empty(), primitiveTypes, primitiveDataParserCallback);
        verifyResult(entityData);
    }

    private void verifyResult(OWLEntityData entityData) {
        Optional<OWLPrimitiveData> expectedData = Optional.of(entityData);
        Mockito.verify(primitiveDataParserCallback).onSuccess(expectedData);
    }
}

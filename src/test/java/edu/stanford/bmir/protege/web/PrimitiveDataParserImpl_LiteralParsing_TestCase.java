package edu.stanford.bmir.protege.web;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.primitive.EntityDataLookupHandler;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataParserCallback;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataParserImpl;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 11/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class PrimitiveDataParserImpl_LiteralParsing_TestCase {

    @Mock
    protected EntityDataLookupHandler lookupHandler;

    @Mock
    protected PrimitiveDataParserCallback primitiveDataParserCallback;


    protected Set<PrimitiveType> primitiveTypes;


    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    private PrimitiveDataParserImpl parser;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        primitiveTypes = Sets.newHashSet();
        primitiveTypes.add(PrimitiveType.LITERAL);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                AsyncCallback<Optional<OWLEntityData>> callback = (AsyncCallback<Optional<OWLEntityData>>) invocationOnMock.getArguments()[2];
                callback.onSuccess(Optional.absent());
                return null;
            }
        }).when(lookupHandler).lookupEntity(any(String.class), any(Set.class), any(AsyncCallback.class));
        parser = new PrimitiveDataParserImpl(lookupHandler);
    }

    @Test
    public void shouldParseStringWithoutLanguageTagAsXSDString() {
        parser.parsePrimitiveData("Hello World!", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("Hello World!", OWL2Datatype.XSD_STRING);
    }

    @Test
    public void shouldParseStringWithLanguageTagAsRDFPlainLiteral() {
        parser.parsePrimitiveData("Hello World!", Optional.of("en"), primitiveTypes, primitiveDataParserCallback);
        verifyResult("Hello World!@en", OWL2Datatype.RDF_PLAIN_LITERAL);
    }

    @Test
    public void shouldParseStringIncludingLanguageTagAsXSDString() {
        parser.parsePrimitiveData("Hello World!@fr", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("Hello World!@fr", OWL2Datatype.XSD_STRING);
    }

    @Test
    public void shouldParseDecimalAsXSDDecimal() {
        parser.parsePrimitiveData("3.3", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("3.3", OWL2Datatype.XSD_DECIMAL);
    }

    @Test
    public void shouldParseDecimalWithCommaSeparatorsAsXSDDecimal() {
        parser.parsePrimitiveData("3,333.3", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("3333.3", OWL2Datatype.XSD_DECIMAL);
    }

    @Test
    public void shouldParseIntegerAsXSDInteger() {
        parser.parsePrimitiveData("3", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("3", OWL2Datatype.XSD_INTEGER);
    }

    @Test
    public void shouldParseIntegerWithCommanSeparatorsAsXSDInteger() {
        parser.parsePrimitiveData("3,333", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("3333", OWL2Datatype.XSD_INTEGER);
    }

    @Test
    public void shouldParseFloatAsXSDFloat() {
        parser.parsePrimitiveData("3f", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("3", OWL2Datatype.XSD_FLOAT);
    }

    @Test
    public void shouldParseFloatWithCommaSeparatorsAsXSDFloat() {
        parser.parsePrimitiveData("3,333f", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("3333", OWL2Datatype.XSD_FLOAT);
    }

    @Test
    public void shouldParseYyyyMmDdAsXSDDateTime() {
        parser.parsePrimitiveData("2004-10-13", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("2004-10-13T00:00:00", OWL2Datatype.XSD_DATE_TIME);
    }

    @Test
    public void shouldParseYyyyMmDdTHhMmSsAsXSDDateTime() {
        parser.parsePrimitiveData("2004-10-13T12:51:12", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("2004-10-13T12:51:12", OWL2Datatype.XSD_DATE_TIME);
    }

    @Test
    public void shouldParseYyyyMmDdTHhMmSsDotSsAsXSDDateTime() {
        parser.parsePrimitiveData("2004-10-13T12:51:12.34", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("2004-10-13T12:51:12.34", OWL2Datatype.XSD_DATE_TIME);
    }

    @Test
    public void shouldParseYyyyMmDdSpaceHhMmSsAsXSDDateTime() {
        parser.parsePrimitiveData("2004-10-13 12:51:12", Optional.absent(), primitiveTypes, primitiveDataParserCallback);
        verifyResult("2004-10-13T12:51:12", OWL2Datatype.XSD_DATE_TIME);
    }


    private void verifyResult(String lexicalValue, OWL2Datatype datatype) {
        OWLLiteral expected = dataFactory.getOWLLiteral(lexicalValue, datatype);
        Optional<OWLPrimitiveData> expectedData = Optional.of(new OWLLiteralData(expected));
        Mockito.verify(primitiveDataParserCallback).onSuccess(expectedData);
    }
}

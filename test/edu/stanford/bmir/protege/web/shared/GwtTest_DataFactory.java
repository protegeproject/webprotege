package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;
import com.google.gwt.junit.client.GWTTestCase;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/04/2014
 */
public class GwtTest_DataFactory extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        delayTestFinish(10000);
    }

    public void test_shouldParseInteger() {
        OWLLiteral literal = DataFactory.parseLiteral("3", Optional.<String>absent());
        assertThat(literal.getLiteral(), is(equalTo("3")));
        assertThat(literal.getDatatype().getIRI(), is(equalTo(OWL2Datatype.XSD_INTEGER.getIRI())));
        finishTest();
    }

    public void test_shouldParseDecimal() {
        OWLLiteral literal = DataFactory.parseLiteral("3.3", Optional.<String>absent());
        assertThat(literal.getLiteral(), is(equalTo("3.3")));
        assertThat(literal.getDatatype().getIRI(), is(equalTo(OWL2Datatype.XSD_DECIMAL.getIRI())));
        finishTest();
    }

    public void test_shouldParseFloat() {
        OWLLiteral literal = DataFactory.parseLiteral("3.3f", Optional.<String>absent());
        assertThat(literal.getLiteral(), is(equalTo("3.3f")));
        assertThat(literal.getDatatype().getIRI(), is(equalTo(OWL2Datatype.XSD_FLOAT.getIRI())));
        finishTest();
    }

    public void test_shouldParseNowAsDateTime() {
        OWLLiteral literal = DataFactory.parseLiteral("now", Optional.<String>absent());
        assertThat(literal.getDatatype().getIRI(), is(equalTo(OWL2Datatype.XSD_DATE_TIME.getIRI())));
        finishTest();
    }
}

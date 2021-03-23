package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.data.EntityFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import org.junit.Before;
import org.junit.Test;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplString;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class FormControlValueDeserializer_TestCase {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        var objectMapperProvider = new ObjectMapperProvider();
        objectMapper = objectMapperProvider.get();

    }

    @Test
    public void shouldRoundTripOwlClass() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLClass());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlObjectProperty() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLObjectProperty());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlDataProperty() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDataProperty());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlAnnotationProperty() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLAnnotationProperty());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlNamedIndividual() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLNamedIndividual());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlDatatype() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDatatype());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }
}
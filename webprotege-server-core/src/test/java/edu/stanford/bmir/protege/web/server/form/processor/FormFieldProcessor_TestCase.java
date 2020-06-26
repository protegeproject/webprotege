package edu.stanford.bmir.protege.web.server.form.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormFieldBindingMissingException;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormFieldData;
import edu.stanford.bmir.protege.web.shared.form.data.TextControlData;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@RunWith(MockitoJUnitRunner.class)
public class FormFieldProcessor_TestCase {

    private FormFieldProcessor formFieldProcessor;

    @Mock
    private FormControlDataProcessor formControlDataProcessor;

    @Mock
    private OwlBinding binding;

    @Mock
    private FormControlData formControlData;

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private FormFieldData fieldData;

    @Mock
    private FormFieldDescriptor formFieldDescriptor;

    @Mock
    private FormFieldId fieldId;

    @Before
    public void setUp() {
        formFieldProcessor = new FormFieldProcessor(formControlDataProcessor);
        when(fieldData.getFormFieldDescriptor())
                .thenReturn(formFieldDescriptor);
        when(formFieldDescriptor.getId())
                .thenReturn(fieldId);
        when(formFieldDescriptor.getOwlBinding())
                .thenReturn(Optional.of(binding));
        when(fieldData.getFormControlData())
                .thenReturn(new Page<>(1, 1, ImmutableList.of(formControlData), 1));
    }

    @Test
    public void shouldProcessFormControlData() {
        formFieldProcessor.processFormFieldData(fieldData, formFrameBuilder);
        verify(formControlDataProcessor, times(1))
                .processFormControlData(binding, formControlData, formFrameBuilder);
    }

    @Test(expected = FormFieldBindingMissingException.class)
    public void shouldThrowMissingBindingExceptionIfBindingIsNotPresent() {
        when(formFieldDescriptor.getOwlBinding())
                .thenReturn(Optional.empty());
        formFieldProcessor.processFormFieldData(fieldData, formFrameBuilder);
    }
}

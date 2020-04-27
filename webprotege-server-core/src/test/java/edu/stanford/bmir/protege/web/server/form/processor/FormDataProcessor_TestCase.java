package edu.stanford.bmir.protege.web.server.form.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptorMissingException;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormFieldData;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
public class FormDataProcessor_TestCase {

    private FormDataProcessor processor;

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private FormFieldProcessor formFieldProcessor;

    @Mock
    private FormData formData;

    @Mock
    private FormFieldData formFieldData;

    @Mock
    private FormSubject subject;

    @Mock
    private FormDescriptor formDescriptor;

    @Mock
    private FormSubjectFactoryDescriptor subjectFactoryDescriptor;

    @Before
    public void setUp() {
        processor = new FormDataProcessor(() -> formFrameBuilder,
                                          formFieldProcessor);
        when(formData.getFormFieldData())
                .thenReturn(ImmutableList.of(formFieldData));
        when(formData.getSubject())
                .thenReturn(Optional.of(subject));
        when(formData.getFormDescriptor())
                .thenReturn(formDescriptor);
        when(formDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.of(subjectFactoryDescriptor));
    }

    @Test
    public void shouldUseFormFrameBuilder() {
        var ffb = processor.processFormData(formData, false);
        assertThat(ffb, is(formFrameBuilder));
    }

    @Test
    public void shouldProcessSubject() {
        processor.processFormData(formData, false);
        verify(formFrameBuilder, times(1))
                .setSubject(subject);
    }

    @Test
    public void shouldProcessFormFieldData() {
        processor.processFormData(formData, false);
        verify(formFieldProcessor, times(1))
                .processFormFieldData(formFieldData, formFrameBuilder);
    }

    @Test
    public void shouldSetSubjectFactory() {
        processor.processFormData(formData, false);
        verify(formFrameBuilder, times(1))
                .setSubjectFactoryDescriptor(subjectFactoryDescriptor);
    }

    @Test(expected = FormSubjectFactoryDescriptorMissingException.class)
    public void shouldThrowExceptionIfSubjectFactoryDescriptorIsMissing() {
        when(formDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.empty());
        processor.processFormData(formData, true);
    }
}

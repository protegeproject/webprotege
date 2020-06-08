package edu.stanford.bmir.protege.web.server.form.processor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@RunWith(MockitoJUnitRunner.class)
public class FormControlDataProcessor_TestCase {

    private FormControlDataProcessor processor;

    @Mock
    private GridControlDataProcessor gridControlDataProcessor;

    @Mock
    private FormDataProcessor formDataProcessor;

    @Mock
    private OWLLiteral literal;

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private OwlBinding binding;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI iri;

    private ImmutableSet<FormRegionOrdering> ordering = ImmutableSet.of();

    @Before
    public void setUp() {
        processor = new FormControlDataProcessor(gridControlDataProcessor,
                                                 () -> formDataProcessor);
    }

    @Test
    public void shouldProcessTextControlData() {
        var textControlData = TextControlData.get(mock(TextControlDescriptor.class), literal);
        processor.processFormControlData(binding, textControlData, formFrameBuilder);
        verify(formFrameBuilder, times(1))
                .addLiteralValue(binding, literal);
    }

    @Test
    public void shouldProcessNumberControlData() {
        var numberControlData = NumberControlData.get(mock(NumberControlDescriptor.class), literal);
        processor.processFormControlData(binding, numberControlData, formFrameBuilder);
        verify(formFrameBuilder, times(1))
                .addLiteralValue(binding, literal);
    }

    @Test
    public void shouldProcessEntityControlData() {
        var entityNameControlDescriptor = mock(EntityNameControlDescriptor.class);
        var entityControlData = EntityNameControlData.get(entityNameControlDescriptor, entity);
        processor.processFormControlData(binding, entityControlData, formFrameBuilder);
        verify(formFrameBuilder, times(1))
                .addEntityValue(binding, entity);
    }

    @Test
    public void shouldProcessImageControlData() {
        var imageControlDescriptor = mock(ImageControlDescriptor.class);
        var entityControlData = ImageControlData.get(imageControlDescriptor, iri);
        processor.processFormControlData(binding, entityControlData, formFrameBuilder);
        verify(formFrameBuilder, times(1))
                .addIriValue(binding, iri);
    }

    @Test
    public void shouldProcessMultiChoiceControlData() {
        var multiChoiceControlDescriptor = mock(MultiChoiceControlDescriptor.class);
        var iriFormControlValue = IriFormControlData.get(iri);
        var literalFormControlValue = LiteralFormControlData.get(literal);
        var values = ImmutableList.of(iriFormControlValue, literalFormControlValue);
        var multiChoiceControlData = MultiChoiceControlData.get(multiChoiceControlDescriptor, values);
        processor.processFormControlData(binding, multiChoiceControlData, formFrameBuilder);
        verify(formFrameBuilder, times(1))
                .addIriValue(binding, iri);
        verify(formFrameBuilder, times(1))
                .addLiteralValue(binding, literal);
    }

    @Test
    public void shouldProcessSingleChoiceControlData() {
        var singleChoiceControlDescriptor = mock(SingleChoiceControlDescriptor.class);
        var iriFormControlValue = IriFormControlData.get(iri);
        var singleChoiceControlData = SingleChoiceControlData.get(singleChoiceControlDescriptor, iriFormControlValue);
        processor.processFormControlData(binding, singleChoiceControlData, formFrameBuilder);
        verify(formFrameBuilder, times(1))
                .addIriValue(binding, iri);
    }

    @Test
    public void shouldProcessGridControlData() {
        var gridControlDescriptor = mock(GridControlDescriptor.class);
        var page = new Page<>(1, 1, ImmutableList.of(mock(GridRowData.class)), 1);
        var gridControlData = GridControlData.get(gridControlDescriptor, page, ordering);
        processor.processFormControlData(binding, gridControlData, formFrameBuilder);
        verify(gridControlDataProcessor, times(1))
                .processGridControlData(binding, gridControlData, formFrameBuilder);
    }

    @Test
    public void shouldProcessFormData() {
        var formData = FormData.get(Optional.empty(),
                                    mock(FormDescriptor.class),
                                    ImmutableList.of());
        processor.processFormControlData(binding, formData, formFrameBuilder);
        verify(formDataProcessor, times(1))
                .processFormData(formData, true);
    }
}

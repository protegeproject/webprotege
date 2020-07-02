package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
@RunWith(MockitoJUnitRunner.class)
public class FormFieldPresenter_TestCase {

    private FormFieldPresenter presenter;

    @Mock
    private FormFieldView view;

    @Mock
    private FormFieldId fieldId;

    @Mock
    private OwlBinding owlBinding;

    @Mock
    private LanguageMap labelMap;

    @Mock
    private FormControlDescriptorDto formControlDescriptor;

    @Mock
    private LanguageMap helpMap;

    @Mock
    private LanguageMapCurrentLocaleMapper languageMapCurrentLocalMapper;

    @Mock
    private FormControlStackPresenter formControlStackPresenter;

    @Before
    public void setUp() {
        FormFieldDescriptorDto fieldDescriptor = FormFieldDescriptorDto.get(fieldId,
                                                                      owlBinding,
                                                                      labelMap,
                                                                      FieldRun.START,
                                                                      formControlDescriptor,
                                                                      Optionality.OPTIONAL,
                                                                            Repeatability.NON_REPEATABLE,
                                                                            true,
                                                                      ExpansionState.COLLAPSED,
                                                                      helpMap);


        when(languageMapCurrentLocalMapper.getValueForCurrentLocale(labelMap))
                .thenReturn("TheLabel");
        when(languageMapCurrentLocalMapper.getValueForCurrentLocale(helpMap))
                .thenReturn("TheHelpText");
        presenter = new FormFieldPresenter(view,
                                           fieldDescriptor,
                                           formControlStackPresenter,
                                           languageMapCurrentLocalMapper);
    }



    @Test
    public void shouldBeExpandedByDefault() {
        assertThat(presenter.getExpansionState(), is(ExpansionState.EXPANDED));
    }

    @Test
    public void shouldToggleExpansionState() {
        presenter.toggleExpansionState();
        assertThat(presenter.getExpansionState(), is(ExpansionState.COLLAPSED));
        verify(view, times(1)).collapse();

        presenter.toggleExpansionState();
        assertThat(presenter.getExpansionState(), is(ExpansionState.EXPANDED));
        verify(view, times(1)).expand();
    }

    @Test
    public void shouldStart() {
        presenter.start();
    }
}

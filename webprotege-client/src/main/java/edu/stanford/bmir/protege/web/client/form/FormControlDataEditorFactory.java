package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 *
 * An interface to {@link ValueEditorFactory} objects that provide editors for {@link FormControlData}
 */
public interface FormControlDataEditorFactory {

    @Nonnull
    FormControl createFormControl();
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class TextControlDescriptorPresenter implements FormControlDescriptorPresenter {

    @Nonnull
    private final TextControlDescriptorView view;

    @Inject
    public TextControlDescriptorPresenter(@Nonnull TextControlDescriptorView view) {
        this.view = checkNotNull(view);
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        return new TextControlDescriptor(view.getPlaceholder(),
                                         view.getStringType(),
                                         view.getLineMode(),
                                         view.getPattern(),
                                         view.getPatternViolationMessage());
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        if(!(formControlDescriptor instanceof TextControlDescriptor)) {
            clear();
            return;
        }
        TextControlDescriptor descriptor = (TextControlDescriptor) formControlDescriptor;
        view.setStringType(descriptor.getStringType());
        view.setLineMode(descriptor.getLineMode());
        view.setPatternViolationMessage(descriptor.getPlaceholder());
        view.setPattern(descriptor.getPattern());
        view.setPatternViolationMessage(descriptor.getPatternViolationErrorMessage());
    }

    @Override
    public void clear() {
        view.setLineMode(LineMode.SINGLE_LINE);
        view.setStringType(StringType.SIMPLE_STRING);
        view.setPattern("");
        view.setPatternViolationMessage(LanguageMap.empty());
        view.setPlaceholder(LanguageMap.empty());
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}

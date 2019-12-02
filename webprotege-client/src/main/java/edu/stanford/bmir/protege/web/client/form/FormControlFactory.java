package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.i18n.client.LocaleInfo;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jul 2017
 */
public class FormControlFactory {

    @Nonnull
    private final Provider<TextControl> textFieldEditorProvider;

    @Nonnull
    private final Provider<RadioButtonChoiceControl> choiceFieldRadioButtonEditorProvider;

    @Nonnull
    private final Provider<CheckBoxChoiceControl> choiceFieldCheckBoxEditorProvider;

    @Nonnull
    private final Provider<SegmentedButtonChoiceControl> choiceFieldSegmentedEditorProvider;

    @Nonnull
    private final Provider<ComboBoxChoiceControl> choiceFieldComboBoxEditorProvider;

    @Nonnull
    private final Provider<ClassNameFieldEditor> classNameFieldEditorProvider;

    @Nonnull
    private final Provider<ImageControl> imageFieldEditorProvider;

    @Nonnull
    private final Provider<NumberEditorControl> numberFieldEditorProvider;

    @Nonnull
    private final Provider<GridControl> gridControlProvider;

    @Inject
    public FormControlFactory(@Nonnull Provider<TextControl> textFieldEditorProvider,
                              @Nonnull Provider<RadioButtonChoiceControl> choiceFieldRadioButtonEditorProvider,
                              @Nonnull Provider<CheckBoxChoiceControl> choiceFieldCheckBoxEditorProvider,
                              @Nonnull Provider<SegmentedButtonChoiceControl> choiceFieldSegmentedEditorProvider,
                              @Nonnull Provider<ComboBoxChoiceControl> choiceFieldComboBoxEditorProvider,
                              @Nonnull Provider<ClassNameFieldEditor> classNameFieldEditorProvider,
                              @Nonnull Provider<ImageControl> imageFieldEditorProvider,
                              @Nonnull Provider<NumberEditorControl> numberFieldEditorProvider,
                              @Nonnull Provider<GridControl> gridControlProvider) {
        this.textFieldEditorProvider = textFieldEditorProvider;
        this.choiceFieldRadioButtonEditorProvider = choiceFieldRadioButtonEditorProvider;
        this.choiceFieldCheckBoxEditorProvider = choiceFieldCheckBoxEditorProvider;
        this.choiceFieldSegmentedEditorProvider = choiceFieldSegmentedEditorProvider;
        this.choiceFieldComboBoxEditorProvider = choiceFieldComboBoxEditorProvider;
        this.classNameFieldEditorProvider = classNameFieldEditorProvider;
        this.imageFieldEditorProvider = imageFieldEditorProvider;
        this.numberFieldEditorProvider = numberFieldEditorProvider;
        this.gridControlProvider = gridControlProvider;
    }

    /**
     * Get the value editor for the specified {@link FormControlDescriptor}.  If an editor for
     * specified descriptor cannot be found then an {@link Optional#empty()} value will be
     * returned.
     * @param formControlDescriptor The form field descriptor that describes the editor to be
     *                            created.
     * @return The {@link ValueEditorFactory} for the specified descriptor.
     */
    @Nonnull
    public Optional<ValueEditorFactory<FormDataValue>> getValueEditorFactory(@Nonnull FormControlDescriptor formControlDescriptor) {
        if (formControlDescriptor.getAssociatedType().equals(TextControlDescriptor.getType())) {
            return getTextFieldEditorFactory((TextControlDescriptor) formControlDescriptor);
        }
        else if (formControlDescriptor.getAssociatedType().equals(ChoiceControlDescriptor.getType())) {
            return getChoiceFieldEditorFactory((ChoiceControlDescriptor) formControlDescriptor);
        }
        else if (formControlDescriptor.getAssociatedType().equals(EntityNameControlDescriptor.getFieldTypeId())) {
            return getEntityNameEditorFactory((EntityNameControlDescriptor) formControlDescriptor);
        }
        else if(formControlDescriptor.getAssociatedType().equals(ImageControlDescriptor.getFieldTypeId())) {
            return getImageFieldEditorFactory();
        }
        else if(formControlDescriptor.getAssociatedType().equals(NumberControlDescriptor.getTypeId())) {
            return getNumberFieldEditorFactory((NumberControlDescriptor) formControlDescriptor);
        }
        else if(formControlDescriptor.getAssociatedType().equals(GridControlDescriptor.getType())) {
            return getGridFieldEditorFactory((GridControlDescriptor) formControlDescriptor);
        }
        else {
            return Optional.empty();
        }
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getNumberFieldEditorFactory(NumberControlDescriptor formFieldDescriptor) {
        return Optional.of(
                () -> {
                    NumberEditorControl editor = numberFieldEditorProvider.get();
                    editor.setFormat(formFieldDescriptor.getFormat());
                    editor.setRange(formFieldDescriptor.getRange());
                    editor.setLength(formFieldDescriptor.getLength());
                    LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
                    String localeName = localeInfo.getLocaleName();
                    String placeholder = formFieldDescriptor.getPlaceholder().get(localeName);
                    editor.setPlaceholder(placeholder);
                    return editor;
                }
        );
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getImageFieldEditorFactory() {
        return Optional.of(
                imageFieldEditorProvider::get
        );
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getEntityNameEditorFactory(EntityNameControlDescriptor formFieldDescriptor) {
        return Optional.of(
                () -> {
                    ClassNameFieldEditor editor = classNameFieldEditorProvider.get();
                    LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
                    editor.setPlaceholder(formFieldDescriptor.getPlaceholder().get(localeInfo.getLocaleName()));
                    return editor;
                }
        );
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getChoiceFieldEditorFactory(ChoiceControlDescriptor formFieldDescriptor) {
        return Optional.of(
                () -> {
                    ChoiceControl editor;
                    if (formFieldDescriptor.getWidgetType() == ChoiceControlType.RADIO_BUTTON) {
                        editor = choiceFieldRadioButtonEditorProvider.get();
                    }
                    else if (formFieldDescriptor.getWidgetType() == ChoiceControlType.CHECK_BOX) {
                        editor = choiceFieldCheckBoxEditorProvider.get();
                    }
                    else if(formFieldDescriptor.getWidgetType() == ChoiceControlType.SEGMENTED_BUTTON) {
                        editor = choiceFieldSegmentedEditorProvider.get();
                    }
                    else {
                        editor = choiceFieldComboBoxEditorProvider.get();
                    }
                    editor.setChoices(formFieldDescriptor.getChoices());
                    editor.setDefaultChoices(formFieldDescriptor.getDefaultChoices());
                    return editor;
                }
        );
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getTextFieldEditorFactory(TextControlDescriptor formFieldDescriptor) {
        return Optional.of(
                () -> {
                    String localeName = LocaleInfo.getCurrentLocale().getLocaleName();
                    TextControl textControl = textFieldEditorProvider.get();
                    textControl.setPlaceholder(formFieldDescriptor.getPlaceholder().get(localeName));
                    textControl.setLineMode(formFieldDescriptor.getLineMode());
                    textControl.setStringType(formFieldDescriptor.getStringType());
                    if (!formFieldDescriptor.getPattern().isEmpty()) {
                        textControl.setPattern(formFieldDescriptor.getPattern());
                        textControl.setPatternViolationErrorMessage(formFieldDescriptor.getPatternViolationErrorMessage().get(localeName));
                    }
                    return textControl;
                }
        );
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getGridFieldEditorFactory(GridControlDescriptor descriptor) {
        return Optional.of(
                () -> {
                    GridControl gridControl = gridControlProvider.get();
                    gridControl.setDescriptor(descriptor);
                    return gridControl;
                }
        );
    }
}

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
public class FormEditorFactory {

    @Nonnull
    private final Provider<TextFieldEditor> textFieldEditorProvider;

    @Nonnull
    private final Provider<ChoiceFieldRadioButtonEditor> choiceFieldRadioButtonEditorProvider;

    @Nonnull
    private final Provider<ChoiceFieldCheckBoxEditor> choiceFieldCheckBoxEditorProvider;

    @Nonnull
    private final Provider<ChoiceFieldSegmentedEditor> choiceFieldSegmentedEditorProvider;

    @Nonnull
    private final Provider<ChoiceFieldComboBoxEditor> choiceFieldComboBoxEditorProvider;

    @Nonnull
    private final Provider<ClassNameFieldEditor> classNameFieldEditorProvider;

    @Nonnull
    private final Provider<ImageFieldEditor> imageFieldEditorProvider;

    @Nonnull
    private final Provider<NumberFieldEditor> numberFieldEditorProvider;

    @Nonnull
    private final Provider<GridPresenter> gridPresenterProvider;

    @Inject
    public FormEditorFactory(@Nonnull Provider<TextFieldEditor> textFieldEditorProvider,
                             @Nonnull Provider<ChoiceFieldRadioButtonEditor> choiceFieldRadioButtonEditorProvider,
                             @Nonnull Provider<ChoiceFieldCheckBoxEditor> choiceFieldCheckBoxEditorProvider,
                             @Nonnull Provider<ChoiceFieldSegmentedEditor> choiceFieldSegmentedEditorProvider,
                             @Nonnull Provider<ChoiceFieldComboBoxEditor> choiceFieldComboBoxEditorProvider,
                             @Nonnull Provider<ClassNameFieldEditor> classNameFieldEditorProvider,
                             @Nonnull Provider<IndividualNameFieldEditor> individualNameFieldEditorProvider,
                             @Nonnull Provider<ImageFieldEditor> imageFieldEditorProvider,
                             @Nonnull Provider<NumberFieldEditor> numberFieldEditorProvider,
                             @Nonnull Provider<CompositeFieldEditor> compositeFieldEditorProvider,
                             @Nonnull Provider<GridPresenter> gridPresenterProvider) {
        this.textFieldEditorProvider = textFieldEditorProvider;
        this.choiceFieldRadioButtonEditorProvider = choiceFieldRadioButtonEditorProvider;
        this.choiceFieldCheckBoxEditorProvider = choiceFieldCheckBoxEditorProvider;
        this.choiceFieldSegmentedEditorProvider = choiceFieldSegmentedEditorProvider;
        this.choiceFieldComboBoxEditorProvider = choiceFieldComboBoxEditorProvider;
        this.classNameFieldEditorProvider = classNameFieldEditorProvider;
        this.imageFieldEditorProvider = imageFieldEditorProvider;
        this.numberFieldEditorProvider = numberFieldEditorProvider;
        this.gridPresenterProvider = gridPresenterProvider;
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
                    NumberFieldEditor editor = numberFieldEditorProvider.get();
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
                    ChoiceFieldEditor editor;
                    if (formFieldDescriptor.getWidgetType() == ChoiceFieldType.RADIO_BUTTON) {
                        editor = choiceFieldRadioButtonEditorProvider.get();
                    }
                    else if (formFieldDescriptor.getWidgetType() == ChoiceFieldType.CHECK_BOX) {
                        editor = choiceFieldCheckBoxEditorProvider.get();
                    }
                    else if(formFieldDescriptor.getWidgetType() == ChoiceFieldType.SEGMENTED_BUTTON) {
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
                    TextFieldEditor textFieldEditor = textFieldEditorProvider.get();
                    textFieldEditor.setPlaceholder(formFieldDescriptor.getPlaceholder().get(localeName));
                    textFieldEditor.setLineMode(formFieldDescriptor.getLineMode());
                    textFieldEditor.setStringType(formFieldDescriptor.getStringType());
                    if (!formFieldDescriptor.getPattern().isEmpty()) {
                        textFieldEditor.setPattern(formFieldDescriptor.getPattern());
                        textFieldEditor.setPatternViolationErrorMessage(formFieldDescriptor.getPatternViolationErrorMessage().get(localeName));
                    }
                    return textFieldEditor;
                }
        );
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getGridFieldEditorFactory(GridControlDescriptor descriptor) {
        return Optional.of(
                () -> {
                    GridPresenter gridPresenter = gridPresenterProvider.get();
                    gridPresenter.setDescriptor(descriptor);
                    return new GridEditor(gridPresenter);
                }
        );
    }
}

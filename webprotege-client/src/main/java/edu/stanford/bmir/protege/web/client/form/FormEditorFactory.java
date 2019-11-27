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
     * Get the value editor for the specified {@link FormFieldDescriptor}.  If an editor for
     * specified descriptor cannot be found then an {@link Optional#empty()} value will be
     * returned.
     * @param formFieldDescriptor The form field descriptor that describes the editor to be
     *                            created.
     * @return The {@link ValueEditorFactory} for the specified descriptor.
     */
    @Nonnull
    public Optional<ValueEditorFactory<FormDataValue>> getValueEditorFactory(@Nonnull FormFieldDescriptor formFieldDescriptor) {
        if (formFieldDescriptor.getAssociatedType().equals(TextFieldDescriptor.getType())) {
            return getTextFieldEditorFactory((TextFieldDescriptor) formFieldDescriptor);
        }
        else if (formFieldDescriptor.getAssociatedType().equals(ChoiceFieldDescriptor.getType())) {
            return getChoiceFieldEditorFactory((ChoiceFieldDescriptor) formFieldDescriptor);
        }
        else if (formFieldDescriptor.getAssociatedType().equals(EntityNameFieldDescriptor.getFieldTypeId())) {
            return getEntityNameEditorFactory((EntityNameFieldDescriptor) formFieldDescriptor);
        }
        else if(formFieldDescriptor.getAssociatedType().equals(ImageFieldDescriptor.getFieldTypeId())) {
            return getImageFieldEditorFactory();
        }
        else if(formFieldDescriptor.getAssociatedType().equals(NumberFieldDescriptor.getTypeId())) {
            return getNumberFieldEditorFactory((NumberFieldDescriptor) formFieldDescriptor);
        }
        else if(formFieldDescriptor.getAssociatedType().equals(GridFieldDescriptor.getType())) {
            return getGridFieldEditorFactory((GridFieldDescriptor) formFieldDescriptor);
        }
        else {
            return Optional.empty();
        }
    }

    @Nonnull
    private Optional<ValueEditorFactory<FormDataValue>> getNumberFieldEditorFactory(NumberFieldDescriptor formFieldDescriptor) {
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
    private Optional<ValueEditorFactory<FormDataValue>> getEntityNameEditorFactory(EntityNameFieldDescriptor formFieldDescriptor) {
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
    private Optional<ValueEditorFactory<FormDataValue>> getChoiceFieldEditorFactory(ChoiceFieldDescriptor formFieldDescriptor) {
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
    private Optional<ValueEditorFactory<FormDataValue>> getTextFieldEditorFactory(TextFieldDescriptor formFieldDescriptor) {
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
    private Optional<ValueEditorFactory<FormDataValue>> getGridFieldEditorFactory(GridFieldDescriptor descriptor) {
        return Optional.of(
                () -> {
                    GridPresenter gridPresenter = gridPresenterProvider.get();
                    gridPresenter.setDescriptor(descriptor);
                    return new GridEditor(gridPresenter);
                }
        );
    }
}

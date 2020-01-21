package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

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
    private final Provider<EntityNameControl> classNameFieldEditorProvider;

    @Nonnull
    private final Provider<ImageControl> imageFieldEditorProvider;

    @Nonnull
    private final Provider<NumberEditorControl> numberFieldEditorProvider;

    @Nonnull
    private final Provider<GridControl> gridControlProvider;

    @Nonnull
    private final Provider<FormPresenter> formPresenterProvider;

    @Nonnull
    private final Provider<SubFormControl> subFormControlProvider;

    @Inject
    public FormControlFactory(@Nonnull Provider<TextControl> textFieldEditorProvider,
                              @Nonnull Provider<RadioButtonChoiceControl> choiceFieldRadioButtonEditorProvider,
                              @Nonnull Provider<CheckBoxChoiceControl> choiceFieldCheckBoxEditorProvider,
                              @Nonnull Provider<SegmentedButtonChoiceControl> choiceFieldSegmentedEditorProvider,
                              @Nonnull Provider<ComboBoxChoiceControl> choiceFieldComboBoxEditorProvider,
                              @Nonnull Provider<EntityNameControl> classNameFieldEditorProvider,
                              @Nonnull Provider<ImageControl> imageFieldEditorProvider,
                              @Nonnull Provider<NumberEditorControl> numberFieldEditorProvider,
                              @Nonnull Provider<GridControl> gridControlProvider,
                              @Nonnull Provider<FormPresenter> formPresenterProvider,
                              @Nonnull Provider<SubFormControl> subFormControlProvider) {
        this.textFieldEditorProvider = textFieldEditorProvider;
        this.choiceFieldRadioButtonEditorProvider = choiceFieldRadioButtonEditorProvider;
        this.choiceFieldCheckBoxEditorProvider = choiceFieldCheckBoxEditorProvider;
        this.choiceFieldSegmentedEditorProvider = choiceFieldSegmentedEditorProvider;
        this.choiceFieldComboBoxEditorProvider = choiceFieldComboBoxEditorProvider;
        this.classNameFieldEditorProvider = classNameFieldEditorProvider;
        this.imageFieldEditorProvider = imageFieldEditorProvider;
        this.numberFieldEditorProvider = numberFieldEditorProvider;
        this.gridControlProvider = gridControlProvider;
        this.formPresenterProvider = formPresenterProvider;
        this.subFormControlProvider = subFormControlProvider;
    }

    /**
     * Get the value editor for the specified {@link FormControlDescriptor}.
     *
     * @param formControlDescriptor The form field descriptor that describes the editor to be
     *                              created.
     * @return The {@link ValueEditorFactory} for the specified descriptor.
     */
    @Nonnull
    public ValueEditorFactory<FormControlData> getValueEditorFactory(@Nonnull FormControlDescriptor formControlDescriptor) {
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<ValueEditorFactory<FormControlData>>() {
            @Override
            public ValueEditorFactory<FormControlData> visit(TextControlDescriptor textControlDescriptor) {
                return getTextFieldEditorFactory(textControlDescriptor);
            }

            @Override
            public ValueEditorFactory<FormControlData> visit(NumberControlDescriptor numberControlDescriptor) {
                return getNumberFieldEditorFactory(numberControlDescriptor);
            }

            @Override
            public ValueEditorFactory<FormControlData> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                return getSingleChoiceFieldEditorFactory(singleChoiceControlDescriptor);
            }

            @Override
            public ValueEditorFactory<FormControlData> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                return getMultiChoiceFieldEditorFactory(multiChoiceControlDescriptor);
            }

            @Override
            public ValueEditorFactory<FormControlData> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return getEntityNameEditorFactory(entityNameControlDescriptor);
            }

            @Override
            public ValueEditorFactory<FormControlData> visit(ImageControlDescriptor imageControlDescriptor) {
                return getImageFieldEditorFactory(imageControlDescriptor);
            }

            @Override
            public ValueEditorFactory<FormControlData> visit(GridControlDescriptor gridControlDescriptor) {
                return getGridFieldEditorFactory(gridControlDescriptor);
            }

            @Override
            public ValueEditorFactory<FormControlData> visit(SubFormControlDescriptor subFormControlDescriptor) {
                return getSubFormControlFactory(subFormControlDescriptor);
            }
        });
    }

    @Nonnull
    private ValueEditorFactory<FormControlData> getTextFieldEditorFactory(TextControlDescriptor formFieldDescriptor) {
        return () -> {
                    TextControl textControl = textFieldEditorProvider.get();
                    textControl.setDescriptor(formFieldDescriptor);
                    return textControl;
                };
    }

    @Nonnull
    private ValueEditorFactory<FormControlData> getNumberFieldEditorFactory(NumberControlDescriptor formFieldDescriptor) {
        return () -> {
                    NumberEditorControl editor = numberFieldEditorProvider.get();
                    editor.setDescriptor(formFieldDescriptor);
                    return editor;
                };
    }

    @Nonnull
    private ValueEditorFactory<FormControlData> getSingleChoiceFieldEditorFactory(SingleChoiceControlDescriptor formFieldDescriptor) {
        return () -> {
                    SingleChoiceControl editor;
                    if(formFieldDescriptor.getWidgetType() == SingleChoiceControlType.RADIO_BUTTON) {
                        editor = choiceFieldRadioButtonEditorProvider.get();
                    }
                    else if(formFieldDescriptor.getWidgetType() == SingleChoiceControlType.SEGMENTED_BUTTON) {
                        editor = choiceFieldSegmentedEditorProvider.get();
                    }
                    else {
                        editor = choiceFieldComboBoxEditorProvider.get();
                    }
                    editor.setDescriptor(formFieldDescriptor);
                    return editor;
                };
    }

    private ValueEditorFactory<FormControlData> getMultiChoiceFieldEditorFactory(MultiChoiceControlDescriptor descriptor) {
        return () -> {
            MultiValueChoiceControl editor = choiceFieldCheckBoxEditorProvider.get();
            editor.setDescriptor(descriptor);
            return editor;
        };
    }

    @Nonnull
    private ValueEditorFactory<FormControlData> getEntityNameEditorFactory(EntityNameControlDescriptor formFieldDescriptor) {
        return () -> {
            EntityNameControl editor = classNameFieldEditorProvider.get();
            editor.setDescriptor(formFieldDescriptor);
            return editor;
        };
    }

    @Nonnull
    private ValueEditorFactory<FormControlData> getImageFieldEditorFactory(ImageControlDescriptor imageControlDescriptor) {
        return () -> {
            ImageControl imageControl = imageFieldEditorProvider.get();
            imageControl.setDescriptor(imageControlDescriptor);
            return imageControl;
        };
    }

    @Nonnull
    private ValueEditorFactory<FormControlData> getGridFieldEditorFactory(GridControlDescriptor descriptor) {
        return () -> {
                    GridControl gridControl = gridControlProvider.get();
                    gridControl.setDescriptor(descriptor);
                    return gridControl;
                };
    }

    private ValueEditorFactory<FormControlData> getSubFormControlFactory(SubFormControlDescriptor subFormControlDescriptor) {
        return () -> {
            SubFormControl subFormControl = subFormControlProvider.get();
            subFormControl.start();
            return subFormControl;
        };
    }
}

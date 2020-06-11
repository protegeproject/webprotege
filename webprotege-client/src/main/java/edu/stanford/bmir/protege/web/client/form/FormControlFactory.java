package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
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
    public FormControlDataEditorFactory getDataEditorFactory(@Nonnull FormControlDescriptorDto formControlDescriptor) {
        return formControlDescriptor.accept(new FormControlDescriptorDtoVisitor<FormControlDataEditorFactory>() {
            @Override
            public FormControlDataEditorFactory visit(TextControlDescriptorDto textControlDescriptor) {
                return getTextFieldEditorFactory(textControlDescriptor);
            }

            @Override
            public FormControlDataEditorFactory visit(NumberControlDescriptorDto numberControlDescriptor) {
                return getNumberFieldEditorFactory(numberControlDescriptor);
            }

            @Override
            public FormControlDataEditorFactory visit(SingleChoiceControlDescriptorDto singleChoiceControlDescriptor) {
                return getSingleChoiceFieldEditorFactory(singleChoiceControlDescriptor);
            }

            @Override
            public FormControlDataEditorFactory visit(MultiChoiceControlDescriptorDto multiChoiceControlDescriptor) {
                return getMultiChoiceFieldEditorFactory(multiChoiceControlDescriptor);
            }

            @Override
            public FormControlDataEditorFactory visit(EntityNameControlDescriptorDto entityNameControlDescriptor) {
                return getEntityNameEditorFactory(entityNameControlDescriptor);
            }

            @Override
            public FormControlDataEditorFactory visit(ImageControlDescriptorDto imageControlDescriptor) {
                return getImageFieldEditorFactory(imageControlDescriptor);
            }

            @Override
            public FormControlDataEditorFactory visit(GridControlDescriptorDto gridControlDescriptor) {
                return getGridFieldEditorFactory(gridControlDescriptor);
            }

            @Override
            public FormControlDataEditorFactory visit(SubFormControlDescriptorDto subFormControlDescriptor) {
                return getSubFormControlFactory(subFormControlDescriptor);
            }
        });
    }

    @Nonnull
    private FormControlDataEditorFactory getTextFieldEditorFactory(TextControlDescriptorDto formFieldDescriptor) {
        return () -> {
                    TextControl textControl = textFieldEditorProvider.get();
                    textControl.setDescriptor(formFieldDescriptor.getDescriptor());
                    return textControl;
                };
    }

    @Nonnull
    private FormControlDataEditorFactory getNumberFieldEditorFactory(NumberControlDescriptorDto formFieldDescriptor) {
        return () -> {
                    NumberEditorControl editor = numberFieldEditorProvider.get();
                    editor.setDescriptor(formFieldDescriptor);
                    return editor;
                };
    }

    @Nonnull
    private FormControlDataEditorFactory getSingleChoiceFieldEditorFactory(SingleChoiceControlDescriptorDto formFieldDescriptor) {
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

    private FormControlDataEditorFactory getMultiChoiceFieldEditorFactory(MultiChoiceControlDescriptorDto descriptor) {
        return () -> {
            MultiValueChoiceControl editor = choiceFieldCheckBoxEditorProvider.get();
            editor.setDescriptor(descriptor);
            return editor;
        };
    }

    @Nonnull
    private FormControlDataEditorFactory getEntityNameEditorFactory(EntityNameControlDescriptorDto formFieldDescriptor) {
        return () -> {
            EntityNameControl editor = classNameFieldEditorProvider.get();
            editor.setDescriptor(formFieldDescriptor);
            return editor;
        };
    }

    @Nonnull
    private FormControlDataEditorFactory getImageFieldEditorFactory(ImageControlDescriptorDto imageControlDescriptor) {
        return () -> {
            ImageControl imageControl = imageFieldEditorProvider.get();
            imageControl.setDescriptor(imageControlDescriptor);
            return imageControl;
        };
    }

    @Nonnull
    private FormControlDataEditorFactory getGridFieldEditorFactory(GridControlDescriptorDto descriptor) {
        return () -> {
                    GridControl gridControl = gridControlProvider.get();
                    gridControl.setDescriptor(descriptor);
                    return gridControl;
                };
    }

    private FormControlDataEditorFactory getSubFormControlFactory(SubFormControlDescriptorDto subFormControlDescriptor) {
        return () -> {
            SubFormControl subFormControl = subFormControlProvider.get();
            subFormControl.start();
            return subFormControl;
        };
    }
}

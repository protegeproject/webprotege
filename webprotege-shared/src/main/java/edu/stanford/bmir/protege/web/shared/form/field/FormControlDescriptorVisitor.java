package edu.stanford.bmir.protege.web.shared.form.field;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public interface FormControlDescriptorVisitor<R> {

    R visit(TextControlDescriptor textControlDescriptor);

    R visit(NumberControlDescriptor numberControlDescriptor);

    R visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor);

    R visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor);

    R visit(EntityNameControlDescriptor entityNameControlDescriptor);

    R visit(ImageControlDescriptor imageControlDescriptor);

    R visit(GridControlDescriptor gridControlDescriptor);

    R visit(SubFormControlDescriptor subFormControlDescriptor);
}

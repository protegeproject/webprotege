package edu.stanford.bmir.protege.web.shared.form.field;

public interface FormControlDescriptorDtoVisitor<R> {

    R visit(TextControlDescriptorDto textControlDescriptorDto);

    R visit(SingleChoiceControlDescriptorDto singleChoiceControlDescriptorDto);

    R visit(MultiChoiceControlDescriptorDto multiChoiceControlDescriptorDto);

    R visit(NumberControlDescriptorDto numberControlDescriptorDto);

    R visit(ImageControlDescriptorDto imageControlDescriptorDto);

    R visit(GridControlDescriptorDto gridControlDescriptorDto);

    R visit(SubFormControlDescriptorDto subFormControlDescriptorDto);

    R visit(EntityNameControlDescriptorDto entityNameControlDescriptorDto);
}

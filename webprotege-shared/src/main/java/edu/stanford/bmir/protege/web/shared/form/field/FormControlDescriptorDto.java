package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

public interface FormControlDescriptorDto extends IsSerializable {

    <R> R accept(FormControlDescriptorDtoVisitor<R> visitor);

    FormControlDescriptor toFormControlDescriptor();
}

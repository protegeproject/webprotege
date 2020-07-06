package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;

public interface FormControlDataDto extends IsSerializable {

    <R> R accept(FormControlDataDtoVisitorEx<R> visitor);

    @Nonnull
    FormControlData toFormControlData();

    int getDepth();
}

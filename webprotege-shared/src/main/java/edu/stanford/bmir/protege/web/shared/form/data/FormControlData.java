package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
public interface FormControlData extends IsSerializable {

    <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor);

    void accept(@Nonnull FormControlDataVisitor visitor);
}

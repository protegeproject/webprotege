package edu.stanford.bmir.protege.web.shared.viz;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
public interface EdgeCriteria extends IsSerializable {

    <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor);
}

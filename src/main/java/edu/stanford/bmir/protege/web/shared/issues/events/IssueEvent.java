package edu.stanford.bmir.protege.web.shared.issues.events;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public interface IssueEvent extends IsSerializable {

    @Nonnull
    UserId getUserId();

    long getTimestamp();
}

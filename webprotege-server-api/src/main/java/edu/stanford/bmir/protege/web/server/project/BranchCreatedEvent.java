package edu.stanford.bmir.protege.web.server.project;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.project.BranchId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
@AutoValue
public abstract class BranchCreatedEvent {

    @Nonnull
    public abstract BranchId getBranchId();
}

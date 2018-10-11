package edu.stanford.bmir.protege.web.shared.change;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class RevertRevisionResult implements Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    @Nonnull
    public static RevertRevisionResult get(@Nonnull ProjectId projectId,
                                           @Nonnull RevisionNumber revisionNumber,
                                           @Nonnull EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_RevertRevisionResult(projectId, revisionNumber, eventList);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract RevisionNumber getRevisionNumber();

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();


}

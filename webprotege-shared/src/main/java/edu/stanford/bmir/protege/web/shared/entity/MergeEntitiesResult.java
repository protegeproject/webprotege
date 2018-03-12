package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventList;

    public MergeEntitiesResult(@Nonnull EventList<ProjectEvent<?>> eventList) {
        this.eventList = checkNotNull(eventList);
    }

    @GwtSerializationConstructor
    private MergeEntitiesResult() {
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventList);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MergeEntitiesResult)) {
            return false;
        }
        MergeEntitiesResult other = (MergeEntitiesResult) obj;
        return this.eventList.equals(other.eventList);
    }


    @Override
    public String toString() {
        return toStringHelper("MergeEntitiesResult")
                .toString();
    }
}

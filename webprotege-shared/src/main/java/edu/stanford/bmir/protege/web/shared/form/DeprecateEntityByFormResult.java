package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DeprecateEntityByFormResult implements Result, HasEventList<ProjectEvent<?>> {

    public static DeprecateEntityByFormResult get(EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_DeprecateEntityByFormResult(eventList);
    }

    @Override
    @Nonnull
    public abstract EventList<ProjectEvent<?>> getEventList();
}

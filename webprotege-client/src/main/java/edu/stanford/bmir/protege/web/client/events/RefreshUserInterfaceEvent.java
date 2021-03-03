package edu.stanford.bmir.protege.web.client.events;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.web.bindery.event.shared.Event;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class RefreshUserInterfaceEvent extends Event<RefreshUserInterfaceHandler> {

    public static final Type<RefreshUserInterfaceHandler> REFRESH_USER_INTERFACE = new Type<>();

    @Override
    public Type<RefreshUserInterfaceHandler> getAssociatedType() {
        return REFRESH_USER_INTERFACE;
    }

    @Override
    protected void dispatch(RefreshUserInterfaceHandler handler) {
        handler.handleRefreshUserInterfaceEvent();
    }

    @Nonnull
    public static RefreshUserInterfaceEvent get() {
        return new AutoValue_RefreshUserInterfaceEvent();
    }
}

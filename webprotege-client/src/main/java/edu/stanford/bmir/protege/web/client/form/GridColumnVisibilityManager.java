package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-01
 */
public class GridColumnVisibilityManager {

    public interface VisibleColumnsChangedHandler {
        void handleVisibleColumnsChanged();
    }


    private Set<GridColumnId> visibleColumns = new HashSet<>();

    private Set<VisibleColumnsChangedHandler> handlers = new LinkedHashSet<>();


    public void setVisibleColumns(@Nonnull ImmutableSet<GridColumnId> visibleColumns) {
        checkNotNull(visibleColumns);
        if(this.visibleColumns.containsAll(visibleColumns) && visibleColumns.containsAll(this.visibleColumns)) {
            return;
        }
        this.visibleColumns.clear();
        this.visibleColumns.addAll(visibleColumns);
        fireVisibleColumnsChanged();
    }

    @Nonnull
    public HandlerRegistration addVisibleColumnsChangedHandler(@Nonnull VisibleColumnsChangedHandler handler) {
        handlers.add(handler);
        return () -> handlers.remove(handler);
    }

    private void fireVisibleColumnsChanged() {
        new ArrayList<>(handlers)
                .forEach(VisibleColumnsChangedHandler::handleVisibleColumnsChanged);
    }

    @Nonnull
    public ImmutableSet<GridColumnId> getVisibleColumns() {
        return ImmutableSet.copyOf(visibleColumns);
    }

    public boolean isVisible(@Nonnull GridColumnId columnId) {
        return visibleColumns.contains(columnId);
    }
}

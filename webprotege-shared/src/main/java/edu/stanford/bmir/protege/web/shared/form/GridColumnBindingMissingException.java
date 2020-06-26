package edu.stanford.bmir.protege.web.shared.form;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GridColumnBindingMissingException extends RuntimeException implements IsSerializable {

    private GridColumnId columnId;

    public GridColumnBindingMissingException(GridColumnId columnId) {
        super("Grid column binding missing for " + columnId + ".  Form is not configured properly");
        this.columnId = checkNotNull(columnId);
    }

    @GwtSerializationConstructor
    private GridColumnBindingMissingException() {
    }

    @Nonnull
    public GridColumnId getColumnId() {
        return columnId;
    }
}

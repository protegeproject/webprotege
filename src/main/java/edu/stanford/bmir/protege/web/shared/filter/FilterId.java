package edu.stanford.bmir.protege.web.shared.filter;

import edu.stanford.bmir.protege.web.client.library.common.HasLabel;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class FilterId implements HasLabel {

    private final String label;

    public FilterId(String label) {
        this.label = checkNotNull(label);
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FilterId)) {
            return false;
        }
        FilterId other = (FilterId) obj;
        return this.label.equals(other.label);
    }


    @Override
    public String toString() {
        return toStringHelper("FilterItem")
                .addValue(label)
                .toString();
    }
}

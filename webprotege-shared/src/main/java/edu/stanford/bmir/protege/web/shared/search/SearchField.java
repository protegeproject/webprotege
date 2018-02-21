package edu.stanford.bmir.protege.web.shared.search;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class SearchField implements IsSerializable {

    private static final SearchField SEARCH_FIELD = new SearchField("Display name");

    private String fieldName;

    @GwtSerializationConstructor
    private SearchField() {
    }

    public SearchField(@Nonnull String fieldName) {
        this.fieldName = fieldName;
    }

    @Nonnull
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public int hashCode() {
        return fieldName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SearchField)) {
            return false;
        }
        SearchField other = (SearchField) obj;
        return this.fieldName.equals(other.fieldName);
    }


    @Override
    public String toString() {
        return toStringHelper("SearchField")
                .addValue(fieldName)
                .toString();
    }

    public static SearchField displayName() {
        return SEARCH_FIELD;
    }
}


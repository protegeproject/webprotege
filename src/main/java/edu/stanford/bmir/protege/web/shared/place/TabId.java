package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/12/15
 */
public class TabId {

    private final String tabName;

    public TabId(String tabName) {
        this.tabName = checkNotNull(tabName);
    }

    public String getTabName() {
        return tabName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tabName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TabId)) {
            return false;
        }
        TabId other = (TabId) obj;
        return this.tabName.equals(other.tabName);
    }

    @Override
    public String toString() {
        return toStringHelper("TabId")
                .addValue(tabName)
                .toString();
    }
}

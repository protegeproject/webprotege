package edu.stanford.bmir.protege.web.client.ui.tab;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public class TabId {

    private final String tabId;

    public TabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabId() {
        return tabId;
    }

    @Override
    public int hashCode() {
        return tabId.hashCode();
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
        return this.tabId.equals(other.tabId);
    }


    @Override
    public String toString() {
        return toStringHelper("TabId")
                .addValue(tabId)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.client.ui.tab;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public enum TabNames {

    CLASSES_TAB("edu.stanford.bmir.protege.web.client.ui.ontology.classes");

    private final String tabName;

    TabNames(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }
}

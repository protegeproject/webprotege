package edu.stanford.bmir.protege.web.shared.reasoning;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public enum DLQueryResultSection {

    EQUIVALENT_CLASSES("Equivalent classes"),

    DIRECT_SUPERCLASSES("Direct superclasses"),

    SUPERCLASSES("Superclasses"),

    DIRECT_SUBCLASSES("Direct subclasses"),

    SUBCLASSES("Subclasses"),

    DIRECT_INSTANCES("Direct instances"),

    INSTANCES("Instances");

    private String displayName;

    DLQueryResultSection(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

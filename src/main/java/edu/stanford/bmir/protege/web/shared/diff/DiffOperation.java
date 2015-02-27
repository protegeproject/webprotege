package edu.stanford.bmir.protege.web.shared.diff;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
public enum DiffOperation {

    ADD("Add", "\u2295"),

    REMOVE("Remove", "\u2296");

    private String displayName;

    private String symbol;

    DiffOperation(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }
}

package edu.stanford.bmir.protege.web.client.ui.ontology.search;

public enum DefaultSearchStringTypeEnum {
    None("@none@"), Entity("@entity@"), Property("@property@");

    private String stringValue;

    DefaultSearchStringTypeEnum(String label) {
        this.stringValue = label;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
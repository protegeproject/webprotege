package edu.stanford.bmir.protege.web.shared.crud.supplied;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/18/13
 */
public enum  WhiteSpaceTreatment {

    TRANSFORM_TO_CAMEL_CASE("Transform to camel case (most common)", "'Ontology engineer' becomes 'OntologyEngineer'"),

    REPLACE_WITH_UNDERSCORES("Replace with underscores", "'Ontology engineer' becomes 'Ontology_engineer'"),

    REPLACE_WITH_DASHES("Replace with dashes", "'Ontology engineer' becomes 'Ontology-engineer'"),

    ESCAPE("Escape (spaces will be shown in the User Interface but escaped with %20 when saved)", "'Ontology engineer' becomes 'Ontology%20engineer'");

    private String displayName;

    private String example;

    private WhiteSpaceTreatment(String displayName, String example) {
        this.displayName = displayName;
        this.example = example;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getExample() {
        return example;
    }
}

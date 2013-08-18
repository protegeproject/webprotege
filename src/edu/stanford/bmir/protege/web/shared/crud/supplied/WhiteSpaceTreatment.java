package edu.stanford.bmir.protege.web.shared.crud.supplied;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/18/13
 */
public enum  WhiteSpaceTreatment {

    ESCAPE("Leave intact", "'Ontology engineer' remains as 'Ontology engineer'"),

    TRANSFORM_TO_CAMEL_CASE("Collapse and transform to CamelCase", "'Ontology engineer' becomes 'OntologyEngineer'"),

    REPLACE_WITH_UNDERSCORES("Replace with underscores", "'Ontology engineer' becomes 'Ontology_engineer'"),

    REPLACE_WITH_DASHES("Replace with dashes", "'Ontology engineer' becomes 'Ontology-engineer'");

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

    public String transform(String input) {
        if(input.indexOf(' ') == -1) {
            return input;
        }
        String trimmedInput = input.trim();
        switch (this) {
            case ESCAPE:
                return trimmedInput.replaceAll("\\s+", "%20");
            case TRANSFORM_TO_CAMEL_CASE:
                return transformToCamelCase(trimmedInput);
            case REPLACE_WITH_UNDERSCORES:
                return trimmedInput.replaceAll("\\s+", "_");
            case REPLACE_WITH_DASHES:
                return trimmedInput.replaceAll("\\s+", "-");
        }
        throw new IllegalStateException();
    }



    private static String transformToCamelCase(String input) {
        if(input.indexOf(' ') == -1) {
            return input;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean lastWasWhiteSpace = false;
        boolean inbetweenWords = false;
        for(int index = 0; index < input.length(); index++) {
            char ch = input.charAt(index);
            if(ch != ' ') {
                if(lastWasWhiteSpace && inbetweenWords) {
                    stringBuilder.append(Character.toUpperCase(ch));
                    lastWasWhiteSpace = false;
                }
                else {
                    stringBuilder.append(ch);
                }
                lastWasWhiteSpace = false;
                inbetweenWords = true;
            }
            else {
                lastWasWhiteSpace = true;
            }
        }
        return stringBuilder.toString();
    }
}

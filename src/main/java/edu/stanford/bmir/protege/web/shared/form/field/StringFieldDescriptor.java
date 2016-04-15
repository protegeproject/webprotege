package edu.stanford.bmir.protege.web.shared.form.field;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class StringFieldDescriptor implements FormFieldDescriptor {

    private static final String FIELD_TYPE_ID = "StringField";

    private String placeholder;

    private StringType stringType;

    private LineMode lineMode;

    private String pattern;

    private String patternViolationErrorMessage;

    private StringFieldDescriptor() {
    }

    public StringFieldDescriptor(String placeholder, StringType stringType, LineMode lineMode,
                                 String pattern,
                                 String patternViolationErrorMessage) {
        this.placeholder = placeholder;
        this.stringType = stringType;
        this.lineMode = lineMode;
        this.pattern = pattern;
        this.patternViolationErrorMessage = patternViolationErrorMessage;
    }

    @Override
    public String getAssociatedFieldTypeId() {
        return FIELD_TYPE_ID;
    }

    public static String getFieldTypeId() {
        return FIELD_TYPE_ID;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public StringType getStringType() {
        return stringType;
    }

    public String getPattern() {
        return pattern;
    }

    public String getPatternViolationErrorMessage() {
        return patternViolationErrorMessage;
    }

    public LineMode getLineMode() {
        return lineMode;
    }
}

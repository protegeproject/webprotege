package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(TextFieldDescriptor.TYPE)
public class TextFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "TEXT";

    private LanguageMap placeholder = LanguageMap.empty();

    private StringType stringType = StringType.SIMPLE_STRING;

    private LineMode lineMode = LineMode.SINGLE_LINE;

    private String pattern = "";

    private LanguageMap patternViolationErrorMessage = LanguageMap.empty();

    private TextFieldDescriptor() {
    }

    public TextFieldDescriptor(@Nonnull LanguageMap placeholder,
                               @Nonnull StringType stringType,
                               @Nonnull LineMode lineMode,
                               @Nonnull String pattern,
                               @Nonnull LanguageMap patternViolationErrorMessage) {
        this.placeholder = checkNotNull(placeholder);
        this.stringType = checkNotNull(stringType);
        this.lineMode = checkNotNull(lineMode);
        this.pattern = checkNotNull(pattern);
        this.patternViolationErrorMessage = checkNotNull(patternViolationErrorMessage);
    }

    @Nonnull
    public static String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof TextFieldDescriptor)) {
            return false;
        }
        TextFieldDescriptor other = (TextFieldDescriptor) obj;
        return this.placeholder.equals(other.placeholder)
                && this.stringType.equals(other.stringType)
                && this.pattern.equals(other.pattern)
                && this.patternViolationErrorMessage.equals(other.patternViolationErrorMessage)
                && this.lineMode.equals(other.lineMode);
    }

    @Override
    public String toString() {
        return toStringHelper("TextFieldDescriptor")
                .add("placeholder", placeholder)
                .add("stringType", stringType)
                .add("pattern", pattern)
                .add("patternViolationErrorMessage", patternViolationErrorMessage)
                .add("lineMode", lineMode)
                .toString();
    }

    @Nonnull
    @Override
    @JsonIgnore
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    public LineMode getLineMode() {
        return lineMode;
    }

    @Nonnull
    public String getPattern() {
        return pattern;
    }

    @Nonnull
    public LanguageMap getPatternViolationErrorMessage() {
        return patternViolationErrorMessage;
    }

    @Nonnull
    public LanguageMap getPlaceholder() {
        return placeholder;
    }

    @Nonnull
    public StringType getStringType() {
        return stringType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(placeholder, stringType, pattern, patternViolationErrorMessage, lineMode);
    }
}

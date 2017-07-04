package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
@JsonTypeName(NumberFieldDescriptor.TYPE)
public class NumberFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "Number";

    @Nonnull
    private String numberFormat = "#######";

    private int length = 6;

    private String placeholder = "";

    @Nonnull
    private NumberFieldRange range = NumberFieldRange.range(Double.MAX_VALUE,
                                                          NumberFieldRange.BoundType.INCLUSIVE,
                                                          Double.MAX_VALUE,
                                                          NumberFieldRange.BoundType.INCLUSIVE);

    @Nonnull
    private NumberFieldType widgetType = NumberFieldType.PLAIN;

    @GwtSerializationConstructor
    private NumberFieldDescriptor() {
    }

    public NumberFieldDescriptor(@Nonnull String numberFormat,
                                 @Nonnull NumberFieldRange range,
                                 @Nonnull NumberFieldType widgetType,
                                 int length,
                                 @Nonnull String placeholder) {
        this.numberFormat = checkNotNull(numberFormat);
        this.range = checkNotNull(range);
        this.widgetType = checkNotNull(widgetType);
        this.length = length;
        this.placeholder = checkNotNull(placeholder);
    }

    public static String getTypeId() {
        return TYPE;
    }

    @Nonnull
    public String getNumberFormat() {
        return numberFormat;
    }

    @Nonnull
    public NumberFieldRange getRange() {
        return range;
    }

    public int getLength() {
        return length;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Nonnull
    public NumberFieldType getWidgetType() {
        return widgetType;
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }
}

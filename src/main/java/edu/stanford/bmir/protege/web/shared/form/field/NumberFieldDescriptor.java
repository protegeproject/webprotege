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
    private String numberFormat;

    @Nonnull
    private NumberFieldRange range;

    @Nonnull
    private NumberFieldType widgetType;

    @GwtSerializationConstructor
    private NumberFieldDescriptor() {
    }

    public NumberFieldDescriptor(@Nonnull String numberFormat,
                                 @Nonnull NumberFieldRange range,
                                 @Nonnull NumberFieldType widgetType) {
        this.numberFormat = checkNotNull(numberFormat);
        this.range = checkNotNull(range);
        this.widgetType = checkNotNull(widgetType);
    }

    @Nonnull
    public String getNumberFormat() {
        return numberFormat;
    }

    @Nonnull
    public NumberFieldRange getRange() {
        return range;
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

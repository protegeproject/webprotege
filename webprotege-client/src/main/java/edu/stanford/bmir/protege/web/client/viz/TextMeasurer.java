package edu.stanford.bmir.protege.web.client.viz;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
public interface TextMeasurer {

    void setStyleNames(@Nonnull String styles);

    @Nonnull
    TextDimensions getTextDimensions(@Nonnull String text);

    double getStrokeWidth();
}
package edu.stanford.bmir.protege.web.client.viz;

import com.google.auto.value.AutoValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
@AutoValue
public abstract class TextDimensions {

    public static TextDimensions get(int width, int height) {
        return new AutoValue_TextDimensions(width, height);
    }

    public abstract int getWidth();

    public abstract int getHeight();
}

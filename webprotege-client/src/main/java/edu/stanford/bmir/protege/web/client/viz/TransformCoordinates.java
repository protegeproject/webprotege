package edu.stanford.bmir.protege.web.client.viz;

import com.google.auto.value.AutoValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Oct 2018
 */
@AutoValue
public abstract class TransformCoordinates {

    public static TransformCoordinates get(double x, double y, double k) {
        return new AutoValue_TransformCoordinates(x, y, k);
    }

    public abstract double getX();

    public abstract double getY();

    public abstract double getK();

}

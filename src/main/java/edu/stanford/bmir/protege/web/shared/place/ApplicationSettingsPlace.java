package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class ApplicationSettingsPlace extends Place {

    private static final ApplicationSettingsPlace INSTANCE = new ApplicationSettingsPlace();

    private ApplicationSettingsPlace() {
    }

    public static ApplicationSettingsPlace get() {
        return INSTANCE;
    }

    @Override
    public int hashCode() {
        return 33;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof ApplicationSettingsPlace;
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationSettingsPlace" )
                .toString();
    }
}

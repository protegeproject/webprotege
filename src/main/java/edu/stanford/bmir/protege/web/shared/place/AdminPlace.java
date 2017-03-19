package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class AdminPlace extends Place {

    private static final AdminPlace INSTANCE = new AdminPlace();

    private AdminPlace() {
    }

    public static AdminPlace get() {
        return INSTANCE;
    }

    @Override
    public int hashCode() {
        return 33;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof AdminPlace;
    }


    @Override
    public String toString() {
        return toStringHelper("AdminPlace" )
                .toString();
    }
}

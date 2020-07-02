package edu.stanford.bmir.protege.web.shared;

import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
public class DeserializationUtil {

    public static String nonNull(@Nullable String value) {
        if(value == null) {
            return "";
        }
        else {
            return value;
        }
    }
}

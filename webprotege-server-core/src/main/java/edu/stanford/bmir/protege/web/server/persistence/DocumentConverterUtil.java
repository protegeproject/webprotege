package edu.stanford.bmir.protege.web.server.persistence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class DocumentConverterUtil {

    @Nonnull
    public static String orEmptyString(@Nullable String s) {
        if(s == null) {
            return "";
        }
        else {
            return s;
        }
    }


}

package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
public class NumberFormatter {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getFormat("#,##0;(#,##0)");

    public static String format(Number number) {
        return NUMBER_FORMAT.format(number);
    }
}

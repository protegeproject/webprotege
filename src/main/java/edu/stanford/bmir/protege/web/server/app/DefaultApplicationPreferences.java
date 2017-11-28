package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class DefaultApplicationPreferences {

    public static final String DEFAULT_APPLICATION_NAME = "WebProtégé";


    public static final String EMPTY_EMAIL_ADDRESS = "";


    private static final String DEFAULT_SCHEME = "https";

    private static final String DEFAULT_HOST = "";


    private static final String DEFAULT_PATH = "";

    private static final int DEFAULT_PORT = 443;

    private static final ApplicationPreferences DEFAULT_SETTINGS = new ApplicationPreferences(
            DEFAULT_APPLICATION_NAME,
            EMPTY_EMAIL_ADDRESS,
            new ApplicationLocation(DEFAULT_SCHEME,
                                    DEFAULT_HOST,
                                    DEFAULT_PATH,
                                    DEFAULT_PORT),
            Long.MAX_VALUE);

    public static ApplicationPreferences get() {
            return DEFAULT_SETTINGS;
    }
}

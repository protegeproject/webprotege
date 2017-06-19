package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class DefaultApplicationSettings {

    public static final String DEFAULT_APPLICATION_NAME = "WebProtégé";

    public static final String NO_CUSTOM_LOGO = "";

    public static final String EMPTY_EMAIL_ADDRESS = "";


    private static final String DEFAULT_SCHEME = "https";

    private static final String DEFAULT_HOST = "";


    private static final String DEFAULT_PATH = "";

    private static final int DEFAULT_PORT = 443;

    private static final ApplicationSettings DEFAULT_SETTINGS = new ApplicationSettings(
            DEFAULT_APPLICATION_NAME,
            NO_CUSTOM_LOGO,
            EMPTY_EMAIL_ADDRESS,
            new ApplicationLocation(DEFAULT_SCHEME,
                                    DEFAULT_HOST,
                                    DEFAULT_PATH,
                                    DEFAULT_PORT),
            Long.MAX_VALUE);

    public static ApplicationSettings get() {
            return DEFAULT_SETTINGS;
    }
}

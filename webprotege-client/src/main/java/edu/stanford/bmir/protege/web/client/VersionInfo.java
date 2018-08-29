package edu.stanford.bmir.protege.web.client;

import com.google.gwt.i18n.client.Constants;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Aug 2018
 */
public interface VersionInfo extends Constants {

    @Key("version")
    String version();

    @Key("build.timestamp")
    String buildTimestamp();

    @Key("build.number")
    String buildNumber();

    @Key("version.string")
    String versionString();
}

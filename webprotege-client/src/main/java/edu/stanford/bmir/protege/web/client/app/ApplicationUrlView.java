package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public interface ApplicationUrlView extends IsWidget {


    void setScheme(@Nonnull SchemeValue scheme);

    @Nonnull
    SchemeValue getScheme();

    void setHost(@Nonnull String host);

    @Nonnull
    String getHost();

    void setPath(@Nonnull String path);

    @Nonnull
    String getPath();

    void setPort(@Nonnull String port);

    @Nonnull
    String getPort();

}

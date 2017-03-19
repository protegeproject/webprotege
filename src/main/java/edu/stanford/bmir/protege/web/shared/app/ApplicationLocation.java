package edu.stanford.bmir.protege.web.shared.app;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;


import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
public class ApplicationLocation implements IsSerializable {

    private String scheme;

    private String host;

    private String path;

    private int port;

    @GwtSerializationConstructor
    private ApplicationLocation() {
    }

    public ApplicationLocation(@Nonnull String scheme,
                               @Nonnull String host,
                               @Nonnull String path,
                               int port) {
        this.scheme = checkNotNull(scheme);
        this.host = checkNotNull(host);
        this.path = checkNotNull(path);
        this.port = port;
    }

    @Nonnull
    public String getScheme() {
        return scheme;
    }

    @Nonnull
    public String getHost() {
        return host;
    }

    @Nonnull
    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scheme,
                                host,
                                path,
                                port);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApplicationLocation)) {
            return false;
        }
        ApplicationLocation other = (ApplicationLocation) obj;
        return this.scheme.equals(other.scheme)
                && this.host.equals(other.host)
                && this.path.equals(other.path)
                && this.port == other.port;
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationLocation" )
                .add("scheme", scheme)
                .add("host", host)
                .add("path", path)
                .add("port", port)
                .toString();
    }
}

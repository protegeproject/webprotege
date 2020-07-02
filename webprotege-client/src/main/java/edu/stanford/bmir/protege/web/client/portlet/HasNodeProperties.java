package edu.stanford.bmir.protege.web.client.portlet;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public interface HasNodeProperties {

    void setNodeProperty(@Nonnull String propertyName, @Nonnull String propertyValue);

    String getNodeProperty(@Nonnull String propertyName, String defaultValue);

}

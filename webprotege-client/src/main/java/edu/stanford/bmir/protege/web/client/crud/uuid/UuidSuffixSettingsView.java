package edu.stanford.bmir.protege.web.client.crud.uuid;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public interface UuidSuffixSettingsView extends IsWidget {

    @Nonnull
    UuidFormat getFormat();

    void setFormat(@Nonnull UuidFormat format);
}

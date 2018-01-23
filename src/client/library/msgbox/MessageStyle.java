package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.resources.client.DataResource;

import javax.annotation.Nullable;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public enum MessageStyle {

    PLAIN(Optional.empty()),

    MESSAGE(Optional.of(BUNDLE.messageIcon())),

    QUESTION(Optional.of(BUNDLE.questionIcon())),

    ALERT(Optional.of(BUNDLE.alertIcon()));

    @Nullable
    private transient DataResource image;

    MessageStyle(Optional<DataResource> image) {
        this.image = image.orElse(null);
    }

    public Optional<DataResource> getImage() {
        return Optional.ofNullable(image);
    }
}

package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.resources.client.ImageResource;

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

    MESSAGE(java.util.Optional.of(BUNDLE.messageIcon())),

    QUESTION(java.util.Optional.of(BUNDLE.questionIcon())),

    ALERT(java.util.Optional.of(BUNDLE.alertIcon()));

    @Nullable
    private ImageResource image;

    MessageStyle(Optional<ImageResource> image) {
        this.image = image.orElse(null);
    }

    public Optional<ImageResource> getImage() {
        return Optional.ofNullable(image);
    }
}

package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.common.base.Optional;
import com.google.gwt.resources.client.ImageResource;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public enum MessageStyle {

    PLAIN(Optional.<ImageResource>absent()),

    MESSAGE(Optional.of(BUNDLE.messageIcon())),

    QUESTION(Optional.of(BUNDLE.questionIcon())),

    ALERT(Optional.of(BUNDLE.alertIcon()));

    private Optional<ImageResource> image;

    private MessageStyle(Optional<ImageResource> image) {
        this.image = image;
    }

    public Optional<ImageResource> getImage() {
        return image;
    }
}

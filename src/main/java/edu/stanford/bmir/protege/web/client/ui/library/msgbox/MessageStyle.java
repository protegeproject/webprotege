package edu.stanford.bmir.protege.web.client.ui.library.msgbox;

import com.google.common.base.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public enum MessageStyle {

    PLAIN(Optional.<String>absent()),

    MESSAGE(Optional.of("images/message-icon.png")),

    QUESTION(Optional.of("images/question-icon.png")),

    ALERT(Optional.of("images/alert-icon.png"));

    private Optional<String> url;

    private MessageStyle(Optional<String> url) {
        this.url = url;
    }

    public Optional<String> getUrl() {
        return url;
    }
}

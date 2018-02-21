package edu.stanford.bmir.protege.web.client.help;

import edu.stanford.bmir.protege.web.client.about.AboutBox;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/09/2013
 */
public class ShowAboutBoxHandlerImpl implements ShowAboutBoxHandler {

    @Override
    public void handleShowAboutBox() {
        AboutBox aboutBox = new AboutBox();
        aboutBox.show();
    }
}

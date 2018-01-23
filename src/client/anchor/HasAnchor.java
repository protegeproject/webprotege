package edu.stanford.bmir.protege.web.client.anchor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public interface HasAnchor extends HasAnchorClickedHandlers {

    void setAnchorVisible(boolean b);

    void setAnchorTitle(String title);
}

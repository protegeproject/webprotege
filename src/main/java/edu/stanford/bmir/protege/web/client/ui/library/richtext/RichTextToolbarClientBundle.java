package edu.stanford.bmir.protege.web.client.ui.library.richtext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public interface RichTextToolbarClientBundle extends ClientBundle {

    public static RichTextToolbarClientBundle instance = GWT.create(RichTextToolbarClientBundle.class);

    @Source("../../../../../../../../../../war/images/bulleted-list.png")
    ImageResource bulletedList();

    @Source("../../../../../../../../../../war/images/numbered-list.png")
    ImageResource numberedList();
}

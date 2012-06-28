package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jack Elliott <jacke@stanford.edu>
 */
public class ICPSTopPanel extends TopPanel{
    public interface ICPSImages extends com.google.gwt.user.client.ui.ImageBundle {
        public AbstractImagePrototype iCatPSLogo();
    }

    @Override
            protected Image getImage() {
                ICPSImages images = (ICPSImages ) GWT.create(ICPSImages .class);
                return images.iCatPSLogo().createImage();
            }
}

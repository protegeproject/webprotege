package edu.stanford.bmir.protege.web.client.diff;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
public interface DiffClientBundle extends ClientBundle {

    DiffClientBundle INSTANCE = GWT.create(DiffClientBundle.class);

    @Source("diff.css")
    DiffCssResource style();

    interface DiffCssResource extends CssResource {

        String line();

        String add();

        String addBullet();

        String remove();

        String removeBullet();

        String source();

        String lineElement();
    }
}

package edu.stanford.bmir.protege.web.client.merge;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public interface MergeClientBundle extends ClientBundle {

    public static final MergeClientBundle BUNDLE = GWT.create(MergeClientBundle.class);

    @Source("merge.css")
    MergeCss style();

    public static interface MergeCss extends CssResource {

        String add();

        String remove();
    }
}

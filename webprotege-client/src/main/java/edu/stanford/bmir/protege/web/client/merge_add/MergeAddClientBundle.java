package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface MergeAddClientBundle extends ClientBundle {


    MergeAddClientBundle BUNDLE = GWT.create(MergeAddClientBundle.class);

    @Source("merge_add.css")
    MergeAddClientBundle.MergeCss style();

    interface MergeCss extends CssResource {

        String add();

        String remove();
    }


}

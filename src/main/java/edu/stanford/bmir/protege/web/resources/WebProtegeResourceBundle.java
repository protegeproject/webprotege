package edu.stanford.bmir.protege.web.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 02/10/2014
 */
public interface WebProtegeResourceBundle extends ClientBundle {

    public static final WebProtegeResourceBundle INSTANCE = GWT.create(WebProtegeResourceBundle.class);

    @ClientBundle.Source("WebProtege.css")
    public WebProtegeCss style();


    public static interface WebProtegeCss extends CssResource {

        String webProtegeLaf();

        String formMain();

        String formGroup();

        String formLabel();

        String dlgLabel();

//        String valueList();
    }
}

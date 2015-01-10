package edu.stanford.bmir.protege.web.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public interface WebProtegeClientBundle extends ClientBundle {

    public static final WebProtegeClientBundle BUNDLE = GWT.create(WebProtegeClientBundle.class);

    @Source("protege-logo.png")
    ImageResource webProtegeLogo();

    @Source("about.html")
    TextResource aboutBoxText();

    @Source("feedback.html")
    TextResource feedbackBoxText();

    @Source("class.png")
    ImageResource classIcon();

    @Source("property.png")
    ImageResource propertyIcon();

    @Source("property.png")
    ImageResource objectPropertyIcon();

    @Source("property.png")
    ImageResource dataPropertyIcon();

    @Source("annotation-property.png")
    ImageResource annotationPropertyIcon();

    @Source("individual.png")
    ImageResource individualIcon();

    @Source("download.png")
    ImageResource downloadIcon();

    @Source("trash.png")
    ImageResource trashIcon();

    @Source("warning.png")
    ImageResource warningIcon();

    @ClientBundle.Source("webprotege.css")
    public WebProtegeCss style();


    public static interface WebProtegeCss extends CssResource {

        String webProtegeLaf();

        String formMain();

        String formGroup();

        String formLabel();

        String dlgLabel();

        String formField();

        String warningLabel();

        String classIcon();

        String deprecatedClassIcon();

        String classIconInset();

        String objectPropertyIcon();

        String deprecatedObjectPropertyIcon();

        String objectPropertyIconInset();

        String dataPropertyIcon();

        String dataPropertyIconInset();

        String deprecatedDataPropertyIcon();

        String annotationPropertyIcon();

        String deprecatedAnnotationPropertyIcon();

        String annotationPropertyIconInset();
    }
}

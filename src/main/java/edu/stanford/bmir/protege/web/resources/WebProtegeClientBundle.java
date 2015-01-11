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

    @Source("datatype.png")
    ImageResource datatypeIcon();

    @Source("individual.png")
    ImageResource individualIcon();

    @Source("literal.png")
    ImageResource literalIcon();

    @Source("link.png")
    ImageResource linkIcon();

    @Source("iri.png")
    ImageResource iriIcon();

    @Source("number.png")
    ImageResource numberIcon();

    @Source("date-time.png")
    ImageResource dateTimeIcon();

    @Source("download.png")
    ImageResource downloadIcon();

    @Source("trash.png")
    ImageResource trashIcon();

    @Source("warning.png")
    ImageResource warningIcon();

    @Source("eye.png")
    ImageResource eyeIcon();

    @Source("eye-down.png")
    ImageResource eyeDownIcon();

    @Source("note.png")
    ImageResource noteIcon();

    @Source("user.png")
    ImageResource userIcon();

    @Source("alert-icon.png")
    ImageResource alertIcon();

    @Source("message-icon.png")
    ImageResource messageIcon();

    @Source("question-icon.png")
    ImageResource questionIcon();

    @Source("numbered-list.png")
    ImageResource numberedListIcon();

    @Source("bulleted-list.png")
    ImageResource bulletedListIcon();

    @Source("link-black.png")
    ImageResource linkBlackIcon();

    @Source("progress.gif")
    ImageResource progressAnimation();

    @Source("comment.png")
    ImageResource commentIcon();

    @Source("comment-small.png")
    ImageResource commentSmallIcon();

    @Source("comment-small-filled.png")
    ImageResource commentSmallFilledIcon();



    @Source("webprotege.css")
    WebProtegeCss style();




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

        String datatypeIcon();

        String deprecatedDatatypeIcon();

        String datatypeIconInset();

        String literalIcon();

        String literalIconInset();

        String individualIcon();

        String deprecatedIndividualIcon();

        String individualIconInset();

        String linkIcon();

        String linkIconInset();

        String iriIcon();

        String iriIconInset();

        String numberIcon();

        String numberIconInset();

        String dateTimeIcon();

        String dateTimeIconInset();
    }
}

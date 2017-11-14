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

    WebProtegeClientBundle BUNDLE = GWT.create(WebProtegeClientBundle.class);

    @Source("protege-logo-large.png")
    ImageResource webProtegeLogoLarge();

    @Source("protege-blender.png")
    ImageResource protegeBlender();

    @Source("protege-blender-monochrome.png")
    ImageResource protegeBlenderGrayScale();

    @Source("about.html")
    TextResource aboutBoxText();

    @Source("feedback.html")
    TextResource feedbackBoxText();

    @Source("busy.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource busy();

    @Source("class.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgClassIcon();

    @Source("deprecated-class.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgDeprecatedClassIcon();

    @Source("property.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgPropertyIcon();

    @Source("data-property.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgDataPropertyIcon();

    @Source("object-property.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgObjectPropertyIcon();

    @Source("annotation-property.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgAnnotationPropertyIcon();

    @Source("datatype.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgDatatypeIcon();

    @Source("individual.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgIndividualIcon();

    @Source("literal.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgLiteralIcon();

    @Source("link.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgLinkIcon();

    @Source("iri.png")
    ImageResource iriIcon();

    @Source("number.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgNumberIcon();

    @Source("iri.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgIriIcon();

    @Source("date-time.png")
    ImageResource dateTimeIcon();

    @Source("download.png")
    ImageResource downloadIcon();

    @Source("trash.png")
    ImageResource trashIcon();

    @Source("warning.png")
    ImageResource warningIcon();

    @Source("eye.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgEyeIcon();

    @Source("eye-down.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgEyeIconDown();

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

    @Source("comment-small-filled.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCommentSmallFilledIcon();

    @Source("WebProtege.css")
    WebProtegeCss style();

    @Source("WebProtegeLaf.css")
    WebProtegeLaf laf();

    @Source("WebProtegeButtons.css")
    WebProtegeButtons buttons();

    @Source("WebProtegeMenus.css")
    WebProtegeMenu menu();

    @Source("WebProtegeDialog.css")
    WebProtegeDialog dialog();

    @Source("filter.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgFilter();

    @Source("language-codes.txt")
    TextResource languageCodes();

    interface WebProtegeCss extends CssResource {

        String entityIcon();

        String focusBorder();

        String noFocusBorder();

        String webProtegeLaf();

        String formMain();

        String formGroup();

        String formLabel();

        String dlgLabel();

        String formField();

        String warningLabel();

        String classIconInset();

        String objectPropertyIconInset();

        String dataPropertyIconInset();

        String annotationPropertyIconInset();

        String datatypeIconInset();

        String literalIconInset();

        String dateTimeIconInset();

        String individualIconInset();

        String linkIconInset();

        String iriIconInset();

        String numberIconInset();

        String portletToolbar();

        String inTrash();

        String userName();

        String sharingDropDown();

        String discussionThreadStatusOpen();

        String discussionThreadStatusClosed();

        String commentIconInset();

        String derivedInformation();

        String deleteButton();

        String errorBorder();

        String noErrorBorder();

        String errorLabel();

        String selection();

        String noSelection();

        String helpText();
    }

    interface WebProtegeLaf extends CssResource {

        @ClassName("wp-TopBar")
        String topBar();

        @ClassName("wp-TopBar-title")
        String topBarTitle();

        @ClassName("wp-TopBar-separator")
        String topBarSeparator();

        @ClassName("wp-TopBar-button")
        String topBarButton();
    }

    interface WebProtegeButtons extends CssResource {

        @ClassName("wp-button-base")
        String base();

        @ClassName("wp-button-primary")
        String primary();

        @ClassName("wp-button-secondary")
        String secondary();

        @ClassName("btn-inline")
        String btnInline();


        @ClassName("wp-ToolBar-button")
        String toolbarButton();

        String toolbarButtonSelected();

        @ClassName("wp-cancel-button")
        String cancelButton();

        @ClassName("wp-accept-button")
        String acceptButton();

//        String button();
    }

    interface WebProtegeDialog extends CssResource {

        String dialog();

        String title();

        String content();

        String group();

        String buttonBar();

        String errorLabel();

        @ClassName("gwt-TextBox")
        String gwtTextBox();

        @ClassName("gwt-PasswordTextBox")
        String gwtPasswordTextBox();

        @ClassName("gwt-Label")
        String gwtLabel();
    }

    interface WebProtegeMenu extends CssResource {

        String popupMenu();

        String popupMenuInner();

        String popupMenuItem();

        String popupMenuItemDisabled();

        String separator();
    }
}

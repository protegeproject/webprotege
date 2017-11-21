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

    @Source("alert-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource alertIcon();

    @Source("question-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource questionIcon();

    @Source("message-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource messageIcon();

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

    @Source("home-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource homeIcon();

    @Source("WebProtege.css")
    WebProtegeCss style();

    @Source("WebProtegeLaf.css")
    WebProtegeLaf laf();

    @Source("WebProtegeButtons.css")
    WebProtegeButtons buttons();

    @Source("WebProtegeMenus.css")
    WebProtegeMenu menu();

    @Source("WebProtegeSettingsPage.css")
    WebProtegeSettingsPage dialog();

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

        String emptyIconInset();

        String linkIconInset();

        String iriIconInset();

        String numberIconInset();

        String inTrash();

        String userName();

        String sharingDropDown();

        String discussionThreadStatusOpen();

        String discussionThreadStatusClosed();

        String commentIconInset();

        String derivedInformation();

        String errorBorder();

        String noErrorBorder();

        String errorLabel();

        String selection();

        String noSelection();

        String helpText();
    }

    interface WebProtegeLaf extends CssResource {

        @ClassName("wp-top-bar")
        String topBar();

        @ClassName("wp-top-bar__logo")
        String topBarIcon();

        @ClassName("wp-top-bar__content")
        String topBarContent();

        @ClassName("wp-top-bar__title")
        String topBarTitle();

        @ClassName("wp-top-bar__separator")
        String topBarSeparator();

        @ClassName("wp-toolbar")
        String toolbar();
    }

    interface WebProtegeButtons extends CssResource {

        /**
         * The base class for buttons
         */
        @ClassName("wp-btn")
        String button();

        /**
         * A button that appears on a page (rather than a dialog, for example)
         */
        @ClassName("wp-btn--page")
        String pageButton();

        @ClassName("wp-btn--page-small")
        String smallPageButton();

        /**
         * A button that appears on a dialog popup
         */
        @ClassName("wp-btn--dialog")
        String dialogButton();

        /**
         * The primary button on a page or on a dialog
         */
        @ClassName("wp-btn--primary")
        String primaryButton();

        /**
         * An alternate button on a page or on a dialog
         */
        @ClassName("wp-btn--alternate")
        String alternateButton();

        /**
         * A button to clear/cancel/return-from the current state.  For example
         * a dialog "Cancel" button, or a "Clear" form button
         */
        @ClassName("wp-btn--escape")
        String escapeButton();

        /**
         * A button to accept/apply the current state.  For example
         * a dialog "OK" button, or an "Apply" form button
         */
        @ClassName("wp-btn--accept")
        String acceptButton();



        @ClassName("wp-top-bar__btn")
        String topBarButton();


        @ClassName("wp-toolbar__btn")
        String toolbarButton();

        @ClassName("wp-toolbar__btn--highlighted")
        String highlightedToolbarButton();



        @ClassName("wp-btn-delete")
        String deleteButton();

        @ClassName("wp-btn-add-tab")
        String addTabButton();
    }

    interface WebProtegeSettingsPage extends CssResource {

        String dialogContainer();

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

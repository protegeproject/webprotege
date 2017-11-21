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

    @Source("protege-blender-monochrome.png")
    ImageResource protegeBlenderGrayScale();

    @Source("about.html")
    TextResource aboutBoxText();

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

    @Source("number.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgNumberIcon();

    @Source("iri.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgIriIcon();

    @Source("date-time.png")
    ImageResource dateTimeIcon();

    @Source("eye.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgEyeIcon();

    @Source("eye-down.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgEyeIconDown();

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

    @Source("progress.gif")
    ImageResource progressAnimation();

    @Source("comment-small-filled.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCommentSmallFilledIcon();

    @Source("home-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource homeIcon();

    @Source("WebProtege.css")
    WebProtegeCss style();

    @Source("topbar.css")
    TopBar laf();

    @Source("toolbar.css")
    ToolbarCss toolbar();

    @Source("login.css")
    LoginCss login();

    @Source("buttons.css")
    ButtonsCss buttons();

    @Source("menu.css")
    MenuCss menu();

    @Source("WebProtegeSettingsPage.css")
    WebProtegeSettingsPage settings();

    @Source("filter.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgFilter();

    @Source("language-codes.txt")
    TextResource languageCodes();

    @Source("protege-blender.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource protegeBlender();

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

    interface TopBar extends CssResource {

        @ClassName("wp-topbar")
        String topBar();

        @ClassName("wp-topbar__logo")
        String topBarLogo();

        @ClassName("wp-topbar__content")
        String topBarContent();

        @ClassName("wp-topbar__title")
        String topBarTitle();

        @ClassName("wp-topbar__separator")
        String topBarSeparator();
    }

    interface LoginCss extends CssResource {

        @ClassName("wp-login")
        String login();

        @ClassName("wp-login__logo")
        String loginLogo();
    }

    interface ToolbarCss extends CssResource {

        @ClassName("wp-toolbar")
        String toolbar();

        @ClassName("wp-toolbar__btn")
        String toolbarButton();

        @ClassName("wp-toolbar__btn--highlighted")
        String highlightedToolbarButton();
    }

    interface ButtonsCss extends CssResource {

        /**
         * The base class for buttons
         */
        @ClassName("wp-btn")
        String button();

        /**
         * A button that appears on a page (rather than a settings, for example)
         */
        @ClassName("wp-btn--page")
        String pageButton();

        @ClassName("wp-btn--page-small")
        String smallPageButton();

        /**
         * A button that appears on a settings popup
         */
        @ClassName("wp-btn--dialog")
        String dialogButton();

        /**
         * The primary button on a page or on a settings
         */
        @ClassName("wp-btn--primary")
        String primaryButton();

        /**
         * An alternate button on a page or on a settings
         */
        @ClassName("wp-btn--alternate")
        String alternateButton();

        /**
         * A button to clear/cancel/return-from the current state.  For example
         * a settings "Cancel" button, or a "Clear" form button
         */
        @ClassName("wp-btn--escape")
        String escapeButton();

        /**
         * A button to accept/apply the current state.  For example
         * a settings "OK" button, or an "Apply" form button
         */
        @ClassName("wp-btn--accept")
        String acceptButton();



        @ClassName("wp-topbar__btn")
        String topBarButton();


        @ClassName("wp-btn-delete")
        String deleteButton();

        @ClassName("wp-btn-add-tab")
        String addTabButton();
    }

    interface WebProtegeSettingsPage extends CssResource {

        String outer();

        String inner();

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

    interface MenuCss extends CssResource {

        String popupMenu();

        String popupMenuInner();

        String popupMenuItem();

        String popupMenuItemDisabled();

        String separator();
    }
}

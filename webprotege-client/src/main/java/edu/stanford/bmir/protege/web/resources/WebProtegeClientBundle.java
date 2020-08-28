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

    @Source("help.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgHelpIcon();

    @Source("ascending.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgAscendingIcon();

    @Source("descending.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgDescendingIcon();

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

    @Source("Wikipedia_W.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgWikipediaIcon();

    @Source("plus.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgPlusIcon();

    @Source("create-class.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCreateIcon();

    @Source("create-individual.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCreateIndividualIcon();

    @Source("delete-individual.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgDeleteIndividualIcon();

    @Source("create-property.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCreatePropertyIcon();

    @Source("delete-property.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgDeletePropertyIcon();

    @Source("delete-class.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgDeleteIcon();

    @Source("cross.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCrossIcon();

    @Source("date-time.png")
    ImageResource dateTimeIcon();

    @Source("eye.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgEyeIcon();

    @Source("eye-down.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgEyeIconDown();

    @Source("goto.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource gotoIcon();

    @Source("sync-selection.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource syncSelectionIcon();

    @Source("hierarchy-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource hierarchyIcon();


    @Source("up-arrow.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource upArrowIcon();

    @Source("down-arrow.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource downArrowIcon();

    @Source("move-to-parent.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource moveToParentIcon();

    @Source("move-to-child.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource moveToChildIcon();

    @Source("move-to-sibling.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource moveToSiblingIcon();

    @Source("search-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource searchIcon();

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

    @Source("entitynode.css")
    EntityNodeCss entityNode();

    @Source("datetimepicker.css")
    DateTimePicker dateTimePicker();

    @Source("tags.css")
    Tags tags();

    @Source("topbar.css")
    TopBar laf();

    @Source("modal.css")
    ModalCss modal();

    @Source("toolbar.css")
    ToolbarCss toolbar();

    @Source("login.css")
    LoginCss login();

    @Source("buttons.css")
    ButtonsCss buttons();

    @Source("menu.css")
    MenuCss menu();

    @Source("discussion.css")
    DiscussionCss discussion();

    @Source("WebProtegeSettingsPage.css")
    SettingsPageCss settings();

    @Source("value-list.css")
    ValueListCss valueList();

    @Source("project-list.css")
    ProjectList projectList();

    @Source("filter.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgFilter();

    @Source("funnel.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgFunnel();

    @Source("language-codes.txt")
    TextResource languageCodes();

    @Source("protege-blender.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource protegeBlender();


    @Source("protege-cloud.png")
    @DataResource.MimeType("image/png")
    DataResource protegeCloud();

    @Source("refresh-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgRefreshIcon();

    @Source("create-thread.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCreateThread();

    @Source("editor-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgEditorIcon();

    @Source("changes-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgChangesIcon();

    @Source("viz-icon.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource vizIcon();

    @Source("download.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource downloadIcon();


    @Source("settings.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource settingsIcon();


    @Source("filter.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource filterIcon();

    @Source("expand-all.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgExpandAllIcon();

    @Source("collapse-all.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource svgCollapseAllIcon();

    @Source("glyphs.css")
    Glyphs glyphs();

    @Source("primitive-data.css")
    WebProtegePrimitiveDataCss primitiveData();

    interface WebProtegePrimitiveDataCss extends CssResource {

        @ClassName("wp-pd")
        String primitiveData();

        @ClassName("wp-pd__error-message")
        String primitiveData__errorMessage();

        @ClassName("wp-pd__icon")
        String primitiveData__icon();

        @ClassName("wp-pd--deprecated")
        String primitiveData_____deprecated();

        @ClassName("wp-pd__text")
        String primitiveData__text();
    }

    interface WebProtegeCss extends CssResource {

        String focusBorder();

        String noFocusBorder();

        @ClassName("wp-form")
        String form();

        @ClassName("wp-form-group")
        String formGroup();

        @ClassName("wp-form-group--narrow")
        String formGroupNarrow();

        @ClassName("wp-form-group__row")
        String formGroupRow();

        @ClassName("wp-form-group--multi-col")
        String formGroupMultiCol();

        @ClassName("wp-form-group--single-col")
        String formGroupSingleCol();

        @ClassName("wp-form-stretch")
        String formStretch();

        @ClassName("wp-form-label")
        String formLabel();

        @ClassName("wp-form-label--error")
        String formLabelError();

        @ClassName("wp-form-editor")
        String formEditor();

        @ClassName("wp-form-editor--error")
        String formEditorError();

        @ClassName("wp-form-checkbox")
        String formCheckBox();

        @ClassName("wp-form-help-text")
        String formHelpText();

        @ClassName("wp-form-input-group")
        String formInputGroup();

        @ClassName("wp-form-before-input")
        String formBeforeInput();

        @ClassName("wp-form-after-input")
        String formAfterInput();

        @ClassName("wp-form-help-text--checkbox-indented")
        String formHelpTextCheckBoxIndented();

        @ClassName("wp-table-header")
        String tableHeader();

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

        String wikipediaIconInset();

        String numberIconInset();

        String inTrash();

        String commentIconInset();

        @ClassName("wp-comment-small-filled-icon")
        String commentSmallFilledIcon();

        String derivedInformation();

        String errorBorder();

        String errorBorderColor();

        String noErrorBorder();

        String errorLabel();

        String selection();

        String noSelection();

        @ClassName("wp-panel-filled")
        String panelFilled();

        @ClassName("wp-panel-filled--border")
        String panelFilledBorder();

        @ClassName("wp-panel-neutral")
        String panelNeutral();

        @ClassName("wp-form_section-checkbox")
        String formSectionCheckBox();

        @ClassName("wp-form__horizontal-radio-button-group")
        String formHorizontalRadioButtonGroup();

        @ClassName("home-icon")
        String homeIcon();

        @ClassName("wp-form__control-stack--horizontal")
        String formStackHorizontal();

        @ClassName("wp-form__control-stack--vertical")
        String formStackVertical();

        @ClassName("wp-form__control-stack__item--delete-hover")
        String formStackItemDeleteHover();

        @ClassName("wp-form__form-tab-bar")
        String formTabBar();

        @ClassName("wp-form__form-tab-bar__tab")
        String formTabBar__tab();

        @ClassName("wp-form__form-tab-bar__tab--selected")
        String formTabBar__tab__selected();

        @ClassName("wp-form__text-block")
        String formTextBlock();

        @ClassName("wp-form__iri-field")
        String formIriField();

        @ClassName("wp-form__grid")
        String formGrid();

        @ClassName("wp-form__grid__header-row")
        String formGridHeaderRow();

        @ClassName("wp-form__grid__row")
        String formGridRow();

        @ClassName("wp-form__grid__column")
        String formGridColumn();

        @ClassName("wp-form__grid__cell")
        String formGridCell();

        @ClassName("wp-help-icon")
        String helpIcon();

        @ClassName("wp-form__filtered-indication")
        String formFilteredIndication();
    }

    interface DateTimePicker extends CssResource {
        @ClassName("wp-date-time-picker")
        String picker();
    }

    interface Tags extends CssResource {
        @ClassName("wp-tag")
        String tag();

        @ClassName("wp-tag--tag-list-tag")
        String tagTagListTag();

        @ClassName("wp-tag--inline-tag")
        String tagHierarchyInlineTag();

        @ClassName("wp-tags-view--tags-hidden")
        String tagsHidden();
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

        @ClassName("wp-topbar__home-icon")
        String topBarHomeIcon();


    }

    interface LoginCss extends CssResource {

        @ClassName("wp-login")
        String login();

        @ClassName("wp-login__logo")
        String loginLogo();

        @ClassName("wp-login__form")
        String loginForm();

        @ClassName("wp-login__footnote")
        String loginFootnote();

        @ClassName("wp-login__message")
        String loginMessage();

        @ClassName("wp-login__forgot-user-name-or-password")
        String loginForgotUsernameOrPassword();
    }

    interface DiscussionCss extends CssResource {

        @ClassName("wp-disc-thread")
        String discussion();


        @ClassName("wp-disc-thread__outer")
        String threadOuter();

        @ClassName("wp-disc-thread__inner")
        String threadInner();


        @ClassName("wp-disc-thread__status")
        String status();

        @ClassName("wp-disc-thread__status--open")
        String discussionThreadStatusOpen();

        @ClassName("wp-disc-thread__status--closed")
        String discussionThreadStatusClosed();

        @ClassName("wp-comment")
        String comment();

        @ClassName("wp-comment__author-name")
        String commentAuthor();

        @ClassName("wp-comment__date")
        String commentDate();

        @ClassName("wp-comment__body")
        String commentBody();

        @ClassName("wp-comment__user-mention")
        String userMention();

    }

    interface ToolbarCss extends CssResource {

        @ClassName("wp-toolbar")
        String toolbar();

        @ClassName("wp-toolbar__btn")
        String toolbarButton();

        @ClassName("wp-toolbar__g-btn")
        String toolbarGlyphButton();

        @ClassName("wp-toolbar__btn--highlighted")
        String highlightedToolbarButton();
    }

    interface ModalCss extends CssResource {

        @ClassName("wp-modal")
        String modal();

        @ClassName("wp-modal__caption")
        String caption();

        @ClassName("wp-modal__content")
        String content();

        @ClassName("wp-modal__button-bar")
        String buttonBar();

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
         * A button that appears before or after an input as if it is attached to the input
         * @return
         */
        @ClassName("wp-btn--input")
        String inputButton();

        /**
         * The primary button on a page or on a settings
         */
        @ClassName("wp-btn--primary")
        String primaryButton();

        @ClassName("wp-btn--secondary")
        String secondaryButton();

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

        @ClassName("wp-btn-add")
        String addButton();

        @ClassName("wp-btn-delete")
        String deleteButton();

        @ClassName("wp-btn-add-tab")
        String addTabButton();

        @ClassName("wp-btn-up")
        String upButton();

        @ClassName("wp-btn-down")
        String downButton();

        @ClassName("wp-btn-g")
        String btnGlyph();

        @ClassName("wp-btn-g--small")
        String btnGlyphSmall();

        @ClassName("wp-btn-g--sync-selection")
        String syncSelection();

        @ClassName("wp-btn-g--move-to-parent")
        String moveToParent();

        @ClassName("wp-btn-g--move-to-sibling")
        String moveToSibling();

        @ClassName("wp-btn-g--move-to-child")
        String moveToChild();

        @ClassName("wp-btn-g--show-hierarchy")
        String showHierarchy();

        @ClassName("wp-btn-g--create-class")
        String create();

        @ClassName("wp-btn-g--create-individual")
        String createIndividual();

        @ClassName("wp-btn-g--delete-individual")
        String deleteIndividual();

        @ClassName("wp-btn-g--create-property")
        String createProperty();

        @ClassName("wp-btn-g--delete-property")
        String deleteProperty();

        @ClassName("wp-btn-g--delete-class")
        String delete();

        @ClassName("wp-btn-g--search")
        String search();

        @ClassName("wp-btn-g--refresh")
        String refresh();

        @ClassName("wp-btn-g--create-thread")
        String createThread();

        @ClassName("wp-btn-g--editor")
        String editor();

        @ClassName("wp-btn-g--changes")
        String changes();

        @ClassName("wp-btn-g--viz")
        String viz();

        @ClassName("wp-btn-g--container")
        String btnGlyphContainer();

        @ClassName("wp-btn-g--download")
        String download();

        @ClassName("wp-btn-g--settings")
        String settings();

        @ClassName("wp-btn-g--filter")
        String filter();

        @ClassName("wp-btn-g--funnel")
        String funnel();

        @ClassName("wp-btn-g--cross")
        String cross();

        @ClassName("wp-btn-g--expand-all")
        String expandAll();

        @ClassName("wp-btn-g--collapse-all")
        String collapseAll();

        @ClassName("wp-btn-g--sort-ascending")
        String sortAscending();


        @ClassName("wp-btn-g--sort-descending")
        String sortDescending();


    }

    interface SettingsPageCss extends CssResource {

        @ClassName("wp-settings")
        String settings();

        @ClassName("wp-settings__section")
        String section();

        @ClassName("wp-settings__section__title")
        String title();

        @ClassName("wp-settings__section__content")
        String content();

        String group();

        String buttonBar();
    }

    interface MenuCss extends CssResource {

        @ClassName("wp-popup-menu")
        String popupMenu();

        @ClassName("wp-popup-menu__inner")
        String popupMenuInner();

        @ClassName("wp-popup-menu__item")
        String popupMenuItem();

        @ClassName("wp-popup-menu__item--selected")
        String popupMenuItemSelected();

        @ClassName("wp-popup-menu__item--disabled")
        String popupMenuItemDisabled();

        @ClassName("wp-popup-menu__separator")
        String separator();
    }

    interface ValueListCss extends  CssResource {

        @ClassName("wp-value-list")
        String valueList();

        @ClassName("wp-value-list__row")
        String valueListRow();

        @ClassName("wp-value-list__editor")
        String valueListEditor();

        @ClassName("wp-value-list__delete-button")
        String valueListDeleteButton();

    }

    interface ProjectList extends CssResource {

        @ClassName("wp-project-list")
        String projectList();

        @ClassName("wp-project-list__header")
        String projectListHeader();

        @ClassName("wp-project-list__name-col")
        String projectNameCol();

        @ClassName("wp-project-list__owner-col")
        String ownerCol();

        @ClassName("wp-project-list__last-opened-col")
        String lastOpenCol();

        @ClassName("wp-project-list__last-modified-col")
        String lastModifiedCol();

        @ClassName("wp-project-list__menu-button-col")
        String menuButtonCol();

        @ClassName("wp-project-list__rows")
        String rows();

        @ClassName("wp-project-list__rows__row")
        String row();


        @ClassName("wp-project-list__cell")
        String cell();
    }

    interface EntityNodeCss extends CssResource {

        @ClassName("wp-entity-node")
        String wpEntityNode();

        @ClassName("wp-entity-node__display-name")
        String wpEntityNode__displayName();

        @ClassName("wp-entity-node__display-name__primary-language")
        String wpEntityNode__primaryLanguage();

        @ClassName("wp-entity-node__display-name__secondary-language")
        String wpEntityNode__secondaryLanguage();
    }

    interface Glyphs extends CssResource {

    }
}

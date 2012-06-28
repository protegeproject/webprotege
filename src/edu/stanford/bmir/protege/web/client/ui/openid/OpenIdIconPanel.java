/**
 * 
 */
package edu.stanford.bmir.protege.web.client.ui.openid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.ui.login.LoginUtil;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;

/**
 * Created Panel to display open id providers
 * 
 * @author z.khan
 * 
 */
public class OpenIdIconPanel extends Composite {

    protected DisclosurePanel disclosurePanel;
    protected HorizontalPanel mainPanel;
    final protected PopupPanel openIdPopup = new PopupPanel();

    protected String imageBaseUrl;
    protected boolean isLoginWithHttps;//if false then check if userlogin when user clicks image/button
    protected HandlerRegistration windowCloseHandlerRegistration; // handler for removing window close handler

    public void setLoginWithHttps(boolean isLoginWithHttps) {
        this.isLoginWithHttps = isLoginWithHttps;
    }

    public OpenIdIconPanel(Window win) {
        init();
        initUrl();
        createFrontIconWidget(win);
        createOpenIdIconPopup(win);
        addHandlers(win);
        initWidget(mainPanel);
    }

    protected void initUrl() {
        imageBaseUrl = GWT.getModuleBaseURL();
        imageBaseUrl = imageBaseUrl.substring(0, imageBaseUrl.lastIndexOf('/'));
        imageBaseUrl = imageBaseUrl.substring(0, imageBaseUrl.lastIndexOf('/'));
    }

    protected void init() {
        mainPanel = new HorizontalPanel();
        DOM.setStyleAttribute(openIdPopup.getElement(), "zIndex", "10000");
        disclosurePanel = new DisclosurePanel();
        disclosurePanel.setHeader(new Button("<span style='font-size:75%; margin-top: 3px;'>&nbsp;&#9660;</span>"));
        disclosurePanel.setOpen(true);
    }

    protected void addHandlers(final Window win) {
        disclosurePanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {

            public void onOpen(OpenEvent<DisclosurePanel> event) {
                disclosurePanel.setHeader(new Button(
                        "<span style='font-size:75%; margin-top: 3px;'>&nbsp;&#9660;</span>"));
                win.setHeight(win.getHeight() + 70);
            }
        });
        disclosurePanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {

            public void onClose(CloseEvent<DisclosurePanel> event) {
                disclosurePanel.setHeader(new Button(
                        "<span style='font-size:75%; margin-top: 3px;'>&nbsp;&#9658;</span>"));
                win.setHeight(win.getHeight() - 70);

            }
        });
    }

    protected void createFrontIconWidget(final Window win) {
        final OpenIdUtil openIdUtil = new OpenIdUtil();
        FlexTable frontOpenIdIconFlex = new FlexTable();

        Image googleImage = new Image(imageBaseUrl + "/images/openid/google.png");
        googleImage.setTitle("Login with your Google account");
        googleImage.setStyleName("menuBar");
        addFrontIconClickHandler(win, openIdUtil, googleImage, 1);

        Image yahooImage = new Image(imageBaseUrl + "/images/openid/yahoo.png");
        yahooImage.setStyleName("menuBar");
        yahooImage.setTitle("Login with your Yahoo account");
        addFrontIconClickHandler(win, openIdUtil, yahooImage, 2);

        Image verisignImage = new Image(imageBaseUrl + "/images/openid/verisign.jpg");
        verisignImage.setStyleName("menuBar");
        verisignImage.setSize("34px", "34px");
        verisignImage.setTitle("Login with your Verisign account");
        addFrontIconClickHandler(win, openIdUtil, verisignImage, 7);

        Anchor moreOpenIdAnchor = new Anchor("More...");
        moreOpenIdAnchor.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (isLoginWithHttps) {
                    setWindowSize("405", "490");
                }
                Anchor moreOIdAnchor = (Anchor) event.getSource();
                moreOIdAnchor.getAbsoluteLeft();
                int left = moreOIdAnchor.getAbsoluteLeft();
                int top = moreOIdAnchor.getAbsoluteTop();
                openIdPopup.setSize("100px", "100px");
                openIdPopup.setPopupPosition(left - 150, top + 20);

                openIdPopup.show();

            }
        });

        
            openIdPopup.addCloseHandler(new CloseHandler<PopupPanel>() {

                public void onClose(CloseEvent<PopupPanel> event) {
                    if (isLoginWithHttps) {
                    setWindowSize("405", "408");
                    }
                }
            });
        frontOpenIdIconFlex.setWidget(0, 1, googleImage);
        frontOpenIdIconFlex.setWidget(0, 3, yahooImage);
        frontOpenIdIconFlex.setWidget(0, 5, verisignImage);
        frontOpenIdIconFlex.setWidget(0, 7, moreOpenIdAnchor);

        frontOpenIdIconFlex.getFlexCellFormatter().setWidth(0, 0, "40px");
        frontOpenIdIconFlex.getFlexCellFormatter().setWidth(0, 2, "10px");
        frontOpenIdIconFlex.getFlexCellFormatter().setWidth(0, 4, "10px");
        frontOpenIdIconFlex.getFlexCellFormatter().setWidth(0, 6, "10px");

        mainPanel.add(frontOpenIdIconFlex);

    }

    protected void createOpenIdIconPopup(final Window win) {

        final OpenIdUtil openIdUtil = new OpenIdUtil();
        openIdPopup.setAutoHideEnabled(true);

        Image flickrImage = new Image(imageBaseUrl + "/images/openid/flickr.png");
        flickrImage.setStyleName("menuBar");
        flickrImage.setTitle("Login with your Flickr account");
        addOpenIdPopupImageClickHandler(win, openIdUtil, flickrImage, 5);

        HTML flickrLabel = new HTML("&nbsp<b><span style='font-size:75%;'>Flickr</span></b>");
        flickrLabel.setTitle("Login with your Flickr account");
        flickrLabel.setStyleName("menuBar");
        addOpenIdPopupLabelClickHandler(win, openIdUtil, flickrLabel, 5);

        Image mySpaceImage = new Image(imageBaseUrl + "/images/openid/myspace.jpg");
        mySpaceImage.setHeight("16px");
        mySpaceImage.setTitle("Login with your MySpace account");
        mySpaceImage.setStyleName("menuBar");
        addOpenIdPopupImageClickHandler(win, openIdUtil, mySpaceImage, 4);

        HTML mySpaceLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MySpace</span></b>");
        mySpaceLabel.setTitle("Login with your MySpace account");
        mySpaceLabel.setStyleName("menuBar");
        addOpenIdPopupLabelClickHandler(win, openIdUtil, mySpaceLabel, 4);

        Image mydImage = new Image(imageBaseUrl + "/images/openid/myid.jpg");
        mydImage.setStyleName("menuBar");
        mydImage.setHeight("16px");
        mydImage.setTitle("Login with your MyId account");
        addOpenIdPopupImageClickHandler(win, openIdUtil, mydImage, 8);

        HTML myIdLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MyId</span></b>");
        myIdLabel.setTitle("Login with your MyId account");
        myIdLabel.setStyleName("menuBar");
        addOpenIdPopupLabelClickHandler(win, openIdUtil, myIdLabel, 8);

        Image myOpenIdImage = new Image(imageBaseUrl + "/images/openid/myopenid.png");
        myOpenIdImage.setStyleName("menuBar");
        myOpenIdImage.setTitle("Login with your MyOpenId account");
        addOpenIdPopupImageClickHandler(win, openIdUtil, myOpenIdImage, 3);

        HTML myOpenIdLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MyOpenId</span></b>");
        myOpenIdLabel.setTitle("Login with your MyOpenId account");
        myOpenIdLabel.setStyleName("menuBar");
        addOpenIdPopupLabelClickHandler(win, openIdUtil, myOpenIdLabel, 3);

        Image aolImage = new Image(imageBaseUrl + "/images/openid/aol.png");
        aolImage.setStyleName("menuBar");
        aolImage.setTitle("Login with your Aol account");
        addOpenIdPopupImageClickHandler(win, openIdUtil, aolImage, 10);

        HTML aolLabel = new HTML("&nbsp<b><span style='font-size:75%;'>Aol</span></b>");
        aolLabel.setTitle("Login with your Aol account");
        aolLabel.setStyleName("menuBar");
        addOpenIdPopupLabelClickHandler(win, openIdUtil, aolLabel, 10);

        Image myvidoopImage = new Image(imageBaseUrl + "/images/openid/myvidoop.png");
        myvidoopImage.setStyleName("menuBar");
        myvidoopImage.setTitle("Login with your MyVidoop account");
        addOpenIdPopupImageClickHandler(win, openIdUtil, myvidoopImage, 6);

        HTML myVidoopLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MyVidoop</span></b>");
        myVidoopLabel.setTitle("Login with your MyId account");
        myVidoopLabel.setStyleName("menuBar");
        addOpenIdPopupLabelClickHandler(win, openIdUtil, myVidoopLabel, 6);

        Image closeImage = new Image(imageBaseUrl + "/images/delete_grey.png");
        closeImage.setStyleName("menuBar");
        closeImage.setTitle("Close");

        closeImage.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                openIdPopup.hide();
            }
        });

        FlexTable popupFlex = new FlexTable();
        popupFlex.setWidget(0, 4, closeImage);
        popupFlex.setWidget(2, 0, flickrImage);
        popupFlex.setWidget(2, 1, flickrLabel);
        popupFlex.setWidget(2, 3, mySpaceImage);
        popupFlex.setWidget(2, 4, mySpaceLabel);
        popupFlex.setWidget(4, 0, myOpenIdImage);
        popupFlex.setWidget(4, 1, myOpenIdLabel);
        popupFlex.setWidget(4, 3, mydImage);
        popupFlex.setWidget(4, 4, myIdLabel);
        popupFlex.setWidget(6, 0, myvidoopImage);
        popupFlex.setWidget(6, 1, myVidoopLabel);
        popupFlex.setWidget(6, 3, aolImage);
        popupFlex.setWidget(6, 4, aolLabel);
        popupFlex.getFlexCellFormatter().setWidth(0, 2, "10px");
        popupFlex.getFlexCellFormatter().setHeight(1, 0, "10px");
        popupFlex.getFlexCellFormatter().setHeight(3, 0, "10px");
        popupFlex.getFlexCellFormatter().setHeight(5, 0, "10px");
        popupFlex.getFlexCellFormatter().setHorizontalAlignment(0, 4, HorizontalPanel.ALIGN_RIGHT);
        popupFlex.setWidget(2, 2, new HTML("&nbsp&nbsp"));
        openIdPopup.add(popupFlex);
    }

    /**
     * @param win
     * @param openIdUtil
     * @param openIdProvImage
     */
    protected void addFrontIconClickHandler(final Window win, final OpenIdUtil openIdUtil, Image openIdProvImage,
            final int providerId) {
        openIdProvImage.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                win.close();

                final LoginUtil loginUtil = new LoginUtil();
                if (!isLoginWithHttps) {
                    AdminServiceManager.getInstance().clearPreviousLoginAuthenticationData(
                            new clearLoginAuthDataHandler(providerId, loginUtil, isLoginWithHttps));

                } else {
                    openIdUtil.openProviderForOpenIdAuth(providerId, isLoginWithHttps);
                }
                if (windowCloseHandlerRegistration != null) {
                    windowCloseHandlerRegistration.removeHandler();
                }
                // Warning: this will only work for https if invoked after the call to windowCloseHandlerRegistration.removeHandler
                if (isLoginWithHttps) {
                    loginUtil.closeWindow();
                }
            }
        });
    }

    /**
     * @param win
     * @param openIdUtil
     * @param openIdProvImage
     */
    protected void addOpenIdPopupImageClickHandler(final Window win, final OpenIdUtil openIdUtil,
            Image openIdProvImage, final int providerId) {
        openIdProvImage.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                win.close();
                openIdPopup.hide();
                final LoginUtil loginUtil = new LoginUtil();
                if (!isLoginWithHttps) {
                    AdminServiceManager.getInstance().clearPreviousLoginAuthenticationData(
                            new clearLoginAuthDataHandler(providerId, loginUtil, isLoginWithHttps));

                } else { 
                    openIdUtil.openProviderForOpenIdAuth(providerId, isLoginWithHttps);
                }
                if (windowCloseHandlerRegistration != null) {
                    windowCloseHandlerRegistration.removeHandler();
                }
                // Warning: this will only work for https if invoked after the call to windowCloseHandlerRegistration.removeHandler
                if (isLoginWithHttps) {
                    loginUtil.closeWindow();
                }
            }
        });
    }

    /**
     * @param win
     * @param openIdUtil
     * @param openIdProvLabel
     * @param providerId
     */
    protected void addOpenIdPopupLabelClickHandler(final Window win, final OpenIdUtil openIdUtil,
            Label openIdProvLabel, final int providerId) {
        openIdProvLabel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                win.close();
                openIdPopup.hide();
                final LoginUtil loginUtil = new LoginUtil();
                if (!isLoginWithHttps) {
                    AdminServiceManager.getInstance().clearPreviousLoginAuthenticationData(
                            new clearLoginAuthDataHandler(providerId, loginUtil, isLoginWithHttps));

                } else {
                    openIdUtil.openProviderForOpenIdAuth(providerId, isLoginWithHttps);
                }
                if (windowCloseHandlerRegistration != null) {
                    windowCloseHandlerRegistration.removeHandler();
                }
                // Warning: this will only work for https if invoked after the call to windowCloseHandlerRegistration.removeHandler                                
                if (isLoginWithHttps) {
                    loginUtil.closeWindow();
                }
            }
        });
    }

    public static native void setWindowSize(String width, String height)/*-{
        $wnd.resizeTo(width, height);
    }-*/;

    class clearLoginAuthDataHandler extends AbstractAsyncHandler<Void> {
        private LoginUtil loginUtil;
        private int providerId;
        private boolean isLoginWithHttps;

        public clearLoginAuthDataHandler(int providerId, LoginUtil loginUtil, boolean isLoginWithHttps) {
            this.providerId = providerId;
            this.loginUtil = loginUtil;
            this.isLoginWithHttps = isLoginWithHttps;
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

        }

        @Override
        public void handleSuccess(Void result) {
            OpenIdUtil openIdUtil = new OpenIdUtil();
            openIdUtil.openProviderForOpenIdAuth(providerId, isLoginWithHttps);
            loginUtil.getTimeoutAndCheckUserLoggedInMethod(loginUtil, null);
            //loginUtil.checkUserLoggedInMethod(null);

        }

    }

    /**
     * @param windowCloseHandlerRegistration
     *            the windowCloseHandlerRegistration to set
     */
    public void setWindowCloseHandlerRegistration(HandlerRegistration windowCloseHandlerRegistration) {
        this.windowCloseHandlerRegistration = windowCloseHandlerRegistration;
    }

}

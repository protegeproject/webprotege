/**
 * 
 */
package edu.stanford.bmir.protege.web.client.ui.openid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OpenIdServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.OpenIdData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;

/**
 * Created Popup with providers icon, to associate new OpenId with User Account
 * 
 * @author z.khan
 * 
 */
class OpenIdAllIconPopup extends PopupPanel {

    protected String imageBaseUrl = null;

    public OpenIdAllIconPopup(FlexTable editProfileTable, Window win, int windowBaseHt) {
        super(true);
        imageBaseUrl = GWT.getModuleBaseURL();
        imageBaseUrl = imageBaseUrl.substring(0, imageBaseUrl.lastIndexOf('/'));
        imageBaseUrl = imageBaseUrl.substring(0, imageBaseUrl.lastIndexOf('/'));

        FlexTable popUpFlex = createOpenIdIconPopup(editProfileTable, win, windowBaseHt);

        setWidget(popUpFlex);
    }

    protected FlexTable createOpenIdIconPopup(final FlexTable editProfileTable, final Window win, final int windowBaseHt) {

        final OpenIdUtil openIdUtil = new OpenIdUtil();
        this.setAutoHideEnabled(true);
        DOM.setStyleAttribute(this.getElement(), "zIndex", "10000");

        FlexTable popupFlex = getWidgetOpenIdIconPopup(editProfileTable, win, windowBaseHt, openIdUtil);
        popupFlex.getFlexCellFormatter().setWidth(0, 2, "10px");
        popupFlex.getFlexCellFormatter().setHeight(1, 0, "10px");
        popupFlex.getFlexCellFormatter().setHeight(3, 0, "10px");
        popupFlex.getFlexCellFormatter().setHeight(5, 0, "10px");
        popupFlex.getFlexCellFormatter().setHeight(7, 0, "10px");
        popupFlex.getFlexCellFormatter().setHeight(9, 0, "10px");
        popupFlex.getFlexCellFormatter().setHorizontalAlignment(0, 4, HorizontalPanel.ALIGN_RIGHT);

        this.add(popupFlex);
        return popupFlex;
    }

    /**
     * Creates Open Id icon popup
     * 
     * @param editProfileTable
     * @param win
     * @param windowBaseHt
     * @param openIdUtil
     * @return
     */
    protected FlexTable getWidgetOpenIdIconPopup(final FlexTable editProfileTable, final Window win,
            final int windowBaseHt, final OpenIdUtil openIdUtil) {
        Image googleImage = new Image(imageBaseUrl + "/images/openid/google.ico");
        googleImage.setTitle("Login with your Google account");
        googleImage.setStyleName("menuBar");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, googleImage, 1);

        HTML googleLabel = new HTML("&nbsp<b><span style='font-size:75%;'>Google</span></b>");
        googleLabel.setStyleName("menuBar");
        googleLabel.setTitle("Login with your Google account");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, googleLabel, 1);

        Image yahooImage = new Image(imageBaseUrl + "/images/openid/yahoo.ico");
        yahooImage.setStyleName("menuBar");
        yahooImage.setTitle("Login with your Yahoo account");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, yahooImage, 2);

        HTML yahooLabel = new HTML("&nbsp<b><span style='font-size:75%;'>Yahoo</span></b>");
        yahooLabel.setTitle("Login with your Yahoo account");
        yahooLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, yahooLabel, 2);

        Image flickrImage = new Image(imageBaseUrl + "/images/openid/flickr.ico");
        flickrImage.setStyleName("menuBar");
        flickrImage.setTitle("Login with your Flickr account");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, flickrImage, 5);

        HTML flickrLabel = new HTML("&nbsp<b><span style='font-size:75%;'>Flickr</span></b>");
        flickrLabel.setTitle("Login with your Flickr account");
        flickrLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, flickrLabel, 5);

        Image myvidoopImage = new Image(imageBaseUrl + "/images/openid/vidoop.ico");
        myvidoopImage.setStyleName("menuBar");
        myvidoopImage.setTitle("Login with your MyVidoop account");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, myvidoopImage, 6);

        HTML myVidoopLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MyVidoop</span></b>");
        myVidoopLabel.setTitle("Login with your MyVidoop account");
        myVidoopLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, myVidoopLabel, 6);

        Image verisignImage = new Image(imageBaseUrl + "/images/openid/verisign.ico");
        verisignImage.setStyleName("menuBar");
        verisignImage.setTitle("Login with your Verisign account");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, verisignImage, 7);

        HTML verisignLabel = new HTML("&nbsp<b><span style='font-size:75%;'>Verisign</span></b>");
        verisignLabel.setTitle("Login with your Verisign account");
        verisignLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, verisignLabel, 7);

        Image mydImage = new Image(imageBaseUrl + "/images/openid/myid.jpg");
        mydImage.setStyleName("menuBar");
        mydImage.setHeight("16px");
        mydImage.setTitle("Login with your MyId account");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, mydImage, 8);

        HTML myIdLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MyId</span></b>");
        myIdLabel.setTitle("Login with your MyId account");
        myIdLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, myIdLabel, 8);

        Image myOpenIdImage = new Image(imageBaseUrl + "/images/openid/myopenid.ico");
        myOpenIdImage.setStyleName("menuBar");
        myOpenIdImage.setTitle("Login with your MyOpenId account");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, myOpenIdImage, 3);

        HTML myOpenIdLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MyOpenId</span></b>");
        myOpenIdLabel.setTitle("Login with your MyOpenId account");
        myOpenIdLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, myOpenIdLabel, 3);

        Image aolImage = new Image(imageBaseUrl + "/images/openid/aol.ico");
        aolImage.setStyleName("menuBar");
        aolImage.setTitle("Login with your Aol account");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, aolImage, 10);

        HTML aolLabel = new HTML("&nbsp<b><span style='font-size:75%;'>Aol</span></b>");
        aolLabel.setTitle("Login with your Aol account");
        aolLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, aolLabel, 10);

        Image mySpaceImage = new Image(imageBaseUrl + "/images/openid/myspace.jpg");
        mySpaceImage.setHeight("16px");
        mySpaceImage.setTitle("Login with your MySpace account");
        mySpaceImage.setStyleName("menuBar");
        addOpenIdAllIconImageClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, mySpaceImage, 4);

        HTML mySpaceLabel = new HTML("&nbsp<b><span style='font-size:75%;'>MySpace</span></b>");
        mySpaceLabel.setTitle("Login with your MySpace account");
        mySpaceLabel.setStyleName("menuBar");
        addOpenIdAllIconLabClickHandler(editProfileTable, win, windowBaseHt, openIdUtil, mySpaceLabel, 4);

        Image closeImage = new Image(imageBaseUrl + "/images/delete_grey.png");
        closeImage.setStyleName("menuBar");
        closeImage.setTitle("Close");

        closeImage.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                hidePopup();
            }
        });

        FlexTable popupFlex = new FlexTable();
        popupFlex.setWidget(0, 4, closeImage);
        popupFlex.setWidget(2, 0, googleImage);
        popupFlex.setWidget(2, 1, googleLabel);
        popupFlex.setWidget(2, 3, yahooImage);
        popupFlex.setWidget(2, 4, yahooLabel);
        popupFlex.setWidget(4, 0, flickrImage);
        popupFlex.setWidget(4, 1, flickrLabel);
        popupFlex.setWidget(4, 3, myvidoopImage);
        popupFlex.setWidget(4, 4, myVidoopLabel);
        popupFlex.setWidget(6, 0, verisignImage);
        popupFlex.setWidget(6, 1, verisignLabel);
        popupFlex.setWidget(6, 3, mydImage);
        popupFlex.setWidget(6, 4, myIdLabel);
        popupFlex.setWidget(8, 0, myOpenIdImage);
        popupFlex.setWidget(8, 1, myOpenIdLabel);
        popupFlex.setWidget(8, 3, aolImage);
        popupFlex.setWidget(8, 4, aolLabel);
        popupFlex.setWidget(10, 0, mySpaceImage);
        popupFlex.setWidget(10, 1, mySpaceLabel);
        popupFlex.setWidget(2, 2, new HTML("&nbsp&nbsp"));
        return popupFlex;
    }

    protected void hidePopup() {
        this.hide();
    }

    protected void isOpenIdInSessForAddNewOpenId(final FlexTable editProfileTable, final Window win,
            final int windowBaseHt) {
        final long initTime = System.currentTimeMillis();
        final Timer addOpenIdTimer = new Timer() {// checks if the user has logged
            // in using open id
            public void run() {
                final Timer timer = this;
                long curTime = System.currentTimeMillis();
                if (curTime - initTime > 900000) {//15min
                    timer.cancel();
                }
                OpenIdServiceManager.getInstance().isOpenIdInSessForAddNewOpenId(
                        new AsyncCallback<UserData>() {

                            public void onSuccess(UserData userData) {
                                if (userData != null) {//open id URL attribute is present HttpSession 
                                    if (userData.getName() != null) { //open id URL is already associated
                                        MessageBox.alert("Open Id already associated with WebProtege user '"
                                                + userData.getName() + "'.");
                                        timer.cancel();
                                    } else { // associate open id to current user
                                        String name = GlobalSettings.getGlobalSettings().getUserName();

                                        OpenIdServiceManager.getInstance().assocNewOpenIdToUser(name,
                                                new AsyncCallback<OpenIdData>() {

                                                    public void onSuccess(OpenIdData result) {
                                                        OpenIdUtil opIdUtil = new OpenIdUtil();
                                                        opIdUtil.displayUsersOpenIdList(result, editProfileTable, win,
                                                                true, windowBaseHt);
                                                    }

                                                    public void onFailure(Throwable caught) {

                                                    }
                                                });
                                        timer.cancel();
                                    }
                                }

                            }

                            public void onFailure(Throwable caught) {
                                timer.cancel();
                            }
                        });
            }
        };
        addOpenIdTimer.scheduleRepeating(2000);
    }

    /**
     * @param editProfileTable
     * @param win
     * @param windowBaseHt
     * @param openIdUtil
     * @param openIdAllIconPopupImage
     * @param providerId
     */
    protected void addOpenIdAllIconImageClickHandler(final FlexTable editProfileTable, final Window win,
            final int windowBaseHt, final OpenIdUtil openIdUtil, Image openIdAllIconPopupImage, final int providerId) {
        openIdAllIconPopupImage.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                hidePopup();
                AdminServiceManager.getInstance().clearPreviousLoginAuthenticationData(new clearLoginAuthDataHandler(providerId,  false));
                isOpenIdInSessForAddNewOpenId(editProfileTable, win, windowBaseHt);
            }
        });
    }

    /**
     * @param editProfileTable
     * @param win
     * @param windowBaseHt
     * @param openIdUtil
     * @param openIdAllIconPopupLabel
     * @param providerId
     */
    protected void addOpenIdAllIconLabClickHandler(final FlexTable editProfileTable, final Window win,
            final int windowBaseHt, final OpenIdUtil openIdUtil, Label openIdAllIconPopupLabel, final int providerId) {
        openIdAllIconPopupLabel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                hidePopup();
                AdminServiceManager.getInstance().clearPreviousLoginAuthenticationData(new clearLoginAuthDataHandler(providerId, false));
                isOpenIdInSessForAddNewOpenId(editProfileTable, win, windowBaseHt);
            }
        });
    }
    
    class clearLoginAuthDataHandler extends AbstractAsyncHandler<Void> {
        private int providerId;
        private boolean isLoginWithHttps;

        public clearLoginAuthDataHandler( int providerId,  boolean isLoginWithHttps) {
            this.providerId = providerId;
            this.isLoginWithHttps = isLoginWithHttps;
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);

        }

        @Override
        public void handleSuccess(Void result) {
            OpenIdUtil openIdUtil = new OpenIdUtil();
        openIdUtil.openProviderForOpenIdAuth(new Integer(providerId),isLoginWithHttps);

        }

    }
}
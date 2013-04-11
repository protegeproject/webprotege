package edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.model.ShareOntologyAccessEventManager;
import edu.stanford.bmir.protege.web.client.rpc.AccessPolicyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.AccessPolicyUserData;
import edu.stanford.bmir.protege.web.client.ui.constants.OntologyShareAccessConstants;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.*;

/**
 * Manages Access Privileges for Ontologies
 *
 * @author z.khan
 *
 */
public class AccessPolicyUtil {
//    protected AcessListScrollPanel readOnlyPermlistPanel = null;
//    protected AcessListScrollPanel writePermlistPanel = null;
//
//    protected ProjectId currentSelectedProject = null;
//
//    protected AccessPolicyVerticalPanel mainPanel;
//    protected Window addUserReadOnlyPermWindow;
//    protected Window addUserWritePermWindow;
//    protected Window shareAccessWindow;
//
//    protected HorizontalPanel buttonPanel;
//
//    protected Collection<AccessPolicyUserData> currentListForReadOnlyAccess;
//    protected Collection<AccessPolicyUserData> currentListForWriteAccess;
//
//    protected Collection<AccessPolicyUserData> removeListForReadOnlyAccess;
//    protected Collection<AccessPolicyUserData> removeListForWriteAccess;
//
//    protected Collection<AccessPolicyUserData> addListForReadOnlyAccess;
//    protected Collection<AccessPolicyUserData> addListForWriteAccess;
//
//    int updationCount = 0;
//    int actualUpdationPerformed = 0;
//    int UpdationErrorOccured = 0;
//
//    boolean isAdditionalPolicyPresent = true;
//
//    /**
//     * Constructor
//     */
//    private AccessPolicyUtil() {
//
//        // TODO: This is really dodgy  WTF?  WHY ARE
//        EventBusManager.getManager().registerHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
//            @Override
//            public void handleUserLoggedOut(UserLoggedOutEvent event) {
//                if (shareAccessWindow != null) {
//                    shareAccessWindow.hide();
//                }
//            }
//        });
//
//    }
//
//    /**
//     *
//     */
//    private void initalizeAccessLists() {
//        currentListForReadOnlyAccess = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//        currentListForWriteAccess = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//
//        removeListForReadOnlyAccess = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//        removeListForWriteAccess = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//
//        addListForReadOnlyAccess = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//        addListForWriteAccess = new TreeSet<AccessPolicyUserData>(new AccessPolicyUserData());
//    }
//
//    public void displayShareAccessWindow(ProjectId currentSelectedProject) {
//        initalizeAccessLists();
//        if (currentSelectedProject == null) {
//            return;
//        }
//        this.currentSelectedProject = currentSelectedProject;
//
//        shareAccessWindow = new Window();
//
//        shareAccessWindow.setTitle("Share your Ontology and Access Privileges");
//        shareAccessWindow.setClosable(true);
//        shareAccessWindow.setPaddings(7);
//        shareAccessWindow.setCloseAction(Window.CLOSE);
//
//        mainPanel = new AccessPolicyVerticalPanel();
//        mainPanel.getElement().setId("mainpanelid");
//        mainPanel.getElement().setAttribute("align", "center");
//        shareAccessWindow.add(mainPanel);
//        Element vPanelElement = mainPanel.getElement();
//
//        checkIfAdditionalPolicyPresent(vPanelElement);
//
//    }
//
//    private void checkIfAdditionalPolicyPresent(final Element vPanelElement) {
//        if (currentSelectedProject == null) {
//            return;
//        }
//        AccessPolicyServiceManager.getInstance().checkIfAdditionalPolicyPresent(currentSelectedProject,
//                new AsyncCallback<Boolean>() {
//
//                    public void onSuccess(Boolean result) {
//                        isAdditionalPolicyPresent = result;
//                        createReadOnlyListGUI(vPanelElement);
//
//                        createWriteListGUI(vPanelElement);
//
//                        createAndAddOkAndCancelButton();
//
//                        createAndAddInvitationLink(vPanelElement);
//
//                        showMessageIfAdditionalPolicyPresent(vPanelElement);
//
//                        addHandlerToMainPanel();
//
//                        shareAccessWindow.show();
//                        shareAccessWindow.center();
//
//                        updateScrollPanelOnResize();
//                        addBorderAndColorToScrollPanel();
//                    }
//
//                    private void showMessageIfAdditionalPolicyPresent(Element vPanelElement) {
//                        if (isAdditionalPolicyPresent) {
//                            Element additionalPolicymessageElement = DOM.createElement("div");
//                            additionalPolicymessageElement
//                                    .setInnerHTML("<br><b>This project is currently controlled by another <br>policy manager. Please contact the administrator <br>to make changes to the access policies.</b><br><br>");
//                            DOM.appendChild(vPanelElement, additionalPolicymessageElement);
//                        }
//                    }
//
//                    public void onFailure(Throwable caught) {
//                        MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//                    }
//                });
//
//    }
//
//    private void updateScrollPanelOnResize() {
//        shareAccessWindow.addListener(new PanelListenerAdapter() {
//
//            /*
//             * (non-Javadoc)
//             *
//             * @see
//             * com.gwtext.client.widgets.event.PanelListenerAdapter#onBodyResize
//             * (com.gwtext.client.widgets.Panel, java.lang.String,
//             * java.lang.String)
//             */
//            @Override
//            public void onBodyResize(Panel panel, String width, String height) {
//                Element readOnlyListPanelElement = readOnlyPermlistPanel.getElement();
//                Element writeListPanelElement = writePermlistPanel.getElement();
//                ExtElement winBody = shareAccessWindow.getBody();
//                int heightChange = winBody.getHeight(true)
//                        - (mainPanel.getElement().getOffsetHeight() + buttonPanel.getOffsetHeight());
//                if (heightChange != 0) {
//                    int newReadPanelHeight, newWritePanelHeight;
//
//                    newReadPanelHeight = new Integer(readOnlyListPanelElement.getOffsetHeight()) + (heightChange / 2);
//                    newWritePanelHeight = new Integer(writeListPanelElement.getOffsetHeight()) + (heightChange / 2);
//
//                    newReadPanelHeight = (newReadPanelHeight < OntologyShareAccessConstants.SHARE_ACCESS_SCROLL_PANEL_MIN_HEIGHT) ? OntologyShareAccessConstants.SHARE_ACCESS_SCROLL_PANEL_MIN_HEIGHT
//                            : newReadPanelHeight;
//                    newWritePanelHeight = (newWritePanelHeight < OntologyShareAccessConstants.SHARE_ACCESS_SCROLL_PANEL_MIN_HEIGHT) ? OntologyShareAccessConstants.SHARE_ACCESS_SCROLL_PANEL_MIN_HEIGHT
//                            : newWritePanelHeight;
//
//                    readOnlyListPanelElement.getStyle().setHeight(new Double(newReadPanelHeight), Style.Unit.PX);
//                    writeListPanelElement.getStyle().setHeight(new Double(newWritePanelHeight), Style.Unit.PX);
//                }
//            }
//        });
//    }
//
//    /**
//     * Format scroll panel
//     */
//    private void addBorderAndColorToScrollPanel() {
//        Element readOnlyListPanelElement = readOnlyPermlistPanel.getElement();
//        readOnlyListPanelElement.getStyle().setBorderColor("black");
//        readOnlyListPanelElement.getStyle().setBorderStyle(Style.BorderStyle.SOLID);
//        readOnlyListPanelElement.getStyle().setBorderWidth(new Double("2"), Style.Unit.PX);
//        readOnlyListPanelElement.getStyle().setBackgroundColor("white");
//
//        Element writeListPanelElement = writePermlistPanel.getElement();
//        writeListPanelElement.getStyle().setBorderColor("black");
//        writeListPanelElement.getStyle().setBorderStyle(Style.BorderStyle.SOLID);
//        writeListPanelElement.getStyle().setBorderWidth(new Double("2"), Style.Unit.PX);
//        writeListPanelElement.getStyle().setBackgroundColor("white");
//    }
//
//    /**
//     * Implements the list for users with read access.
//     *
//     * @param vPanelElement
//     */
//    protected void createReadOnlyListGUI(Element vPanelElement) {
//        Element readOnlyAccessHeadingElement = DOM.createElement("div");
//        readOnlyAccessHeadingElement.setInnerHTML("<b>Ontology is shared with read-only access with:</b><br><br>");
//        DOM.appendChild(vPanelElement, readOnlyAccessHeadingElement);
//
//        readOnlyPermlistPanel = new AcessListScrollPanel();
//        readOnlyPermlistPanel.setSize("315px", OntologyShareAccessConstants.SHARE_ACCESS_SCROLL_PANEL_MIN_HEIGHT + "px");
//        Element readOnlyListPanelElement = readOnlyPermlistPanel.getElement();
//
//        updateReadOnlyAccessList();
//        addReadOnlyPermRemoveLinkHandler();
//
//        DOM.appendChild(vPanelElement, readOnlyListPanelElement);
//
//        String addUserimageHtml = "<img id ='" + OntologyShareAccessConstants.ADD_USER_WITH_READ_ONLY_PERMISSION_IMAGE_ID
//                + "' src='" + GWT.getHostPageBaseURL() + "images/shareaccess/user_add.png'/>";
//
//        if (!isAdditionalPolicyPresent) {
//            Element addUserWithReadOnlyPermElement = DOM.createElement("div");
//            addUserWithReadOnlyPermElement.setInnerHTML("<br><a href='javascript:;'>" + addUserimageHtml + "<b><span id='"
//                    + OntologyShareAccessConstants.ADD_USER_WITH_READ_ONLY_PERMISSION_LINK_ID
//                    + "' style='font-size:100%; text-decoration:underline;'>" + " Add user with read-only permission"
//                    + "</span></b></a><br><br>");
//            DOM.appendChild(vPanelElement, addUserWithReadOnlyPermElement);
//        }
//
//        Element separatorElement = DOM.createElement("hr");
//        DOM.appendChild(vPanelElement, separatorElement);
//    }
//
//    /**
//     * Implements the list for users with write access.
//     *
//     * @param vPanelElement
//     */
//    protected void createWriteListGUI(Element vPanelElement) {
//        Element writeAccessHeadingElement = DOM.createElement("div");
//        writeAccessHeadingElement.setInnerHTML("<b>Ontology is shared with write access with:</b><br><br>");
//        DOM.appendChild(vPanelElement, writeAccessHeadingElement);
//
//        writePermlistPanel = new AcessListScrollPanel();
//        writePermlistPanel.setSize("315px", OntologyShareAccessConstants.SHARE_ACCESS_SCROLL_PANEL_MIN_HEIGHT + "px");
//        Element writeListPanelElement = writePermlistPanel.getElement();
//
//        updateWriteAccessList();
//        addWritePermRemoveLinkHandler();
//
//        DOM.appendChild(vPanelElement, writeListPanelElement);
//
//        String addUserimageHtml = "<img id ='" + OntologyShareAccessConstants.ADD_USER_WITH_WRITE_PERMISSION_IMAGE_ID + "' src='"
//                + GWT.getHostPageBaseURL() + "images/shareaccess/user_add.png'/>";
//
//        if (!isAdditionalPolicyPresent) {
//            Element addUserWithWritePermElement = DOM.createElement("div");
//            addUserWithWritePermElement.setInnerHTML("<br><a href='javascript:;'>" + addUserimageHtml + "<b><span id='"
//                    + OntologyShareAccessConstants.ADD_USER_WITH_WRITE_PERMISSION_LINK_ID
//                    + "' style='font-size:100%; text-decoration:underline;'>" + " Add user with write permission"
//                    + "</span></b></a><br><br>");
//            DOM.appendChild(vPanelElement, addUserWithWritePermElement);
//        }
//    }
//
//    /**
//     * Creates and adds buttons to share access window
//     */
//    private void createAndAddOkAndCancelButton() {
//        Button okButton = new Button("Ok");
//        Button cancelButton = new Button("Cancel");
//        okButton.setTitle("Save changes");
//        cancelButton.setTitle("Cancel changes");
//
//        buttonPanel = new HorizontalPanel();
//        buttonPanel.setSpacing(10);
//        buttonPanel.add(okButton);
//        buttonPanel.add(cancelButton);
//        buttonPanel.getElement().setAttribute("align", "center");
//        shareAccessWindow.add(buttonPanel);
//
//        okButton.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//
//                actualUpdationPerformed = 0;
//                UpdationErrorOccured = 0;
//                updationCount = 0;
//                if (addListForReadOnlyAccess.size() > 0) {
//                    List<String> addList = getStringListFromCollection(addListForReadOnlyAccess);
//                    AccessPolicyServiceManager.getInstance().addReadPermission(currentSelectedProject, addList,
//                            new AsyncCallback<Void>() {
//
//                                public void onSuccess(Void result) {
//                                    addListForReadOnlyAccess.clear();
//                                    isAccessPrivilegeUpdated(true, false);
//                                }
//
//                                public void onFailure(Throwable caught) {
//                                    isAccessPrivilegeUpdated(false, true);
//                                }
//                            });
//                } else {
//                    isAccessPrivilegeUpdated(false, false);
//                }
//
//                if (removeListForReadOnlyAccess.size() > 0) {
//                    List<String> removeList = getStringListFromCollection(removeListForReadOnlyAccess);
//                    AccessPolicyServiceManager.getInstance().removeReadPermission(currentSelectedProject, removeList,
//                            new AsyncCallback<Void>() {
//
//                                public void onSuccess(Void result) {
//                                    removeListForReadOnlyAccess.clear();
//                                    isAccessPrivilegeUpdated(true, false);
//                                }
//
//                                public void onFailure(Throwable caught) {
//                                    isAccessPrivilegeUpdated(false, true);
//                                }
//                            });
//                } else {
//                    isAccessPrivilegeUpdated(false, false);
//                }
//
//                if (addListForWriteAccess.size() > 0) {
//                    List<String> addList = getStringListFromCollection(addListForWriteAccess);
//                    AccessPolicyServiceManager.getInstance().addWritePermission(currentSelectedProject, addList,
//                            new AsyncCallback<Void>() {
//
//                                public void onSuccess(Void result) {
//                                    addListForWriteAccess.clear();
//                                    isAccessPrivilegeUpdated(true, false);
//                                }
//
//                                public void onFailure(Throwable caught) {
//                                    isAccessPrivilegeUpdated(false, true);
//                                }
//                            });
//                } else {
//                    isAccessPrivilegeUpdated(false, false);
//                }
//
//                if (removeListForWriteAccess.size() > 0) {
//                    List<String> removeList = getStringListFromCollection(removeListForWriteAccess);
//                    AccessPolicyServiceManager.getInstance().removeWritePermission(currentSelectedProject, removeList,
//                            new AsyncCallback<Void>() {
//
//                                public void onSuccess(Void result) {
//                                    removeListForWriteAccess.clear();
//                                    isAccessPrivilegeUpdated(true, false);
//                                }
//
//                                public void onFailure(Throwable caught) {
//                                    isAccessPrivilegeUpdated(false, true);
//                                }
//                            });
//                } else {
//                    isAccessPrivilegeUpdated(false, false);
//                }
//            }
//        });
//        cancelButton.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                shareAccessWindow.close();
//            }
//        });
//    }
//
//    protected void createAndAddInvitationLink(Element vPanelElement) {
//
//        Element addUserWithWritePermElement = DOM.createElement("div");
//        addUserWithWritePermElement.setInnerHTML("<br><a href='javascript:;'><b><span id='"
//                + OntologyShareAccessConstants.SEND_INVITATION_LINK_ID + "' style='font-size:100%; text-decoration:underline;'>"
//                + " Share with someone else" + "</span></b></a><br><br>");
//        DOM.appendChild(vPanelElement, addUserWithWritePermElement);
//
//    }
//
//    /**
//     * @param isActualUpdationPerformed
//     */
//    private void isAccessPrivilegeUpdated(boolean isActualUpdationPerformed, boolean isUpdationErrorOccured) {
//        if (isActualUpdationPerformed) {
//            actualUpdationPerformed++;
//        }
//
//        if (isUpdationErrorOccured) {
//            UpdationErrorOccured++;
//        }
//        updationCount++;
//        if (updationCount == 4) {
//            updationCount = 0;
//            actualUpdationPerformed = 0;
//            if (UpdationErrorOccured > 0) {
//                UpdationErrorOccured = 0;
//                MessageBox.alert("Error occured while updating access privileges.");
//            } else {
//                shareAccessWindow.close();
//            }
//        }
//    }
//
//    protected List<String> getStringListFromCollection(Collection<AccessPolicyUserData> collectionList) {
//        List<String> stringList = new ArrayList<String>();
//        for (Iterator iterator = collectionList.iterator(); iterator.hasNext();) {
//            AccessPolicyUserData userData = (AccessPolicyUserData) iterator.next();
//            stringList.add(userData.getName());
//        }
//        return stringList;
//    }
//
//    private void addHandlerToMainPanel() {
//        mainPanel.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                NativeEvent nativeEvent = event.getNativeEvent();
//                EventTarget eventTarget = nativeEvent.getEventTarget();
//                if (Element.is(eventTarget)) {
//                    Element clickedElement = eventTarget.cast();
//                    if (clickedElement.getId().trim().equals(
//                            OntologyShareAccessConstants.ADD_USER_WITH_READ_ONLY_PERMISSION_LINK_ID)
//                            || clickedElement.getId().trim().equals(
//                            OntologyShareAccessConstants.ADD_USER_WITH_READ_ONLY_PERMISSION_IMAGE_ID)) {
//                        displayUsersToAddReadOnlyPermWindow();
//                    } else if (clickedElement.getId().trim().equals(
//                            OntologyShareAccessConstants.ADD_USER_WITH_WRITE_PERMISSION_LINK_ID)
//                            || clickedElement.getId().trim().equals(
//                            OntologyShareAccessConstants.ADD_USER_WITH_WRITE_PERMISSION_IMAGE_ID)) {
//                        displayUsersToAddWritePermWindow();
//                    } else if (clickedElement.getId().trim().equals(OntologyShareAccessConstants.SEND_INVITATION_LINK_ID)) {
//                        InviteUserUtil inviteUserUtil = new InviteUserUtil();
//                        inviteUserUtil.displayInvitationWindow(currentSelectedProject);
//
//                    }
//                }
//            }
//        });
//    }
//
//    protected void updateReadOnlyAccessList() {
//
//        AccessPolicyServiceManager.getInstance().getUsersWithReadOnlyAccess(currentSelectedProject,
//                new AsyncCallback<Collection<AccessPolicyUserData>>() {
//
//                    public void onSuccess(Collection<AccessPolicyUserData> readOnlyAccessCollection) {
//                        currentListForReadOnlyAccess = readOnlyAccessCollection;
//                        updateReadOnlyAccessList(currentListForReadOnlyAccess);
//
//                    }
//
//                    public void onFailure(Throwable caught) {
//                        MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//
//                    }
//
//                });
//    }
//
//    /**
//     * @param readOnlyAccessUsers
//     */
//    protected void updateReadOnlyAccessList(Collection<AccessPolicyUserData> readOnlyAccessUsers) {
//        Element readOnlyListPanelElement = readOnlyPermlistPanel.getElement();
//        Element oldChildElement = DOM.getChild(readOnlyListPanelElement, 0);
//        while (oldChildElement != null) {
//            DOM.removeChild(readOnlyListPanelElement, oldChildElement);
//            oldChildElement = DOM.getChild(readOnlyListPanelElement, 0);
//        }
//        String innerHtml = "";
//        if (readOnlyAccessUsers != null && readOnlyAccessUsers.size() == 0) {
//            innerHtml = "<spam>There is no user with Read-Only Permission. You can add one by clicking on the <b>Add user with read-only permission</b> link.</spam>";
//        }
//
//        for (Iterator iterator = readOnlyAccessUsers.iterator(); iterator.hasNext();) {
//            AccessPolicyUserData userData = (AccessPolicyUserData) iterator.next();
//            String userName = userData.getName();
//            String removeUserimageHtml = "<img id= '" + OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_IMAGE_ID_PREFIX
//                    + userName + "' src='" + GWT.getHostPageBaseURL() + "images/shareaccess/user_delete.png'/>";
//            if (userData.isPartofReaders() && !isAdditionalPolicyPresent) {
//                innerHtml += "<spam' style='font-size:110%;line-height:180%;'>&nbsp;" + userName + "&nbsp;( </spam><a href='javascript:;'>" + removeUserimageHtml
//                        + "</a><a href='javascript:;'><b><span id='"
//                        + OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_LINK_ID_PREFIX + userName
//                        + "' style='font-size:100%; text-decoration:underline;line-height:180%;'>" + " remove"
//                        + "</span></b></a><spam' style='font-size:110%;line-height:180%;'> )</spam><br>";
//            } else {
//                innerHtml += "<spam style='font-size:110%;line-height:180%;' >&nbsp;" + userName + "</spam><br>";
//            }
//        }
//        Element list = DOM.createElement("div");
//        list.setInnerHTML(innerHtml);
//
//        DOM.appendChild(readOnlyListPanelElement, list);
//    }
//
//    private void addReadOnlyPermRemoveLinkHandler() {
//        readOnlyPermlistPanel.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                NativeEvent nativeEvent = event.getNativeEvent();
//                EventTarget eventTarget = nativeEvent.getEventTarget();
//                if (Element.is(eventTarget)) {
//                    Element clickedElement = eventTarget.cast();
//                    if (clickedElement.getId().startsWith(OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_IMAGE_ID_PREFIX)
//                            || clickedElement.getId().startsWith(OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_LINK_ID_PREFIX)) {
//
//                        String toRemoveUser = clickedElement.getId().replace(
//                                OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_IMAGE_ID_PREFIX, "").replace(
//                                OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_LINK_ID_PREFIX, "");
//
//                        AccessPolicyUserData userDataToRemove = getUserDataFromListWithName(currentListForReadOnlyAccess,
//                                toRemoveUser);
//                        if (userDataToRemove != null) {
//                            removeListForReadOnlyAccess.add(userDataToRemove);
//                            addListForReadOnlyAccess.remove(userDataToRemove);
//                            currentListForReadOnlyAccess.remove(userDataToRemove);
//                        }
//                        updateReadOnlyAccessList(currentListForReadOnlyAccess);
//
//                    }
//                }
//            }
//        });
//    }
//
//    protected AccessPolicyUserData getUserDataFromListWithName(Collection<AccessPolicyUserData> readOnlyAccessList,
//                                                               String toRemoveUser) {
//        for (Iterator iterator = readOnlyAccessList.iterator(); iterator.hasNext();) {
//            AccessPolicyUserData userData = (AccessPolicyUserData) iterator.next();
//            if (userData.getName().equals(toRemoveUser)) {
//                return userData;
//            }
//        }
//        return null;
//    }
//
//    protected void updateWriteAccessList() {
//
//        AccessPolicyServiceManager.getInstance().getUsersWithWriteAccess(currentSelectedProject,
//                new AsyncCallback<Collection<AccessPolicyUserData>>() {
//
//                    public void onSuccess(Collection<AccessPolicyUserData> writeAccessCollection) {
//                        currentListForWriteAccess = writeAccessCollection;
//                        updateWriteAccessList(currentListForWriteAccess);
//                    }
//
//                    public void onFailure(Throwable caught) {
//                        MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//
//                    }
//                });
//
//    }
//
//    /**
//     * @param writeAccessUsers
//     */
//    protected void updateWriteAccessList(Collection<AccessPolicyUserData> writeAccessUsers) {
//        Element writeListPanelElement = writePermlistPanel.getElement();
//        Element oldChildElement = DOM.getChild(writeListPanelElement, 0);
//        while (oldChildElement != null) {
//            DOM.removeChild(writeListPanelElement, oldChildElement);
//            oldChildElement = DOM.getChild(writeListPanelElement, 0);
//        }
//        String innerHtml = "";
//        if (writeAccessUsers != null && writeAccessUsers.size() == 0) {
//            innerHtml = "<spam>There is no user with Write Permission. You can add one by clicking on the <b> Add user with write permission</b> link.</spam>";
//        }
//        for (Iterator iterator = writeAccessUsers.iterator(); iterator.hasNext();) {
//            AccessPolicyUserData userData = (AccessPolicyUserData) iterator.next();
//            String userName = userData.getName();
//            String removeUserimageHtml = "<img id= '" + OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_IMAGE_ID_PREFIX
//                    + userName + "' src='" + GWT.getHostPageBaseURL() + "images/shareaccess/user_delete.png'/>";
//            if (userData.isPartofWriters() && !isAdditionalPolicyPresent) {
//                innerHtml += "<spam style='font-size:110%;line-height:180%;'>&nbsp;" + userName + "&nbsp;(</spam><a href='javascript:;'>" + removeUserimageHtml
//                        + "</a><a href='javascript:;'><b><span id='"
//                        + OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_LINK_ID_PREFIX + userName
//                        + "' style='font-size:100%; text-decoration:underline;line-height:180%;'>" + " remove"
//                        + "</span></b></a><spam 'style='font-size:110%;line-height:180%;'> )</spam><br>";
//            } else {
//                innerHtml += "<spam style='font-size:110%;line-height:180%;' >&nbsp;" + userName + "</spam><br>";
//            }
//        }
//
//        Element list = DOM.createElement("div");
//        list.setInnerHTML(innerHtml);
//
//        DOM.appendChild(writeListPanelElement, list);
//    }
//
//    private void addWritePermRemoveLinkHandler() {
//        writePermlistPanel.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                NativeEvent nativeEvent = event.getNativeEvent();
//                EventTarget eventTarget = nativeEvent.getEventTarget();
//                if (Element.is(eventTarget)) {
//                    Element clickedElement = eventTarget.cast();
//                    if (clickedElement.getId().startsWith(OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_IMAGE_ID_PREFIX)
//                            || clickedElement.getId().startsWith(OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_LINK_ID_PREFIX)) {
//                        String toRemoveUser = clickedElement.getId().replace(
//                                OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_IMAGE_ID_PREFIX, "").replace(
//                                OntologyShareAccessConstants.SHARE_ACCESS_REMOVE_LINK_ID_PREFIX, "");
//
//                        AccessPolicyUserData userDataToRemove = getUserDataFromListWithName(currentListForWriteAccess,
//                                toRemoveUser);
//
//                        if (userDataToRemove != null) {
//                            removeListForWriteAccess.add(userDataToRemove);
//                            addListForWriteAccess.remove(userDataToRemove);
//                            currentListForWriteAccess.remove(userDataToRemove);
//                        }
//                        updateWriteAccessList(currentListForWriteAccess);
//                    }
//                }
//            }
//        });
//    }
//
//    public class AccessPolicyVerticalPanel extends VerticalPanel implements HasClickHandlers {
//        public AccessPolicyVerticalPanel() {
//            super();
//            onAttach();
//        }
//
//        public HandlerRegistration addClickHandler(ClickHandler handler) {
//            return addDomHandler(handler, ClickEvent.getType());
//        }
//    }
//
//    public class AcessListScrollPanel extends ScrollPanel implements HasClickHandlers {
//        public AcessListScrollPanel() {
//            super();
//            onAttach();
//        }
//
//        public HandlerRegistration addClickHandler(ClickHandler handler) {
//            return addDomHandler(handler, ClickEvent.getType());
//        }
//    }
//
//    public void displayUsersToAddReadOnlyPermWindow() {
//        if (currentSelectedProject == null) {
//            return;
//        }
//        addUserReadOnlyPermWindow = new Window();
//        addUserReadOnlyPermWindow.setTitle("Add Users with read-only permission");
//        addUserReadOnlyPermWindow.setClosable(true);
//        addUserReadOnlyPermWindow.setClosable(true);
//        addUserReadOnlyPermWindow.setPaddings(7);
//        addUserReadOnlyPermWindow.setCloseAction(Window.HIDE);
//        displayListToAddUsersWithReadPermission();
//
//    }
//
//    private void addUsersWithReadPermissionHandler(Button addUser, Button cancelButton, final ListBox userListBox) {
//        addUser.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                List<String> selectedUsersList = new ArrayList<String>();
//                for (int i = 0; i < userListBox.getItemCount(); i++) {
//                    if (userListBox.isItemSelected(i)) {
//                        selectedUsersList.add(userListBox.getValue(i));
//                    }
//                }
//                if (selectedUsersList.size() > 0) {
//                    AccessPolicyUserData userEveryBody = new AccessPolicyUserData();
//                    userEveryBody.setName(OntologyShareAccessConstants.USER_EVERYBODY_NAME);
//                    currentListForReadOnlyAccess.remove(userEveryBody); // removes user "<everybody>"if present
//
//                    for (Iterator iterator = selectedUsersList.iterator(); iterator.hasNext();) {
//                        String toAddUserName = (String) iterator.next();
//                        AccessPolicyUserData toAddUserData = new AccessPolicyUserData();
//                        toAddUserData.setName(toAddUserName);
//                        toAddUserData.setPartofReaders(true);
//                        if (!addListForReadOnlyAccess.contains(toAddUserData)) {
//                            addListForReadOnlyAccess.add(toAddUserData);
//                        }
//                        removeListForReadOnlyAccess.remove(toAddUserData); // remove the users to add from remove list if present.
//                        if (!currentListForReadOnlyAccess.contains(toAddUserData)) {
//                            currentListForReadOnlyAccess.add(toAddUserData);
//                        }
//                    }
//
//                    addUserReadOnlyPermWindow.close();
//                    updateReadOnlyAccessList(currentListForReadOnlyAccess);
//
//                }
//            }
//        });
//
//        cancelButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                addUserReadOnlyPermWindow.close();
//            }
//        });
//
//    }
//
//    private void displayListToAddUsersWithReadPermission() {
//        AccessPolicyServiceManager.getInstance().getUsers(new AsyncCallback<List<String>>() {
//
//            public void onSuccess(final List<String> allUsersList) {
//                if (allUsersList == null || allUsersList.size() == 0) {
//                    displayListEmptyMessage();
//                    return;
//                }
//                AccessPolicyServiceManager.getInstance().getUsersWithReadOnlyAccess(currentSelectedProject,
//                        new AsyncCallback<Collection<AccessPolicyUserData>>() {
//
//                            public void onSuccess(Collection<AccessPolicyUserData> readOnlyAccessCollection) {
//                                final List<String> readOnlyAccessList = new ArrayList<String>();
//                                if (readOnlyAccessCollection != null) {
//                                    for (AccessPolicyUserData userData : readOnlyAccessCollection) {
//                                        if (userData.getName() != null) {
//                                            readOnlyAccessList.add(userData.getName());
//                                        }
//
//                                    }
//                                }
//
//                                for (String readOnlyAccessUserName : readOnlyAccessList) {
//                                    allUsersList.remove(readOnlyAccessUserName);
//                                }
//
//                                allUsersList.remove(Application.get().getUserId().getUserName());
//                                if (removeListForReadOnlyAccess != null) {
//                                    for (AccessPolicyUserData uData : removeListForReadOnlyAccess) {
//                                        String removedUserName = uData.getName();
//                                        if (!allUsersList.contains(removedUserName)) {
//                                            allUsersList.add(removedUserName);
//                                        }
//                                    }
//                                }
//                                if (addListForReadOnlyAccess != null) {
//                                    for (AccessPolicyUserData uData : addListForReadOnlyAccess) {
//                                        String addedUserName = uData.getName();
//                                        allUsersList.remove(addedUserName);
//                                    }
//                                }
//
//                                if (allUsersList.size() > 0) {
//                                    displayUserList(allUsersList);
//                                } else {
//                                    displayListEmptyMessage();
//                                }
//                            }
//
//                            public void onFailure(Throwable caught) {
//                                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//
//                            }
//                        });
//            }
//
//            public void onFailure(Throwable caught) {
//                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//
//            }
//
//            private void displayUserList(List<String> allUsersList) {
//                final VerticalPanel addUserPanel = new VerticalPanel();
//                addUserPanel.setWidth("100%");
//                addUserReadOnlyPermWindow.clear();
//                addUserReadOnlyPermWindow.add(addUserPanel);
//
//                final ListBox userListBox = new ListBox(true);
//
//                userListBox.setSize(OntologyShareAccessConstants.ADD_USER_LISTBOX_MIN_WIDTH + "px",
//                        OntologyShareAccessConstants.ADD_USER_LISTBOX_MIN_HEIGHT + "px");
//                addUserPanel.add(userListBox);
//                addUserPanel.setCellHorizontalAlignment(userListBox, HasHorizontalAlignment.ALIGN_CENTER);
//
//                HorizontalPanel buttonPanel = new HorizontalPanel();
//                Button addUserButton = new Button("Add");
//                buttonPanel.add(addUserButton);
//                Button cancelButton = new Button("Cancel");
//                buttonPanel.add(cancelButton);
//                buttonPanel.setSpacing(10);
//                addUserPanel.add(buttonPanel);
//
//                addUserPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
//                userListBox.clear();
//                for (Iterator iterator = allUsersList.iterator(); iterator.hasNext();) {
//                    String userName = (String) iterator.next();
//                    userListBox.addItem(userName, userName);
//                }
//                addUserReadOnlyPermWindow.show();
//                addUsersWithReadPermissionHandler(addUserButton, cancelButton, userListBox);
//                addUserReadOnlyPermWindow.addListener(new PanelListenerAdapter() {
//                    @Override
//                    public void onBodyResize(Panel panel, String width, String height) {
//                        updateAddUserListboxOnResize(addUserReadOnlyPermWindow, addUserPanel, userListBox);
//                    }
//
//                });
//            }
//
//            private void displayListEmptyMessage() {
//                AccessPolicyUtil.this.displayListEmptyMessage(addUserReadOnlyPermWindow, "read");
//
//            }
//        });
//    }
//
//    public void displayUsersToAddWritePermWindow() {
//        if (currentSelectedProject == null) {
//            return;
//        }
//
//        addUserWritePermWindow = new Window();
//
//        addUserWritePermWindow.setTitle("Add Users with write permission");
//        addUserWritePermWindow.setClosable(true);
//        addUserWritePermWindow.setClosable(true);
//        addUserWritePermWindow.setPaddings(7);
//        addUserWritePermWindow.setCloseAction(Window.HIDE);
//        displayListToAddUsersWithWritePermission();
//
//    }
//
//    private void displayListToAddUsersWithWritePermission() {
//        AccessPolicyServiceManager.getInstance().getUsers(new AsyncCallback<List<String>>() {
//
//            public void onSuccess(final List<String> allUsersList) {
//                if (allUsersList == null || allUsersList.size() == 0) {
//                    displayListEmptyMessage();
//                    return;
//                }
//                AccessPolicyServiceManager.getInstance().getUsersWithWriteAccess(currentSelectedProject,
//                        new AsyncCallback<Collection<AccessPolicyUserData>>() {
//
//                            public void onSuccess(Collection<AccessPolicyUserData> writeAccessCollection) {
//                                final List<String> writeAccessList = new ArrayList<String>();
//                                if (writeAccessCollection != null) {
//                                    for (AccessPolicyUserData userData : writeAccessCollection) {
//                                        if (userData.getName() != null) {
//                                            writeAccessList.add(userData.getName());
//                                        }
//
//                                    }
//                                }
//
//                                for (String writeAccessUserName : writeAccessList) {
//                                    allUsersList.remove(writeAccessUserName);
//                                }
//
//                                    allUsersList.remove(Application.get().getUserId().getUserName());
//
//                                if (removeListForWriteAccess != null) {
//                                    for (AccessPolicyUserData uData : removeListForWriteAccess) {
//                                        String removedUserName = uData.getName();
//                                        if (!allUsersList.contains(removedUserName)) {
//                                            allUsersList.add(removedUserName);
//                                        }
//
//                                    }
//                                }
//                                if (addListForWriteAccess != null) {
//                                    for (AccessPolicyUserData uData : addListForWriteAccess) {
//                                        String addedUserName = uData.getName();
//                                        allUsersList.remove(addedUserName);
//
//                                    }
//                                }
//
//                                if (allUsersList.size() > 0) {
//                                    displayUserList(allUsersList);
//                                } else {
//                                    displayListEmptyMessage();
//                                }
//
//                            }
//
//                            public void onFailure(Throwable caught) {
//                                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//                            }
//                        });
//            }
//
//            public void onFailure(Throwable caught) {
//                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
//
//            }
//
//            private void displayUserList(List<String> allUsersList) {
//                final VerticalPanel addUserPanel = new VerticalPanel();
//                addUserPanel.setWidth("100%");
//                addUserWritePermWindow.clear();
//                addUserWritePermWindow.add(addUserPanel);
//
//                final ListBox userListBox = new ListBox(true);
//                userListBox.setSize(OntologyShareAccessConstants.ADD_USER_LISTBOX_MIN_WIDTH + "px",
//                        OntologyShareAccessConstants.ADD_USER_LISTBOX_MIN_HEIGHT + "px");
//                addUserPanel.add(userListBox);
//                addUserPanel.setCellHorizontalAlignment(userListBox, HasHorizontalAlignment.ALIGN_CENTER);
//
//                HorizontalPanel buttonPanel = new HorizontalPanel();
//                Button addUserButton = new Button("Add");
//                buttonPanel.add(addUserButton);
//                Button cancelButton = new Button("Cancel");
//                buttonPanel.add(cancelButton);
//                buttonPanel.setSpacing(10);
//
//                addUserPanel.add(buttonPanel);
//
//                addUserPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
//                userListBox.clear();
//                for (Iterator iterator = allUsersList.iterator(); iterator.hasNext();) {
//                    String userName = (String) iterator.next();
//                    userListBox.addItem(userName, userName);
//                }
//                addUserWritePermWindow.show();
//                addUsersWithWritePermissionHandler(addUserButton, cancelButton, userListBox);
//
//                addUserWritePermWindow.addListener(new PanelListenerAdapter() {
//                    @Override
//                    public void onBodyResize(Panel panel, String width, String height) {
//                        updateAddUserListboxOnResize(addUserWritePermWindow, addUserPanel, userListBox);
//                    }
//
//                });
//            }
//
//            private void displayListEmptyMessage() {
//                AccessPolicyUtil.this.displayListEmptyMessage(addUserWritePermWindow, "write");
//            }
//        });
//    }
//
//    private void addUsersWithWritePermissionHandler(Button addUser, Button cancelButton, final ListBox userListBox) {
//        addUser.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                List<String> selectedUsersList = new ArrayList<String>();
//                for (int i = 0; i < userListBox.getItemCount(); i++) {
//                    if (userListBox.isItemSelected(i)) {
//                        selectedUsersList.add(userListBox.getValue(i));
//                    }
//                }
//                if (selectedUsersList.size() > 0) {
//                    AccessPolicyUserData userEveryBody = new AccessPolicyUserData();
//                    userEveryBody.setName(OntologyShareAccessConstants.USER_EVERYBODY_NAME);
//                    currentListForWriteAccess.remove(userEveryBody); // removes user "<everybody>" if present
//
//                    for (Iterator iterator = selectedUsersList.iterator(); iterator.hasNext();) {
//                        String toAddUserName = (String) iterator.next();
//                        AccessPolicyUserData toAddUserData = new AccessPolicyUserData();
//                        toAddUserData.setName(toAddUserName);
//                        toAddUserData.setPartofWriters(true);
//                        if (!addListForWriteAccess.contains(toAddUserData)) {
//                            addListForWriteAccess.add(toAddUserData);
//                        }
//                        removeListForWriteAccess.remove(toAddUserData); // remove the users to add from remove list if present.
//                        if (!currentListForWriteAccess.contains(toAddUserData)) {
//                            currentListForWriteAccess.add(toAddUserData);
//                        }
//
//                    }
//                    addUserWritePermWindow.close();
//                    updateWriteAccessList(currentListForWriteAccess);
//                }
//            }
//        });
//
//        cancelButton.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                addUserWritePermWindow.close();
//
//            }
//        });
//    }
//
//    /**
//     * @param addUserPanel
//     * @param userListBox
//     */
//    private void updateAddUserListboxOnResize(Window addUserPermWindow, final VerticalPanel addUserPanel,
//                                              final ListBox userListBox) {
//        ExtElement winBody = addUserPermWindow.getBody();
//        int heightChange = winBody.getHeight(true) - (addUserPanel.getElement().getOffsetHeight());
//        if (heightChange != 0) {
//            int listBoxHeight;
//
//            listBoxHeight = new Integer(userListBox.getElement().getOffsetHeight()) + (heightChange);
//
//            listBoxHeight = (listBoxHeight <OntologyShareAccessConstants.ADD_USER_LISTBOX_MIN_HEIGHT) ? OntologyShareAccessConstants.ADD_USER_LISTBOX_MIN_HEIGHT
//                    : listBoxHeight;
//
//            userListBox.getElement().getStyle().setHeight(new Double(listBoxHeight), Style.Unit.PX);
//        }
//    }
//
//    /**
//     * @param addUserPermWindow
//     *
//     */
//    private void displayListEmptyMessage(final Window addUserPermWindow, String permission) {
//        VerticalPanel messagePanel = new VerticalPanel();
//        messagePanel.setWidth("100%");
//        addUserPermWindow.clear();
//        addUserPermWindow.add(messagePanel);
//
//        HTML message = new HTML("There is no user to add with <br><b>" + permission + "</b> Permission.");
//        messagePanel.add(message);
//        messagePanel.setCellHorizontalAlignment(message, HasHorizontalAlignment.ALIGN_CENTER);
//
//        HorizontalPanel buttonPanel = new HorizontalPanel();
//        buttonPanel.setSpacing(10);
//        Button okButton = new Button("Ok");
//        buttonPanel.add(okButton);
//        messagePanel.add(buttonPanel);
//
//        messagePanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
//        addUserPermWindow.show();
//
//        okButton.addClickHandler(new ClickHandler() {
//
//            public void onClick(ClickEvent event) {
//                addUserPermWindow.close();
//            }
//        });
//    }
//
////    public static void updateShareLink(final ProjectId projectId) {
////        ShareOntologyAccessEventManager.getShareOntologyAccessManager().notifyToUpdateShareLink(false, projectId);
////        if (!Application.get().getUserId().isGuest()) {
////            AccessPolicyServiceManager.getInstance().canManageProject(projectId, Application.get().getUserId(), new AsyncCallback<Boolean>() {
////
////                public void onSuccess(Boolean result) {
////                    ShareOntologyAccessEventManager.getShareOntologyAccessManager().notifyToUpdateShareLink(result, projectId);
////
////                }
////
////                public void onFailure(Throwable caught) {
////                    ShareOntologyAccessEventManager.getShareOntologyAccessManager().notifyToUpdateShareLink(false, projectId);
////
////                }
////            });
////        }
////
////    }
}
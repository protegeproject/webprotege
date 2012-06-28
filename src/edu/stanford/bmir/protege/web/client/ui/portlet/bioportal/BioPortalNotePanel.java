package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;

import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioportalProposalsManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;


/**
 * A panel that shows one BioPortal note. It has associated a {@link Record} that contains the note data.
 *
 * A note status can be: Submitted, Rejected, Under Discussion, Under Review, Approved, Rejected, Implemented, Published
 *
 * @author ttania
 *
 */
public class BioPortalNotePanel extends Panel{

    private Record record;
    private EntityData entityData;

    private AsyncCallback<Void> updateNotesCallback;

    public BioPortalNotePanel() {
        setAutoScroll(true);
        updateUI();
    }

    //TODO: this should be done with a template
    private void updateUI() {
        removeAll(true);

        if (record != null) {
            VerticalPanel vPanel = new VerticalPanel();
            vPanel.getElement().getStyle().setBackgroundColor(isArchived() ? "#EBEBEB" : "#E2EBF0"); //ffe6d5
            vPanel.getElement().getStyle().setProperty("width", "100%");
            // vPanel.getElement().getStyle().setProperty("overflow", "auto");

            vPanel.add(createHeaderHtml());
            vPanel.add(createBodyHtml());
            vPanel.add(new HTML("<hr />"));
            vPanel.add(createFooterHtml());
            add(vPanel);
        }

        doLayout();
    }

    private Widget createHeaderHtml() {
        double dateVal = record.getAsDouble(BioPortalNoteConstants.CREATED);
        String dateStr = new Date((long) dateVal).toString(); //FIXME: use dateformat

        String bkgColor = isArchived() ? "gray" : "#234979";
        String archivedText = isArchived() ? " (archived)" : "";

        String str = "<table width=\"100%\" style=\"background-color:" + bkgColor + "; color:white; padding: 6px; \"><tr>" +
        "<td><img src=\"images/bp_icon.png\" style=\"margin: -7px 0 -5px -7px;\"/></td>" +
        "<td><b>" + record.getAsString(BioPortalNoteConstants.SUBJECT) + archivedText + "</b></td>" +
        "<td>" + BioPortalUsersCache.getBioPortalUserName(record.getAsString(BioPortalNoteConstants.AUTHOR)) + "</td>" +
        "<td>" + record.getAsString(BioPortalNoteConstants.TYPE) + "</td>" +
        "<td>" + dateStr + "</td>" +
        "</tr></table>";

        HTML titleHtml = new HTML(str);

        //TODO: the actions menu is not displayed nice. Should enable it, when you figure out how to display it nicely
        //HorizontalPanel hPanel = new HorizontalPanel();
        //hPanel.add(titleHtml);
        //hPanel.add(getActionsMenuBar());
        //return hPanel;

        return titleHtml;
    }

    private Widget createBodyHtml() {
        String bodyHtml = record.getAsString(BioPortalNoteConstants.BODY);
        HTMLPanel body = new HTMLPanel(bodyHtml);

        body.getElement().getStyle().setProperty("overflow", "auto");
        body.getElement().getStyle().setProperty("padding", "7px 7px 15px 7px");

        return body;
    }

    private Widget createFooterHtml() {
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.getElement().getStyle().setProperty("padding", "0 7px 7px 7px");
        hPanel.getElement().getStyle().setProperty("width", "100%");

        Anchor replyAnchor = new Anchor("<b>Reply</b>", true);
        replyAnchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onNewOrReplyNote();
            }
        });

        String status = record.getAsString(BioPortalNoteConstants.STATUS);
        status = status == null || status.length() == 0 ? "not set" : status;

        Anchor deleteAnchor = new Anchor("<b>Delete note</b>", true);
        deleteAnchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onDeleteNote();
            }
        });

        Anchor archiveAnchor = new Anchor(isArchived() ? "<b>Unarchive note</b>" : "<b>Archive note</b>" , true);
        archiveAnchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onChangeArchiveStatus(false);
            }
        });


        Anchor archiveThreadAnchor = new Anchor(isArchived() ? "<b>Unarchive thread</b>" : "<b>Archive thread</b>", true);
        archiveThreadAnchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onChangeArchiveStatus(true);
            }
        });

        hPanel.add(replyAnchor);
        hPanel.add(createChangeStatusDropDown());
        hPanel.add(archiveAnchor);
        //TODO: disabling thread actions because they don't work in BP. just uncomment this when they fix it.
 /*       if (hasReplies()) {
            hPanel.add(archiveThreadAnchor);
        }
 */

        //TODO:disable delete, because of BP bug, uncomment when fixed
        //hPanel.add(deleteAnchor);

        return hPanel;
    }


    protected Widget createChangeStatusDropDown() {
        HorizontalPanel hPanel = new HorizontalPanel();

        final ListBox statusDropDown = new ListBox(false);
        statusDropDown.addStyleName("gwt-dropdown");

        Set<String> statusList = BioPortalNoteConstants.STATUS_2_INDEX_MAP.keySet();
        for (String status : statusList) {
            statusDropDown.addItem(status);
        }

        String initialStatus = record.getAsString(BioPortalNoteConstants.STATUS);
        if (initialStatus != null && initialStatus.length() > 0) {
            Integer index = BioPortalNoteConstants.STATUS_2_INDEX_MAP.get(initialStatus);
            if (index != null) {
                statusDropDown.setSelectedIndex(index);
            }
        }

        statusDropDown.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                int index = statusDropDown.getSelectedIndex();
                List<String> statusList = new ArrayList<String>(BioPortalNoteConstants.STATUS_2_INDEX_MAP.keySet());
                String newStatus = statusList.get(index);
                onChangeStatus(newStatus);
            }
        });

        hPanel.add(new HTML("<span style=\"color:#327BAA; font-weight:bold; margin-right:3px;\">Status</span>"));
        hPanel.add(statusDropDown);

        return hPanel;
    }

    protected void onChangeStatus(String newStatus) {
        if (!UIUtil.confirmIsLoggedIn()) {
            return;
        }

        final String newStatus2 = newStatus.equals(BioPortalNoteConstants.STATUS_NOT_SET) ? "" : newStatus;

        BioportalProposalsManager.getBioportalProposalsManager().changeNoteStatus(null, null,
                record.getAsString(BioPortalNoteConstants.ONTOLOGY_ID),
                record.getAsString(BioPortalNoteConstants.ID), newStatus2, new AbstractAsyncHandler<String>() {
            @Override
            public void handleFailure(Throwable caught) {
                MessageBox.alert("Error", "There was an error at setting the note status. Please try again later.");
            }

            @Override
            public void handleSuccess(String result) {
                record.set(BioPortalNoteConstants.STATUS, newStatus2);
            }
        });
    }


    protected void onDeleteNote() {
        if (!UIUtil.confirmIsLoggedIn()) {
            return;
        }

        if (hasReplies()) {
            MessageBox.alert("This note has replies and it cannot be deleted. Please consider archiving the thread.");
            return;
        }

        if (!isAuthorOfNote()) {
            MessageBox.alert("Only the author of a note can delete it.");
            return;
        }

        MessageBox.confirm("Delete note?", "Are you sure you want to delete this note?" , new ConfirmCallback() {
            public void execute(String btnID) {
                if (btnID == null || !btnID.equalsIgnoreCase("yes")) {
                    return;
                }
            }
        });

        BioportalProposalsManager.getBioportalProposalsManager().deleteNote(null, null,
                record.getAsString(BioPortalNoteConstants.ONTOLOGY_ID), true,
                record.getAsString(BioPortalNoteConstants.ID),
                new AbstractAsyncHandler<Void>() {
                    @Override
                    public void handleFailure(Throwable caught) {
                        MessageBox.alert("There was an error deleting this note. Please try again later.");
                        if (updateNotesCallback != null) {
                            updateNotesCallback.onFailure(caught);
                        }
                    }

                    @Override
                    public void handleSuccess(Void result) {
                        record = null;
                        updateUI();
                        if (updateNotesCallback != null) {
                            updateNotesCallback.onSuccess(result);
                        }
                    }
                });

    }

    protected void onChangeArchiveStatus(boolean applyOnThread) {
        if (!UIUtil.confirmIsLoggedIn()) {
            return;
        }

        boolean isArchived = isArchived();
        final Boolean archive = applyOnThread ? null : (isArchived ? null : true);
        final Boolean unarchive = applyOnThread ? null : (isArchived ? true : null);
        final Boolean archivethread = applyOnThread ? (isArchived ? null : true) : null;
        final Boolean unarchivethread = applyOnThread ? (isArchived ? true : null) : null;

        BioportalProposalsManager.getBioportalProposalsManager().updateNote(
                null, null,
                record.getAsString(BioPortalNoteConstants.ONTOLOGY_ID), true,
                record.getAsString(BioPortalNoteConstants.ID),
                archive, archivethread, unarchive, unarchivethread,
                null, null, null, null, null, null, null, null, new AbstractAsyncHandler<Void>() {
                    @Override
                    public void handleFailure(Throwable caught) {
                       MessageBox.alert("There was an error at changing the archive status. Please try again later.");
                    }
                    @Override
                    public void handleSuccess(Void result) {
                        //TODO - change record
                        updateUI();
                        if (updateNotesCallback != null) {
                            updateNotesCallback.onSuccess(result);
                        }
                    }
                });

    }

    private boolean hasReplies() {
        return record.getAsObject(BioPortalNoteConstants.ASSOCIATED) != null;
    }

    private boolean isAuthorOfNote() {
        return BioPortalUsersCache.getCurrentBpUser().equals(record.getAsString(BioPortalNoteConstants.AUTHOR));
    }

    private boolean isArchived(){
        if (record == null) {
            return false;
        }
        return record.getAsBoolean(BioPortalNoteConstants.ARCHIVED);
    }

    protected void onNewOrReplyNote() {
        NewNotePanel newNotePanel = new NewNotePanel();
        newNotePanel.setRecord(record);
        newNotePanel.setEntityData(entityData);
        newNotePanel.showPopup(record == null ? "New note" : "Reply to note");
    }

    public void setRecord(Record record) {
        this.record = record;
        updateUI();
    }

    public void setEntityData(EntityData entityData) {
        this.entityData = entityData;
    }

    public void setUpdateNotesCallback(AsyncCallback<Void> updateNotesCallback) {
        this.updateNotesCallback = updateNotesCallback;
    }

    //FIXME: to be figured out later
    private MenuBar getActionsMenuBar() {
        MenuBar menubar = new MenuBar(true);
        menubar.setAutoOpen(true);
        menubar.setAnimationEnabled(true);

        MenuBar actionsMenu = new MenuBar(true);
        actionsMenu.setAnimationEnabled(true);

        actionsMenu.addItem(new MenuItem("Reply", new Command() {
            public void execute() {
                onNewOrReplyNote();
            }
        }));

        actionsMenu.addItem(new MenuItem("Change status", new Command() {
            public void execute() {
                //onChangeStatus();
            }
        }));

        menubar.addItem("Actions", actionsMenu);

        return menubar;
    }
}

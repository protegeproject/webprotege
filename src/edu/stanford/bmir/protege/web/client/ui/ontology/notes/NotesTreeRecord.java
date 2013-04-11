package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.ui.util.DateUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesTreeRecord {

    private NotesData record = null;

    private DisclosurePanel uiObject = null;

    private FlexTable repliesPanel = null;

    private HTML openTimeStamp = null;

    private HTML closedTimeStamp = null;

    private HTML openNumReplies = null;

    private HTML closedNumReplies = null;

    private ProjectId projectId = null;

    private EntityData currentEntity = null;

    private NotesTreeRecord parent = null;

    private Date latestUpdate = null;

    private NotesTreePortlet container = null;

    private HTML message = null;

    private HTML openArchivePanel = null;

    private HTML closedArchivePanel = null;

    private HTML openSubjectPanel = null;

    private HTML closedSubjectPanel = null;

    private HTML openTypePanel = null;

    private HTML closedTypePanel = null;

    private Anchor archiveAnchor = null;

    private boolean repliesFetched = false;

    private Widget closedAuthorPanel;

    private Widget openAuthorPanel;

    private Widget closedHeader = null;

    private Widget openHeader = null;

    private List<NotesTreeRecord> childRecords;

    private boolean cascadeSelection = false;

    private boolean expandAll = false;

    private static DisclosurePanelImages images = (DisclosurePanelImages) GWT.create(DisclosurePanelImages.class);

    public NotesTreeRecord(NotesData record, ProjectId projectId, EntityData currentEntity, NotesTreeRecord parent) {
        this.projectId = projectId;
        this.currentEntity = currentEntity;
        this.record = record;
        this.parent = parent;
        latestUpdate = this.record.getLatestUpdate() != null ? this.record.getLatestUpdate() : new Date();
    }

    public Widget getUIObject() {
        if (uiObject == null) {
            createUIObject();
        }
        updateTimestamps();

        return uiObject;
    }

    private void createUIObject() {
        uiObject = new DisclosurePanel();
        uiObject.setStyleName("custom-gwt-DisclosurePanel");
        if (record != null) {
            addContentPanel();
            addHeaderWidgets();
            uiObject.setAnimationEnabled(true);
            uiObject.setVisible(true);
        }
    }

    public void refreshBody(NotesData newData) {
        this.record.setBody(newData.getBody());
        this.record.setType(newData.getType());
        this.record.setSubject(newData.getSubject());
        this.record.setLatestUpdate(newData.getLatestUpdate());
        message.setHTML(this.record.getBody());
        updateSubjects();
        updateArchivePanels();
        updateTimestamps();
        updateTypeHTML();
        this.updateLatestChange(this.record.getLatestUpdate(), true);
    }

    private void addHeaderWidgets() {
        updateTimestamps();
        updateNumReplies();
        closedHeader = getClosedHeaderWidget();
        openHeader = getOpenHeaderWidget();
        addHeaderClickHandlers(openHeader, closedHeader);
        uiObject.setHeader(closedHeader);
        uiObject.setOpen(false);
        updateHTMLStyling();
    }

    private void addContentPanel() {
        FlexTable contentPanel = new FlexTable();
        contentPanel.setStylePrimaryName("custom-FlexTableContentPanel");
        contentPanel.setWidget(0, 0, getBodyWidget());
        contentPanel.setWidget(1, 0, getActionsPanel());
        contentPanel.setWidget(2, 0, getRepliesPanel());
        FlexTable wrapperPanel = new FlexTable();
        wrapperPanel.setStylePrimaryName("custom-FlexTableContentPanelWrapper");
        wrapperPanel.setWidget(0, 0, contentPanel);
        uiObject.setContent(wrapperPanel);
    }

    private Anchor getReplyAnchor() {
        Anchor replyAnchor = new Anchor("Reply");
        replyAnchor.setStylePrimaryName("custom-ActionAnchor");
        ClickHandler replyLinkClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                reply();
            }
        };
        replyAnchor.addClickHandler(replyLinkClickHandler);
        return replyAnchor;
    }

    private Anchor getArchiveAnchor() {
        Anchor archiveAnchor = new Anchor(this.record.isArchived() ? "UnArchive" : "Archive");
        archiveAnchor.setStylePrimaryName("custom-ActionAnchor");
        ClickHandler archivelinkClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                archiveNote(!record.isArchived());
            }
        };
        archiveAnchor.addClickHandler(archivelinkClickHandler);
        return archiveAnchor;
    }

    private void archiveNote(final boolean archive) {
        if (Application.get().getUserId().isGuest()) {
            MessageBox.alert("To post a message you need to be logged in.");
            return;
        }
        ChAOServiceManager.getInstance().archiveNote(projectId, this.record.getNoteId().getName(), archive, new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                GWT.log("Error archiving note ", caught);
                MessageBox.alert("Error", "There was a problem archiving note. Please try again later");
            }

            public void onSuccess(Boolean result) {
                if (result) {
                    record.setArchived(archive);
                    updateArchivePanels();
                    updateHTMLStyling();
                    archiveAnchor.setText(archive ? "UnArchive" : "Archive");
                }
                else {
                    GWT.log("Error archiving note ");
                    MessageBox.alert("Error", "There was a problem archiving note. Please try again later");
                }
            }
        });
    }


    private void updateHTMLStyling() {
        if (this.record.isArchived()) {
            this.openTimeStamp.setStyleName("customArchived-gwt-HTMLTimeStamp");
            this.closedTimeStamp.setStyleName("customArchived-gwt-HTMLTimeStamp");
            this.openNumReplies.setStyleName("customArchived-gwt-HTMLNumReplies");
            this.closedNumReplies.setStyleName("customArchived-gwt-HTMLNumReplies");
            this.openArchivePanel.setStyleName("customArchived-gwt-HTMLArchive");
            this.closedArchivePanel.setStyleName("customArchived-gwt-HTMLArchive");
            this.openSubjectPanel.setStyleName("customArchived-gwt-HTMLSubject");
            this.closedSubjectPanel.setStyleName("customArchived-gwt-HTMLSubject");
            this.openTypePanel.setStyleName("customArchived-gwt-HTMLType");
            this.closedTypePanel.setStyleName("customArchived-gwt-HTMLType");
            this.openAuthorPanel.setStyleName("customArchived-gwt-HTMLAuthor");
            this.closedAuthorPanel.setStyleName("customArchived-gwt-HTMLAuthor");
        }
        else {
            this.openTimeStamp.setStyleName("gwt-HTMLTimeStamp");
            this.closedTimeStamp.setStyleName("gwt-HTMLTimeStamp");
            this.openNumReplies.setStyleName("gwt-HTMLNumReplies");
            this.closedNumReplies.setStyleName("gwt-HTMLNumReplies");
            this.openArchivePanel.setStyleName("gwt-HTMLArchive");
            this.closedArchivePanel.setStyleName("gwt-HTMLArchive");
            this.openSubjectPanel.setStyleName("gwt-HTMLSubject");
            this.closedSubjectPanel.setStyleName("gwt-HTMLSubject");
            this.openTypePanel.setStyleName("gwt-HTMLType");
            this.closedTypePanel.setStyleName("gwt-HTMLType");
            this.openAuthorPanel.setStyleName("gwt-HTMLAuthor");
            this.closedAuthorPanel.setStyleName("gwt-HTMLAuthor");
        }
    }

    private void updateSubjects() {
        openSubjectPanel.setHTML(getSubjectAsString(true));
        closedSubjectPanel.setHTML(getSubjectAsString(false));
    }

    private void updateArchivePanels() {
        openArchivePanel.setHTML(this.record.isArchived() ? "<i>Archived</i>" : "");
        closedArchivePanel.setHTML(this.record.isArchived() ? "<i>Archived</i>" : "");
    }

    private Anchor getDeleteAnchor() {
        ClickHandler deletelinkClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (Application.get().getUserId().isGuest()) {
                    MessageBox.alert("To delete a message you need to be logged in.");
                    return;
                }

                if (!isDeletable()) {
                    MessageBox.alert("Delete is not allowed! You may delete only notes that were created by you and which did not get any replies yet.");
                    return;
                }
                deleteNote();
            }
        };

        Anchor deleteAnchor = new Anchor("Delete");
        deleteAnchor.setStylePrimaryName("custom-ActionAnchor");
        deleteAnchor.addClickHandler(deletelinkClickHandler);
        return deleteAnchor;
    }

    private Anchor getOpenAnchor() {
        ClickHandler openLinkClickHandler = new ClickHandler() {

            public void onClick(ClickEvent event) {
                Window window = getWindow();
                window.show();
            }
        };

        Anchor openAnchor = new Anchor("Open");
        openAnchor.setStylePrimaryName("custom-ActionAnchor");
        openAnchor.addClickHandler(openLinkClickHandler);
        return openAnchor;
    }

    private Window getWindow() {
        String title = record.getSubject();
        title = "Topic: " + title + "  [Type: " + record.getType() + "]";
        HTML p = new HTML(record.getBody());
        p.setSize("100%", "100%");

        Window window = new Window();
        window.setLayout(new FitLayout());
        window.setTitle(title);
        window.setHeight(400);
        window.setWidth(600);
        window.setMinHeight(360);
        window.setMinWidth(550);

        ScrollPanel spanel = new ScrollPanel();
        spanel.setStylePrimaryName("custom-windowScrollPanel");
        spanel.setSize("600px", "350px");
        spanel.add(p);
        window.add(spanel);
        window.add(getWindowActionsPanel(window));
        window.setPaddings(5, 5, 5, 40);
        return window;
    }

    public NotesTreePortlet getContainer() {
        return container;
    }

    public void setContainer(NotesTreePortlet container) {
        this.container = container;
    }

    private Widget getWindowActionsPanel(final Window window) {
        FlexTable actionsPanel = new FlexTable();
        actionsPanel.setHeight("20px");
        actionsPanel.setStylePrimaryName("custom-FlexTableActionsPanel");
        FlexCellFormatter cellFormatter = actionsPanel.getFlexCellFormatter();
        //actionsPanel.setCellSpacing(5);

        ClickHandler genericAnchorLinkClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                window.close();
            }
        };

        Anchor replyAnchor = getReplyAnchor();
        replyAnchor.addClickHandler(genericAnchorLinkClickHandler);
        Anchor archiveAnchor = getArchiveAnchor();
        archiveAnchor.addClickHandler(genericAnchorLinkClickHandler);
        Anchor deleteAnchor = getDeleteAnchor();
        deleteAnchor.addClickHandler(genericAnchorLinkClickHandler);
        Anchor editAnchor = getEditAnchor();
        editAnchor.addClickHandler(genericAnchorLinkClickHandler);

        actionsPanel.setWidget(0, 0, replyAnchor);
        cellFormatter.setWidth(0, 0, "80px");
        actionsPanel.setWidget(0, 1, editAnchor);
        cellFormatter.setWidth(0, 1, "80px");
        actionsPanel.setWidget(0, 2, archiveAnchor);
        cellFormatter.setWidth(0, 2, "80px");
        actionsPanel.setWidget(0, 3, deleteAnchor);
        cellFormatter.setWidth(0, 3, "80px");

        FlexTable wrapper = new FlexTable();
        wrapper.setStylePrimaryName("custom-FlexTableActionsWrapperPanel");
        wrapper.setHeight("20px");
        wrapper.setWidget(0, 0, actionsPanel);
        return wrapper;
    }

    private Anchor getEditAnchor() {
        Anchor editAnchor = new Anchor("Edit");
        editAnchor.setStylePrimaryName("custom-ActionAnchor");
        ClickHandler editlinkClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                edit();
            }
        };
        editAnchor.addClickHandler(editlinkClickHandler);
        return editAnchor;
    }

    private Widget getActionsPanel() {
        FlexTable actionsPanel = new FlexTable();
        actionsPanel.setHeight("20px");
        actionsPanel.setStylePrimaryName("custom-FlexTableActionsPanel");
        FlexCellFormatter cellFormatter = actionsPanel.getFlexCellFormatter();
        //actionsPanel.setCellSpacing(5);

        Anchor replyAnchor = getReplyAnchor();
        archiveAnchor = getArchiveAnchor();
        Anchor deleteAnchor = getDeleteAnchor();
        Anchor editAnchor = getEditAnchor();
        Anchor openAnchor = getOpenAnchor();

        actionsPanel.setWidget(0, 0, replyAnchor);
        cellFormatter.setWidth(0, 0, "90px");
        actionsPanel.setWidget(0, 1, openAnchor);
        cellFormatter.setWidth(0, 1, "90px");
        actionsPanel.setWidget(0, 2, editAnchor);
        cellFormatter.setWidth(0, 2, "90px");
        actionsPanel.setWidget(0, 3, archiveAnchor);
        cellFormatter.setWidth(0, 3, "90px");
        actionsPanel.setWidget(0, 4, deleteAnchor);
        cellFormatter.setWidth(0, 4, "90px");

        FlexTable wrapper = new FlexTable();
        wrapper.setStylePrimaryName("custom-FlexTableActionsWrapperPanel");
        wrapper.setHeight("20px");
        wrapper.setWidget(0, 0, actionsPanel);

        HTML creationTimeHTML = new HTML(this.record.getCreationDate());
        creationTimeHTML.setStylePrimaryName("custom-CreationTimestampHTML");
        creationTimeHTML.setHorizontalAlignment(HTML.ALIGN_RIGHT);
        wrapper.setWidget(0, 1, creationTimeHTML);
        return wrapper;
    }

    private void deleteNote() {

        final String id = this.record.getNoteId().getName();
        if (id == null) {
            MessageBox.alert("Error", "Something went wrong at retrieving the notes. Please try again later.");
            return;
        }

        final String subj = this.record.getSubject();
        final NotesTreeRecord caller = this;
        MessageBox.confirm("Confirm delete note", "Are you sure you want to delete the note with subject \"" + subj + "\"?", new MessageBox.ConfirmCallback() {
            public void execute(String btnID) {
                if (btnID.equalsIgnoreCase("yes")) {
                    ChAOServiceManager.getInstance().deleteNote(projectId, id, new AsyncCallback<Void>() {
                        public void onFailure(Throwable caught) {
                            // TODO Auto-generated method stub
                            GWT.log("Error at removing note with id: " + id, caught);
                            MessageBox.alert("Error", "There was a problem at removing the note with id: " + id);
                        }

                        public void onSuccess(Void result) {
                            caller.deleteAndRemoveFromParent();
                            MessageBox.alert("Success", "Note deleted successfully");
                        }
                    });
                }
            }
        });
    }

    private void deleteAndRemoveFromParent() {
        if (parent != null) {
            parent.deleteReply(this.record);
        }
        if (container != null) {
            this.container.removeNote(this);
        }
        uiObject.removeFromParent();
    }

    public void deleteReply(NotesData child) {
        if (this.record.getReplies() != null) {
            int index = this.record.getReplies().indexOf(child);
            int i = index + 1;
            for (i = index + 1; i < this.repliesPanel.getRowCount(); i++) {
                this.repliesPanel.setWidget(i - 1, 0, this.repliesPanel.getWidget(i, 0));
            }
            if (i == this.repliesPanel.getRowCount()) {
                this.repliesPanel.removeRow(i - 1);
            }
            this.record.getReplies().remove(child);
            updateNumReplies();
        }
    }

    private Widget getBodyWidget() {
        message = new HTML(record.getBody());
        //TODO: add styling
        return message;
    }

    private Widget getRepliesPanel() {
        repliesPanel = new FlexTable();
        repliesPanel.setStylePrimaryName("custom-FlexTableRepliesPanel");
        fillRepliesPanel(repliesPanel, record.getReplies());
        return repliesPanel;
    }

    public Date getLatestUpdate() {
        return latestUpdate;
    }

    public void setLatestUpdate(Date latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    private void addHeaderClickHandlers(final Widget openHeader, final Widget closedHeader) {
        uiObject.addOpenHandler(new OpenHandler<DisclosurePanel>() {
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                doOnOpen();
            }
        });
        uiObject.addCloseHandler(new CloseHandler<DisclosurePanel>() {
            public void onClose(CloseEvent<DisclosurePanel> event) {
                doOnClose();
            }
        });
    }

    private void doOnOpen() {
        updateTimestamps();
        uiObject.setHeader(openHeader);

        if (!repliesFetched) {
            doGetReplies();
        }
        else {
            if (this.childRecords != null && this.cascadeSelection) {
                for (NotesTreeRecord child : this.childRecords) {
                    child.setOpen(true);
                }
            }
            this.cascadeSelection = false;
            this.expandAll = true;
        }
    }

    private void doOnClose() {
        updateTimestamps();
        uiObject.setHeader(closedHeader);
        if (this.childRecords != null && cascadeSelection) {
            for (NotesTreeRecord child : this.childRecords) {
                child.setOpen(false);
            }
        }
        cascadeSelection = false;
    }

    public void setOpen(boolean doExpand) {
        this.cascadeSelection = true;
        this.expandAll = doExpand;
        if (this.uiObject.isOpen() != doExpand) {
            this.uiObject.setOpen(doExpand);
        }
        else {
            if (doExpand) {
                this.doOnOpen();
            }
            else {
                this.doOnClose();
            }
        }
    }

    private void doGetReplies() {
        ChAOServiceManager.getInstance().getReplies(projectId, this.record.getNoteId().getName(), true, new AsyncCallback<List<NotesData>>() {
            public void onFailure(Throwable caught) {
                GWT.log("Error getting notes for " + currentEntity, caught);
            }

            public void onSuccess(List<NotesData> result) {
                addChildRepliesHandler(result);
            }
        });
    }

    private void addChildRepliesHandler(List<NotesData> result) {
        record.setReplies((ArrayList<NotesData>) result);
        fillRepliesPanel(this.repliesPanel, result);
        this.repliesFetched = true;

    }

    private void fillRepliesPanel(FlexTable repliesPanel2, List<NotesData> notes) {
        if (notes != null) {
            this.childRecords = new ArrayList<NotesTreeRecord>();
            for (NotesData reply : notes) {
                NotesTreeRecord r = new NotesTreeRecord(reply, projectId, this.currentEntity, this);
                this.childRecords.add(r);
                Widget childPanel = r.getUIObject();
                int rowCount = repliesPanel2.getRowCount();
                repliesPanel2.setWidget(rowCount, 0, childPanel);
            }
            if (cascadeSelection) {
                for (NotesTreeRecord ntr : this.childRecords) {
                    ntr.setOpen(this.expandAll);
                }
            }
            this.cascadeSelection = false;
            this.expandAll = false;
        }
    }

    private void updateNumReplies() {
        if (openNumReplies == null) {
            openNumReplies = new HTML();
        }
        if (closedNumReplies == null) {
            closedNumReplies = new HTML();
        }

        //int n = record.getReplies() != null ? record.getReplies().size() : 0;
        if (this.record.getReplies() != null) {
            this.record.setNumOfReplies(this.record.getReplies().size());
        }
        int n = this.record.getNumOfReplies();
        String s = " replies";
        if (n == 1) {
            s = " reply";
        }
        openNumReplies.setText(Integer.toString(n) + s);
        closedNumReplies.setText(Integer.toString(n) + s);
    }

    private Widget getClosedHeaderWidget() {
        FlexTable header = new FlexTable();
        FlexCellFormatter cellFormatter = header.getFlexCellFormatter();

        header.setStylePrimaryName("custom-FlexTableHeader");
        header.setCellSpacing(2);

        header.setWidget(0, 0, images.disclosurePanelClosed().createImage());
        cellFormatter.setWidth(0, 0, "25px");

        closedSubjectPanel = (HTML) getSubject(false);
        cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
        header.setWidget(0, 1, closedSubjectPanel);

        closedArchivePanel = (HTML) getArchiveWidget();
        cellFormatter.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 2, closedArchivePanel);
        cellFormatter.setWidth(0, 2, "50px");

        closedAuthorPanel = getAuthor();
        cellFormatter.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 3, closedAuthorPanel);
        cellFormatter.setWidth(0, 3, "75px");

        closedTypePanel = (HTML) getType();
        cellFormatter.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 4, closedTypePanel);
        cellFormatter.setWidth(0, 4, "125px");

        cellFormatter.setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 5, closedNumReplies);
        cellFormatter.setWidth(0, 5, "75px");

        cellFormatter.setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 6, closedTimeStamp);
        cellFormatter.setWidth(0, 6, "75px");

        return header;
    }

    private Widget getArchiveWidget() {
        HTML archive = new HTML(getArchivedAsString());
        archive.setStylePrimaryName("gwt-HTMLArchive");
        return archive;
    }

    private String getArchivedAsString() {
        return this.record.isArchived() ? "<i>Archived</i>" : "";
    }

    private Widget getOpenHeaderWidget() {
        FlexTable header = new FlexTable();
        FlexCellFormatter cellFormatter = header.getFlexCellFormatter();

        header.setStylePrimaryName("custom-FlexTableHeader");
        header.setCellSpacing(2);

        header.setWidget(0, 0, images.disclosurePanelOpen().createImage());
        cellFormatter.setWidth(0, 0, "25px");

        openSubjectPanel = (HTML) getSubject(true);
        cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
        header.setWidget(0, 1, openSubjectPanel);

        openArchivePanel = (HTML) getArchiveWidget();
        cellFormatter.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 2, openArchivePanel);
        cellFormatter.setWidth(0, 2, "50px");

        openAuthorPanel = getAuthor();
        cellFormatter.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 3, openAuthorPanel);
        cellFormatter.setWidth(0, 3, "75px");

        openTypePanel = (HTML) getType();
        cellFormatter.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 4, openTypePanel);
        cellFormatter.setWidth(0, 4, "125px");

        cellFormatter.setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 5, openNumReplies);
        cellFormatter.setWidth(0, 5, "75px");

        cellFormatter.setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_RIGHT);
        header.setWidget(0, 6, openTimeStamp);
        cellFormatter.setWidth(0, 6, "75px");

        return header;
    }

    private Widget getSubject(boolean open) {
        HTML subjectPanel = new HTML(getSubjectAsString(open));
        subjectPanel.setStylePrimaryName("gwt-HTMLSubject");
        return subjectPanel;
    }

    private String getSubjectAsString(boolean open) {
        String subject = record.getSubject();
        if (subject == null || subject.length() == 0) {
            subject = "(no subject)";
        }
        if (subject.length() > 55 && !open) {
            subject = subject.substring(0, 54) + "...";
        }
        return subject;
    }

    public void updateLatestChange(Date d, boolean propagateChange) {
        if (d != null) {
            this.latestUpdate = d;
            updateTimestamps();
            if (parent != null) {
                parent.updateLatestChange(d, propagateChange);
            }
            if (propagateChange && parent != null) {
                parent.moveNoteToTop(this);
            }
            if (propagateChange && container != null) {
                container.moveNoteToTop(this);
            }
        }
    }

    private Widget getAuthor() {
        HTML newauthorPanel = new HTML(record.getAuthor());
        newauthorPanel.setStylePrimaryName("gwt-HTMLAuthor");
        return newauthorPanel;
    }

    private Widget getType() {
        HTML typePanel = new HTML(record.getType());
        typePanel.setStylePrimaryName("gwt-HTMLType");
        return typePanel;
    }

    private void updateTypeHTML() {
        openTypePanel.setText(record.getType());
        closedTypePanel.setText(record.getType());
    }

    private void updateTimestamps() {
        if (this.openTimeStamp == null) {
            this.openTimeStamp = new HTML();
            this.openTimeStamp.setStylePrimaryName("gwt-HTMLTimeStamp");
        }
        if (this.closedTimeStamp == null) {
            this.closedTimeStamp = new HTML();
            this.closedTimeStamp.setStylePrimaryName("gwt-HTMLTimeStamp");
        }

        openTimeStamp.setTitle(latestUpdate.toLocaleString());
        closedTimeStamp.setTitle(latestUpdate.toLocaleString());

        String shortDate = DateUtil.getAbbreviatedDate(latestUpdate);

        openTimeStamp.setText(shortDate);
        closedTimeStamp.setText(shortDate);
    }

    private void reply() {

        if (Application.get().getUserId().isGuest()) {
            MessageBox.alert("To post a message you need to be logged in.");
            return;
        }

        String subj = this.record.getSubject();
        if (subj != null) {
            subj = "Re: " + subj;
        }
        String text = this.record.getBody();
        if (text != null && text.length() > 0) {
            text = "<br><br>===== " + this.record.getAuthor() + " wrote on " + this.record.getCreationDate() + ": ======<br>" + text;
        }
        else {
            text = "";
        }

        final NotesTreeRecord caller = this;
        NoteInputHandler nih = new NoteInputHandler(projectId, this.currentEntity, new AsyncCallback<NotesData>() {

            public void onFailure(Throwable caught) {
                GWT.log("Error at creating note", caught);
                com.google.gwt.user.client.Window.alert("There were problems at creating the note.\n" + "Please try again later.");
            }

            public void onSuccess(NotesData result) {
                caller.addChildNote(result);
            }
        });
        nih.setRecord(record);
        nih.showInWindow(true, subj, text);
    }

    private void edit() {
        if (Application.get().getUserId().isGuest()) {
            MessageBox.alert("To edit a message you need to be logged in.");
            return;
        }

        if (!isEditable()) {
            MessageBox.alert("You can't edit notes which are created by other users or notes which have replies.");
            return;
        }

        NoteInputHandler nih = new NoteInputHandler(projectId, this.currentEntity, new EditNoteHandler(this));
        nih.setRecord(record);
        nih.editNote();
    }


    public void addChildNote(NotesData child) {
        this.record.addReply(0, child);
        NotesTreeRecord r = new NotesTreeRecord(child, projectId, currentEntity, this);
        DisclosurePanel w = (DisclosurePanel) r.getUIObject();
        int i = repliesPanel.insertRow(0);
        repliesPanel.setWidget(i, 0, w);
        w.setOpen(true);
        updateNumReplies();
        updateLatestChange(child.getLatestUpdate(), true);
    }

    public void moveNoteToTop(NotesTreeRecord child) {
        FlexTable notesTable = this.repliesPanel;
        DisclosurePanel w = (DisclosurePanel) child.getUIObject();

        int rowCount = notesTable.getRowCount();
        if (rowCount == 0) {
            notesTable.setWidget(0, 1, w);
            return;
        }

        DisclosurePanel startWidget = (DisclosurePanel) notesTable.getWidget(0, 0);
        if (startWidget.equals(w)) {
            return;
        }

        int i;
        for (i = 1; i < rowCount; i++) {
            DisclosurePanel iter = (DisclosurePanel) notesTable.getWidget(i, 0);
            notesTable.setWidget(i, 0, startWidget);
            startWidget = iter;
            if (startWidget.equals(w)) {
                notesTable.setWidget(0, 0, w);
                break;
            }
        }

        if (i == rowCount) {
            notesTable.setWidget(i, 0, startWidget);
            notesTable.setWidget(0, 0, w);
        }

        if (parent != null) {
            parent.moveNoteToTop(this);
        }
        if (container != null) {
            container.moveNoteToTop(this);
        }

    }

    public boolean isEditable() {
        return isModifiable(this.record);
    }

    public boolean isDeletable() {
        return isModifiable(this.record);
    }

    private boolean isModifiable(NotesData nd) {
        String loggedInUser = (Application.get().getUserId().isGuest()) ? "" : Application.get().getUserId().getUserName();
        if (loggedInUser.length() > 0 && loggedInUser.equals(nd.getAuthor())) {
            if (nd.getReplies() != null && nd.getReplies().size() > 0) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    class EditNoteHandler implements AsyncCallback<NotesData> {

        private NotesTreeRecord caller = null;

        public EditNoteHandler(NotesTreeRecord caller) {
            this.caller = caller;
        }

        public void onFailure(Throwable caught) {
            GWT.log("Error at creating note", caught);
            com.google.gwt.user.client.Window.alert("There were problems at creating the note.\n" + "Please try again later.");
        }

        public void onSuccess(NotesData result) {
            this.caller.refreshBody(result);
        }
    }

}

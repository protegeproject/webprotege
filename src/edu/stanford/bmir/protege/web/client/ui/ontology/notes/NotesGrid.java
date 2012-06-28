package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowParams;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Vivek Tripathi <vivekyt@stanford.edu>
 */
public class NotesGrid extends GridPanel {

    private static final int PAGE_SIZE = 5;

    protected RecordDef recordDef;
    protected Store store;
    protected Store tempstore;
    protected boolean showPreview = false;
    protected EntityData _currentEntity;
    protected Panel headerPanel;
    private Hyperlink replyLink;
    private Hyperlink deleteLink;
    private Hyperlink expandlink;
    private Hyperlink nextlink;
    private Hyperlink prevlink;
    protected Project project;
    private int rowSelected = -1;
    private int endofpage;
    private int startofpage;
    private int pageNo;
    private Label label;
    private boolean replyEnable = false;
    private boolean deleteEnable = false;
    protected boolean topLevel;
    protected String[][] commentTypes;

    private Renderer subjectRenderer = new Renderer() {
        public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            return Format.format("<b>{0}</b>", new String[] { record.getAsString("subject") });
        }
    };

    Renderer bodyRenderer = new Renderer() {
        public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            String stringVal = record.getAsString("value");
            return Format.format(
               "<style type=\"text/css\">.x-grid3-cell-inner, .x-grid3-hd-inner { white-space:normal !important; }</style> {0}",
                      new String[] { stringVal == null ? "(empty)" : stringVal });
        }
    };


    public NotesGrid(Project project) {
        this(project, false);
    }

    public NotesGrid(Project project, boolean topLevel) {
        this.project = project;
        this.topLevel = topLevel;
        headerPanel = new Panel();
        headerPanel.setLayout(new HorizontalLayout(10));
        label = new Label();

        createGrid();
        setPaddings(0, 0, 0, 30);
        startofpage = 0;
        endofpage = PAGE_SIZE;
        pageNo = 0;
        fillTempStore(0, PAGE_SIZE);
    }

    protected void createGrid() {
        createColumns();
        addHeaderLinks();
        loadStore();
        setGridView();
        addRowListeners();
        addCellListeners();
        addSpacer(30);
        addPreviousLink();
        displayPageNo();
        addNextLink();
        add(headerPanel);
    }

    private void toggleDetails(boolean pressed) {
        showPreview = pressed;
        getView().refresh();
    }

    public void setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
    }

    protected void addHeaderLinks() {
        ClickHandler hyperlinkClickHanlder = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (GlobalSettings.getGlobalSettings().getUserName() == null) {
                    MessageBox.alert("To post a message you need to be logged in.");
                    return;
                }
                createNewPost();
            }
        };

        Hyperlink newTopicLink = new Hyperlink("New Topic", "");
        newTopicLink.setStyleName("discussion-link");
        newTopicLink.addClickHandler(hyperlinkClickHanlder);
        headerPanel.add(newTopicLink);

        ClickHandler replyLinkClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (GlobalSettings.getGlobalSettings().getUserName() == null) {
                    MessageBox.alert("To post a message you need to be logged in.");
                    return;
                }
                reply();
            }
        };

        replyLink = new Hyperlink("Reply", "");
        replyLink.setStyleName("discussion-link");
        replyLink.addClickHandler(replyLinkClickHandler);
        headerPanel.add(replyLink);

        ClickHandler deletelinkClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (GlobalSettings.getGlobalSettings().getUserName() == null) {
                    MessageBox.alert("To delete a message you need to be logged in.");
                    return;
                }
                deleteNote();
            }
        };

        deleteLink = new Hyperlink("Delete", "");
        deleteLink.setStyleName("discussion-link");
        deleteLink.addClickHandler(deletelinkClickHandler);
        headerPanel.add(deleteLink);

        ClickHandler expandlinkClickListener = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (expandlink.getText().equalsIgnoreCase("Expand")) {
                    expandlink.setText("Collapse");
                    toggleDetails(true);
                } else {
                    expandlink.setText("Expand");
                    toggleDetails(false);
                }
            }
        };

        expandlink = new Hyperlink("Expand", "");
        expandlink.setStyleName("discussion-link");
        expandlink.addClickHandler(expandlinkClickListener);
        headerPanel.add(expandlink);

    }

    protected void loadStore() {
        recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("entity"), new StringFieldDef("subject"),
                new StringFieldDef("author"), new StringFieldDef("date"), new StringFieldDef("body"),
                new StringFieldDef("type"), new StringFieldDef("id"), new IntegerFieldDef("repliesCount") });

        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy dataProxy = new MemoryProxy(new Object[][] {});
        MemoryProxy dummyProxy = new MemoryProxy(new Object[][] {});
        store = new Store(dataProxy, reader);
        tempstore = new Store(dummyProxy, reader);
        store.load();
        fillTempStore(0, PAGE_SIZE);
        setStore(tempstore);

        setHeight(200);
        setAutoWidth(true);
        setAutoScroll(true);

        setStripeRows(true);
        setAutoExpandColumn(0);
        setSelectionModel(new RowSelectionModel());
    }

    protected void setGridView() {
        GridView view = new GridView() {
            @Override
            public String getRowClass(Record record, int index, RowParams rowParams, Store store) {
                if (showPreview) {
                    String stringValue = record.getAsString("body");
                    rowParams.setBody(Format.format("<STYLE TYPE=\"text/css\">"
                            + "<!--.indented{padding-left: 20pt;}--></STYLE><p>"
                            + "<DIV CLASS=\"indented\">{0}</DIV><br></p></Blockquote>", stringValue == null ? "(empty)" : stringValue));
                    return "x-grid3-row-expanded";
                } else {
                    return "x-grid3-row-collapsed";
                }
            }
        };
        view.setForceFit(true);
        view.setEnableRowBody(true);
        setView(view);
    }

    protected void addNextLink() {
        ClickHandler nextlinkClickListener = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if ((store.getCount() - 1) / PAGE_SIZE <= pageNo) {
                    return;
                }
                rowSelected = -1;
                pageNo++;
                prevlink.setVisible(true);
                fillTempStore(endofpage, endofpage + PAGE_SIZE);
                getView().refresh();
            }
        };

        nextlink = new Hyperlink("Next>", "");
        nextlink.setStyleName("discussion-link-inactive");
        nextlink.addClickHandler(nextlinkClickListener);
        headerPanel.add(nextlink);
    }

    protected void addPreviousLink() {
        ClickHandler prevlinkClickListener = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (startofpage <= 0) {
                    return;
                }
                rowSelected = -1;
                pageNo--;
                fillTempStore(startofpage - PAGE_SIZE, startofpage);
                nextlink.setVisible(true);
                getView().refresh();
            }
        };

        prevlink = new Hyperlink("<Previous", "");
        prevlink.setStyleName("discussion-link-inactive");
        prevlink.addClickHandler(prevlinkClickListener);
        // v prevlink.setVisible(false);
        headerPanel.add(prevlink);
    }

    protected void displayPageNo() {
        if ((store.getCount() / PAGE_SIZE + 1) == 0) {
            label.setText("Displaying page 0 of 0 pages");
        } else {
            label.setText("Displaying page " + (pageNo + 1) + " of " + (store.getCount() / PAGE_SIZE + 1) + " pages");
        }
        headerPanel.add(label);
    }

    protected void addSpacer(int len) {
        String spaceString = "";
        for (int i = 0; i < len; i++) {
            spaceString += "&nbsp;";
        }
        Label spacer = new Label();
        spacer.setHtml(spaceString);
        headerPanel.add(spacer);
    }

    protected void addRowListeners() {
        addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                updateReplyAvailable(grid, rowIndex);
                updateDeleteAvailable(grid, rowIndex);
            }

            @Override
            public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                openMessageInNewWindow(grid, rowIndex, e);
            }

        });

    }

    protected void openMessageInNewWindow(GridPanel grid, int rowIndex, EventObject e) {
        rowSelected = rowIndex;
        setReplyEnabled(true);
        Record rec = store.getRecordAt(rowSelected + pageNo * PAGE_SIZE);
        String title = rec.getAsString("subject");
        title = "Topic: " + title + "  [Type: " + rec.getAsString("type") + "]";

        Panel p = new Panel();
        p.setHtml(rec.getAsString("body"));

        final Window window = new Window();
        window.setLayout(new FitLayout());
        window.setTitle(title);
        window.setHeight(400);
        window.setWidth(600);
        window.setMinHeight(360);
        window.setMinWidth(550);

        Panel footer = new Panel();
        addNewWindowListeners(footer, window);
        window.add(p);
        window.add(footer);
        window.setPaddings(5, 5, 5, 40);
        window.show();

    }

    protected void addNewWindowListeners(Panel footer, final Window window) {
        ClickHandler replyHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                reply();
                window.close();

            }
        };

        Hyperlink localreplylink = new Hyperlink("Reply", "");
        localreplylink.setStyleName("discussion-link");
        localreplylink.addClickHandler(replyHandler);

        ClickHandler closeHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                window.close();
            }
        };

        Hyperlink closelink = new Hyperlink("Close", "");
        closelink.setStyleName("discussion-link");
        closelink.addClickHandler(closeHandler);

        footer.setLayout(new HorizontalLayout(15));
        footer.add(localreplylink);
        footer.add(closelink);
        footer.setBodyBorder(true);
        footer.setBorder(true);
        footer.setPaddings(5, 5, 5, 0);

    }

    protected void addCellListeners() {
    /*
        addGridCellListener(new GridCellListenerAdapter() {
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            }

            @Override
            public void onCellDblClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
            }
        });
	*/
    }

    private void updateReplyAvailable(GridPanel grid, int currRowIndex) {
        rowSelected = getSelectedRowIndex(grid, currRowIndex);
        setReplyEnabled(rowSelected != -1);
    }

    private void updateDeleteAvailable(GridPanel grid, int currRowIndex) {
        rowSelected = getSelectedRowIndex(grid, currRowIndex);
        Record record = store.getRecordAt(rowSelected + pageNo * PAGE_SIZE);
        String authorName = record.getAsString("author");
        Integer repliesCount = record.getAsInteger("repliesCount");
        setDeleteEnabled( rowSelected != -1 &&
                authorName != null &&
                authorName.equals(GlobalSettings.getGlobalSettings().getUserName()) &&
                repliesCount != null &&
                repliesCount == 0);
    }

    private void setReplyEnabled(boolean enabled) {
        replyEnable = enabled;
        replyLink.setStyleName(replyEnable ? "discussion-link" : "discussion-link-inactive");
    }

    private void setDeleteEnabled(boolean enabled) {
        deleteEnable = enabled;
        deleteLink.setStyleName(deleteEnable ? "discussion-link" : "discussion-link-inactive");
    }

    private int getSelectedRowIndex(GridPanel grid, int currRowIndex) {
        int selRowIndex = -1;
        RowSelectionModel selectionModel = grid.getSelectionModel();
        int selectionCount = selectionModel.getCount();
        //set rowIndex only if we have exactly one row selected
        if (selectionCount == 1) {
            if (selectionModel.isSelected(currRowIndex)) {
                selRowIndex = currRowIndex;
            } else {
                int i = 0;
                while (i < selectionCount) {
                    if (selectionModel.isSelected(i)) {
                        selRowIndex = i;
                        break;
                    }
                    i++;
                }
            }
        }

        return selRowIndex;
    }

    protected void createColumns() {
        ColumnConfig textCol = new ColumnConfig();
        textCol.setHeader("Subject");
        textCol.setDataIndex("subject");
        textCol.setResizable(true);
        textCol.setSortable(true);
        textCol.setCss("white-space:normal; color: rgb(0,0,255); margin-left: 10px;");
        textCol.setRenderer(subjectRenderer);
        textCol.setWidth(120);

        ColumnConfig authorCol = new ColumnConfig();
        authorCol.setHeader("Author");
        authorCol.setDataIndex("author");
        authorCol.setResizable(true);
        authorCol.setSortable(true);
        authorCol.setWidth(70);

        ColumnConfig typeCol = new ColumnConfig();
        typeCol.setHeader("Type");
        typeCol.setDataIndex("type");
        typeCol.setResizable(true);
        typeCol.setSortable(true);
        typeCol.setWidth(100);

        ColumnConfig dateCol = new ColumnConfig();
        dateCol.setHeader("Date");
        dateCol.setDataIndex("date");
        dateCol.setResizable(true);
        dateCol.setSortable(true);
        dateCol.setWidth(100);

        ColumnConfig idCol = new ColumnConfig();
        idCol.setHeader("Id");
        idCol.setDataIndex("id");
        idCol.setResizable(true);
        idCol.setSortable(false);
        idCol.setHidden(true);
        idCol.setWidth(100);

        ColumnConfig replCntCol = new ColumnConfig();
        replCntCol.setHeader("Nr.Replies");
        replCntCol.setDataIndex("repliesCount");
        replCntCol.setResizable(true);
        replCntCol.setSortable(false);
        replCntCol.setHidden(true);
        replCntCol.setWidth(60);

        ColumnConfig[] columns = new ColumnConfig[] { textCol, authorCol, typeCol, dateCol, idCol, replCntCol };
        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);
    }

    private void reply() {
        if (rowSelected == -1) {
            MessageBox.alert("Please select one (and only one) note to reply to.");
            return;
        } else if (!replyEnable) {
            MessageBox.alert("Reply is not available.");
            return;
        }
        Record rec = store.getRecordAt(rowSelected + pageNo * PAGE_SIZE);
        if (rec == null) {
            MessageBox.alert("\"null\" record found. Please select one (and only one) note to reply to.");
            return;
        }

        String subject = rec.getAsString("subject");
        if (subject != null) {
            subject = "Re: " + subject;
        }
        String text = rec.getAsString("body");
        if (text != null && text.length() > 0) {
            text = "<br><br>===== " + rec.getAsString("author") + " wrote on " + rec.getAsString("date")
                    + ": ======<br>" + text;
        } else {
            text = "";
        }

        showInWindow(true, subject, text);
    }


    protected void deleteNote() {
        if (rowSelected == -1) {
            MessageBox.alert("Please select a note to delete.");
            return;
        } else if (!deleteEnable) {
            MessageBox.alert("Delete is not allowed! You may delete only notes that were created by you and which did not get any replies yet.");
            return;
        }
        Record rec = store.getRecordAt(rowSelected + pageNo * PAGE_SIZE);
        if (rec == null) {
            MessageBox.alert("\"null\" record found. Please select one note to delete.");
            return;
        }

        final String id = rec.getAsString("id");
        if (id == null) {
            MessageBox.alert("Error", "Something went wrong at retrieving the notes. Please try again later.");
            return;
        }

        final String subj = rec.getAsString("subject");
        MessageBox.confirm("Confirm delete note",
                "Are you sure you want to delete the note with subject \"" + subj + "\"?",
                new MessageBox.ConfirmCallback() {
                    public void execute(String btnID) {
                        if (btnID.equalsIgnoreCase("yes")) {
                            ChAOServiceManager.getInstance().deleteNote(project.getProjectName(), id, new DeleteNoteHandler(id));
                        }
                    }
                }
        );
    }

    protected ComboBox getTypeComboBox() {
        Store cbstore = new SimpleStore(new String[] { "commentType" }, getCommentTypes());
        cbstore.load();
        ComboBox cb = new ComboBox();
        cb.setStore(cbstore);
        cb.setForceSelection(true);
        cb.setMinChars(1);
        cb.setFieldLabel("Type");
        cb.setDisplayField("commentType");
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setEmptyText("Select Type");
        cb.setTypeAhead(true);
        cb.setSelectOnFocus(true);
        cb.setWidth(200);
        cb.setHideTrigger(false);
        return cb;
    }

    // TODO: change me
    protected void onReplyButton(NotesData note) {
        if (GlobalSettings.getGlobalSettings().getUserName() == null) {
            MessageBox.alert("To post a message you need to be logged in.");
            return;
        }
        String subject = note.getSubject();
        subject = (subject == null || subject.length() == 0 ? "(no subject)" : subject);
        note.setSubject(subject);
        final EntityData noteId = note.getNoteId();
        final String entityName = (noteId == null ? "" : noteId.getName());
        Record plant = recordDef.createRecord(new Object[] { "newAnnId",
                "<FONT COLOR=\"#cc6600\">" + subject + "</FONT>", GlobalSettings.getGlobalSettings().getUserName(),
                note.getCreationDate(), note.getBody(), note.getType(), entityName, getRepliesCount(note) });
        store.insert(rowSelected + 1 + pageNo * PAGE_SIZE, plant);
        if (rowSelected + 1 == PAGE_SIZE) {
            pageNo++;
        }
        fillTempStore(pageNo*PAGE_SIZE, (pageNo+1)*PAGE_SIZE);
        if (store.getCount() % PAGE_SIZE == 1) {
            nextlink.setVisible(true);
        }
        getView().refresh();

        ChAOServiceManager.getInstance().createNote(project.getProjectName(), note, false, new CreateNote());
    }

    private int getRepliesCount(NotesData note) {
        List<NotesData> replies = note.getReplies();
        return replies == null ? 0 : replies.size();
    }

    private void createNewPost() {
        showInWindow(false, "", "");
    }

    // TODO: change this
    protected void onPostButton(NotesData note) {
        if (GlobalSettings.getGlobalSettings().getUserName() == null) {
            MessageBox.alert("To post a message you need to be logged in.");
            return;
        }
        String subject = note.getSubject();
        if (subject == null || subject.length() == 0) {
            subject = "(no subject)";
        }
        note.setSubject(subject);
        final EntityData noteId = note.getNoteId();
        // MH: Too many checks for null!  Needs tidying.
        final String entityName = (noteId == null ? "" : noteId.getName());
        Record plant = recordDef.createRecord(new Object[] { "newAnnId", subject,
                GlobalSettings.getGlobalSettings().getUserName(), note.getCreationDate(),
                note.getBody(), note.getType(), entityName, getRepliesCount(note) });
        store.insert(store.getCount(), plant);
        pageNo = store.getCount() / PAGE_SIZE;
        if (store.getCount() % PAGE_SIZE == 0) {
            pageNo--;
        }
        fillTempStore(pageNo*PAGE_SIZE, (pageNo+1)*PAGE_SIZE);
        if (store.getCount() > PAGE_SIZE) {
            nextlink.setVisible(true);
        }
        getView().refresh();

        // make the remote call
        ChAOServiceManager.getInstance().createNote(project.getProjectName(), note, topLevel, new CreateNote());
    }

    private EntityData getAnnotatedEntity(boolean isReply) {
        EntityData annotatedEntity = null;

        if (isReply) {
            Record selectedAnnotationRecord = store.getRecordAt(rowSelected + pageNo * PAGE_SIZE);
            if (selectedAnnotationRecord != null) {
                String annotatedEntityId = selectedAnnotationRecord.getAsString("entity");
                if (annotatedEntityId != null) {
                    annotatedEntity = new EntityData(annotatedEntityId);
                }
            }
            if (annotatedEntity == null) {
                annotatedEntity = new EntityData(_currentEntity != null ? _currentEntity.getName() : null);
            }
        } else {
            if (_currentEntity != null) {
                annotatedEntity = new EntityData(_currentEntity.getName());
            }
        }

        return annotatedEntity;
    }

    protected void showInWindow(final boolean isReply, String subject, String text) {
        //        // create the FormPanel and set the label position to top
        //        FormPanel formPanel = new FormPanel();
        //        formPanel.setFrame(true);
        //        formPanel.setPaddings(5, 5, 5, 0);
        //        formPanel.setWidth(600);
        //        formPanel.setHeight(500);
        //
        //        final TextField subjectField = new TextField("Subject", "subject");
        //        subjectField.setValue(subject);
        //        subjectField.setWidth(300);
        //        formPanel.add(subjectField, new AnchorLayoutData("100%"));
        //
        //        final ComboBox typeComboBox = getTypeComboBox();
        //        formPanel.add(typeComboBox, new AnchorLayoutData("100%"));
        //
        //        final HtmlEditor htmlEditor = new HtmlEditor("", "message");
        //        htmlEditor.setHideLabel(true);
        //        htmlEditor.setHeight(200);
        //        htmlEditor.setValue(text);
        //        htmlEditor.focus();
        //        formPanel.add(htmlEditor, new AnchorLayoutData("100% -53"));

        final Window window = new Window();
        window.setTitle(isReply ? "Reply" : "New topic");
        window.setWidth(600);
        window.setHeight(450);
        window.setMinWidth(300);
        window.setMinHeight(200);
        window.setLayout(new FitLayout());
        window.setPaddings(5);
        window.setButtonAlign(Position.CENTER);
        //
        //        Button send = new Button("Send", new ButtonListenerAdapter() {
        //            @Override
        //            public void onClick(Button button, EventObject e) {
        //                String messageBody = htmlEditor.getValueAsString();
        //                if (isReply) {
        //                    onReplyButton(messageBody, subjectField.getText(), typeComboBox.getValueAsString());
        //                } else {
        //                    onPostButton(messageBody, subjectField.getText(), typeComboBox.getValueAsString());
        //                }
        //                window.close();
        //            }
        //        });
        //
        //        Button cancel = new Button("Cancel", new ButtonListenerAdapter() {
        //            @Override
        //            public void onClick(Button button, EventObject e) {
        //                window.close();
        //            }
        //        });
        //
        //        window.addButton(send);
        //        window.addButton(cancel);

        //window.setCloseAction(Window.HIDE);
        window.setPlain(true);

        NoteInputPanel nip = new NoteInputPanel(project, "Please enter your note:", false, subject, text,
                getAnnotatedEntity(isReply), new AsyncCallback<NotesData>() {
                    public void onFailure(Throwable caught) {
                        // TODO Auto-generated method stub
                        window.close();
                    }

                    public void onSuccess(NotesData result) {
                        if (isReply) {
                            onReplyButton(result);
                        } else {
                            onPostButton(result);
                        }
                        window.close();
                    }
                });
        window.add(nip);

        window.show();
        nip.getMainComponentForFocus().focus();
    }

    void fillTempStore(int i, int j) {
        setReplyEnabled(false);
        setDeleteEnabled(false);

        tempstore.removeAll();
        int gen = 0;
        endofpage = j;
        startofpage = i;
        for (int count = i; count < j; count++) {
            Record record1 = store.getRecordAt(count);

            if (record1 != null) {
                tempstore.insert(gen++, record1);
            }
        }
        int max = store.getCount() / PAGE_SIZE;
        if (store.getCount() % PAGE_SIZE != 0) {
            max++;
        }

        if (max == 0) {
            label.setText("Displaying page 0 of 0 pages");
        } else {
            label.setText("Displaying page " + (pageNo + 1) + " of " + max + " pages");
        }

        if (prevlink != null) {
            prevlink.setStyleName((pageNo == 0 || max == 0) ? "discussion-link-inactive" : "discussion-link");
        }
        if (nextlink != null) {
            nextlink.setStyleName((pageNo + 1 == max || max == 0) ? "discussion-link-inactive" : "discussion-link");
        }
    }


    public void setEntity(EntityData newEntity) {
        // Shortcut
        if ((_currentEntity != null && _currentEntity.equals(newEntity)) && !topLevel) {
            return;
        }
        store.removeAll();
        tempstore.removeAll();
        _currentEntity = newEntity;
        if (_currentEntity == null && !topLevel) {
            return;
        }
        pageNo = 0;
        startofpage = 0;
        endofpage = PAGE_SIZE;
        reload();
    }

    public void reload() {
        ChAOServiceManager.getInstance().getNotes(project.getProjectName(), _currentEntity == null ? null :_currentEntity.getName(), topLevel,
                new GetNotes());
    }

    private void addReplyToStore(NotesData note, int indent) {
        if(note.getReplies() != null){
            List<NotesData> replies = note.getReplies();
            Iterator<NotesData> it = replies.iterator();
            String head = "Re: ";
            for (int count = 1; count < indent; count++) {
                head = head + "Re: ";
            }

            while (it.hasNext()) {
                NotesData n = it.next();
                String time = shortenTime(n.getCreationDate());
                String subject = n.getSubject();
                if (subject == null || subject.length() == 0) {
                    subject = "(no subject)";
                }
                final EntityData noteId = n.getNoteId();
                final String entityName = (noteId == null ? "" : noteId.getName());
                Record record = recordDef.createRecord(new Object[] { entityName, subject, n.getAuthor(),
                        time, n.getBody(), n.getType(), entityName, getRepliesCount(n) });
                store.add(record);
                addReplyToStore(n, indent + 1);
            }
        }
    }

    private String[][] getCommentTypes() {
        if (commentTypes == null) {
            commentTypes = new String[][] { new String[] { "Comment" }, new String[] { "Question" },
                    new String[] { "Example" }, new String[] { "AgreeDisagreeVoteProposal" },
                    new String[] { "AgreeDisagreeVote" } };
        }
        return commentTypes;
    }

    public void refresh() {
        store.removeAll();
        tempstore.removeAll();
        reload();
    }

    private String shortenTime(String time) {
        return time;
    }

    /*
     * Asynchronous calls
     */
    class GetNotes extends AbstractAsyncHandler<List<NotesData>> {

        public void handleFailure(Throwable caught) {
            GWT.log("Error getting notes for " + _currentEntity, caught);
        }

        public void handleSuccess(List<NotesData> notes) {
            Iterator<NotesData> iterator = notes.iterator();

            while (iterator.hasNext()) {
                NotesData note = (NotesData) iterator.next();
                String time = shortenTime(note.getCreationDate());
                String subject = note.getSubject();
                if (subject == null || subject.length() == 0) {
                    subject = "(no subject)";
                }
                final EntityData noteId = note.getNoteId();
                final String noteIdName = (noteId == null ? "" : noteId.getName());
                Record record = recordDef.createRecord(new Object[] { noteIdName, subject,
                        note.getAuthor(), time, note.getBody(), note.getType(), noteIdName, getRepliesCount(note) });
                store.add(record);
                addReplyToStore(note, 1);

            }
            if (store.getCount() > PAGE_SIZE) {
                nextlink.setVisible(true);
            }
            //pageNo = 0;
            fillTempStore(startofpage, endofpage);
            rowSelected = -1;
            getView().refresh();

        }
    }

    class CreateNote extends AbstractAsyncHandler<NotesData> {

        public void handleFailure(Throwable caught) {
            GWT.log("Error at creating note", caught);
            com.google.gwt.user.client.Window.alert("There were problems at creating the note.\n"
                    + "Please try again later.");
        }

        public void handleSuccess(NotesData result) {
            GWT.log("Note created successfully", null);
            store.removeAll();
            tempstore.removeAll();
            reload();
        }
    }

    class DeleteNoteHandler extends AbstractAsyncHandler<Void> {

        private String id;

        public DeleteNoteHandler(String id) {
            this.id = id;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at removing note with id: " + id, caught);
            MessageBox.alert("Error", "There was a problem at removing the note with id: " + id);
        }

        @Override
        public void handleSuccess(Void result) {
            MessageBox.alert("Success", "Note deleted successfully");
            store.removeAll();
            tempstore.removeAll();
            reload();
        }

    }
}

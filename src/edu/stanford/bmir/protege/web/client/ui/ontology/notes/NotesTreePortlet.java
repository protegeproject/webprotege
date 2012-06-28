package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

public class NotesTreePortlet extends AbstractEntityPortlet {

    private static final int MIN_WIDTH_PORTLET = 650;
    private static final int VERTICAL_SCROLLBAR_WIDTH = 25;
    protected boolean ontologyLevel = true;
    protected NotesTreePanel notesPanel = null;
    protected boolean isInitialized = false;
    protected ScrollPanel scrollPanel = null;
    protected FlexTable header = null;
    private FlexTable innerHeader;

    public NotesTreePortlet(Project project){
        this(project, false);
    }

    public NotesTreePortlet(Project project, boolean topLevel){
        super(project);
        this.ontologyLevel = topLevel;
        setTitle("Notes Tree");
        initWidgets();
        this.setCls("custom-NotesTreePortlet x-panel");

        //resizeComponents(650, 150);
        this.addListener(new PanelListenerAdapter(){

            @Override
            public void onBodyResize(Panel panel, String width, String height){
                resizeComponents(panel.getInnerWidth(), panel.getInnerHeight());
            }
        });

        doLayout();

    }

    @Override
    protected void afterRender() {
        // TODO Auto-generated method stub
        int innerHeight = this.getInnerHeight();
        int innerWidth = this.getInnerWidth();
        if (innerHeight == 0) {
            innerHeight = 150;
        }
        resizeComponents(innerWidth, innerHeight);
        //doLayout();
    }




    private void initWidgets(){
        initHeader();
        initScrollPanel();
    }

    private void initHeader(){
        header = new FlexTable();
        header.setHeight("25px");
        header.setStylePrimaryName("custom-NotesTreePortletHeader");

        innerHeader = new FlexTable();
        innerHeader.setStylePrimaryName("custom-NotesTreePortletInnerHeader");
        FlexCellFormatter cellFormatter = innerHeader.getFlexCellFormatter();



        ClickHandler newTopicClickHanlder = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (GlobalSettings.getGlobalSettings().getUserName() == null) {
                    MessageBox.alert("To post a message you need to be logged in.");
                    return;
                }
                createNewPost();
            }
        };
        Anchor newTopicAnchor = new Anchor("New Topic");
        newTopicAnchor.setStylePrimaryName("custom-NewPostActionAnchor");
        newTopicAnchor.addClickHandler(newTopicClickHanlder);
        innerHeader.setWidget(0, 0, newTopicAnchor);
        cellFormatter.setWidth(0, 0, "50px");

        ClickHandler expandAllClickHanlder = new ClickHandler() {
            public void onClick(ClickEvent event) {
                expandAll(true);
            }
        };
        Anchor expandAllAnchor = new Anchor("Expand All");
        expandAllAnchor.setStylePrimaryName("custom-ExpandAllActionAnchor");
        expandAllAnchor.addClickHandler(expandAllClickHanlder);
        innerHeader.setWidget(0, 1, expandAllAnchor);
        cellFormatter.setWidth(0, 1, "50px");

        ClickHandler collapseAllClickHanlder = new ClickHandler() {
            public void onClick(ClickEvent event) {
                expandAll(false);
            }
        };
        Anchor collapseAllAnchor = new Anchor("Collapse All");
        collapseAllAnchor.setStylePrimaryName("custom-CollapseAllActionAnchor");
        collapseAllAnchor.addClickHandler(collapseAllClickHanlder);
        innerHeader.setWidget(0, 2, collapseAllAnchor);
        cellFormatter.setWidth(0, 2, "50px");

        innerHeader.setWidth("300px");

        header.setWidget(0, 0, innerHeader);
        //header.setWidth("650px !important");
        header.setWidth("650px");
        add(header);
    }

    private void expandAll(boolean expandAll){
        this.notesPanel.expandOrCollapseAll(expandAll);
    }

    private void createNewPost(){
        NoteInputHandler nih = new NoteInputHandler(this.project, this._currentEntity, new AsyncCallback<NotesData>(){
            public void onFailure(Throwable caught) {
                GWT.log("Error at creating note", caught);
                com.google.gwt.user.client.Window.alert("There were problems at creating the note.\n"
                        + "Please try again later.");
            }
            public void onSuccess(NotesData result) {
                if (result == null) {
                    com.google.gwt.user.client.Window.alert("There were problems at creating the note.\n"
                            + "Please try again later.");
                    return;
                }
                NotesTreeRecord r = new NotesTreeRecord(result, project, _currentEntity, null);
                notesPanel.addNewPost(r);
                DisclosurePanel w = (DisclosurePanel) r.getUIObject();
                FlexTable notesTable = (FlexTable)scrollPanel.getWidget();
                w.setOpen(true);
                int i = notesTable.insertRow(0);
                notesTable.setWidget(i, 0, w);
                scrollPanel.scrollToTop();
            }
        });
        nih.setTopLevel(this.ontologyLevel);
        nih.showInWindow(false, "", "");
    }

    public void removeNote(NotesTreeRecord record){
        FlexTable notesTable = (FlexTable)scrollPanel.getWidget();
        DisclosurePanel w = (DisclosurePanel) record.getUIObject();
        int rowCount = notesTable.getRowCount();

        int i=0;
        for(i=0; i<rowCount; i++){
            DisclosurePanel iter = (DisclosurePanel)notesTable.getWidget(i, 0);
            if(iter.equals(w)){
                break;
            }
        }

        int j=i+1;
        this.notesPanel.deletePost(i);
        for(;j<rowCount;j++){
            notesTable.setWidget(j-1, 0, notesTable.getWidget(j, 0));
        }

        if(j==rowCount){
            notesTable.removeRow(j-1);
        }
    }

    public void moveNoteToTop(NotesTreeRecord record){
        FlexTable notesTable = (FlexTable)scrollPanel.getWidget();
        DisclosurePanel w = (DisclosurePanel) record.getUIObject();

        int rowCount = notesTable.getRowCount();
        if(rowCount ==0){
            notesTable.setWidget(0, 1, w);
            return;
        }

        DisclosurePanel startWidget = (DisclosurePanel)notesTable.getWidget(0, 0);
        if(startWidget.equals(w)){
            return;
        }

        int i;
        for(i=1; i<rowCount; i++){
            DisclosurePanel iter = (DisclosurePanel)notesTable.getWidget(i, 0);
            notesTable.setWidget(i, 0, startWidget);
            startWidget = iter;
            if(startWidget.equals(w)){
                notesTable.setWidget(0, 0, w);
                break;
            }
        }

        if(i==rowCount){
            notesTable.setWidget(i, 0, startWidget);
            notesTable.setWidget(0, 0, w);
        }
        scrollPanel.scrollToTop();
    }

    private void initScrollPanel(){
        scrollPanel = new ScrollPanel();
        scrollPanel.setStylePrimaryName("custom-ScrollPanel");
        add(scrollPanel);

        notesPanel = new NotesTreePanel(project, ontologyLevel);
        notesPanel.setContainer(this);
        Widget w = notesPanel.getWidget();
        scrollPanel.add(w);
    }


    @Override
    protected void doOnResize(int width, int height){
        setWidth(width);
        setHeight(height);

        int innerWidth = this.getInnerWidth();
        int innerHeight = this.getInnerHeight();

        resizeComponents(innerWidth, innerHeight);

        doLayout();
    }

    public void resizeComponents(int innerWidth, int innerHeight){

        if(innerWidth>=VERTICAL_SCROLLBAR_WIDTH){
            header.setWidth(Math.max(MIN_WIDTH_PORTLET, (innerWidth-VERTICAL_SCROLLBAR_WIDTH))+"px");
            scrollPanel.setWidth(Integer.toString(Math.max(MIN_WIDTH_PORTLET, innerWidth-VERTICAL_SCROLLBAR_WIDTH))+"px");
            scrollPanel.getWidget().setWidth(Math.max(MIN_WIDTH_PORTLET, (innerWidth-VERTICAL_SCROLLBAR_WIDTH))+"px");
        } else if (innerWidth >0){
            header.setWidth(Math.max(MIN_WIDTH_PORTLET, (innerWidth-VERTICAL_SCROLLBAR_WIDTH))+"px");
            scrollPanel.setWidth(Integer.toString(Math.max(MIN_WIDTH_PORTLET, innerWidth-VERTICAL_SCROLLBAR_WIDTH))+"px");
            scrollPanel.getWidget().setWidth(Math.max(MIN_WIDTH_PORTLET, innerWidth-VERTICAL_SCROLLBAR_WIDTH)+"px");
        }
        if(innerHeight>=100){
            scrollPanel.setHeight(Integer.toString(innerHeight-100)+"px");

        } else if (innerHeight >0){
            int portletHeight = scrollPanel.getWidget().getOffsetHeight();
            //scrollPanel.setHeight(Integer.toString(innerHeight)+"px");
            scrollPanel.setHeight(Integer.toString(portletHeight)+"px");
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {
        if (_currentEntity != null) {
            setTitle(ontologyLevel ? "Ontology notes" : "Notes Tree for "
                    + _currentEntity.getBrowserText());
        }
        notesPanel.setEntity(_currentEntity);
    }

    public Collection<EntityData> getSelection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void onRefresh() {
        reload();
        notesPanel.refresh();
    }
}

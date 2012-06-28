package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;


public class NotesTreePanel{

    protected EntityData _currentEntity;

    private FlexTable notesContainerPanel;
    private final Project project;
    private final boolean ontologyLevel;
    private NotesTreePortlet container=null;
    private List<NotesTreeRecord> notesTreeRecordList = null;

    public NotesTreePanel(Project project, boolean ontologyLevel){
        this.project = project;
        this.ontologyLevel = ontologyLevel;
        initUI();
    }

    private void initUI(){
        notesContainerPanel = new FlexTable();
        notesContainerPanel.setStylePrimaryName("custom-FlexTablePortlet");
        notesContainerPanel.setCellSpacing(2);
    }

    public void setEntity(EntityData currentEntity) {
        if (_currentEntity != null && _currentEntity.equals(currentEntity)) {
            return;
        }
        _currentEntity = currentEntity;
        refresh();
    }

    public Widget getWidget(){
        return this.notesContainerPanel;
    }

    public NotesTreePortlet getContainer() {
        return container;
    }

    public void setContainer(NotesTreePortlet container) {
        this.container = container;
    }

    public void expandOrCollapseAll(boolean doExpand){
        if(notesTreeRecordList == null){
            return;
        }
        for(NotesTreeRecord rec : this.notesTreeRecordList){
            rec.setOpen(doExpand);
        }
    }

    public void addNewPost(NotesTreeRecord r){
        if(this.notesTreeRecordList == null){
            notesTreeRecordList = new ArrayList<NotesTreeRecord>();
        }

        notesTreeRecordList.add(0, r);
    }

    public void deletePost(int index){
        if(index > 0 && index < this.notesTreeRecordList.size()){
            this.notesTreeRecordList.remove(index);
        }
    }

    public void refresh(){
        if (_currentEntity == null) { return; } //TODO: update view
        ChAOServiceManager.getInstance().getNotes(project.getProjectName(), _currentEntity.getName(), ontologyLevel, true,
                new AsyncCallback<List<NotesData>>(){
                    public void onFailure(Throwable caught) {
                        GWT.log("Error getting notes for " + _currentEntity, caught);
                    }

                    public void onSuccess(List<NotesData> result) {
                        notesContainerPanel.removeAllRows();
                        notesTreeRecordList = new ArrayList<NotesTreeRecord>();
                        int innerWidth = container.getInnerWidth();
                        int innerHeight = container.getInnerHeight() < 150 ? 150 : container.getInnerHeight();

                        container.resizeComponents(innerWidth, innerHeight);
                        for(NotesData r : result){
                            NotesTreeRecord record = new NotesTreeRecord(r, project, _currentEntity, null);
                            record.setContainer(container);
                            int rowCount = notesContainerPanel.getRowCount();
                            notesContainerPanel.setWidget(rowCount, 0, record.getUIObject());
                            notesTreeRecordList.add(record);
                        }
                    }
        });
    }

}

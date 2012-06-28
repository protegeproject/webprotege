package edu.stanford.bmir.protege.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 */
public class WebProtegeAdmin implements EntryPoint {

    public void onModuleLoad() {
        RootPanel widget = RootPanel.get("projects-list");
        final FlexTable flexTable = new FlexTable();
        final ProjectManagerServiceAsync service = GWT.create(ProjectManagerService.class);
        service.getProjectNames(new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(List<String> result) {
                for(String name : result) {
                    final int rowCount = flexTable.getRowCount();
                    flexTable.setWidget(rowCount, 0, new Label(name));
                    service.getLastAccessTime(new ProjectId(name), new AsyncCallback<Long>() {
                        public void onFailure(Throwable caught) {
                        }

                        public void onSuccess(Long result) {
                            flexTable.setWidget(rowCount, 1, new Label(result.toString()));
                        }
                    });
                }    
            }
        });
        
        widget.add(flexTable);
    }
}

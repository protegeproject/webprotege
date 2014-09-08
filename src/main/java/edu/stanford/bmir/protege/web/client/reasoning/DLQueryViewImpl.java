package edu.stanford.bmir.protege.web.client.reasoning;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.gwtcodemirror.client.GWTCodeMirror;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class DLQueryViewImpl extends Composite implements DLQueryView {
    interface DLQueryViewImplUiBinder extends UiBinder<HTMLPanel, DLQueryViewImpl> {
    }

    private static DLQueryViewImplUiBinder ourUiBinder = GWT.create(DLQueryViewImplUiBinder.class);

    @UiField(provided = true)
    protected GWTCodeMirror editor;

    @UiField
    protected HasHTML resultsDisplay;

    @UiField
    protected Button executeQueryButton;

    @UiField
    protected HasText filterField;

    @UiField
    protected HasText revisionNumberField;

    private ExecuteDLQueryHandler executeDLQueryHandler = new ExecuteDLQueryHandler() {
        @Override
        public void handleExecuteQuery(String query) {

        }
    };

    public DLQueryViewImpl() {
        editor = new GWTCodeMirror();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        executeQueryButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                executeDLQueryHandler.handleExecuteQuery(editor.getValue().trim());
            }
        });
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        editor.setLineWrapping(true);
        editor.setValue("owl:Thing");
    }

    @Override
    public void setMode(DLQueryViewMode mode) {
        editor.setEnabled(mode == DLQueryViewMode.READY);
    }

    @Override
    public void setExecuteDLQueryHandler(ExecuteDLQueryHandler handler) {
        executeDLQueryHandler = handler;
    }

    @Override
    public void clearResults() {
        resultsDisplay.setText("");
    }

    @Override
    public Optional<String> getFilter() {
        String filterText = filterField.getText().trim();
        if(filterText.isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.of(filterText);
        }
    }

    @Override
    public void setAnswers(
            Optional<RevisionNumber> revisionNumber, List<OWLClassData> subClasses, List<OWLClassData> superClasses) {

        StringBuilder sb = new StringBuilder();
        if(!revisionNumber.isPresent()) {
            revisionNumberField.setText("Reasoning not yet available");
        }
        else {
            revisionNumberField.setText("Answers for revision number " + revisionNumber.get().getValue());
            sb.append("</div>");
            renderResultsSection("Direct SubClassOf", superClasses, superClasses.size(), sb);
            renderResultsSection("Direct SuperClassOf", subClasses, subClasses.size(), sb);
        }
        resultsDisplay.setHTML(sb.toString());
    }

    private void renderResultsSection(String sectionName, List<? extends OWLEntityData> displayableResults, int totalResults, StringBuilder sb) {
        List<? extends OWLEntityData> sortedResults = Lists.newArrayList(displayableResults);
        Collections.sort(sortedResults);
        sb.append("<div style=\"font-size: 12px; color: #afafaf;\">").append(sectionName).append("  (").append(totalResults).append(")").append("</div>");
        sb.append("<div style=\"padding-top: 5px; padding-left: 20px; padding-bottom: 20px; padding-right: 5px;\">");
        for(OWLEntityData data : sortedResults) {
            sb.append("<div class=\"class-icon-inset\" style=\"height: 18px; line-height: 18px; background-position: 0px 0px; border-bottom: 1px solid #f1f1f1;\">");
            sb.append(data.getBrowserText());
            sb.append("</div>\n");
        }
        sb.append("</div>");

    }
}

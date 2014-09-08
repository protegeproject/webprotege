package edu.stanford.bmir.protege.web.client.reasoning;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.gwtcodemirror.client.GWTCodeMirror;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.reasoning.*;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
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
        executeQueryButton.setEnabled(mode == DLQueryViewMode.READY);
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
    public void setQueryString(String queryString) {
        editor.setValue(queryString);
    }

    @Override
    public Optional<String> getFilter() {
        String filterText = filterField.getText().trim();
        if (filterText.isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.of(filterText);
        }
    }

    @Override
    public void setResult(DLQueryResult result, RevisionNumber revisionNumber) {
        revisionNumberField.setText("Results (revision " + revisionNumber.getValue() + ")");
        StringBuilder sb = new StringBuilder();

        for (DLQueryEntitySetResult section : result.getSections()) {
            renderResultsSection(section, sb);
        }
        resultsDisplay.setHTML(sb.toString());
    }

    @Override
    public void setReasoningUnavailable() {
        resultsDisplay.setHTML("Reasoning is not available for this project");
    }

    @Override
    public void setReasonerBusy() {
        resultsDisplay.setHTML("The reasoner is busy");
    }

    @Override
    public void setProjectInconsistent() {
        resultsDisplay.setHTML("The query has no results because the ontology is inconsistent");
    }

    @Override
    public void setReasonerError(String reasonerErrorMessage) {
        resultsDisplay.setHTML(reasonerErrorMessage);
    }

    private void renderResultsSection(DLQueryEntitySetResult result, StringBuilder sb) {

        Optional<ImmutableCollection<OWLEntityData>> entityData = result.getEntityData();
        List<? extends OWLEntityData> sortedResults = Lists.newArrayList(entityData.get());
        Collections.sort(sortedResults);
        String size = "-";
        if (entityData.isPresent()) {
            size = Integer.toString(entityData.get().size());
        }
        sb.append("<div style=\"font-size: 12px; color: #afafaf;\">").append(result.getSection().getDisplayName()).append("  (").append(
                size).append(")").append("</div>");
        sb.append("<div style=\"padding-top: 5px; padding-left: 20px; padding-bottom: 20px; padding-right: 5px;\">");
        if (entityData.isPresent()) {
            for (OWLEntityData data : sortedResults) {
                String iconName = "class-icon-inset";
                if(data instanceof OWLNamedIndividualData) {
                    iconName = "individual-icon-inset";
                }
                sb.append(
                        "<div class=\"" + iconName + "\" style=\"height: 18px; line-height: 18px; " +
                                "background-position: 0px 0px; border-bottom: 1px solid #f1f1f1;\">");
                sb.append(data.getBrowserText());
                sb.append("</div>\n");
            }
        }
        sb.append("</div>");
    }
}

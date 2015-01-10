package edu.stanford.bmir.protege.web.client.usage;

import com.google.common.base.Optional;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import edu.stanford.bmir.protege.web.client.csv.CSVGridResources;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomTypeGroup;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import edu.stanford.bmir.protege.web.shared.usage.UsageReferenceComparator;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class UsageViewImpl extends Composite implements UsageView {


    private UsageFilterEditorView filterEditorView;

    private PopupPanel usageEditorViewPopupPanel;

    interface UsageViewImplUiBinder extends UiBinder<HTMLPanel, UsageViewImpl> {

    }

    private static UsageViewImplUiBinder ourUiBinder = GWT.create(UsageViewImplUiBinder.class);

    @UiField(provided = true)
    protected DataGrid<UsageReference> dataGrid;

    private final ListDataProvider<UsageReference> dataProvider;

    private OWLEntity currentSubject;

    @UiField
    protected ButtonBase filterButton;

    @UiField
    protected Label filterMessage;

    public UsageViewImpl() {
        filterEditorView = new UsageFilterEditorViewImpl();
        usageEditorViewPopupPanel = new PopupPanel(true);
        usageEditorViewPopupPanel.setWidget(filterEditorView);
        usageEditorViewPopupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                handleUsageFilterChanged();
            }
        });


        dataGrid = new DataGrid<UsageReference>(Integer.MAX_VALUE, CSVGridResources.INSTANCE);

        dataGrid.addColumn(new EntityColumn(), "Entity");
        dataGrid.addColumn(new EntityTypeColumn(), "Type");
        dataGrid.addColumn(new AxiomTypeColumn(), "Axiom Type");
        dataGrid.addColumn(new Column<UsageReference, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(UsageReference object) {
                SafeHtmlBuilder builder = new SafeHtmlBuilder();
                builder.appendHtmlConstant(object.getAxiomRendering());
                return builder.toSafeHtml();
            }
        }, "Axiom");

        dataGrid.getColumn(0).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        dataGrid.getColumn(1).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        dataGrid.getColumn(2).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        dataGrid.getColumn(3).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        dataGrid.setColumnWidth(0, 180, Style.Unit.PX);
        dataGrid.setColumnWidth(1, 70, Style.Unit.PX);
        dataGrid.setColumnWidth(2, 150, Style.Unit.PX);
        dataGrid.setColumnWidth(3, 100, Style.Unit.PCT);

        dataProvider = new ListDataProvider<UsageReference>(new ArrayList<UsageReference>());
        dataProvider.addDataDisplay(dataGrid);


        ColumnSortEvent.ListHandler<UsageReference> handler = new ColumnSortEvent.ListHandler<UsageReference>(dataProvider.getList());
        handler.setComparator(dataGrid.getColumn(0), new Comparator<UsageReference>() {
            @Override
            public int compare(UsageReference o1, UsageReference o2) {
                if(o1.getSubjectRendering().isEmpty()) {
                    if(!o2.getSubjectRendering().isEmpty()) {
                        return 1;
                    }
                }
                else if(o2.getSubjectRendering().isEmpty()) {
                    return -1;
                }
                return o1.getSubjectRendering().compareToIgnoreCase(o2.getSubjectRendering());
            }
        });
        handler.setComparator(dataGrid.getColumn(1), new Comparator<UsageReference>() {
            @Override
            public int compare(UsageReference o1, UsageReference o2) {
                Optional<OWLEntity> e1 = o1.getAxiomSubject();
                Optional<OWLEntity> e2 = o2.getAxiomSubject();
                if(e1.isPresent()) {
                    if(e2.isPresent()) {
                        int diff = e1.get().compareTo(e2.get());
                        if(diff != 0) {
                            return diff;
                        }
                        else {
                            return new UsageReferenceComparator(currentSubject).compare(o1, o2);
                        }
                    }
                    else {
                        return -1;
                    }
                }
                else {
                    if(e2.isPresent()) {
                        return 1;
                    }
                    else {
                        return new UsageReferenceComparator(currentSubject).compare(o1, o2);
                    }
                }

            }
        });
        handler.setComparator(dataGrid.getColumn(2), new Comparator<UsageReference>() {
            @Override
            public int compare(UsageReference o1, UsageReference o2) {
                int diff = o1.getAxiomType().getIndex() - o2.getAxiomType().getIndex();
                if(diff != 0) {
                    return diff;
                }
                return new UsageReferenceComparator(currentSubject).compare(o1, o2);
            }
        });
        dataGrid.addColumnSortHandler(handler);

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("filterButton")
    protected void handleFilterClicked(ClickEvent event) {
        usageEditorViewPopupPanel.setPopupPosition(filterButton.getAbsoluteLeft(), filterButton.getAbsoluteTop() + filterButton.getOffsetHeight());
        usageEditorViewPopupPanel.show();
    }

    @Override
    public void setData(OWLEntity entity, Collection<UsageReference> references) {
        currentSubject = entity;
        dataProvider.getList().clear();
        dataProvider.getList().addAll(references);
        dataGrid.setVisibleRange(0, references.size());
        ColumnSortEvent.fire(dataGrid, dataGrid.getColumnSortList());
    }

    @Override
    public void clearData() {
        dataGrid.setRowData(Collections.<UsageReference>emptyList());
    }

    @Override
    public UsageFilter getUsageFilter() {
        return filterEditorView.getUsageFilter();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<UsageFilter> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    private void handleUsageFilterChanged() {
        filterMessage.setVisible(getUsageFilter().isFiltering());
        ValueChangeEvent.fire(this, getUsageFilter());
    }

    private static final TextCell CELL = new TextCell();



    private class EntityColumn extends Column<UsageReference, String> {



        private EntityColumn() {
            super(CELL);
        }

        @Override
        public String getValue(UsageReference object) {
            return object.getSubjectRendering();
        }

        @Override
        public void render(Cell.Context context, UsageReference object, SafeHtmlBuilder builder) {
            StringBuilder sb = new StringBuilder();
            final String iconClass;
            if(object.getAxiomSubject().isPresent()) {
                iconClass = object.getAxiomSubject().get().accept(new OWLEntityVisitorEx<String>() {
                    @Override
                    public String visit(OWLClass cls) {
                        return BUNDLE.style().classIconInset();
                    }

                    @Override
                    public String visit(OWLObjectProperty property) {
                        return BUNDLE.style().objectPropertyIconInset();
                    }

                    @Override
                    public String visit(OWLDataProperty property) {
                        return BUNDLE.style().dataPropertyIconInset();
                    }

                    @Override
                    public String visit(OWLNamedIndividual individual) {
                        return "individual-icon-inset";
                    }

                    @Override
                    public String visit(OWLDatatype datatype) {
                        return BUNDLE.style().datatypeIconInset();
                    }

                    @Override
                    public String visit(OWLAnnotationProperty property) {
                        return BUNDLE.style().annotationPropertyIconInset();
                    }
                });
            }
            else {
                iconClass = "";
            }

            sb.append("<div class=\"").append(iconClass);
            final String fontWeight;
            if(object.getAxiomSubject().equals(Optional.fromNullable(currentSubject))) {
                fontWeight = "bold";
            }
            else {
                fontWeight = "normal";
            }

            sb.append("\" style=\"min-height: 18px; background-position: 0 -1px; font-weight: ").append(fontWeight).append(";\">");
            sb.append(object.getSubjectRendering());
            sb.append("</div>");
            builder.appendHtmlConstant(sb.toString());
        }

        @Override
        public boolean isSortable() {
            return true;
        }
    }


    private static class EntityTypeColumn extends Column<UsageReference, String> {

        private EntityTypeColumn() {
            super(CELL);
        }

        @Override
        public String getValue(UsageReference object) {
            Optional<OWLEntity> subject = object.getAxiomSubject();
            if(!subject.isPresent()) {
                return "";
            }
            EntityType<?> subjectType = subject.get().getEntityType();
            if(subjectType == EntityType.NAMED_INDIVIDUAL) {
                return "Individual";
            }
            else if(subjectType == EntityType.OBJECT_PROPERTY) {
                return "Object Property";
            }
            else if(subjectType == EntityType.DATA_PROPERTY) {
                return "Data Property";
            }
            else if(subjectType == EntityType.ANNOTATION_PROPERTY) {
                return "Annotation Property";
            }
            else {
                return subjectType.getName();
            }
        }

        @Override
        public boolean isSortable() {
            return true;
        }
    }


    private class AxiomTypeColumn extends Column<UsageReference, String> {

        private AxiomTypeColumn() {
            super(CELL);
        }

        @Override
        public String getValue(UsageReference object) {
            return getDisplayName(object);
        }

        private String getDisplayName(UsageReference object) {
            final AxiomType axiomType = object.getAxiomType();
            AxiomTypeGroup axiomTypeGroup = AxiomTypeGroup.getAxiomTypeGroup(axiomType);
            if (axiomTypeGroup != AxiomTypeGroup.OTHER) {
                return axiomTypeGroup.getDisplayName();
            }
            else {
                return axiomType.getName();
            }
        }

        @Override
        public void render(Cell.Context context, UsageReference object, SafeHtmlBuilder sb) {
            sb.appendEscaped(getDisplayName(object));
            AxiomTypeGroup axiomTypeGroup = AxiomTypeGroup.getAxiomTypeGroup(object.getAxiomType());
            final Optional<OWLRDFVocabulary> owlrdfVocabulary = axiomTypeGroup.getOWLRDFVocabulary();
            if(owlrdfVocabulary.isPresent()) {
                final OWLRDFVocabulary vocabulary = owlrdfVocabulary.get();
                String shortName = vocabulary.getNamespace().name().toLowerCase() + ":" + vocabulary.getShortName();
                sb.appendHtmlConstant("<br><span style=\"color: gray; font-size: 90%;\">(" + shortName +  ")</span>");
            }
        }

        @Override
        public boolean isSortable() {
            return true;
        }
    }
}
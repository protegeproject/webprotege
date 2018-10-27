package edu.stanford.bmir.protege.web.client.usage;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomTypeGroup;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */

@Portlet(id = "portlets.EntityUsage", title = "Entity Usage", tooltip = "Displays the usage for the selected class, property or individual.")
public class UsagePortletPresenter extends AbstractWebProtegePortletPresenter {

    public static final FilterId SHOW_DEFINING_AXIOMS = new FilterId("Show defining axioms");

    public static final FilterId CLASS_FILTER = new FilterId("Class");

    public static final FilterId PROPERTY_FILTER = new FilterId("Property");

    public static final FilterId INDIVIDUAL_FILTER = new FilterId("Individual");

    public static final FilterId DATATYPE_FILTER = new FilterId("Datatype");

    private UsageView usageView;

    private final DispatchServiceManager dispatchServiceManager;

    private Optional<UsageFilter> filter = Optional.empty();

    private final FilterView filterView;

    @Inject
    public UsagePortletPresenter(SelectionModel selectionModel,
                                 DispatchServiceManager dispatchServiceManager,
                                 FilterView filterView,
                                 ProjectId projectId, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.dispatchServiceManager = dispatchServiceManager;
        usageView = new UsageViewImpl();
        usageView.addValueChangeHandler(event -> updateDisplayForSelectedEntity());

        this.filterView = filterView;

        filterView.addFilter(SHOW_DEFINING_AXIOMS, FilterSetting.ON);
        filterView.addFilterGroup("Show usage by entities of type");
        filterView.addFilter(CLASS_FILTER, FilterSetting.ON);
        filterView.addFilter(PROPERTY_FILTER, FilterSetting.ON);
        filterView.addFilter(INDIVIDUAL_FILTER, FilterSetting.ON);
        filterView.addFilter(DATATYPE_FILTER, FilterSetting.ON);
        filterView.addFilterGroup("Show usage by axioms of type");
        for(AxiomTypeGroup axiomTypeGroup : AxiomTypeGroup.values()) {
            filterView.addFilter(new FilterId(axiomTypeGroup.getDisplayName()), FilterSetting.ON);
        }
        filterView.addValueChangeHandler(event -> applyFilter(event.getValue()));
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(usageView.asWidget());
        portletUi.setFilterView(filterView);
        setDisplaySelectedEntityNameAsSubtitle(true);
    }

    private void applyFilter(FilterSet filterSet) {
        Set<EntityType<?>> entityTypeSet = new HashSet<>();
        if(filterSet.hasSetting(CLASS_FILTER, FilterSetting.ON)) {
            entityTypeSet.add(EntityType.CLASS);
        }
        if(filterSet.hasSetting(PROPERTY_FILTER, FilterSetting.ON)) {
            entityTypeSet.add(EntityType.OBJECT_PROPERTY);
            entityTypeSet.add(EntityType.DATA_PROPERTY);
            entityTypeSet.add(EntityType.ANNOTATION_PROPERTY);
        }
        if(filterSet.hasSetting(INDIVIDUAL_FILTER, FilterSetting.ON)) {
            entityTypeSet.add(EntityType.NAMED_INDIVIDUAL);
        }
        if(filterSet.hasSetting(DATATYPE_FILTER, FilterSetting.ON)) {
            entityTypeSet.add(EntityType.DATATYPE);
        }
        Set<AxiomType<?>> axiomTypes = new HashSet<>();
        for(AxiomTypeGroup group : AxiomTypeGroup.values()) {
            FilterId id = new FilterId(group.getDisplayName());
            if(filterSet.hasSetting(id, FilterSetting.ON)) {
                axiomTypes.addAll(group.getAxiomTypes());
            }
        }
        UsageFilter usageFilter = new UsageFilter(
                filterSet.hasSetting(SHOW_DEFINING_AXIOMS, FilterSetting.ON),
                entityTypeSet,
                axiomTypes
        );
        this.filter = Optional.of(usageFilter);
        updateDisplayForSelectedEntity();
    }


    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        updateDisplayForSelectedEntity();
    }

    private void updateDisplayForSelectedEntity() {
        final Optional<OWLEntity> sel = getSelectedEntity();
        if(sel.isPresent()) {
            showUsageForEntity(sel.get());
        }
        else {
            usageView.clearData();
        }
    }

    private void showUsageForEntity(final OWLEntity entity) {
        final GetUsageAction action = new GetUsageAction(entity, getProjectId(), filter);
        dispatchServiceManager.execute(action, result -> {
            final Collection<UsageReference> references = result.getUsageReferences();
            final int visibleReferences = references.size();
            final int totalReferences = result.getTotalUsageCount();
            //                setTitle("Usage (definition and references) [showing " + visibleReferences + " references of " + totalReferences + "]");
            usageView.setData(entity, references);
            setDisplayedEntity(Optional.of(result.getEntityNode().getEntityData()));
        });
    }
}

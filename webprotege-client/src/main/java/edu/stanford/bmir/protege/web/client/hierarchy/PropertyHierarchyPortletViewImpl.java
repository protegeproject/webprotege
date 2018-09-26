package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 4 Dec 2017
 */
public class PropertyHierarchyPortletViewImpl extends Composite implements PropertyHierarchyPortletView {
    interface PropertyHierarchyPortletViewImplUiBinder extends UiBinder<HTMLPanel, PropertyHierarchyPortletViewImpl> {
    }

    private static PropertyHierarchyPortletViewImplUiBinder ourUiBinder = GWT.create(PropertyHierarchyPortletViewImplUiBinder.class);

    @UiField
    TabBar switcher;

    @UiField
    SimplePanel hierarchyContainer;

    private final List<HierarchyId> hierarchyIds = new ArrayList<>();

    private final List<TreeWidget<EntityNode, OWLEntity>> views = new ArrayList<>();

    @Nonnull
    private HierarchyIdSelectedHandler hierarchyIdSelectedHandler = hierarchyId -> {};

    @Inject
    public PropertyHierarchyPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        switcher.addSelectionHandler(selectionEvent -> handleTabSelectionChanged());
    }

    private void handleTabSelectionChanged() {
        int selection = switcher.getSelectedTab();
        HierarchyId hierarchyId = hierarchyIds.get(selection);
        IsWidget view = views.get(selection);
        hierarchyContainer.setWidget(view);
        hierarchyIdSelectedHandler.handleHierarchyIdSelected(hierarchyId);
    }

    @Override
    public void addHierarchy(@Nonnull HierarchyId hierarchyId,
                             @Nonnull String label,
                             @Nonnull TreeWidget<EntityNode, OWLEntity> view) {
        switcher.addTab(checkNotNull(label));
        hierarchyIds.add(checkNotNull(hierarchyId));
        views.add(checkNotNull(view));
    }

    @Override
    public void setHierarchyIdSelectedHandler(@Nonnull HierarchyIdSelectedHandler hierarchyIdSelectedHandler) {
        this.hierarchyIdSelectedHandler = checkNotNull(hierarchyIdSelectedHandler);
    }

    @Override
    public void setSelectedHierarchy(@Nonnull HierarchyId hierarchyId) {
        int selection = hierarchyIds.indexOf(hierarchyId);
        if(switcher.getSelectedTab() == selection) {
            return;
        }
        GWT.log("[PropertyHierarchyPortletViewImpl] Switching tab to " + hierarchyId);
        switcher.selectTab(selection);
        IsWidget view = views.get(selection);
        hierarchyContainer.setWidget(view);
    }

    @Override
    public Optional<HierarchyId> getSelectedHierarchyId() {
        return Optional.of(hierarchyIds.get(switcher.getSelectedTab()));
    }

    @Override
    public Optional<TreeWidget<EntityNode, OWLEntity>> getSelectedHierarchy() {
        return Optional.of(views.get(switcher.getSelectedTab()));
    }
}
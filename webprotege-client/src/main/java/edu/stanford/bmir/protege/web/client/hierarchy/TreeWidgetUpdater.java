package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.tag.ProjectTagsChangedEvent;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent.ON_PROJECT_SETTINGS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.tag.ProjectTagsChangedEvent.ON_PROJECT_TAGS_CHANGED;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Aug 2018
 */
public class TreeWidgetUpdater {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final TreeWidget<EntityNode, OWLEntity> treeWidget;

    @Nonnull
    private final EntityHierarchyModel hierarchyModel;

    @AutoFactory
    public TreeWidgetUpdater(@Provided @Nonnull ProjectId projectId,
                             @Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget,
                             @Nonnull EntityHierarchyModel hierarchyModel) {
        this.projectId = projectId;
        this.treeWidget = checkNotNull(treeWidget);
        this.hierarchyModel = checkNotNull(hierarchyModel);
    }

    public void start(@Nonnull WebProtegeEventBus eventBus) {
        eventBus.addApplicationEventHandler(ON_PROJECT_SETTINGS_CHANGED, this::handleProjectSettingsChanged);
        eventBus.addProjectEventHandler(projectId, ON_PROJECT_TAGS_CHANGED, this::handleProjectTagsChanged);
    }

    private void handleProjectSettingsChanged(@Nonnull ProjectSettingsChangedEvent event) {
        updateTree();
    }

    private void handleProjectTagsChanged(@Nonnull ProjectTagsChangedEvent event) {
        updateTree();
    }

    private void updateTree() {
        Optional<OWLEntity> firstSelectedKey = treeWidget.getFirstSelectedKey();
        treeWidget.setModel(GraphTreeNodeModel.create(hierarchyModel, EntityNode::getEntity));
        firstSelectedKey.ifPresent(sel -> treeWidget.revealTreeNodesForKey(sel, REVEAL_FIRST));
    }

}

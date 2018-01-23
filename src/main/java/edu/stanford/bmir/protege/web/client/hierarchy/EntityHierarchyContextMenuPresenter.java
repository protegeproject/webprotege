package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 3 Dec 2017
 */
@AutoFactory
public class EntityHierarchyContextMenuPresenter {

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget;

    @Nonnull
    private final EntityHierarchyModel model;

    @Nonnull
    private final UIAction createEntityAction;

    @Nonnull
    private final UIAction deleteEntityAction;

    @Nullable
    private PopupMenu contextMenu;


    public EntityHierarchyContextMenuPresenter(@Nonnull EntityHierarchyModel model,
                                               @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget,
                                               @Nonnull UIAction createEntityAction,
                                               @Nonnull UIAction deleteEntityAction,
                                               @Provided Messages messages) {
        this.messages = checkNotNull(messages);
        this.treeWidget = checkNotNull(treeWidget);
        this.model = checkNotNull(model);
        this.createEntityAction = checkNotNull(createEntityAction);
        this.deleteEntityAction = checkNotNull(deleteEntityAction);
    }

    /**
     * Install the context menu on its tree
     */
    public void install() {
        treeWidget.addContextMenuHandler(this::showContextMenu);
    }

    private void showContextMenu(ContextMenuEvent event) {
        if(contextMenu == null) {
            createContextMenu();
        }
        int x = event.getNativeEvent().getClientX();
        int y = event.getNativeEvent().getClientY();
        contextMenu.show(x, y);
    }

    private void createContextMenu() {
        contextMenu = new PopupMenu();
        contextMenu.addItem(createEntityAction);
        contextMenu.addItem(deleteEntityAction);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.tree_pruneBranchToRoot(), this::pruneSelectedNodesToRoot);
        contextMenu.addItem(messages.tree_pruneAllBranchesToRoot(), this::pruneToKey);
        contextMenu.addItem(messages.tree_clearPruning(), this::clearPruning);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.showIri(), this::showIriForSelection);
        contextMenu.addItem(messages.showDirectLink(), this::showUrlForSelection);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.refreshTree(), this::handleRefresh);
    }


    private void pruneSelectedNodesToRoot() {
        treeWidget.pruneToSelectedNodes();
    }

    private void pruneToKey() {
        treeWidget.getFirstSelectedKey().ifPresent(sel -> treeWidget.pruneToNodesContainingKey(sel, () -> {}));
    }

    private void clearPruning() {
        treeWidget.clearPruning();
    }


    private void showIriForSelection() {
        treeWidget.getFirstSelectedKey().ifPresent(sel -> {
            String iri = sel.getIRI().toString();
            InputBox.showOkDialog(messages.classIri(), true, iri, input -> {});
        });
    }

    private void showUrlForSelection() {
        String location = Window.Location.getHref();
        InputBox.showOkDialog(messages.directLink(), true, location, input -> {});
    }

    private void handleRefresh() {
        Optional<OWLEntity> firstSelectedKey = treeWidget.getFirstSelectedKey();
        treeWidget.setModel(GraphTreeNodeModel.create(model, EntityHierarchyNode::getEntity));
        firstSelectedKey.ifPresent(sel -> treeWidget.revealTreeNodesForKey(sel, REVEAL_FIRST));
    }

}

package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 3 Dec 2017
 */
public class EntityHierarchyContextMenuPresenter {

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget;

    @Nonnull
    private final UIAction createEntityAction;

    @Nonnull
    private final UIAction deleteEntityAction;


    public EntityHierarchyContextMenuPresenter(@Nonnull Messages messages,
                                               @Nonnull TreeWidget<EntityHierarchyNode, OWLEntity> treeWidget,
                                               @Nonnull UIAction createEntityAction,
                                               @Nonnull UIAction deleteEntityAction) {
        this.messages = checkNotNull(messages);
        this.treeWidget = checkNotNull(treeWidget);
        this.createEntityAction = checkNotNull(createEntityAction);
        this.deleteEntityAction = checkNotNull(deleteEntityAction);
    }

    public void showContextMenu(ContextMenuEvent event) {
        PopupMenu contextMenu = new PopupMenu();
        contextMenu.addItem(createEntityAction);
        contextMenu.addItem(deleteEntityAction);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.tree_pruneBranchToRoot(), this::pruneSelectedNodesToRoot);
        contextMenu.addItem(messages.tree_pruneAllBranchesToRoot(), this::pruneToKey);
        contextMenu.addItem(messages.tree_clearPruning(), this::clearPruning);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.showIri(), showIriForSelection());
        contextMenu.addItem(messages.showDirectLink(), this::showUrlForSelection);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.refreshTree(), this::handleRefresh);
        int x = event.getNativeEvent().getClientX();
        int y = event.getNativeEvent().getClientY() + 5;
        contextMenu.show(x, y);
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


    private Runnable showIriForSelection() {
        return () -> {
            treeWidget.getFirstSelectedKey().ifPresent(sel -> {
                String iri = sel.getIRI().toQuotedString();
                InputBox.showOkDialog(messages.classIri(), true, iri, input -> {});
            });
        };
    }

    private void showUrlForSelection() {
        String location = Window.Location.getHref();
        InputBox.showOkDialog(messages.directLink(), true, location, input -> {});
    }

    private void handleRefresh() {
        Set<OWLEntity> selection = treeWidget.getSelectedKeys();
        treeWidget.reload();
//        treeWidget.setSelected(selection);
    }

}

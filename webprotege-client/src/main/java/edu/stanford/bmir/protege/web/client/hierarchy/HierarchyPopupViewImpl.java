package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Sep 2018
 */
public class HierarchyPopupViewImpl extends Composite implements HierarchyPopupView {

    private final EntityNodeHtmlRenderer renderer;

    interface HierarchyPopupViewImplUiBinder extends UiBinder<HTMLPanel, HierarchyPopupViewImpl> {

    }

    private static HierarchyPopupViewImplUiBinder ourUiBinder = GWT.create(HierarchyPopupViewImplUiBinder.class);

    @UiField(provided = true)
    TreeWidget<EntityNode, OWLEntity> treeWidget;

    private Consumer<EntityNode> selectionChangedHandler = node -> {};

    @Inject
    public HierarchyPopupViewImpl(TreeWidget<EntityNode, OWLEntity> treeWidget,
                                  EntityNodeHtmlRenderer renderer) {
        this.treeWidget = checkNotNull(treeWidget);
        this.treeWidget.setRenderer(renderer);
        treeWidget.addSelectionChangeHandler(event -> treeWidget.getFirstSelectedUserObject()
                .ifPresent(n -> {
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            selectionChangedHandler.accept(n);
                        }
                    };
                    t.schedule(200);
                }));
        this.renderer = renderer;
        this.renderer.setRenderTags(false);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setSelectionChangedHandler(Consumer<EntityNode> handler) {
        this.selectionChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setModel(@Nonnull EntityHierarchyModel model) {
        treeWidget.setModel(GraphTreeNodeModel.create(model, EntityNode::getEntity));
    }

    @Override
    public void revealEntity(@Nonnull OWLEntity selectedEntity) {
        treeWidget.revealTreeNodesForKey(selectedEntity, RevealMode.REVEAL_FIRST);
    }

    @Override
    public void setDisplayNameSettings(@Nonnull DisplayNameSettings settings) {
        renderer.setDisplayLanguage(settings);
        treeWidget.setRenderer(renderer);
    }
}
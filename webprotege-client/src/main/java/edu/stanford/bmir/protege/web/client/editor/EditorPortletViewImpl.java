package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class EditorPortletViewImpl extends Composite implements EditorPortletView {

    private EditorPaneChangedHandler editorPaneChangedHandler = () -> {};

    private List<Tooltip> tooltips = new ArrayList<>();

    interface EditorPortletViewImplUiBinder extends UiBinder<HTMLPanel, EditorPortletViewImpl> {

    }

    private static EditorPortletViewImplUiBinder ourUiBinder = GWT.create(EditorPortletViewImplUiBinder.class);

    @UiField
    SimplePanel tagListViewContainer;

    @UiField
    HTMLPanel paneContainer;

    @UiField
    TabBar tabBar;

    @Nonnull
    private final Map<String, SimplePanel> containerMap = new HashMap<>();

    @Nonnull
    private final List<String> tabs = new ArrayList<>();

    @Inject
    public EditorPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        tabBar.addSelectionHandler(event -> setVisibleIndex(event.getSelectedItem()));
    }

    @Override
    public void setWidget(IsWidget w) {
        paneContainer.clear();
        paneContainer.add(w);
    }

    @Override
    public void setEditorPaneChangedHandler(@Nonnull EditorPaneChangedHandler handler) {
        this.editorPaneChangedHandler = handler;
    }

    @Override
    public boolean isPaneVisible(@Nonnull String displayName) {
        SimplePanel simplePanel = containerMap.get(displayName);
        return simplePanel != null && simplePanel.isVisible();
    }

    @Nonnull
    @Override
    public SimplePanel getTagListViewContainer() {
        return tagListViewContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget addPane(@Nonnull String displayName,
                                    @Nonnull String additionalStyles) {
        EditorPaneTabSelector widget = new EditorPaneTabSelector();
        if (!additionalStyles.isEmpty()) {
            widget.addStyleName(additionalStyles);
        }
        tooltips.add(Tooltip.create(widget, displayName));
        tabBar.addTab(widget);
        SimplePanel simplePanel = new SimplePanel();
        Style style = simplePanel.getElement().getStyle();
        style.setTop(0, Style.Unit.PX);
        style.setLeft(0, Style.Unit.PX);
        style.setRight(0, Style.Unit.PX);
        style.setBottom(0, Style.Unit.PX);
        style.setPosition(Style.Position.ABSOLUTE);
        containerMap.put(displayName, simplePanel);
        tabs.add(displayName);
        return simplePanel;
    }


    @Override
    protected void onLoad() {
        super.onLoad();
        if(tabs.isEmpty()) {
            return;
        }
    }

    public void setVisibleIndex(int index) {
        for(int i = 0; i < tabs.size(); i++) {
            String dn = tabs.get(i);
            SimplePanel container = containerMap.get(dn);
            container.setVisible(i == index);
            if(i == index) {
                paneContainer.add(container);
            }
            else {
                container.removeFromParent();
            }
        }
        tabBar.selectTab(index, false);
        editorPaneChangedHandler.handleEditorPaneChanged();
    }

    @Override
    public void dispose() {
        tooltips.forEach(Tooltip::dispose);
    }
}

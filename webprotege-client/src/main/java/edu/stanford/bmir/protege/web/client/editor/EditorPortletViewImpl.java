package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

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

    interface EditorPortletViewImplUiBinder extends UiBinder<HTMLPanel, EditorPortletViewImpl> {

    }

    private static EditorPortletViewImplUiBinder ourUiBinder = GWT.create(EditorPortletViewImplUiBinder.class);

    @UiField
    SimplePanel tagListViewContainer;

    @UiField
    SimplePanel paneContainer;

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

    @Nonnull
    @Override
    public SimplePanel getTagListViewContainer() {
        return tagListViewContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget addPane(@Nonnull String displayName) {
        tabBar.addTab(displayName);
        SimplePanel simplePanel = new SimplePanel();
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
        setVisibleIndex(0);
    }

    private void setVisibleIndex(int index) {
        for(int i = 0; i < tabs.size(); i++) {
            String dn = tabs.get(i);
            SimplePanel container = containerMap.get(dn);
            container.setVisible(i == index);
            if(i == index) {
                paneContainer.setWidget(container);
            }
            else {
                container.removeFromParent();
            }
        }
        tabBar.selectTab(index, false);
    }
}
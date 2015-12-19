package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListener;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractPropertyWidget;
import edu.stanford.bmir.protege.web.client.ui.portlet.PropertyWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.Collection;
import java.util.Map;

/**
 * This portlet allows the association of a property with a widget, similar
 * to the forms in the Protege rich client.
 * The portlet has a tabbed form, and the widgets can be organized in tabs.
 * The loading of values is done lazy: we set the subject of all widgets, but only
 * load the values via remote calls for the widgets in the current active tab.
 * There is also some logic that prevents making remote calls if the subject
 * of the widget did not change. To force a refresh of the widget values, call {@link #reload()}.
 * The layout of the tabs is controlled by the {@link FormGenerator}.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class PropertyFieldPortlet extends AbstractOWLEntityPortlet {

    private TabPanel wrappingPanel;
    private FormGenerator formGenerator;

    private PanelListener panelListener;

    private final DispatchServiceManager dispatchServiceManager;

    private final EventBus eventBus;

    /*
     * This portlet should replace the displayed form based on the
     * configuration. However! I had problems with adding and removing
     * dynamically a TabPanel in the portlet and that's why we made a
     * workaround in which the form generator will add the tabs to an existing
     * tab panel (the wrapping panel). In the ideal case, there should be no
     * wrapping panel, and the generator should generate a tab panel (methods
     * are already available), but for some mysterious reason, this does not
     * work.
     */

    public PropertyFieldPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, Project project) {
        super(selectionModel, eventBus, project);
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void initialize() {
        setTitle("Details");
        wrappingPanel = new TabPanel();
        wrappingPanel.setBorder(false);
        wrappingPanel.setHeight(700);
        wrappingPanel.setStyleName("tabpanel-multirow");
       // wrappingPanel.setEnableTabScroll(true);
       // wrappingPanel.setDeferredRender(false); //not clear this is needed
        add(wrappingPanel);
    }

    protected PanelListener getPanelListener() {
        if (panelListener == null) {
            panelListener = new PanelListenerAdapter() {
                @Override
                public void onActivate(Panel panel) {
                    PropertyFieldPortlet.this.onActivate(panel);
                }
            };
        }
        return panelListener;
    }

    protected void attachPanelListenerToTabs() {
        Collection<Panel> tabs = formGenerator.getTabs();
        for (Panel tab : tabs) {
            tab.addListener(getPanelListener());
        }
    }

    protected void detachPanelListenerFromTabs() {
        //TODO: we need a remove listener method! None available in gwt-ext
        Collection<Panel> tabs = formGenerator.getTabs();
        for (Panel tab : tabs) {
            //remove listener
        }
    }

    protected void onActivate(Panel panel) {
       fillWidgetValues();
    }

    private EntityData getEntity() {
        Optional<OWLEntityData> entityData = getSelectedEntityData();
        if(entityData.isPresent()) {
            OWLEntityData theEntityData = entityData.get();
            return new EntityData(theEntityData.getEntity().getIRI().toString(), theEntityData.getBrowserText());
        }
        else {
            return null;
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        if(entityData.isPresent()) {
            setTitle("Details for " + entityData.get().getBrowserText());
        }
        else {
            setTitle("Details: nothing selected");
        }
        checkFormGenerator(getEntity());

        setSubject(getEntity());

        fillWidgetValues();

        Panel activeTab = wrappingPanel.getActiveTab();
        if (activeTab != null) {
            Collection<PropertyWidget> widgets = formGenerator.getWidgetsInTab(activeTab);
            if (widgets == null) { return; }
            for (PropertyWidget widget : widgets) {
                widget.refresh();
            }
        }
    }

    protected void fillWidgetValues() {
        //set the subject and fill values only for widgets in the active tab
        Panel activeTab = wrappingPanel.getActiveTab();
        if (activeTab != null) {
            Collection<PropertyWidget> widgets = formGenerator.getWidgetsInTab(activeTab);
            if (widgets == null) { return; }
            for (PropertyWidget widget : widgets) {
                ((AbstractPropertyWidget)widget).fillValues(); //TODO: move this to interface?
            }
        }
    }

    protected void setSubject(EntityData subject) {
        Collection<PropertyWidget> widgets = formGenerator.getWidgets();
        if (widgets == null) { return; }
        for (PropertyWidget widget : widgets) {
            widget.setSubject(subject);
        }
    }


    protected boolean needsNewFormGenerator(EntityData newEntity) {
        try {
            if (getEntity() != null && newEntity != null) {
                Collection<EntityData> currentTypes = getEntity().getTypes();
                Collection<EntityData> newTypes = newEntity.getTypes();

                if (currentTypes == null || newTypes == null) {
                    return true;
                }

                if (currentTypes != null && newTypes != null) {
                    if (currentTypes.size() == newTypes.size()) {
                        return  !currentTypes.equals(newTypes);
                    } else {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return  true;
        }
        return true;
    }

    protected FormGenerator checkFormGenerator(EntityData newEntity) {
        Map<String, Object> properties = getPortletConfiguration().getProperties();
        if (properties == null) { return null; }

        if (formGenerator == null) { // at init
            formGenerator = createFormGenerator();
            adjustTabs(newEntity);
        } else {
            if (needsNewFormGenerator(newEntity)) {
                adjustTabs(newEntity);
            }
        }
        return formGenerator;
    }

    protected void adjustTabs(EntityData newEntity) {
        if (newEntity == null) {
            hideAllTabs();
            return;
        }

        Collection<EntityData> types = newEntity.getTypes();
        if (types == null) {
            hideAllTabs();
            return;
        }

        boolean changed = false;
        Panel activeTab = wrappingPanel.getActiveTab();
        Panel firstVisibleTab = null;

        Collection<Panel> tabs = formGenerator.getTabs();
        for (Panel tab : tabs) {
            if (formGenerator.isSuitableForType(tab, types)) {
                if (!formGenerator.isTabVisible(tab)) {
                    wrappingPanel.unhideTabStripItem(tab);
                    formGenerator.setTabVisible(tab, true);
                    changed = true;
                }
                if (firstVisibleTab == null) {
                    firstVisibleTab = tab;
                }
            } else {
                if (formGenerator.isTabVisible(tab)) {
                    wrappingPanel.hideTabStripItem(tab);
                    formGenerator.setTabVisible(tab, false);
                    changed = true;
                }
            }
        }
        if (changed) {
            if (formGenerator.isTabVisible(activeTab)) {
                wrappingPanel.activate(activeTab.getId());
            }
            else if (firstVisibleTab != null) {
                wrappingPanel.activate(firstVisibleTab.getId());
            }
            else {
                //TODO deactivate tab if possible
            }
        }
    }

    private void hideAllTabs() {
        Collection<Panel> tabs = formGenerator.getTabs();
        for (Panel tab : tabs) {
            wrappingPanel.hideTabStripItem(tab);
            formGenerator.setTabVisible(tab, false);
        }
    }

    protected FormGenerator createFormGenerator() {
        Map<String, Object> properties = getPortletConfiguration().getProperties();
        formGenerator = new FormGenerator(getProject(), eventBus, dispatchServiceManager, properties);
        formGenerator.addFormToTabPanel(wrappingPanel);
        attachPanelListenerToTabs();
        wrappingPanel.activate(0);
        wrappingPanel.doLayout();
        return formGenerator;
    }

}

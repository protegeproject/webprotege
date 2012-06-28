package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.event.ComboBoxCallback;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.event.OntologyEvent;
import edu.stanford.bmir.protege.web.client.model.listener.OntologyListenerAdapter;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.individuals.PagedIndividualsProxyImpl;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class InstanceComboBox extends AbstractFieldWidget {

    private static final String ENTITY_NAME_FIELD = "entityName";
    private static final String BROWSER_TEXT_FIELD = "browserText";

    private static int DEFAULT_PAGE_SIZE = 20;

    private ComboBox comboBox;
    private Store store;
    private PagedIndividualsProxyImpl proxy;
    private RecordDef recordDef;

    public InstanceComboBox(Project project) {
        super(project);
    }


    @Override
    public Field createFieldComponent() {
        comboBox = new ComboBox();
        comboBox.setForceSelection(true);
        comboBox.setMinChars(1);
        comboBox.setTriggerAction(ComboBox.ALL);
        comboBox.setEmptyText("Select a value");
        comboBox.setTypeAhead(true);
        comboBox.setForceSelection(true);
        comboBox.setSelectOnFocus(true);
        comboBox.setHideTrigger(false);
        comboBox.setPageSize(UIUtil.getIntegerConfigurationProperty(getWidgetConfiguration(), FormConstants.PAGE_SIZE, DEFAULT_PAGE_SIZE));

        String labelText = UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), FormConstants.LABEL, getProperty().getBrowserText());
        comboBox.setLabel(labelText);

        recordDef = new RecordDef(
                new FieldDef[] {
                    //would be better to store the EntityData itself, but I could not get the selected record from the combobox
                    new StringFieldDef(ENTITY_NAME_FIELD),
                    new StringFieldDef(BROWSER_TEXT_FIELD),
                }
            );
        ArrayReader reader = new ArrayReader(recordDef);

        proxy = new PagedIndividualsProxyImpl();
        proxy.setProjectName(getProject().getProjectName());
        proxy.setClassName(UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), FormConstants.ONT_TYPE, null));
        store = new Store(proxy, reader);

        comboBox.setDisplayField(BROWSER_TEXT_FIELD);
        comboBox.setValueField(ENTITY_NAME_FIELD);
        comboBox.setStore(store);

        return comboBox;
    }

    @Override
    protected void attachFieldChangeListener() {
        comboBox.addListener(new ComboBoxListenerAdapter() {
            @Override
            public boolean doBeforeQuery(ComboBox comboBox, ComboBoxCallback cb) {
                return isWriteOperationAllowed();
            }

            @Override
            public boolean doBeforeSelect(ComboBox comboBox, Record record, int index) {
                if (!isWriteOperationAllowed()) {
                    return false;
                }
                int oldIndex = store.find(ENTITY_NAME_FIELD, comboBox.getValue(), 0, true, true);
                EntityData oldValueEd = new EntityData(comboBox.getValue(), oldIndex >= 0 ? store.getAt(oldIndex).getAsString(BROWSER_TEXT_FIELD) : null);
                EntityData newValue = new EntityData(record.getAsString(ENTITY_NAME_FIELD), record.getAsString(BROWSER_TEXT_FIELD));
                onChangeValue(getSubject(), oldValueEd, newValue);
                return true;
            }
        });
    }

    @Override
    protected void displayValues(Collection<EntityData> values) {
        if (values == null || values.size() == 0) {
            comboBox.clearValue();
        } else {
            //FIXME: for now only deal with one value
            EntityData value = values.iterator().next();
            if (value != null) {
                comboBox.setValue(value.getName());
            } else {
                comboBox.clearValue();
            }
        }
    }

    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);
        cacheAllowedValues();
        addOntolgyChangeListener();
    }

    protected void addOntolgyChangeListener() {
        getProject().addOntologyListener(new OntologyListenerAdapter() {
            @Override
            public void individualAddedRemoved(OntologyEvent ontologyEvent) {
                cacheAllowedValues(); //reset cache
            }
        });
    }

    private void cacheAllowedValues() {
        String allowedType= UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), FormConstants.ONT_TYPE, null);
        if (allowedType == null) {
            return;
        }
        store.removeAll();
        OntologyServiceManager.getInstance().getIndividuals(getProject().getProjectName(), allowedType, new FillAllowedValuesCacheHandler());
    }


    /*
     * Remote handlers
     */

    class FillAllowedValuesCacheHandler extends AbstractAsyncHandler<List<EntityData>> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Could not retrieve allowed values for combobox " + getProperty(), caught);
        }

        @Override
        public void handleSuccess(List<EntityData> instances) {
            store.removeAll();
            for (EntityData inst : instances) {
                store.add(recordDef.createRecord(new Object[]{inst.getName(), UIUtil.getDisplayText(inst)}));
            }
        }

    }


}

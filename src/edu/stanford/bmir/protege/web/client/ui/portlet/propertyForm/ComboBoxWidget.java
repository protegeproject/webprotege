package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.TextField;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * Use {@link InstanceComboBox}
 *
 */
@Deprecated
public class ComboBoxWidget extends AbstractFieldWidget {

	private ComboBox comboBox;
	private Store store;
	private RecordDef recordDef;

	public ComboBoxWidget(Project project) {
		super(project);
	}


	@Override
	protected TextField createFieldComponent() {
		comboBox = new ComboBox();
		comboBox.setForceSelection(true);
		comboBox.setMinChars(1);
		comboBox.setMode(ComboBox.LOCAL);
		comboBox.setTriggerAction(ComboBox.ALL);
		comboBox.setEmptyText("Select a value");
		comboBox.setTypeAhead(true);
		comboBox.setSelectOnFocus(true);
		comboBox.setHideTrigger(false);

		recordDef = new RecordDef(
				new FieldDef[] {
					new StringFieldDef("entityData"),
					new StringFieldDef("browserText"),
				}
			);

		ArrayReader reader = new ArrayReader(recordDef);
		MemoryProxy dataProxy = new MemoryProxy(getStoreData());
		store = new Store(dataProxy, reader);
        store.load();

		comboBox.setValueField("entityData");
		comboBox.setDisplayField("browserText");
		comboBox.setStore(store);

        store.load();
		//comboBox.setLazyRender(true);

		return comboBox;
	}

	private Object[][] getStoreData() {
	    Map<String, String> allowedValues = UIUtil.getAllowedValuesConfigurationProperty(getWidgetConfiguration());

	    if (allowedValues == null) {
	        return new String[][]{};
	    }
	    else {
	        String[][] data = new String[allowedValues.size()][2];
	        int i = 0;
	        for (String allowedValueLabel : allowedValues.keySet()) {
	            data[i][0] = allowedValues.get(allowedValueLabel);
	            data[i][1] = allowedValueLabel;
	            i++;
	        }

	        return data;
	    }
    }


    @Override
	public void setup(Map<String, Object> widgetConfiguration,
			PropertyEntityData propertyEntityData) {
		super.setup(widgetConfiguration, propertyEntityData);

        Map<String, String> allowedValuesMap = UIUtil.getAllowedValuesConfigurationProperty(widgetConfiguration);
        List<EntityData> allowedValues = null;
        if (allowedValuesMap != null) {
            allowedValues = new ArrayList<EntityData>(allowedValuesMap.size());
            for (String allowedValueLabel : allowedValuesMap.keySet()) {
                EntityData entityData = new EntityData(allowedValuesMap.get(allowedValueLabel), allowedValueLabel);
                entityData.setValueType(ValueType.Instance);
                allowedValues.add(entityData);
            }
        }
        propertyEntityData.setAllowedValues(allowedValues);

		configureComboBox(propertyEntityData);
	}


	private void configureComboBox(PropertyEntityData propertyEntityData) {
		List<EntityData> allowedVals = propertyEntityData.getAllowedValues();
		if (allowedVals == null) { return; }

		store.removeAll();

		Object[][] data = new Object[allowedVals.size()][2];
		int i = 0;
		for (EntityData allowedVal : allowedVals) {
			store.add(recordDef.createRecord(new Object[]{allowedVal.getName(), UIUtil.getDisplayText(allowedVal)}));
			data[i][0] = allowedVal.getName();
			data[i][1] = UIUtil.getDisplayText(allowedVal);
			i++;
		}
		store.reload();
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
	public void setProperty(PropertyEntityData property) {
		super.setProperty(property);
		configureComboBox(property);
	}

}

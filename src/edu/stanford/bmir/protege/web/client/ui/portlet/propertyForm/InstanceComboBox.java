package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.event.ComboBoxCallback;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.individuals.PagedIndividualsProxyImpl;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.NamedIndividualFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.NamedIndividualFrameChangedEventHandler;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        proxy.setProjectId(getProjectId());
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
        GWT.log("WARNING:  InstanceComboBox is adding a listener that will never be removed.  This needs cleaning up!");
        EventBusManager.getManager().registerHandler(NamedIndividualFrameChangedEvent.TYPE, new NamedIndividualFrameChangedEventHandler() {
            @Override
            public void namedIndividualFrameChanged(NamedIndividualFrameChangedEvent event) {
                cacheAllowedValues();
            }
        });
    }

    private void cacheAllowedValues() {
        String allowedType= UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), FormConstants.ONT_TYPE, null);
        if (allowedType == null) {
            return;
        }
        store.removeAll();
        DispatchServiceManager.get().execute(new GetIndividualsAction(getProjectId(), DataFactory.getOWLClass(allowedType)), new AbstractWebProtegeAsyncCallback<GetIndividualsResult>() {
            @Override
            public void onSuccess(GetIndividualsResult result) {
                store.removeAll();
                for (OWLNamedIndividualData ind : result.getIndividuals()) {
                    store.add(recordDef.createRecord(new Object[]{ind.getEntity().getIRI().toString(), ind.getBrowserText()}));
                }
            }
        });
    }

}

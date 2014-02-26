package edu.stanford.bmir.protege.web.client.csv;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.csv.ColumnType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVColumnRelationEditorViewImpl extends Composite implements CSVColumnRelationView, HasFocusHandlers, HasValueChangeHandlers<Optional<OWLPrimitiveData>> {

    interface CSVColumnRelationEditorViewImplUiBinder extends UiBinder<HTMLPanel, CSVColumnRelationEditorViewImpl> {

    }

    private static CSVColumnRelationEditorViewImplUiBinder ourUiBinder = GWT.create(CSVColumnRelationEditorViewImplUiBinder.class);

    @UiField(provided = true)
    protected PrimitiveDataEditorImpl propertyField;

    @UiField
    protected ListBox relationTypeField;



    public CSVColumnRelationEditorViewImpl() {
        propertyField = PrimitiveDataEditorGinjector.INSTANCE.getEditor();
        propertyField.setSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);

        propertyField.setFreshEntitiesHandler(new MutableFreshEntitiesHandler());

        propertyField.setObjectPropertiesAllowed(true);
        propertyField.setDataPropertiesAllowed(true);
        propertyField.setAnnotationPropertiesAllowed(true);

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        relationTypeField.getElement().setAttribute("placeholder", "Select value type");

        initWidget(rootElement);
    }

    private void updateRelationTypeField() {
        Optional<OWLPrimitiveData> property = propertyField.getValue();

        int selIndex = relationTypeField.getSelectedIndex();
        Optional<String> selectedItem;
        if(selIndex != -1) {
            selectedItem = Optional.of(relationTypeField.getItemText(selIndex));
        }
        else {
            selectedItem = Optional.absent();
        }

        List<String> nextItems = new ArrayList<String>();
        if(property.isPresent()) {
            OWLPrimitiveData primitiveData = property.get();
            final OWLEntity prop = (OWLEntity) primitiveData.getObject();
            boolean freshEntity = DataFactory.isFreshEntity(prop);
            if(freshEntity) {
                for(ColumnType columnType : ColumnType.values()) {
                    nextItems.add(columnType.getDisplayName());
                }
            }
            else {
                for(ColumnType columnType : ColumnType.values()) {
                    if (columnType.isPropertyType(prop.getEntityType())) {
                        nextItems.add(columnType.getDisplayName());
                    }
                }
            }
        }

            int nextSelIndex = 0;
            relationTypeField.clear();
            for(int i = 0; i < nextItems.size(); i++) {
                String nextItem = nextItems.get(i);
                relationTypeField.addItem(nextItem);
                if(selectedItem.or("").equals(nextItem)) {
                    nextSelIndex = i;
                }
            }
            relationTypeField.setSelectedIndex(nextSelIndex);


    }


    public Optional<OWLPrimitiveData> getProperty() {
        return propertyField.getValue();
    }

    public Optional<ColumnType> getColumnType() {
        final int selectedIndex = relationTypeField.getSelectedIndex();
        String displayName = relationTypeField.getItemText(selectedIndex);
        if(displayName.trim().isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.of(ColumnType.getColumnTypeFromDisplayName(displayName));
        }
    }

    @UiHandler("propertyField")
    protected void handlePropertyFieldChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        updateRelationTypeField();
        ValueChangeEvent.fire(this, event.getValue());
    }

    @UiHandler("relationTypeField")
    protected void handleRelationTypeFieldChanged(ChangeEvent event) {

        final Optional<OWLPrimitiveData> property = propertyField.getValue();
        if(property.isPresent()) {
            EntityType<?> propertyType = ((OWLEntity) property.get().getObject()).getEntityType();
            if(getColumnType().isPresent()) {
                ColumnType type = getColumnType().get();
                List<EntityType<?>> propertyTypes = type.getPropertyTypes();
                if(!propertyTypes.contains(propertyType)) {
                    final EntityType<?> firstPermissiblePropertyType = propertyTypes.get(0);
                    propertyField.coerceToEntityType(firstPermissiblePropertyType);

                }
            }
        }

        ValueChangeEvent.fire(this, property);

    }



    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        relationTypeField.addFocusHandler(handler);
        return propertyField.addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OWLPrimitiveData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

}

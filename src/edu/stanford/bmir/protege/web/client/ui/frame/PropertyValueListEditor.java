package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.client.rpc.GetRendering;
import edu.stanford.bmir.protege.web.client.rpc.GetRenderingCallback;
import edu.stanford.bmir.protege.web.client.rpc.GetRenderingResponse;
import edu.stanford.bmir.protege.web.client.rpc.RenderingServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.client.ui.library.common.EventStrategy;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasEditable;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasTextRendering;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBoxMode;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.HasEntityDataProvider;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class PropertyValueListEditor extends FlowPanel implements ValueEditor<PropertyValueList>, HasEnabled, HasEditable {


    public static final int DELETE_BUTTON_COL = 3;

    public static final int LANG_COLUMN = 2;

    private static final int MAX_POOL_SIZE = 100;

    private PropertyValueGridGrammar grammar;

    public static final int PROPERTY_COLUMN = 0;

    public static final int FILLER_COLUMN = 1;

    private static final String PROPERTY_NAME_PLACEHOLDER_TEXT = "Enter property name";

    private static final String FILLER_EDITOR_PLACE_HOLDER_TEXT = "Enter value";

    public static final String WEB_PROTEGE_ERROR_BACKGROUND_STYLE = "web-protege-error-background";

    private FlexTable table = new FlexTable();

    private ProjectId projectId;

    private boolean dirty = false;

    private Multimap<String, EntityType<?>> freshEntityNamed = HashMultimap.create();

    private boolean enabled = true;


    private FreshEntitiesHandler freshEntitiesHandler = new MutableFreshEntitiesHandler();

    private boolean editable = true;

    private boolean settingValue = false;


    public PropertyValueListEditor(ProjectId projectId, PropertyValueGridGrammar grammar) {
        this.grammar = grammar;
        addStyleName("web-protege-laf");
        addStyleName("web-protege-property-grid");
        this.projectId = projectId;
        table.setBorderWidth(0);
        table.setCellPadding(0);
        add(table);
    }


    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this widget is enabled.
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for(int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
            for(int colIndex = 0; colIndex < table.getCellCount(rowIndex); colIndex++) {
                Widget widget = table.getWidget(rowIndex, colIndex);
                if(widget instanceof HasEnabled) {
                    ((HasEnabled) widget).setEnabled(enabled);
                }
            }
        }
        if(enabled) {
            ensureBlankRow();
        }
    }

    /**
     * Determines if the object implementing this interface is editable.
     * @return {@code true} if the object is editable, otherwise {@code false}.
     */
    @Override
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the editable state of the object implementing this interface.
     * @param editable If {@code true} then the state is set to editable, if {@code false} then the state is set to
     * not editable.
     */
    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        for(int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
            for(int colIndex = 0; colIndex < table.getCellCount(rowIndex); colIndex++) {
                Widget widget = table.getWidget(rowIndex, colIndex);
                if(widget instanceof HasEditable) {
                    ((HasEditable) widget).setEditable(editable);
                }
            }
        }
    }


    private void fillUp(PropertyValueList propertyValueList, HasEntityDataProvider provider) {
        dirty = false;
        settingValue = true;
        try {

            Element tableElement = table.getElement();
            Element parentElement = tableElement.getParentElement();
            Element nextSibling = tableElement.getNextSiblingElement();
            tableElement.removeFromParent();
            long t0 = new Date().getTime();

            recyclePrimitiveDataEditors();
            recycleDeleteButtons();
            table.removeAllRows();

            for (PropertyValue propertyValue : propertyValueList.getPropertyValues()) {
                addRelationship(propertyValue, provider);
            }
            ensureBlankRow();
            long t1 = new Date().getTime();
            GWT.log("        DOM update time: " + (t1 - t0));
            if (nextSibling != null) {
                parentElement.insertBefore(nextSibling, tableElement);
            }
            else {
                parentElement.insertFirst(tableElement);
            }
        }
        catch (Exception e) {
            GWT.log("Error whilst setting value", e);
        }
        finally {
            settingValue = false;
        }

    }


    private void addRelationship(final PropertyValue propertyValue, HasEntityDataProvider provider) {
        final Optional<OWLEntityData> propRendering = provider.getEntityData(propertyValue.getProperty());
        OWLPrimitiveData valueRendering = null;
            if (propertyValue.getValue() instanceof OWLEntity) {
                final Optional<OWLEntityData> propertyData = provider.getEntityData((OWLEntity) propertyValue.getValue());
                valueRendering = propertyData.get();
            }
            else if (propertyValue.getValue() instanceof OWLLiteral) {
                valueRendering = new OWLLiteralData((OWLLiteral) propertyValue.getValue());
            }
            else if (propertyValue.getValue() instanceof IRI) {
                valueRendering = new IRIData((IRI) propertyValue.getValue());
            }
            else {
                return;
            }
        addRow(propRendering.isPresent() ? Optional.<OWLPrimitiveData>of(propRendering.get()) : Optional.<OWLPrimitiveData>absent(), Optional.of(valueRendering));
    }


    public boolean isDirty() {
        return dirty;
    }

    private void addBlankRow() {
        addRow(Optional.<OWLPrimitiveData>absent(), Optional.<OWLPrimitiveData>absent());
    }

    private String getValuePlaceholder(int row) {
        DefaultPrimitiveDataEditor widget = (DefaultPrimitiveDataEditor) table.getWidget(row, 0);
        Optional<OWLPrimitiveData> value = widget.getValue();
        if(!value.isPresent()) {
            return FILLER_EDITOR_PLACE_HOLDER_TEXT;
        }
        OWLPrimitiveData entityData = value.get();
        if (entityData instanceof OWLAnnotationPropertyData) {
            return "Enter string, number etc.";
        }
        else if (entityData instanceof OWLObjectPropertyData) {
            return "Enter class or individual name";
        }
        else if (entityData instanceof OWLDataPropertyData) {
            return "Enter datatype, string, number etc.";
        }
        else {
            return "Enter value";
        }

    }


    public void sort() {
    }

    private void addRow(Optional<OWLPrimitiveData> propertyData, Optional<OWLPrimitiveData> fillerData) {
        final int row = table.getRowCount();


        final DefaultPrimitiveDataEditor propertyEditor = createPrimitiveEditor();
        table.setWidget(row, PROPERTY_COLUMN, propertyEditor);
        propertyEditor.setFreshEntitiesHandler(new MutableFreshEntitiesHandler() {
            /**
             * Gets the policy supported by this handler.
             * @return The {@link edu.stanford.bmir.protege.web.client.primitive.FreshEntitiesPolicy}.  Not {@code null}.
             */
            @Override
            public FreshEntitiesPolicy getFreshEntitiesPolicy() {
                return FreshEntitiesPolicy.NOT_ALLOWED;
            }

            /**
             * Gets the error message for when an entity is a fresh entity.
             * @param browserText The browser text being parsed.
             * @return An error message.  Not {@code null}.
             */
            @Override
            public String getErrorMessage(String browserText) {
                return  "<b>" + browserText + "</b> is a <b>new</b> property name." +
                        "<br>" +
                        "<span style=\"font-size: -2;\">To continue, enter a value for the property (class name, number etc.) and press the TAB key</span>";
            }
        });
        if(!canInferPropertyType()) {
            propertyEditor.setSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);
        }

        propertyEditor.addStyleName("web-protege-property-grid-editor");
        propertyEditor.addStyleName("web-protege-form-layout-editor-input");

        propertyEditor.setAllowedTypes(grammar.getPropertyTypes());


        final DefaultPrimitiveDataEditor fillerEditor = createPrimitiveEditor();
        fillerEditor.setAllowedTypes(grammar.getFillerTypes());
        fillerEditor.setSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);


        FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
        formatter.setWidth(row, 0, "30%");

        if (isLiteralOrCanEditLiteral(propertyData, fillerData)) {
            formatter.setWidth(row, LANG_COLUMN, "20px");
        }
        formatter.setWidth(row, DELETE_BUTTON_COL, "12px");
        if (propertyData.isPresent()) {
            propertyEditor.setValue(propertyData.get());
        }
        else {
            propertyEditor.clearValue();
        }


        propertyEditor.setPlaceholder(PROPERTY_NAME_PLACEHOLDER_TEXT);



        table.setWidget(row, FILLER_COLUMN, fillerEditor);
        if (fillerData.isPresent()) {
            fillerEditor.setValue(fillerData.get());
        }
        else {
            fillerEditor.clearValue();
        }

        if (isLiteralOrCanEditLiteral(propertyData, fillerData)) {
            addLangEditor(row, fillerEditor.getLanguageEditor(), fillerData);
        }


        if (grammar.getFillerTypes().contains(PrimitiveType.LITERAL)) {
            fillerEditor.setMode(ExpandingTextBoxMode.MULTI_LINE);
        }

        fillerEditor.addStyleName("web-protege-form-layout-editor-input");
        fillerEditor.setPlaceholder(FILLER_EDITOR_PLACE_HOLDER_TEXT);

        if (propertyData.isPresent()) {
            addDeleteButton(row);
        }




        HandlerRegistration propHandlerReg = propertyEditor.addValueChangeHandler(new ValueChangeHandler<Optional<OWLPrimitiveData>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
                int row = getRowForWidget(PROPERTY_COLUMN, propertyEditor);
                if (row != -1) {
                    handlePropertyChanged(row, propertyEditor, fillerEditor);
                }
            }
        });

        handlerRegistrations.put(propertyEditor, propHandlerReg);

        HandlerRegistration fillerHandlerReg = fillerEditor.addValueChangeHandler(new ValueChangeHandler<Optional<OWLPrimitiveData>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
                int row = getRowForWidget(FILLER_COLUMN, fillerEditor);
                if (row != -1) {
                    handleFillerChanged(row, propertyEditor, fillerEditor);
                }
            }
        });

        handlerRegistrations.put(fillerEditor, fillerHandlerReg);


    }

    private int getRowForWidget(int column, Widget propertyEditor) {
        for(int i = 0; i < table.getRowCount(); i++) {
            Widget widget = table.getWidget(i, column);
            if(widget == propertyEditor) {
                return i;
            }
        }
        return -1;
    }

    private boolean canInferPropertyType() {
        final SortedSet<PrimitiveType> propertyTypes = grammar.getPropertyTypes();
        return !(propertyTypes.contains(PrimitiveType.ANNOTATION_PROPERTY) && propertyTypes.contains(PrimitiveType.DATA_PROPERTY));
    }


    private List<DefaultPrimitiveDataEditor> pool = new ArrayList<DefaultPrimitiveDataEditor>();

    private Multimap<PrimitiveDataEditor, HandlerRegistration> handlerRegistrations = HashMultimap.create();

    private void recyclePrimitiveDataEditors() {
        for(int row = 0; row < table.getRowCount(); row++) {
            DefaultPrimitiveDataEditor propertyEditor = (DefaultPrimitiveDataEditor) table.getWidget(row, PROPERTY_COLUMN);
            DefaultPrimitiveDataEditor fillerEditor = (DefaultPrimitiveDataEditor) table.getWidget(row, FILLER_COLUMN);
            recycle(propertyEditor);
            recycle(fillerEditor);
        }
    }

    private void recycleDeleteButtons() {
        for(int row = 0; row < table.getRowCount(); row++) {
            DeleteButton deleteButton = (DeleteButton) table.getWidget(row, DELETE_BUTTON_COL);
            if(deleteButton != null) {
                recycle(deleteButton);
            }
        }
    }



    private void recycle(DefaultPrimitiveDataEditor editor) {
        if(pool.size() == MAX_POOL_SIZE) {
            return;
        }

        for(HandlerRegistration registration : handlerRegistrations.removeAll(editor)) {
            registration.removeHandler();
        }

        pool.add(editor);
    }

    private List<DeleteButton> deleteButtonPool = new ArrayList<DeleteButton>();

    private void recycle(DeleteButton deleteButton) {
        if(deleteButtonPool.size() == MAX_POOL_SIZE) {
            return;
        }
        deleteButtonPool.add(deleteButton);
    }

    private DefaultPrimitiveDataEditor createPrimitiveEditor() {
        DefaultPrimitiveDataEditor editor;
        if(!pool.isEmpty()) {
            editor = pool.remove(0);
        }
        else {
            editor = new DefaultPrimitiveDataEditor(projectId);
            editor.setFreshEntitiesHandler(freshEntitiesHandler);
        }
        editor.setEnabled(enabled);
        return editor;
    }

    private boolean isLiteralOrCanEditLiteral(Optional<OWLPrimitiveData> propertyData, Optional<OWLPrimitiveData> fillerData) {
        if(propertyData.isPresent()) {
            if(propertyData.get() instanceof OWLObjectPropertyData) {
                return false;
            }
        }
        if(fillerData.isPresent()) {
            if(fillerData.get() instanceof OWLLiteralData) {
                return true;
            }
        }
        else {
            if(grammar.getFillerTypes().contains(PrimitiveType.LITERAL)) {
                return true;
            }
        }
        return false;
    }

    private void handleFillerChanged(int row, PrimitiveDataEditor propertyEditor, PrimitiveDataEditor fillerEditor) {
        if(shouldShowDeleteButton(row, propertyEditor, fillerEditor)) {
            addDeleteButton(row);
        }
        updateFillerEditor(row, propertyEditor, fillerEditor);
        if(isInvalidValue(propertyEditor)) {
            inferPropertyTypeFromFiller(row, propertyEditor, fillerEditor);
        }
        ensureBlankRow();
        setDirty(true, EventStrategy.FIRE_EVENTS);
        if(propertyEditor.isWellFormed() && fillerEditor.isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    private boolean isInvalidValue(PrimitiveDataEditor editor) {
        return !editor.getValue().isPresent() && !editor.getText().isEmpty();
    }

    private void inferPropertyTypeFromFiller(int row, final PrimitiveDataEditor propertyEditor, final PrimitiveDataEditor fillerEditor) {
        Optional<OWLPrimitiveData> fillerValue = fillerEditor.getValue();
        if(!fillerValue.isPresent()) {
            return;
        }
        GWT.log("Infer property type from filler");

        EntityType<?> inferredType = fillerValue.get().accept(new OWLPrimitiveDataVisitorAdapter<EntityType<?>, RuntimeException>() {
            @Override
            public EntityType<?> visit(OWLClassData data) throws RuntimeException {
                return handleObjectProperty();
            }

            @Override
            public EntityType<?> visit(OWLNamedIndividualData data) throws RuntimeException {
                return handleObjectProperty();
            }

            @Override
            public EntityType<?> visit(OWLLiteralData data) throws RuntimeException {
                if(data.getLiteral().isRDFPlainLiteral() || data.getLiteral().getDatatype().isString()) {
                    if (fillerEditor.isClassesAllowed()) {
                        // Here we assume a class name instead of a literal
                        fillerEditor.coerceToEntityType(EntityType.CLASS);
                        return handleObjectProperty();
                    }
                }
                // Other type of literal so lets assume a data or annotation property
                return handleDataProperty();
            }

            @Override
            public EntityType<?> visit(OWLDatatypeData data) throws RuntimeException {
                return handleDataProperty();
            }

            private EntityType<?> handleDataProperty() {
                if(grammar.getPropertyTypes().contains(PrimitiveType.DATA_PROPERTY)) {
                    return EntityType.DATA_PROPERTY;
                }
                else if(grammar.getPropertyTypes().contains(PrimitiveType.ANNOTATION_PROPERTY)) {
                    return EntityType.ANNOTATION_PROPERTY;
                }
                else {
                    return null;
                }
            }

            private EntityType<?> handleObjectProperty() {
                if(grammar.getPropertyTypes().contains(PrimitiveType.OBJECT_PROPERTY)) {
                    return EntityType.OBJECT_PROPERTY;
                }
                else if(grammar.getPropertyTypes().contains(PrimitiveType.ANNOTATION_PROPERTY)) {
                    return EntityType.ANNOTATION_PROPERTY;
                }
                else {
                    return null;
                }
            }
        });
        if (inferredType != null) {
            propertyEditor.coerceToEntityType(inferredType);
        }

    }



    private void handlePropertyChanged(int row, PrimitiveDataEditor propertyEditor, PrimitiveDataEditor fillerEditor) {
        fillerEditor.setPlaceholder(getValuePlaceholder(row));
        if (shouldShowDeleteButton(row, propertyEditor, fillerEditor)) {
            addDeleteButton(row);
        }
        if(isLiteralOrCanEditLiteral(propertyEditor.getValue(), fillerEditor.getValue())) {
            addLangEditor(row, fillerEditor.getLanguageEditor(), fillerEditor.getValue());
        }
        else {
            removeLangEditor(row);
        }
        updateFillerEditor(row, propertyEditor, fillerEditor);
        ensureBlankRow();
        setDirty(true, EventStrategy.FIRE_EVENTS);
        if(propertyEditor.isWellFormed() && fillerEditor.isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }



    private void removeLangEditor(int row) {
        Widget langEditor = table.getWidget(row, LANG_COLUMN);
        if(langEditor != null) {
            langEditor.setVisible(false);
        }
    }

    private boolean shouldShowDeleteButton(int row, PrimitiveDataEditor propertyEditor, PrimitiveDataEditor fillerEditor) {
        return propertyEditor.getValue().isPresent() || fillerEditor.getValue().isPresent();
    }

    private void addDeleteButton(final int rowCount) {
        Widget widget = table.getWidget(rowCount, DELETE_BUTTON_COL);
        if(widget instanceof DeleteButton) {
            widget.setVisible(true);
            return;
        }
        final DeleteButton deleteButton = createDeleteButton();
        deleteButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                deleteRow(deleteButton);
            }
        });
        table.setWidget(rowCount, DELETE_BUTTON_COL, deleteButton);
    }

    private DeleteButton createDeleteButton() {
        if(!deleteButtonPool.isEmpty()) {
            return deleteButtonPool.remove(0);
        }
        return new DeleteButton();
    }

    private void deleteRow(Widget deleteButton) {
        if(!isEnabled()) {
            return;
        }
        int rowCount = table.getRowCount();
        boolean changed = false;
        for (int i = 0; i < rowCount; i++) {
            Widget candidate = table.getWidget(i, DELETE_BUTTON_COL);
            if (candidate == deleteButton) {
                table.removeRow(i);
                changed = true;
                break;
            }
        }
        if(changed) {
            setDirty(true, EventStrategy.FIRE_EVENTS);
            ValueChangeEvent.fire(this, getValue());
        }
    }

    private void updateFillerEditor(int row, PrimitiveDataEditor propertyEditor, PrimitiveDataEditor fillerEditor) {
        final SortedSet<PrimitiveType> primitiveTypes = new TreeSet<PrimitiveType>();

        Optional<OWLPrimitiveData> propertyData = propertyEditor.getValue();
        if (propertyData.isPresent()) {
            propertyData.get().accept(new OWLPrimitiveDataVisitorAdapter<Object, RuntimeException>() {
                @Override
                public Object visit(OWLObjectPropertyData data) throws RuntimeException {
                    primitiveTypes.add(PrimitiveType.CLASS);
                    primitiveTypes.add(PrimitiveType.NAMED_INDIVIDUAL);
                    return null;
                }

                @Override
                public Object visit(OWLDataPropertyData data) throws RuntimeException {
                    primitiveTypes.add(PrimitiveType.DATA_TYPE);
                    primitiveTypes.add(PrimitiveType.LITERAL);
                    return null;
                }

                @Override
                public Object visit(OWLAnnotationPropertyData data) throws RuntimeException {
                    primitiveTypes.add(PrimitiveType.LITERAL);
                    primitiveTypes.add(PrimitiveType.IRI);
                    return null;
                }
            });
            fillerEditor.setMode(propertyData.get() instanceof OWLAnnotationPropertyData ? ExpandingTextBoxMode.MULTI_LINE : ExpandingTextBoxMode.SINGLE_LINE);
        }
        else {
            if(!canInferPropertyType()) {
                propertyEditor.setSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);
            }
            primitiveTypes.addAll(grammar.getFillerTypes());
        }
        if(primitiveTypes.isEmpty()) {
            GWT.log("WARNING: Allowed types is empty for " + propertyData.orNull());
        }
        fillerEditor.setAllowedTypes(primitiveTypes);
        if(isLiteralOrCanEditLiteral(propertyEditor.getValue(), fillerEditor.getValue())) {
            addLangEditor(row, fillerEditor.getLanguageEditor(), fillerEditor.getValue());
        }
        else {
            removeLangEditor(row);
        }
    }

    private void addLangEditor(int rowCount, LanguageEditor languageEditor, Optional<OWLPrimitiveData> fillerData) {
        Widget widget = table.getWidget(rowCount, LANG_COLUMN);
        if(widget instanceof LanguageEditor) {
            widget.setVisible(true);
            return;
        }
        if (fillerData.isPresent() && fillerData.get() instanceof OWLLiteralData) {
            String lang = ((OWLLiteralData) fillerData.get()).getLiteral().getLang();
            languageEditor.setValue(lang);
        }
        table.setWidget(rowCount, LANG_COLUMN, languageEditor.asWidget());
    }

    private void ensureBlankRow() {
        if (!isLastRowEmpty()) {
            addBlankRow();
        }
    }

    private String getTextAt(int row, int col) {
        Widget widget = table.getWidget(row, col);
        if (widget instanceof HasTextRendering) {
            return ((HasTextRendering) widget).getText();
        }
        else {
            return "";
        }
    }

    private boolean isEmpty(int row, int col) {
        return getTextAt(row, col).isEmpty();
    }

    private boolean isEmpty(int row) {
        return isEmpty(row, 0) && isEmpty(row, 1);
    }

    private boolean isLastRowEmpty() {
        int rowCount = table.getRowCount();
        return rowCount != 0 && isEmpty(rowCount - 1);
    }



    @Override
    public void setValue(final PropertyValueList propertyValueList) {
        dirty = false;
        freshEntitiesHandler = new MutableFreshEntitiesHandler();
        RenderingServiceManager.getManager().execute(new GetRendering(projectId, propertyValueList), new GetRenderingCallback() {
            @Override
            public void onSuccess(GetRenderingResponse result) {
                fillUp(propertyValueList, result);
            }
        });
    }

    @Override
    public void clearValue() {
        setValue(new PropertyValueList(Collections.<PropertyValue>emptyList()));
    }


    private PropertyValueList createPropertyList() {
        Set<PropertyValue> propertyValues = new HashSet<PropertyValue>();
        for(int i = 0; i < table.getRowCount(); i++) {
            Optional<PropertyValue> pv = getPropertyValueForRow(i);

            if(pv.isPresent()) {
                propertyValues.add(pv.get());
            }
        }
        return new PropertyValueList(propertyValues);
    }

    private Optional<PropertyValue> getPropertyValueForRow(int rowIndex) {
        PrimitiveDataEditor propertyEditor = (PrimitiveDataEditor) table.getWidget(rowIndex, PROPERTY_COLUMN);
        if(propertyEditor == null) {
            return Optional.absent();
        }
        PrimitiveDataEditor fillerEditor = (PrimitiveDataEditor) table.getWidget(rowIndex, FILLER_COLUMN);
        if(fillerEditor == null) {
            return Optional.absent();
        }
        Optional<OWLPrimitiveData> propertyData = propertyEditor.getValue();
        if(!propertyData.isPresent()) {
            return Optional.absent();
        }
        Optional<OWLPrimitiveData> fillerData = fillerEditor.getValue();
        if(!fillerData.isPresent()) {
            return Optional.absent();
        }
        return createPropertyData(propertyData, fillerData);
    }

    @Override
    public Optional<PropertyValueList> getValue() {
        return Optional.of(createPropertyList());
    }

    private Optional<PropertyValue> createPropertyData(final Optional<OWLPrimitiveData> property, final Optional<OWLPrimitiveData> fillerData) {
        if(!property.isPresent()) {
            return Optional.absent();
        }
        if(!fillerData.isPresent()) {
            return Optional.absent();
        }
        return property.get().accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
            @Override
            protected Optional<PropertyValue> getDefaultReturnValue() {
                return Optional.absent();
            }

            @Override
            public Optional<PropertyValue> visit(final OWLObjectPropertyData propertyData) throws RuntimeException {
                return fillerData.get().accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                    @Override
                    public Optional<PropertyValue> visit(OWLClassData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyClassValue(propertyData.getEntity(), data.getEntity()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLNamedIndividualData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyIndividualValue(propertyData.getEntity(), data.getEntity()));
                    }
                });
            }

            @Override
            public Optional<PropertyValue> visit(final OWLDataPropertyData propertyData) throws RuntimeException {
                return fillerData.get().accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                    @Override
                    public Optional<PropertyValue> visit(OWLDatatypeData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyDatatypeValue(propertyData.getEntity(), data.getEntity()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLLiteralData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyLiteralValue(propertyData.getEntity(), data.getLiteral()));
                    }
                });
            }

            @Override
            public Optional<PropertyValue> visit(final OWLAnnotationPropertyData propertyData) throws RuntimeException {
                return fillerData.get().accept(new OWLPrimitiveDataVisitorAdapter<Optional<PropertyValue>, RuntimeException>() {
                    @Override
                    public Optional<PropertyValue> visit(OWLLiteralData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getLiteral()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(IRIData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getObject()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLClassData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLObjectPropertyData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLDataPropertyData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLAnnotationPropertyData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLNamedIndividualData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity()));
                    }

                    @Override
                    public Optional<PropertyValue> visit(OWLDatatypeData data) throws RuntimeException {
                        return Optional.<PropertyValue>of(new PropertyAnnotationValue(propertyData.getEntity(), data.getEntity()));
                    }
                });
            }
        });
    }


    private void setDirty(boolean dirty, EventStrategy eventStrategy) {
        if(!settingValue) {
            this.dirty = dirty;
            if (eventStrategy == EventStrategy.FIRE_EVENTS) {
                fireEvent(new DirtyChangedEvent());
            }
        }
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<PropertyValueList>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        for(int i = 0; i < table.getRowCount(); i++) {
            Optional<PropertyValue> propertyValue = getPropertyValueForRow(i);
            if(!isEmpty(i)  && !propertyValue.isPresent()) {
                return false;
            }
        }
        return true;
    }


    private class HandlerReference {

        private HandlerRegistration propertyEditorReg;
        private HandlerRegistration fillerEditorReg;
        private HandlerRegistration deleteButtonReg;

        private HandlerReference(HandlerRegistration propertyEditorReg, HandlerRegistration fillerEditorReg, HandlerRegistration deleteButtonReg) {
            this.propertyEditorReg = propertyEditorReg;
            this.fillerEditorReg = fillerEditorReg;
            this.deleteButtonReg = deleteButtonReg;
        }

        public void removeHandlers() {
            propertyEditorReg.removeHandler();
            fillerEditorReg.removeHandler();
            deleteButtonReg.removeHandler();
        }
    }
}

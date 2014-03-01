package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.primitive.FreshEntitiesPolicy;
import edu.stanford.bmir.protege.web.client.primitive.MutableFreshEntitiesHandler;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorSuggestOracleMode;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Collection;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 */
public class PropertyValueEditorPresenter {

    private PropertyValueDescriptorEditor editor;

    private PropertyValueGridGrammar grammar = PropertyValueGridGrammar.getClassGrammar();

    public PropertyValueEditorPresenter(PropertyValueDescriptorEditor editor) {
        this.editor = editor;
        this.editor.addPropertyValueChangedHandler(new PropertyValuePropertyChangedHandler() {
            @Override
            public void handlePropertyChanged(PropertyValuePropertyChangedEvent event) {
                GWT.log("PropertyValuePropertyChangedHandler.handlingEvent");
                PropertyValueEditorPresenter.this.handlePropertyChanged();
            }
        });
        this.editor.addPropertyValueChangedHandler(new PropertyValueValueChangedHandler() {
            @Override
            public void handlePropertyValueChanged(PropertyValueValueChangedEvent event) {
                GWT.log("PropertyValueValueChangedHandler.handlingEvent");
                PropertyValueEditorPresenter.this.handleValueChanged();
            }
        });
        this.editor.setPropertyFieldFreshEntitiesHandler(new PropertyFieldFreshEntitiesHandler());
        this.editor.setPropertyFieldSuggestMode(PrimitiveDataEditorSuggestOracleMode
                .DO_NOT_SUGGEST_CREATE_NEW_ENTITIES);
        this.editor.setValueFieldSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);
        setGrammar(PropertyValueGridGrammar.getClassGrammar());
    }

    private void handlePropertyChanged() {
        propagateAllowedTypes();
        editor.setValuePlaceholder(getValuePlaceholder());
    }

    private void handleValueChanged() {
        if (!editor.getPropertyFieldValue().isPresent() && !editor.getPropertyFieldLexicalValue().isEmpty()) {
            inferPropertyTypeFromFiller();
        }
    }

    public IsWidget getWidget() {
        return editor.asWidget();
    }

    public void setGrammar(PropertyValueGridGrammar grammar) {
        this.grammar = grammar;
        editor.setAllowedPropertyTypes(grammar.getPropertyTypes());
        editor.setAllowedValueTypes(grammar.getValueTypes());
    }

    private void propagateAllowedTypes() {
        GWT.log("Propagating allowed types based on the property type");
        editor.setAllowedPropertyTypes(grammar.getPropertyTypes());
        Optional<OWLPropertyData> propertyData = editor.getPropertyFieldValue();
        if (propertyData.isPresent()) {
            editor.setAllowedValueTypes(grammar.getValueTypesForPropertyType(propertyData.get().getType()));
        }
        else {
            editor.setAllowedValueTypes(grammar.getValueTypes());
        }
        if (!canInferPropertyType()) {
            editor.setPropertyFieldSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);
        }
        editor.setValueFieldSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);
    }

    public PropertyValueGridGrammar getGrammar() {
        return grammar;
    }

    private boolean canInferPropertyType() {
        final Collection<PrimitiveType> propertyTypes = grammar.getPropertyTypes();
        return !(propertyTypes.contains(PrimitiveType.ANNOTATION_PROPERTY) && propertyTypes.contains(PrimitiveType
                .DATA_PROPERTY));
    }

    private static final String PROPERTY_NAME_PLACEHOLDER_TEXT = "Enter property name";

    private static final String FILLER_EDITOR_PLACE_HOLDER_TEXT = "Enter value";

    private String getValuePlaceholder() {
        Optional<OWLPropertyData> propertyData = editor.getPropertyFieldValue();
        if (!propertyData.isPresent()) {
            return FILLER_EDITOR_PLACE_HOLDER_TEXT;
        }
        OWLPrimitiveData entityData = propertyData.get();
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

    private void inferPropertyTypeFromFiller() {
        Optional<OWLPrimitiveData> fillerValue = editor.getValueFieldValue();
        if (!fillerValue.isPresent()) {
            return;
        }
        GWT.log("Infer property type from filler");
        EntityType<?> inferredType = fillerValue.get().accept(new OWLPrimitiveDataVisitorAdapter<EntityType<?>,
                RuntimeException>() {
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
                if (data.getLiteral().isRDFPlainLiteral() || data.getLiteral().getDatatype().isString()) {
                    // TODO:
//                    if (fillerEditor.isClassesAllowed()) {
//                        // Here we assume a class name instead of a literal
//                        fillerEditor.coerceToEntityType(EntityType.CLASS);
//                        return handleObjectProperty();
//                    }
                }
                // Other type of literal so lets assume a data or annotation property
                return handleDataProperty();
            }

            @Override
            public EntityType<?> visit(OWLDatatypeData data) throws RuntimeException {
                return handleDataProperty();
            }

            private EntityType<?> handleDataProperty() {
                if (grammar.getPropertyTypes().contains(PrimitiveType.DATA_PROPERTY)) {
                    return EntityType.DATA_PROPERTY;
                }
                else if (grammar.getPropertyTypes().contains(PrimitiveType.ANNOTATION_PROPERTY)) {
                    return EntityType.ANNOTATION_PROPERTY;
                }
                else {
                    return null;
                }
            }

            private EntityType<?> handleObjectProperty() {
                if (grammar.getPropertyTypes().contains(PrimitiveType.OBJECT_PROPERTY)) {
                    return EntityType.OBJECT_PROPERTY;
                }
                else if (grammar.getPropertyTypes().contains(PrimitiveType.ANNOTATION_PROPERTY)) {
                    return EntityType.ANNOTATION_PROPERTY;
                }
                else {
                    return null;
                }
            }
        });
        if (inferredType != null) {
            // TODO:
            GWT.log("Inferring type as: " + inferredType);
            editor.coercePropertyFieldToType(inferredType);
        }
    }

    private static class PropertyFieldFreshEntitiesHandler extends MutableFreshEntitiesHandler {

        /**
         * Gets the policy supported by this handler.
         *
         * @return The {@link edu.stanford.bmir.protege.web.client.primitive.FreshEntitiesPolicy}.  Not {@code
         * null}.
         */
        @Override
        public FreshEntitiesPolicy getFreshEntitiesPolicy() {
            return FreshEntitiesPolicy.NOT_ALLOWED;
        }

        /**
         * Gets the error message for when an entity is a fresh entity.
         *
         * @param browserText The browser text being parsed.
         * @return An error message.  Not {@code null}.
         */
        @Override
        public String getErrorMessage(String browserText) {
            return "<b>" + browserText + "</b> is a <b>new</b> property name." +
                    "<br>" +
                    "<span style=\"font-size: -2;\">To continue, enter a value for the property (class name, " +
                    "number etc.) and press the TAB key</span>";
        }
    }
}

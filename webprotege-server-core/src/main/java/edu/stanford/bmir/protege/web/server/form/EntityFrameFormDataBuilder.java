package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.entity.BasicEntityProvider;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.frame.HasPropertyValues;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
public class EntityFrameFormDataBuilder {

    @Nonnull
    private final ImmutableSet<OWLProperty> properties;

    private EntityFrameFormDataBuilder(@Nonnull ImmutableSet<OWLProperty> properties) {
        this.properties = properties;

    }

    public static EntityFrameFormDataBuilder getBuilderForForm(@Nonnull FormDescriptor formDescriptor) {
        var dataFactory = new BasicEntityProvider();
        var properties = formDescriptor.getElements()
                      .stream()
                      .map(FormElementDescriptor::getId)
                      .flatMap(elementId -> EntityFormElementId.toProperty(elementId, dataFactory).stream())
                      .collect(toImmutableSet());
        return new EntityFrameFormDataBuilder(properties);
    }

    public FormData getFormDataFromPropertyValues(HasPropertyValues propertyValues) {
        var map = HashMultimap.<FormElementId, FormDataValue>create();
        propertyValues
                .getPropertyValues()
                .stream()
                .filter(this::isFormProperty)
                .forEach(propertyValue -> {
                    var formElementId = toFormElementId(propertyValue);
                    var formDataValue = toFormDataValue(propertyValue);
                    map.put(formElementId, formDataValue);
                });
        var formDataValues = toFlatMap(map);
        return new FormData(formDataValues);
    }

    private boolean isFormProperty(PropertyValue propertyValue) {
        return properties.contains(propertyValue.getProperty()
                                                .getEntity());
    }

    private static FormElementId toFormElementId(PropertyValue propertyValue) {
        var property = propertyValue.getProperty();
        return EntityFormElementId.toElementId(property.getEntity());
    }

    private static FormDataValue toFormDataValue(PropertyValue propertyValue) {
        return propertyValue.getValue().accept(new PrimitiveDataConverter());
    }

    private Map<FormElementId, FormDataValue> toFlatMap(HashMultimap<FormElementId, FormDataValue> map) {
        var finalMap = new HashMap<FormElementId, FormDataValue>();
        map.asMap()
           .forEach((formElementId, formDataValues) -> {
               FormDataValue reducedFormDataValue;
               if(formDataValues.size() == 1) {
                   reducedFormDataValue = formDataValues.iterator()
                                                        .next();
               }
               else {
                   reducedFormDataValue = FormDataList.of(new ArrayList<>(formDataValues));
               }
               finalMap.put(formElementId, reducedFormDataValue);
           });
        return finalMap;
    }

    private static class PrimitiveDataConverter implements OWLPrimitiveDataVisitor<FormDataValue, RuntimeException> {

        @Override
        public FormDataValue visit(OWLClassData data) {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLObjectPropertyData data) {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLDataPropertyData data) {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLAnnotationPropertyData data) {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLNamedIndividualData data) {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLDatatypeData data) {
            return null;
        }

        @Override
        public FormDataValue visit(OWLLiteralData data) {
            return FormDataPrimitive.get(data.getLiteral());
        }

        @Override
        public FormDataValue visit(IRIData data) {
            return FormDataPrimitive.get(data.getObject());
        }
    }
}

package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.CompositeFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.CompositeFieldDescriptorEntry;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.frame.HasPropertyValues;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.management.Descriptor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
public class EntityFrameFormDataBuilder {

    private final ClassFrameTranslator classFrameTranslator;

    private final NamedIndividualFrameTranslator namedIndividualFrameTranslator;

    @Nonnull
    private final OWLEntityProvider entityProvider;

    @AutoFactory
    @Inject
    public EntityFrameFormDataBuilder(@Nonnull @Provided ClassFrameTranslator classFrameTranslator,
                                      @Nonnull @Provided NamedIndividualFrameTranslator namedIndividualFrameTranslator,
                                      @Nonnull @Provided OWLEntityProvider entityProvider) {
        this.classFrameTranslator = classFrameTranslator;
        this.namedIndividualFrameTranslator = namedIndividualFrameTranslator;
        this.entityProvider = entityProvider;
    }

    private static FormElementId toFormElementId(PropertyValue propertyValue) {
        var property = propertyValue.getProperty();
        return EntityFormElementId.toElementId(property.getEntity());
    }

    private static FormDataValue toFormDataValue(PropertyValue propertyValue) {
        return propertyValue.getValue()
                            .accept(new PrimitiveDataConverter());
    }

    @Nonnull
    public FormData getFormData(@Nonnull OWLEntity entity,
                                @Nonnull FormDescriptor formDescriptor) {
        List<FormElementDescriptor> elements = formDescriptor.getElements();
        var propertyValuesByProperty = getPropertyValues(entity);
        var formDataValue = toFormDataObject(entity, propertyValuesByProperty, elements);
        var map = formDataValue.getMap()
                     .entrySet()
                     .stream()
                     .collect(toMap(
                             entry -> FormElementId.get(entry.getKey()),
                             entry -> entry.getValue()
                     ));
        return new FormData(map);
    }

    private Multimap<OWLProperty, PropertyValue> getPropertyValues(OWLEntity entity) {
        HasPropertyValues propertyValues = entity.accept(new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public HasPropertyValues visit(@Nonnull OWLClass cls) {
                return classFrameTranslator.getFrame(OWLClassData.get(cls, "", ImmutableMap.of()));
            }

            @Nonnull
            @Override
            public HasPropertyValues visit(@Nonnull OWLObjectProperty property) {
                return ImmutableSet::of;
            }

            @Nonnull
            @Override
            public HasPropertyValues visit(@Nonnull OWLDataProperty property) {
                return ImmutableSet::of;
            }

            @Nonnull
            @Override
            public HasPropertyValues visit(@Nonnull OWLNamedIndividual individual) {
                return namedIndividualFrameTranslator.getFrame(OWLNamedIndividualData.get(individual,
                                                                                          "",
                                                                                          ImmutableMap.of()));
            }

            @Nonnull
            @Override
            public HasPropertyValues visit(@Nonnull OWLDatatype datatype) {
                return ImmutableSet::of;
            }

            @Nonnull
            @Override
            public HasPropertyValues visit(@Nonnull OWLAnnotationProperty property) {
                return ImmutableSet::of;
            }
        });
        return propertyValues.getPropertyValues()
                      .stream()
                      .collect(ImmutableListMultimap.toImmutableListMultimap(
                              propertyValue -> propertyValue.getProperty().getEntity(),
                              propertyValue -> propertyValue
                      ));

    }

    private Optional<OWLProperty> getAssociatedOwlProperty(FormElementDescriptor descriptor) {
        return toOwlProperty(descriptor.getId());
    }

    private Optional<OWLProperty> toOwlProperty(FormElementId formElementId) {
        return EntityFormElementId.toProperty(formElementId,
                                              entityProvider);
    }

    private FormDataObject toFormDataObject(OWLEntity subject,
                                            Multimap<OWLProperty, PropertyValue> subjectPropertyValuesByProperty,
                                            List<FormElementDescriptor> descriptors) {
        var map = new HashMap<String, FormDataValue>();
        for(FormElementDescriptor descriptor : descriptors) {
            var associatedOwlProperty = getAssociatedOwlProperty(descriptor);
            if(associatedOwlProperty.isPresent()) {
                var theProperty = associatedOwlProperty.get();
                var propertyValues = subjectPropertyValuesByProperty.get(theProperty);
                var formDataValueForProperty = toFormDataValue(descriptor, propertyValues);
                map.put(descriptor.getId().getId(), formDataValueForProperty);
            }
        }
        return new FormDataObject(map);
    }

    /**
     * Translates the property values to a form data value object.
     * If there are multiple values then a list object will be returned.  Individual values will either be simple
     * form data values or they will be a form data object depending upon whether the descriptor is a composite
     * descriptor or not.
     * @param descriptor The descriptor for the particular field.
     * @param propertyValues The values for the particular field.
     * @return A single {@link FormDataValue} that encompases the translation of the property values
     */
    private FormDataValue toFormDataValue(FormElementDescriptor descriptor, Collection<PropertyValue> propertyValues) {
        if(descriptor.isComposite()) {
            return toCompositeFormDataValues((CompositeFieldDescriptor) descriptor.getFieldDescriptor(), propertyValues);
        }
        else {
            return toSimpleFormDataValues(propertyValues);
        }
    }

    private FormDataValue toSimpleFormDataValues(Collection<PropertyValue> propertyValues) {
        if(propertyValues.size() == 1) {
            return toFormDataValue(propertyValues.iterator().next());
        }
        var formDataValues = propertyValues.stream()
                      .map(EntityFrameFormDataBuilder::toFormDataValue)
                      .collect(toList());
        return new FormDataList(formDataValues);
    }

    private FormDataValue toCompositeFormDataValues(CompositeFieldDescriptor descriptor, Collection<PropertyValue> propertyValues) {
        var childDescriptors = descriptor.getChildDescriptors()
                  .stream()
                  .map(CompositeFieldDescriptorEntry::getDescriptor)
                  .collect(toList());
        // Property values should be entities
        var valueList = propertyValues.stream()
                      .map(PropertyValue::getValue)
                      .map(OWLPrimitiveData::asEntity)
                      .flatMap(Optional::stream)
                      .map(entity -> toFormDataObject(entity, getPropertyValues(entity), childDescriptors))
                      .collect(Collectors.toList());
        if(valueList.size() == 1) {
            return valueList.get(0);
        }
        else {
            return new FormDataList(valueList);
        }

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

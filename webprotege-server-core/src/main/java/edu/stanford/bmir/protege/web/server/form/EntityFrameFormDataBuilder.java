package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.PrimitiveDataConverter;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.HasPropertyValues;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    private static FormDataValue toFormDataValue(PropertyValue propertyValue) {
        return propertyValue.getValue()
                            .accept(new PrimitiveDataConverter());
    }

    private Optional<OWLProperty> getAssociatedOwlProperty(FormFieldDescriptor descriptor) {
        return descriptor.getOwlBinding().flatMap(OwlBinding::getOwlProperty);
    }

    @Nonnull
    public FormData getFormData(@Nonnull OWLEntity entity,
                                @Nonnull FormDescriptor formDescriptor) {
        var propertyValuesByProperty = getPropertyValues(entity);
        return toFormData(entity, propertyValuesByProperty, formDescriptor);
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
                                     propertyValue -> propertyValue.getProperty()
                                                                   .getEntity(),
                                     propertyValue -> propertyValue
                             ));

    }

    //    private Optional<OWLProperty> toOwlProperty(FormFieldId formElementId) {
    //        return EntityFormElementId.toProperty(formElementId,
    //                                              entityProvider);
    //    }

    private FormData toFormData(OWLEntity subject,
                                Multimap<OWLProperty, PropertyValue> subjectPropertyValuesByProperty,
                                FormDescriptor formDescriptor) {
        var map = new HashMap<FormFieldId, FormDataValue>();
        for(FormFieldDescriptor descriptor : formDescriptor.getElements()) {
            var associatedOwlProperty = getAssociatedOwlProperty(descriptor);
            if(associatedOwlProperty.isPresent()) {
                var theProperty = associatedOwlProperty.get();
                var propertyValues = subjectPropertyValuesByProperty.get(theProperty);
                var formDataValueForProperty = toFormDataValue(descriptor, propertyValues);
                map.put(descriptor.getId(), formDataValueForProperty);
            }
        }
        return new FormData(subject, map, formDescriptor);
    }

    /**
     * Translates the property values to a form data value object.
     * If there are multiple values then a list object will be returned.  Individual values will either be simple
     * form data values or they will be a form data object depending upon whether the descriptor is a composite
     * descriptor or not.
     *
     * @param descriptor     The descriptor for the particular field.
     * @param propertyValues The values for the particular field.  The values will all have the same property.
     * @return A single {@link FormDataValue} that encompases the translation of the property values
     */
    private FormDataValue toFormDataValue(FormFieldDescriptor descriptor, Collection<PropertyValue> propertyValues) {
        return toFormDataValue(propertyValues, descriptor.getFormControlDescriptor());
    }

    private FormDataValue toSimpleFormDataValues(Collection<PropertyValue> propertyValues) {
        if(propertyValues.size() == 1) {
            return toFormDataValue(propertyValues.iterator()
                                                 .next());
        }
        var formDataValues = propertyValues.stream()
                                           .map(EntityFrameFormDataBuilder::toFormDataValue)
                                           .collect(toList());
        return new FormDataList(formDataValues);
    }

    @Nonnull
    private FormDataValue toGridFormDataValue(GridControlDescriptor descriptor,
                                              Collection<PropertyValue> propertyValues) {
        // Property values should be entities
        var valueList = propertyValues.stream()
                                      .map(PropertyValue::getValue)
                                      .map(OWLPrimitiveData::asEntity)
                                      .flatMap(Optional::stream)
                                      // Property values that are entities
                                      .map(entityValue -> toFormDataObject(entityValue, getPropertyValues(entityValue), descriptor.getColumns()))
                                      .collect(Collectors.toList());
        if(valueList.size() == 1) {
            return valueList.get(0);
        }
        else {
            return new FormDataList(valueList);
        }
    }

    private FormDataObject toFormDataObject(OWLEntity subject,
                                            Multimap<OWLProperty, PropertyValue> subjectPropertyValuesByProperty,
                                            ImmutableList<GridColumnDescriptor> columnDescriptors) {
        var map = new HashMap<String, FormDataValue>();
        for(GridColumnDescriptor descriptor : columnDescriptors) {
            var associatedOwlProperty = descriptor.getOwlBinding().flatMap(OwlBinding::getOwlProperty);
            if(associatedOwlProperty.isPresent()) {
                var theProperty = associatedOwlProperty.get();
                var propertyValues = subjectPropertyValuesByProperty.get(theProperty);
                var formDataValueForProperty = toFormDataValue(descriptor, propertyValues);
                map.put(descriptor.getId().getId(), formDataValueForProperty);
            }
        }
        return new FormDataObject(map);
    }

    private FormDataValue toFormDataValue(GridColumnDescriptor descriptor, Collection<PropertyValue> propertyValues) {
        return toFormDataValue(propertyValues, descriptor.getFieldDescriptor());
    }

    private FormDataValue toFormDataValue(Collection<PropertyValue> propertyValues,
                                          FormControlDescriptor fieldDescriptor) {
        if(fieldDescriptor instanceof SubFormControlDescriptor) {
            return toSubFormDataValue((SubFormControlDescriptor) fieldDescriptor, propertyValues);
        }
        else if(fieldDescriptor instanceof GridControlDescriptor) {
            return toGridFormDataValue((GridControlDescriptor) fieldDescriptor, propertyValues);
        }
        else {
            return toSimpleFormDataValues(propertyValues);
        }
    }


    private FormDataValue toSubFormDataValue(SubFormControlDescriptor descriptor,
                                             Collection<PropertyValue> propertyValues) {
        // Property values should be entities
        var valueList = propertyValues.stream()
                                      .map(PropertyValue::getValue)
                                      .map(OWLPrimitiveData::asEntity)
                                      .flatMap(Optional::stream)
                                      // Property values that are entities
                                      .map(entityValue -> {
                                          FormDescriptor subFormDescriptor = descriptor.getFormDescriptor();
                                          return toFormData(entityValue, getPropertyValues(entityValue), subFormDescriptor);
                                      })
                                      .collect(Collectors.toList());
        if(valueList.size() == 1) {
            return valueList.get(0);
        }
        else {
            return new FormDataList(valueList);
        }

    }

}

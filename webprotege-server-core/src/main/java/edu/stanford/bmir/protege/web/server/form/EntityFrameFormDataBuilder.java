package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
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
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

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

    @Nonnull
    public FormData getFormData(@Nonnull OWLEntity entity,
                                @Nonnull FormDescriptor formDescriptor) {
        var formProperties = formDescriptor.getElements()
                                           .stream()
                                           .map(FormElementDescriptor::getId)
                                           .map(this::toOwlProperty)
                                           .flatMap(Optional::stream)
                                           .collect(toImmutableSet());
        var map = HashMultimap.<FormElementId, FormDataValue>create();
        getPropertyValues(entity)
                .getPropertyValues()
                .stream()
                .filter(propertyValue -> formProperties.contains(propertyValue.getProperty()
                                                                              .getEntity()))
                .forEach(propertyValue -> {
                    var formElementId = toFormElementId(propertyValue);
                    var formDataValue = toFormDataValue(propertyValue);
                    map.put(formElementId, formDataValue);
                });
        var formDataValues = toFlatMap(map);
        return new FormData(formDataValues);
    }

    private Optional<OWLProperty> toOwlProperty(FormElementId formElementId) {
        return EntityFormElementId.toProperty(formElementId,
                                              entityProvider);
    }

    private HasPropertyValues getPropertyValues(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<>() {
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
    }

    private static FormElementId toFormElementId(PropertyValue propertyValue) {
        var property = propertyValue.getProperty();
        return EntityFormElementId.toElementId(property.getEntity());
    }

    private static FormDataValue toFormDataValue(PropertyValue propertyValue) {
        return propertyValue.getValue()
                            .accept(new PrimitiveDataConverter());
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

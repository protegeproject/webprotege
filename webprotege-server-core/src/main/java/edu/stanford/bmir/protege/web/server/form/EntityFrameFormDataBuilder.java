package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.EntityFrameProvider;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.OWLPrimitive2FormControlDataConverter;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
public class EntityFrameFormDataBuilder {

    /**
     * This is too arbitrary but safer for now in the case of very large ontologies
     */
    private static final int MAX_FIELD_SIZE = 50;

    @Nonnull
    private final EntityFrameProvider entityFrameProvider;

    @Nonnull
    private final EntityFrameMapperFactory entityFrameMapperFactory;

    @Nonnull
    private final OWLPrimitive2FormControlDataConverter converter;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @AutoFactory
    @Inject
    public EntityFrameFormDataBuilder(@Nonnull EntityFrameProvider entityFrameProvider,
                                      @Nonnull EntityFrameMapperFactory entityFrameMapperFactory,
                                      @Nonnull OWLPrimitive2FormControlDataConverter converter,
                                      @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex) {
        this.entityFrameProvider = checkNotNull(entityFrameProvider);
        this.entityFrameMapperFactory = checkNotNull(entityFrameMapperFactory);
        this.converter = converter;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
    }

    private ImmutableList<FormControlData> toFormControlValues(@Nonnull OWLEntity subject,
                                                               @Nonnull BoundControlDescriptor descriptor) {
        var owlBinding = descriptor.getOwlBinding();
        if(owlBinding.isEmpty()) {
            return ImmutableList.of();
        }
        var theBinding = owlBinding.get();
        var entityFrame = entityFrameProvider.getEntityFrame(subject, false);
        var entityFrameMapper = entityFrameMapperFactory.create(entityFrame);
        var values = entityFrameMapper.getValues(theBinding);


        var formControlDescriptor = descriptor.getFormControlDescriptor();
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<>() {
            @Override
            public ImmutableList<FormControlData> visit(TextControlDescriptor textControlDescriptor) {
                return values.stream()
                             .filter(p -> p instanceof OWLLiteral)
                             .map(p -> (OWLLiteral) p)
                             .map(literal -> TextControlData.get(textControlDescriptor, literal))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(NumberControlDescriptor numberControlDescriptor) {
                return values.stream()
                             .filter(p -> p instanceof OWLLiteral)
                             .map(p -> (OWLLiteral) p)
                             .map(value -> NumberControlData.get(numberControlDescriptor, value))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                return values.stream()
                             .map(converter::toFormControlData)
                             .map(value -> SingleChoiceControlData.get(singleChoiceControlDescriptor, value))
                             .limit(1)
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                var vals = values.stream()
                                 .map(converter::toFormControlData)
                                 .collect(toImmutableList());
                return ImmutableList.of(MultiChoiceControlData.get(multiChoiceControlDescriptor,
                                                                   vals));
            }

            @Override
            public ImmutableList<FormControlData> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return values.stream()
                             .filter(p -> p instanceof OWLEntity)
                             .map(p -> (OWLEntity) p)
                             .map(entity -> EntityNameControlData.get(entityNameControlDescriptor, entity))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(ImageControlDescriptor imageControlDescriptor) {
                return values.stream()
                             .filter(p -> p instanceof IRI)
                             .map(p -> (IRI) p)
                             .map(iri -> ImageControlData.get(imageControlDescriptor, iri))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(GridControlDescriptor gridControlDescriptor) {
                return ImmutableList.of(toGridControlData(values, gridControlDescriptor));
            }

            @Override
            public ImmutableList<FormControlData> visit(SubFormControlDescriptor subFormControlDescriptor) {
                // TODO: CHECK FOR CYCLES
                FormDescriptor subFormDescriptor = subFormControlDescriptor.getFormDescriptor();
                return values.stream()
                             .filter(p -> p instanceof OWLEntity)
                             .map(p -> (OWLEntity) p)
                             .map(entity -> toFormData(entity, subFormDescriptor))
                             .collect(toImmutableList());
            }
        });
    }

    public FormData toFormData(@Nonnull OWLEntity subject, FormDescriptor formDescriptor) {
        var fieldData = formDescriptor.getFields()
                                      .stream()
                                      .map(field -> {
                                          ImmutableList<FormControlData> formControlValues = toFormControlValues(subject,
                                                                                                               field);
                                          var controlValuesStream = formControlValues.stream();
                                          if(field.getRepeatability() == Repeatability.NON_REPEATABLE) {
                                              controlValuesStream = controlValuesStream.limit(1);
                                          }
                                          else {
                                              controlValuesStream = controlValuesStream.limit(MAX_FIELD_SIZE);
                                          }
                                          var limitedFormControlValues = controlValuesStream.collect(toImmutableList());
                                          return FormFieldData.get(field,
                                                                   limitedFormControlValues,
                                                                   formControlValues.size());
                                      })
                                      .collect(toImmutableList());
        return FormData.get(Optional.of(FormEntitySubject.get(subject)), formDescriptor, fieldData);
    }

    @Nullable
    private OWLEntity toEntityFormSubject(OWLPrimitive primitive) {
        if(primitive instanceof OWLEntity) {
            return (OWLEntity) primitive;
        }
        else if(primitive instanceof IRI) {
            var iri = (IRI) primitive;
            return entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(iri)
                                                .sorted()
                                                .findFirst()
                                                .orElse(null);
        }
        else {
            return null;
        }
    }

    private GridControlData toGridControlData(ImmutableList<OWLPrimitive> subjects,
                                              GridControlDescriptor gridControlDescriptor) {
        var rowData = subjects.stream()
                              .map(this::toEntityFormSubject)
                              .filter(Objects::nonNull)
                              .limit(MAX_FIELD_SIZE)
                              .map(entity -> {
                                  var columnDescriptors = gridControlDescriptor.getColumns();
                                  // To Cells
                                  var cellData = columnDescriptors.stream()
                                                                  .map(columnDescriptor -> {
                                                                      var formControlData = toFormControlValues(entity,
                                                                                                                columnDescriptor);
                                                                      // What should happen here?  There are multiple values binding
                                                                      // for a given subject - i.e. multiple values per cell
                                                                      if(formControlData.isEmpty()) {
                                                                          return GridCellData.get(columnDescriptor.getId(),
                                                                                                  null);
                                                                      }
                                                                      else {
                                                                          var firstValue = formControlData.get(0);
                                                                          return GridCellData.get(columnDescriptor.getId(),
                                                                                                  firstValue);
                                                                      }
                                                                  })
                                                                  .collect(toImmutableList());
                                  var formSubject = FormEntitySubject.get(entity);
                                  return GridRowData.get(formSubject, cellData);
                              })
                              .collect(toImmutableList());
        return GridControlData.get(gridControlDescriptor, rowData, subjects.size());
    }
}

package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.EntityFrameProvider;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.PrimitiveDataConverter;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
public class EntityFrameFormDataBuilder {

    @Nonnull
    private final EntityFrameProvider entityFrameProvider;

    @AutoFactory
    @Inject
    public EntityFrameFormDataBuilder(@Nonnull EntityFrameProvider entityFrameProvider) {
        this.entityFrameProvider = checkNotNull(entityFrameProvider);
    }

    private ImmutableList<FormControlData> toFormControlValues(@Nonnull OWLEntity subject,
                                                               @Nonnull BoundControlDescriptor descriptor) {
        var owlBinding = descriptor.getOwlBinding();
        if(owlBinding.isEmpty()) {
            return ImmutableList.of();
        }
        var theBinding = owlBinding.get();
        var entityFrame = entityFrameProvider.getEntityFrame(subject);
        var entityFrameMapper = new EntityFrameMapper(entityFrame);
        var values = entityFrameMapper.getValues(theBinding);


        var formControlDescriptor = descriptor.getFormControlDescriptor();
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<ImmutableList<FormControlData>>() {
            @Override
            public ImmutableList<FormControlData> visit(TextControlDescriptor textControlDescriptor) {
                return values.stream()
                             .map(OWLPrimitiveData::asLiteral)
                             .flatMap(Optional::stream)
                             .map(literal -> TextControlData.get(textControlDescriptor, literal))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(NumberControlDescriptor numberControlDescriptor) {
                return values.stream()
                             .map(OWLPrimitiveData::asLiteral)
                             .flatMap(Optional::stream)
                             .map(value -> NumberControlData.get(numberControlDescriptor, value))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                PrimitiveDataConverter converter = new PrimitiveDataConverter();
                return values.stream()
                             .map(value -> value.accept(converter))
                             .map(value -> SingleChoiceControlData.get(singleChoiceControlDescriptor, value))
                             .limit(1)
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                PrimitiveDataConverter converter = new PrimitiveDataConverter();
                var vals = values.stream()
                                 .map(value -> value.accept(converter))
                                 .collect(toImmutableList());
                return ImmutableList.of(MultiChoiceControlData.get(multiChoiceControlDescriptor,
                                                                   vals));
            }

            @Override
            public ImmutableList<FormControlData> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return values.stream()
                      .map(OWLPrimitiveData::asEntity)
                      .flatMap(Optional::stream)
                      .map(entity -> EntityNameControlData.get(entityNameControlDescriptor, entity))
                      .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlData> visit(ImageControlDescriptor imageControlDescriptor) {
                return values.stream()
                             .map(OWLPrimitiveData::asIRI)
                             .flatMap(Optional::stream)
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
                             .map(OWLPrimitiveData::asEntity)
                             .flatMap(Optional::stream)
                             .map(entity -> toFormData(entity, subFormDescriptor))
                             .collect(toImmutableList());
            }
        });
    }

    public FormData toFormData(@Nonnull OWLEntity subject, FormDescriptor formDescriptor) {
        var fieldData = formDescriptor.getFields()
                                      .stream()
                                      .map(field -> {
                                          var controlValues = toFormControlValues(subject, field);
                                          if(field.getRepeatability() == Repeatability.NON_REPEATABLE) {
                                              var limitedControlValues = controlValues.stream()
                                                      .limit(1)
                                                      .collect(toImmutableList());
                                              return FormFieldData.get(field, limitedControlValues);
                                          }
                                          else {
                                              return FormFieldData.get(field, controlValues);
                                          }
                                      })
                                      .collect(toImmutableList());
        return FormData.get(Optional.of(FormEntitySubject.get(subject)), formDescriptor, fieldData);
    }

    private GridControlData toGridControlData(ImmutableList<OWLPrimitiveData> subjects,
                                              GridControlDescriptor gridControlDescriptor) {
        var rowData = subjects.stream()
                              .map(OWLPrimitiveData::asEntity)
                              .flatMap(Optional::stream)
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
        return GridControlData.get(gridControlDescriptor, rowData);
    }
}

package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.EntityFrameProvider;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.OWLPrimitive2FormControlDataConverter;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
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

    public FormData toFormData(@Nonnull OWLEntity subject,
                               @Nonnull FormDescriptor formDescriptor,
                               @Nonnull FormPageRequestIndex formPageRequestIndex) {
        var formSubject = FormSubject.get(subject);
        var fieldData = formDescriptor.getFields()
                                      .stream()
                                      .map(field -> {
                                          ImmutableList<FormControlData> formControlValues = toFormControlValues(subject,
                                                                                                                 field.getId(),
                                                                                                                 field,
                                                                                                                 formPageRequestIndex);
                                          var controlValuesStream = formControlValues.stream();
                                          var pageRequest = formPageRequestIndex.getPageRequest(formSubject, field.getId(), FormPageRequest.SourceType.CONTROL_STACK);
                                          var controlValuesPage = controlValuesStream.collect(PageCollector.toPage(
                                                  pageRequest.getPageNumber(),
                                                  pageRequest.getPageSize()
                                          )).orElse(Page.emptyPage());
                                          return FormFieldData.get(field,
                                                                   controlValuesPage);
                                      })
                                      .collect(toImmutableList());
        return FormData.get(Optional.of(formSubject), formDescriptor, fieldData);
    }

    private ImmutableList<FormControlData> toFormControlValues(@Nonnull OWLEntity subject,
                                                               @Nonnull FormRegionId formFieldId,
                                                               @Nonnull BoundControlDescriptor descriptor,
                                                               @Nonnull FormPageRequestIndex formPageRequestIndex) {
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
                return ImmutableList.of(toGridControlData(subject, formFieldId, values, gridControlDescriptor, formPageRequestIndex));
            }

            @Override
            public ImmutableList<FormControlData> visit(SubFormControlDescriptor subFormControlDescriptor) {
                // TODO: CHECK FOR CYCLES
                FormDescriptor subFormDescriptor = subFormControlDescriptor.getFormDescriptor();
                return values.stream()
                             .filter(p -> p instanceof OWLEntity)
                             .map(p -> (OWLEntity) p)
                             .map(entity -> toFormData(entity, subFormDescriptor, formPageRequestIndex))
                             .collect(toImmutableList());
            }
        });
    }

    private GridControlData toGridControlData(OWLPrimitive root,
                                              FormRegionId formFieldId,
                                              ImmutableList<OWLPrimitive> subjects,
                                              GridControlDescriptor gridControlDescriptor,
                                              @Nonnull FormPageRequestIndex formPageRequestIndex) {
        var rootSubject = getFormSubject(root);
        var pageRequest = formPageRequestIndex.getPageRequest(rootSubject, formFieldId, FormPageRequest.SourceType.GRID_CONTROL);
        var rowData = subjects.stream()
                              .map(this::toEntityFormSubject)
                              .filter(Objects::nonNull)
                              .map(entity -> {
                                  var columnDescriptors = gridControlDescriptor.getColumns();
                                  // To Cells
                                  var cellData = columnDescriptors.stream()
                                                                  .map(columnDescriptor -> {

                                                                      var formControlData = toFormControlValues(entity,
                                                                                                                columnDescriptor.getId(),
                                                                                                                columnDescriptor,
                                                                                                                formPageRequestIndex);
                                                                      // What should happen here?  There are multiple values binding
                                                                      // for a given subject - i.e. multiple values per cell
                                                                      if(formControlData.isEmpty()) {
                                                                          return GridCellData.get(columnDescriptor.getId(),
                                                                                                  ImmutableList.of());
                                                                      }
                                                                      else {
                                                                          if(columnDescriptor.getRepeatability() == Repeatability.NON_REPEATABLE) {
                                                                              var firstValue = formControlData.get(0);
                                                                              return GridCellData.get(columnDescriptor.getId(),
                                                                                                      ImmutableList.of(
                                                                                                              firstValue));

                                                                          }
                                                                          else {
                                                                              return GridCellData.get(columnDescriptor.getId(),
                                                                                                      formControlData);
                                                                          }
                                                                      }
                                                                  })
                                                                  .collect(toImmutableList());
                                  var formSubject = FormEntitySubject.get(entity);
                                  return GridRowData.get(formSubject, cellData);
                              })
                              .collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                            pageRequest.getPageSize()));
        return GridControlData.get(gridControlDescriptor, rowData.orElse(Page.emptyPage()));
    }

    private FormSubject getFormSubject(OWLPrimitive root) {
        if(root instanceof IRI) {
            return FormSubject.get((IRI) root);
        }
        else if(root instanceof OWLEntity) {
            return FormSubject.get((OWLEntity) root);
        }
        else {
            throw new RuntimeException("Cannot process form subjects that are not IRIs or Entities");
        }
    }
}

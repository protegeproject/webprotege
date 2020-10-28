package edu.stanford.bmir.protege.web.server.entity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.form.EntityFormChangeListGeneratorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.FreshEntityIri;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-01
 */
public class CreateEntityFromFormDataChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final EntityFormChangeListGeneratorFactory formChangeListGeneratorFactory;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final EntityType<?> entityType;

    @Nonnull
    private final FreshEntityIri freshEntityIri;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final FormData formData;

    @Nonnull
    private final RenderingManager renderingManager;

    @AutoFactory
    public CreateEntityFromFormDataChangeListGenerator(@Provided @Nonnull EntityFormChangeListGeneratorFactory formChangeListGeneratorFactory,
                                                       @Provided @Nonnull OWLDataFactory dataFactory,
                                                       @Nonnull EntityType<?> entityType,
                                                       @Nonnull FreshEntityIri freshEntityIri,
                                                       @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                       @Nonnull FormData formData,
                                                       @Provided @Nonnull RenderingManager renderingManager) {
        this.formChangeListGeneratorFactory = formChangeListGeneratorFactory;
        this.dataFactory = checkNotNull(dataFactory);
        this.entityType = checkNotNull(entityType);
        this.freshEntityIri = checkNotNull(freshEntityIri);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
        this.formData = checkNotNull(formData);
        this.renderingManager = renderingManager;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        var entity = dataFactory.getOWLEntity(entityType, freshEntityIri.getIri());
        var changeListBuilder = ImmutableList.<OntologyChange>builder();
        var ontologyId = defaultOntologyIdManager.getDefaultOntologyDocumentId();
        // Declare the entity
        changeListBuilder.add(AddAxiomChange.of(ontologyId, dataFactory.getOWLDeclarationAxiom(entity)));
        // Place the entity
        entity.accept(new OWLEntityVisitor() {
            @Override
            public void visit(@Nonnull OWLClass cls) {
                freshEntityIri.getParentEntities(dataFactory, EntityType.CLASS)
                              .stream()
                              .map(entity -> (OWLClass) entity)
                              .map(entity -> dataFactory.getOWLSubClassOfAxiom(cls, entity))
                              .map(ax -> AddAxiomChange.of(ontologyId, ax))
                              .forEach(changeListBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLObjectProperty property) {
                freshEntityIri.getParentEntities(dataFactory, EntityType.OBJECT_PROPERTY)
                              .stream()
                              .map(entity -> (OWLObjectProperty) entity)
                              .map(entity -> dataFactory.getOWLSubObjectPropertyOfAxiom(property, entity))
                              .map(ax -> AddAxiomChange.of(ontologyId, ax))
                              .forEach(changeListBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLDataProperty property) {
                freshEntityIri.getParentEntities(dataFactory, EntityType.DATA_PROPERTY)
                              .stream()
                              .map(entity -> (OWLDataProperty) entity)
                              .map(entity -> dataFactory.getOWLSubDataPropertyOfAxiom(property, entity))
                              .map(ax -> AddAxiomChange.of(ontologyId, ax))
                              .forEach(changeListBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLNamedIndividual individual) {
                freshEntityIri.getParentEntities(dataFactory, EntityType.CLASS)
                              .stream()
                              .map(entity -> (OWLClass) entity)
                              .map(entity -> dataFactory.getOWLClassAssertionAxiom(entity, individual))
                              .map(ax -> AddAxiomChange.of(ontologyId, ax))
                              .forEach(changeListBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLDatatype datatype) {
                // No parents
            }

            @Override
            public void visit(@Nonnull OWLAnnotationProperty property) {
                freshEntityIri.getParentEntities(dataFactory, EntityType.ANNOTATION_PROPERTY)
                              .stream()
                              .map(entity -> (OWLAnnotationProperty) entity)
                              .map(entity -> dataFactory.getOWLSubAnnotationPropertyOfAxiom(property, entity))
                              .map(ax -> AddAxiomChange.of(ontologyId, ax))
                              .forEach(changeListBuilder::add);
            }
        });
        var formId = formData.getFormDescriptor().getFormId();
        var pristineFormData = ImmutableMap.of(formId,
                                               FormData.empty(entity, formId));
        var newEntityFormData = ImmutableMap.of(formId,
                                                formData);
        var formChangesList = formChangeListGeneratorFactory.create(entity, pristineFormData, newEntityFormData);
        var formChanges = formChangesList.generateChanges(context);
        return OntologyChangeList.<OWLEntity>builder()
                          .addAll(changeListBuilder.build())
                          .addAll(formChanges.getChanges())
                          .build(formChanges.getResult());
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        return "Created " + renderingManager.getRendering(result.getSubject())
                .getBrowserText();
    }
}

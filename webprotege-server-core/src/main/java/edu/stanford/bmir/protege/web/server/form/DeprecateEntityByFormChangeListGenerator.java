package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.entity.EntityRenamer;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.FormPurpose;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDeprecationStrategy;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.EntityDeprecationSettings;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorExAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
public class DeprecateEntityByFormChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final OWLEntity entityToBeDeprecated;

    @Nonnull
    private final Optional<FormData> deprecationFormData;

    @Nonnull
    private final Optional<OWLEntity> replacementEntity;

    private EntityDeprecationSettings entityDeprecationSettings;

    @Nonnull
    private final EntityFormChangeListGeneratorFactory formChangeListGeneratorFactory;

    @Nonnull
    private final EntityFormManager entityFormManager;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private String message = "";

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectComponent projectComponent;

    @Nonnull
    private final EntityRenamer entityRenamer;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final OWLDataFactory dataFactory;


    @AutoFactory
    public DeprecateEntityByFormChangeListGenerator(@Nonnull OWLEntity entityToBeDeprecated,
                                                    @Nonnull Optional<FormData> deprecationFormData,
                                                    @Nonnull Optional<OWLEntity> replacementEntity,
                                                    @Nonnull EntityDeprecationSettings entityDeprecationSettings,
                                                    @Provided @Nonnull EntityFormChangeListGeneratorFactory formChangeListGeneratorFactory,
                                                    @Provided @Nonnull EntityFormManager entityFormManager,
                                                    @Provided @Nonnull MessageFormatter messageFormatter,
                                                    @Provided @Nonnull ProjectId projectId,
                                                    @Provided @Nonnull ProjectComponent projectComponent,
                                                    @Provided @Nonnull EntityRenamer entityRenamer,
                                                    @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                    @Provided @Nonnull OWLDataFactory dataFactory) {
        this.entityToBeDeprecated = checkNotNull(entityToBeDeprecated);
        this.deprecationFormData = checkNotNull(deprecationFormData);
        this.replacementEntity = checkNotNull(replacementEntity);
        this.entityDeprecationSettings = checkNotNull(entityDeprecationSettings);
        this.formChangeListGeneratorFactory = formChangeListGeneratorFactory;
        this.entityFormManager = entityFormManager;
        this.messageFormatter = messageFormatter;
        this.projectId = projectId;
        this.projectComponent = projectComponent;
        this.entityRenamer = entityRenamer;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.dataFactory = dataFactory;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        message = messageFormatter.format("Deprecated {0}", entityToBeDeprecated);

        // Note that the order of these changes is important

        var allChangesBuilder = OntologyChangeList.<OWLEntity>builder();

        // Replace usages of the deprecated entity with other replacement entity
        // This effectively removes the deprecated entity from the ontology
        var replaceChanges = getReplaceEntityChanges();

        // TODO: Replaced by annotation

        allChangesBuilder.addAll(replaceChanges);

        var formDescriptors = entityFormManager.getFormDescriptors(entityToBeDeprecated,
                                                                   projectId,
                                                                   FormPurpose.ENTITY_EDITING);

        // Clean up the replacement entity by removing anything from the deprecated entity
        // that was copied over by the above find-and-replace
        var changesToCleanUpReplacementEntity = cleanUpReplacementEntity(context);
        allChangesBuilder.addAll(changesToCleanUpReplacementEntity);

        // Add the stuff we want to preserve to the deprecated entity
        var changesToPreserveFormData = getChangesToPreserveFields(context, formDescriptors);
        allChangesBuilder.addAll(changesToPreserveFormData);

        // Add the stuff from the filled out entity deprecation form to the deprecated entity
        var changesToAddEntityDeprecationFormData = getChangesToAddDeprecationFormData(context).getChanges();
        allChangesBuilder.addAll(changesToAddEntityDeprecationFormData);

        var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        // Mark the deprecated entity as deprecated.  Make sure it's also declared, otherwise
        // it will just be a deprecated IRI
        allChangesBuilder.add(AddAxiomChange.of(defaultOntologyId,
                                                dataFactory.getOWLDeclarationAxiom(entityToBeDeprecated)));
        allChangesBuilder.add(AddAxiomChange.of(defaultOntologyId,
                                                dataFactory.getDeprecatedOWLAnnotationAssertionAxiom(
                                                        entityToBeDeprecated.getIRI())));

        // TODO: Remove the parent

        // Reparent the deprecated entity if a parent is specified by the settings
        getPlacementAxiom()
                .ifPresent(ax -> allChangesBuilder.add(AddAxiomChange.of(defaultOntologyId,
                                                                         ax)));

        return allChangesBuilder.build(entityToBeDeprecated);
    }

    private Optional<OWLAxiom> getPlacementAxiom() {
        return entityToBeDeprecated.accept(new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLClass cls) {
                return entityDeprecationSettings.getDeprecatedClassesParent()
                                                .map(parent -> dataFactory.getOWLSubClassOfAxiom(cls, parent));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLObjectProperty property) {
                return entityDeprecationSettings.getDeprecatedObjectPropertiesParent()
                                                .map(parent -> dataFactory.getOWLSubObjectPropertyOfAxiom(property,
                                                                                                          parent));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLDataProperty property) {
                return entityDeprecationSettings.getDeprecatedDataPropertiesParent()
                                                .map(parent -> dataFactory.getOWLSubDataPropertyOfAxiom(property, parent));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLNamedIndividual individual) {
                return entityDeprecationSettings.getDeprecatedIndividualsParent()
                                                .map(parent -> dataFactory.getOWLClassAssertionAxiom(parent, individual));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLDatatype datatype) {
                return Optional.empty();
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLAnnotationProperty property) {
                return entityDeprecationSettings.getDeprecatedAnnotationPropertiesParent()
                                                .map(parent -> dataFactory.getOWLSubAnnotationPropertyOfAxiom(property,
                                                                                                              parent));
            }
        });
    }

    private List<OntologyChange> getChangesToPreserveFields(ChangeGenerationContext context,
                                                            ImmutableList<FormDescriptor> formDescriptors) {
        var preservedFormDescriptors = formDescriptors.stream()
                                                      .map(formDescriptor -> formDescriptor.withFields(field -> field.getDeprecationStrategy()
                                                                                                                     .equals(FormFieldDeprecationStrategy.LEAVE_VALUES_INTACT)))
                                                      .collect(toImmutableList());
        // By this time, if the deprecated entity was replaced then we need to act on the
        // replacement entity, otherwise we act on the deprecated entity
        var formDataToPreserve = getFormData(preservedFormDescriptors, entityToBeDeprecated);
        return formChangeListGeneratorFactory.createForAdd(entityToBeDeprecated, formDataToPreserve)
                                             .generateChanges(context)
                                             .getChanges();
    }

    private List<OntologyChange> cleanUpReplacementEntity(ChangeGenerationContext context) {

        if(replacementEntity.isEmpty()) {
            return Collections.emptyList();
        }

        var changes = new ArrayList<OntologyChange>();
        // Remove all of the stuff on the deprecated entity

        var formDescriptorsForDeprecatedEntity = entityFormManager.getFormDescriptors(entityToBeDeprecated,
                                                                                      projectId,
                                                                                      FormPurpose.ENTITY_EDITING);
        // First remove all fields that are on the deprecated entity from the replacement entity that are there
        // as a result of the rename
        var formDataOnDeprecatedEntity = getFormData(formDescriptorsForDeprecatedEntity,
                                                     entityToBeDeprecated);
        // Map this data to the replacement entity so that we can remove it from the replacement entity
        var formDataOnReplacementToRemove = formDataOnDeprecatedEntity.values()
                                  .stream()
                                  .map(formData -> FormData.get(Optional.<FormSubject>of(FormEntitySubject.get(replacementEntity.get())),
                                                                formData.getFormDescriptor(),
                                                                formData.getFormFieldData()))
                                  .collect(toImmutableMap(FormData::getFormId, formData -> formData));

        // Generate the changes to remove the deprecated form data from the replacement entity
        changes.addAll(formChangeListGeneratorFactory.createForRemove(replacementEntity.get(),
                                                                      formDataOnReplacementToRemove)
                                                     .generateChanges(context)
                                                     .getChanges());

        // Make sure that we have not inadvertently removed any form data from the replacement.  We do
        // this by adding back in all of the fields on the replacement entity.  Changes that add back existing
        // data will happily be ignored
        var formDescriptorsFormReplacementEntity = entityFormManager.getFormDescriptors(replacementEntity.get(),
                                                                                        projectId,
                                                                                        FormPurpose.ENTITY_EDITING);
        var formDataForReplacement = getFormData(formDescriptorsFormReplacementEntity, replacementEntity.get());
        changes.addAll(formChangeListGeneratorFactory.createForAdd(replacementEntity.get(), formDataForReplacement)
                                                     .generateChanges(context)
                                                     .getChanges());
        return changes;
    }

    private OntologyChangeList<OWLEntity> getChangesToAddDeprecationFormData(ChangeGenerationContext context) {
        if (deprecationFormData.isEmpty()) {
            return OntologyChangeList.<OWLEntity>builder().build(entityToBeDeprecated);
        }
        return formChangeListGeneratorFactory.createForAdd(entityToBeDeprecated,
                                                           ImmutableMap.of(deprecationFormData.get().getFormId(),
                                                                           deprecationFormData.get()))
                                             .generateChanges(context);
    }

    private List<OntologyChange> getReplaceEntityChanges() {
        if(replacementEntity.isEmpty()) {
            return Collections.emptyList();
        }
        var replacement = replacementEntity.get();
        var changes = entityRenamer.generateChanges(ImmutableMap.of(entityToBeDeprecated, replacement.getIRI()));
        return changes.stream()
                      .filter(chg -> chg.accept(new OntologyChangeVisitorEx<>() {
                          @Override
                          public Boolean getDefaultReturnValue() {
                              return true;
                          }

                          @Override
                          public Boolean visit(@Nonnull AddAxiomChange addAxiomChange) {
                              // Don't reposition the replacement entity into the position of the deprecated entity
                              return addAxiomChange.getAxiom().accept(new OWLAxiomVisitorExAdapter<>(true) {
                                  @Nonnull
                                  @Override
                                  public Boolean visit(OWLSubClassOfAxiom axiom) {
                                      return !(axiom.getSubClass().equals(replacement) && axiom.getSuperClass().isNamed());
                                  }
                              });
                          }
                      }))
                .collect(toImmutableList());
    }


    private ImmutableMap<FormId, FormData> getFormData(ImmutableList<FormDescriptor> formDescriptors,
                                                       OWLEntity subject) {
        return formDescriptors.stream()
                              .map(fd -> getFormData(fd, subject))
                              .collect(toImmutableMap(FormDataDto::getFormId, FormDataDto::toFormData));


    }

    private FormDataDto getFormData(FormDescriptor formDescriptor, OWLEntity subject) {
        return projectComponent.getEntityFrameFormDataComponentBuilder(new EntityFrameFormDataModule())
                               .formDataBuilder()
                               .toFormData(subject, formDescriptor);
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        return message;
    }
}

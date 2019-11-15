package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-14
 */
public class EntityFormDataConverterSession {

    @Nonnull
    private final Map<FormData, OWLEntity> formData2Subject = new HashMap<>();

    @Nonnull
    private final Set<String> axiomTemplates = new HashSet<>();

    @Nonnull
    private final Multimap<OWLEntity, PropertyValue> entityPropertyValues = HashMultimap.create();

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Nonnull
    private final EntityFormSubjectFactory formSubjectFactory;

    @Nonnull
    private Optional<IRI> targetOntologyIri = Optional.empty();

    @AutoFactory
    @Inject
    public EntityFormDataConverterSession(@Nonnull ProjectId projectId,
                                          @Nonnull @Provided ProjectDetailsManager projectDetailsManager,
                                          @Nonnull @Provided EntityFormSubjectFactory formSubjectFactory) {
        this.projectId = checkNotNull(projectId);
        this.projectDetailsManager = projectDetailsManager;
        this.formSubjectFactory = formSubjectFactory;
    }

    public void putPropertyValue(@Nonnull OWLEntity entity,
                                 @Nonnull PropertyValue propertyValue) {
        checkNotNull(entity);
        checkNotNull(propertyValue);
        entityPropertyValues.put(entity, propertyValue);
    }

    @Nonnull
    public ImmutableMultimap<OWLEntity, PropertyValue> getEntityPropertyValues() {
        return ImmutableMultimap.copyOf(entityPropertyValues);
    }

    @Nonnull
    public Optional<OWLEntity> getSubject(@Nonnull FormData formData) {
        checkNotNull(formData);
        Optional<OWLEntity> subject = formData.getSubject();
        if(subject.isPresent()) {
            return subject;
        }
        var cachedFormSubject = formData2Subject.get(formData);
        if(cachedFormSubject != null) {
            return Optional.of(cachedFormSubject);
        }
        FormDescriptor descriptor = formData.getFormDescriptor();
        return descriptor.getSubjectFactoryDescriptor()
                         .map(subjectFactoryDescriptor -> {
                             var suppliedNameTemplate = subjectFactoryDescriptor.getSuppliedNameTemplate();
                             var entityType = subjectFactoryDescriptor.getEntityType();
                             var langTag = getDefaultLanguageTag();
                             var formSubject = formSubjectFactory.createSubject(suppliedNameTemplate,
                                                                                entityType,
                                                                                langTag);
                             formData2Subject.put(formData, formSubject);
                             var entityIri = formSubject.getIRI();
                             var axiomTemplates = subjectFactoryDescriptor.getAxiomTemplates()
                                                     .stream()
                                                     .map(axiomTemplate -> axiomTemplate.replace("${subject.iri}", entityIri.toQuotedString()))
                                                     .collect(Collectors.toList());
                             this.axiomTemplates.addAll(axiomTemplates);
                             targetOntologyIri = subjectFactoryDescriptor.getTargetOntologyIri();
                             return formSubject;
                         });
    }

    @Nonnull
    public Optional<IRI> getTargetOntologyIri() {
        return targetOntologyIri;
    }

    @Nonnull
    public Set<String> getAdditionalAxiomTemplates() {
        return ImmutableSet.copyOf(axiomTemplates);
    }

    private Optional<String> getDefaultLanguageTag() {
        var lang = projectDetailsManager.getProjectDetails(projectId)
                                        .getDefaultDictionaryLanguage()
                                        .getLang();
        if(lang.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(lang);
        }
    }

}

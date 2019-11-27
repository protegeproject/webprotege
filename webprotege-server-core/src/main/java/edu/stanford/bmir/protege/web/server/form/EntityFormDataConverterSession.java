package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
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
    private final Map<FormData, OWLEntity> formSubjects = new HashMap<>();

    @Nonnull
    private final Set<String> axiomTemplates = new HashSet<>();

    @Nonnull
    private final Multimap<FormData, PropertyValue> formPropertyValues = HashMultimap.create();

    @Nonnull
    private final EntityFormSubjectFactory formSubjectFactory;

    @Nonnull
    private Optional<IRI> targetOntologyIri = Optional.empty();

    private Optional<String> langTag = Optional.empty();

    @AutoFactory
    @Inject
    public EntityFormDataConverterSession(@Nonnull @Provided EntityFormSubjectFactory formSubjectFactory) {
        this.formSubjectFactory = formSubjectFactory;
    }

    public void setLangTag(Optional<String> langTag) {
        this.langTag = checkNotNull(langTag);
    }

    public void addFormData(@Nonnull FormData formData) {
        checkNotNull(formData);
        Optional<OWLEntity> subject = formData.getSubject();
        if(subject.isPresent()) {
            formSubjects.put(formData, subject.get());
        }
        else {
            var formDescriptor = formData.getFormDescriptor();
            formDescriptor.getSubjectFactoryDescriptor()
                          .ifPresent(subjectFactoryDescriptor -> {
                              // Create and map the form subject
                              var formSubject = createFormSubject(subjectFactoryDescriptor);
                              formSubjects.put(formData, formSubject);
                              // Create and store axiom templates for this subject
                              var axiomTemplates = createAxiomTemplatesForFormSubject(subjectFactoryDescriptor,
                                                                                      formSubject);
                              this.axiomTemplates.addAll(axiomTemplates);
                              targetOntologyIri = subjectFactoryDescriptor.getTargetOntologyIri();
                          });
        }

    }

    private OWLEntity createFormSubject(@Nonnull EntityFormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        var suppliedNameTemplate = subjectFactoryDescriptor.getSuppliedNameTemplate();
        var entityType = subjectFactoryDescriptor.getEntityType();
        return formSubjectFactory.createSubject(suppliedNameTemplate,
                                                entityType,
                                                langTag);
    }

    @Nonnull
    private List<String> createAxiomTemplatesForFormSubject(EntityFormSubjectFactoryDescriptor subjectFactoryDescriptor,
                                                            OWLEntity formSubject) {
        var entityIri = formSubject.getIRI();
        return subjectFactoryDescriptor.getAxiomTemplates()
                                       .stream()
                                       .map(axiomTemplate -> axiomTemplate.replace(
                                               "${subject.iri}",
                                               entityIri.toQuotedString()))
                                       .collect(Collectors.toList());
    }

    @Nonnull
    public Set<String> getAdditionalAxiomTemplates() {
        return ImmutableSet.copyOf(axiomTemplates);
    }

    public Collection<FormData> getFormData() {
        return ImmutableSet.copyOf(formSubjects.keySet());
    }

    public Collection<PropertyValue> getFormDataPropertyValues(FormData formData) {
        return ImmutableList.copyOf(formPropertyValues.get(formData));
    }

    @Nonnull
    public Optional<OWLEntity> getSubject(@Nonnull FormData formData) {
        return Optional.ofNullable(formSubjects.get(formData));
    }

    @Nonnull
    public Optional<IRI> getTargetOntologyIri() {
        return targetOntologyIri;
    }

    public void putPropertyValue(@Nonnull FormData formData,
                                 @Nonnull PropertyValue propertyValue) {
        checkNotNull(formData);
        checkNotNull(propertyValue);
        formPropertyValues.put(formData, propertyValue);
    }

}

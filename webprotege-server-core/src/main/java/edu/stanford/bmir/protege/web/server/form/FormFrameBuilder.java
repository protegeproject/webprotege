package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormIriSubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-13
 */
public class FormFrameBuilder {

    @Nonnull
    private final ImmutableSet.Builder<OWLClass> classes = ImmutableSet.builder();

    @Nonnull
    private final ImmutableSet.Builder<OWLNamedIndividual> instances = ImmutableSet.builder();

    @Nonnull
    private final ImmutableSet.Builder<OWLClass> subClasses = ImmutableSet.builder();

    @Nonnull
    private final ImmutableSet.Builder<PlainPropertyValue> propertyValues = ImmutableSet.builder();

    @Nonnull
    private final Map<FormFrameBuilder, OwlBinding> nestedFrames = new HashMap<>();

    @Nullable
    private FormSubject subject = null;

    @Nullable
    private FormSubjectFactoryDescriptor subjectFactoryDescriptor = null;

    @Inject
    public FormFrameBuilder() {
    }

    /**
     * Add a value that is an entity that is bound to the subject via the specified binding
     * @param binding The binding
     * @param entity The entity value
     */
    public void addEntityValue(@Nonnull OwlBinding binding,
                    @Nonnull OWLEntity entity) {
        addValue(binding, entity);
    }

    /**
     * Add a value that is an literal that is bound to the subject via the specified binding
     * @param binding The binding
     * @param literal The literal value
     */
    public void addLiteralValue(@Nonnull OwlBinding binding,
                    @Nonnull OWLLiteral literal) {
        addValue(binding, literal);
    }

    /**
     * Add a value that is an IRI that is bound to the subject via the specified binding
     * @param binding The binding
     * @param iri The iri value
     */
    public void addIriValue(@Nonnull OwlBinding binding,
                    @Nonnull IRI iri) {
        addValue(binding, iri);
    }

    /**
     * Add a binding to a value that is a complex value (i.e. a grid or subform)
     * @param binding The binding
     * @param formFrameBuilder The formbuilder that represents the complex sub-form
     */
    public void add(@Nonnull OwlBinding binding,
                    @Nonnull FormFrameBuilder formFrameBuilder) {
        nestedFrames.put(formFrameBuilder, binding);
    }

    public FormFrame build(@Nonnull FormSubjectResolver subjectResolver) {
        var nestedFramesBuilder = ImmutableSet.<FormFrame>builder();
        nestedFrames.forEach((nestedFrameBuilder, binding) -> {
            var resolvedNestedSubject = subjectResolver.resolveSubject(nestedFrameBuilder);
            // Add binding to the resolved nested subject
            FormFrameBuilder.this.add(binding, resolvedNestedSubject);
            // Reset the resolved subject in the nested frame builder
            nestedFrameBuilder.setSubject(resolvedNestedSubject);
            // Add placement to parents
            subjectResolver.getResolvedParents(nestedFrameBuilder)
                           .forEach(nestedFrameBuilder::addClass);
            // Build and add the nested frame to this frame
            var nestedFrame = nestedFrameBuilder.build(subjectResolver);
            nestedFramesBuilder.add(nestedFrame);
        });
        // Resolve subject for this form frame
        var resolvedSubject = subjectResolver.resolveSubject(this);
        return FormFrame.get(resolvedSubject,
                             classes.build(),
                             subClasses.build(),
                             instances.build(),
                             propertyValues.build(),
                             nestedFramesBuilder.build());
    }

    @Nonnull
    public Optional<FormSubject> getSubject() {
        return Optional.ofNullable(subject);
    }

    public void setSubject(@Nonnull FormSubject subject) {
        this.subject = subject;
    }

    public Optional<FormSubjectFactoryDescriptor> getSubjectFactoryDescriptor() {
        return Optional.ofNullable(this.subjectFactoryDescriptor);
    }

    public void setSubjectFactoryDescriptor(@Nullable FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        this.subjectFactoryDescriptor = checkNotNull(subjectFactoryDescriptor);
    }

    private void addValue(@Nonnull OwlBinding binding,
                          @Nonnull OWLObject value) {

        if(binding instanceof OwlClassBinding) {
            // Mapped to a type
            if(value instanceof OWLClass) {
                addClass((OWLClass) value);
            }
        }
        else if(binding instanceof OwlInstanceBinding) {
            if(value instanceof OWLNamedIndividual) {
                addIndividual((OWLNamedIndividual) value);
            }
        }
        else if(binding instanceof OwlSubClassBinding) {
            if(value instanceof OWLClass) {
                subClasses.add((OWLClass) value);
            }
        }
        else {
            // Mapped to property value
            var propertyBinding = (OwlPropertyBinding) binding;
            addPropertyValue(propertyBinding, value);
        }
    }



    private void add(OwlBinding binding, FormSubject value) {
        value.accept(new FormSubject.FormDataSubjectVisitor() {
            @Override
            public void visit(@Nonnull FormEntitySubject formDataEntitySubject) {
                addEntityValue(binding, formDataEntitySubject.getEntity());
            }

            @Override
            public void visit(@Nonnull FormIriSubject formDataIriSubject) {
                addIriValue(binding, formDataIriSubject.getIri());
            }
        });
    }

    private void addClass(@Nonnull OWLClass value) {
        classes.add(value);
    }

    private void addIndividual(@Nonnull OWLNamedIndividual value) {
        instances.add(value);
    }

    private void addPropertyValue(OwlPropertyBinding propertyBinding, @Nonnull OWLObject value) {
        propertyBinding.getProperty()
                       .accept(new OWLEntityVisitorAdapter() {
                           @Override
                           public void visit(@Nonnull OWLObjectProperty property) {
                               addObjectPropertyValue(property, value);
                           }

                           public void visit(@Nonnull OWLDataProperty property) {
                               addDataPropertyValue(property, value);
                           }

                           @Override
                           public void visit(@Nonnull OWLAnnotationProperty property) {
                               addAnnotationPropertyValue(property, value);
                           }
                       });
    }

    private void addObjectPropertyValue(@Nonnull OWLObjectProperty property,
                                       @Nonnull OWLObject value) {
        value.accept(new OWLObjectVisitorAdapter() {
            @Override
            public void visit(OWLNamedIndividual individual) {
                var pv = PlainPropertyIndividualValue.get(property, individual, State.ASSERTED);
                propertyValues.add(pv);
            }

            @Override
            public void visit(OWLClass cls) {
                var pv = PlainPropertyClassValue.get(property, cls, State.ASSERTED);
                propertyValues.add(pv);
            }
        });
    }

    private void addDataPropertyValue(@Nonnull OWLDataProperty property,
                                     @Nonnull OWLObject value) {
        value.accept(new OWLObjectVisitorAdapter() {
            @Override
            public void visit(OWLDatatype datatype) {
                var pv = PlainPropertyDatatypeValue.get(property, datatype, State.ASSERTED);
                propertyValues.add(pv);
            }

            @Override
            public void visit(OWLLiteral literal) {
                var pv = PlainPropertyLiteralValue.get(property, literal, State.ASSERTED);
                propertyValues.add(pv);
            }
        });
    }

    private void addAnnotationPropertyValue(@Nonnull OWLAnnotationProperty property,
                                           @Nonnull OWLObject value) {
        value.accept(new OWLObjectVisitorAdapter() {
            @Override
            public void visit(OWLClass ce) {
                addAnnotationPropertyEntity(property, ce);
            }

            @Override
            public void visit(OWLNamedIndividual individual) {
                addAnnotationPropertyEntity(property, individual);
            }

            @Override
            public void visit(OWLObjectProperty objectProperty) {
                addAnnotationPropertyEntity(property, objectProperty);
            }

            @Override
            public void visit(OWLDataProperty dataProperty) {
                addAnnotationPropertyEntity(property, dataProperty);
            }

            @Override
            public void visit(OWLAnnotationProperty annotationProperty) {
                addAnnotationPropertyEntity(property, annotationProperty);
            }

            @Override
            public void visit(OWLDatatype datatype) {
                addAnnotationPropertyEntity(property, datatype);
            }

            @Override
            public void visit(OWLLiteral literal) {
                addAnnotationPropertyLiteral(literal, property);
            }

            @Override
            public void visit(IRI iri) {
                addAnnotationPropertyIri(iri, property);
            }
        });
    }

    private void addAnnotationPropertyEntity(OWLAnnotationProperty property, HasIRI ce) {
        addAnnotationPropertyIri(ce.getIRI(), property);
    }

    private void addAnnotationPropertyLiteral(OWLLiteral literal,
                                             @Nonnull OWLAnnotationProperty property) {
        var pv = PlainPropertyAnnotationValue.get(
                property, literal, State.ASSERTED);
        propertyValues.add(pv);
    }

    private void addAnnotationPropertyIri(IRI iri, @Nonnull OWLAnnotationProperty property) {
        var pv = PlainPropertyAnnotationValue.get(property, iri, State.ASSERTED);
        propertyValues.add(pv);
    }
}

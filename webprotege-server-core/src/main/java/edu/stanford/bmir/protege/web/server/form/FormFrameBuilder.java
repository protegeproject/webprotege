package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormIriSubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlClassBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlInstanceBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlPropertyBinding;
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
    private final RenderingManager renderingManager;

    @Nonnull
    private final ImmutableSet.Builder<OWLClassData> classes = ImmutableSet.builder();

    @Nonnull
    private final ImmutableSet.Builder<OWLNamedIndividualData> instances = ImmutableSet.builder();

    @Nonnull
    private final ImmutableSet.Builder<PropertyValue> propertyValues = ImmutableSet.builder();

    @Nonnull
    private final Map<FormFrameBuilder, OwlBinding> nestedFrames = new HashMap<>();

    @Nullable
    private FormSubject subject = null;

    @Nullable
    private FormSubjectFactoryDescriptor subjectFactoryDescriptor = null;

    private ImmutableSet.Builder<OWLAxiom> placementAxioms = ImmutableSet.builder();

    @Inject
    public FormFrameBuilder(@Nonnull RenderingManager renderingManager) {
        this.renderingManager = checkNotNull(renderingManager);
    }

    public void add(@Nonnull OwlBinding binding,
                    @Nonnull OWLEntity entity) {
        addObject(binding, entity);
    }

    public void add(@Nonnull OwlBinding binding,
                    @Nonnull OWLLiteral literal) {
        addObject(binding, literal);
    }

    private void addObject(@Nonnull OwlBinding binding,
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
        else {
            // Mapped to property value
            var propertyBinding = (OwlPropertyBinding) binding;
            addPropertyValue(propertyBinding, value);
        }
    }


    private void addClass(@Nonnull OWLClass value) {
        var clsData = renderingManager.getClassData(value);
        classes.add(clsData);
    }

    private void addIndividual(@Nonnull OWLNamedIndividual value) {
        var individualData = renderingManager.getIndividualData(value);
        instances.add(individualData);
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

    public void addObjectPropertyValue(@Nonnull OWLObjectProperty property,
                                       @Nonnull OWLObject value) {
        value.accept(new OWLObjectVisitorAdapter() {
            @Override
            public void visit(OWLNamedIndividual individual) {
                var pv = PropertyIndividualValue.get(
                        renderingManager.getObjectPropertyData(property),
                        renderingManager.getIndividualData(individual),
                        State.ASSERTED
                );
                propertyValues.add(pv);
            }

            @Override
            public void visit(OWLClass cls) {
                var pv = PropertyClassValue.get(
                        renderingManager.getObjectPropertyData(property),
                        renderingManager.getClassData(cls),
                        State.ASSERTED
                );
                propertyValues.add(pv);
            }
        });
    }

    public void addDataPropertyValue(@Nonnull OWLDataProperty property,
                                     @Nonnull OWLObject value) {
        value.accept(new OWLObjectVisitorAdapter() {
            @Override
            public void visit(OWLDatatype datatype) {
                var pv = PropertyDatatypeValue.get(
                        renderingManager.getDataPropertyData(property),
                        renderingManager.getDatatypeData(datatype),
                        State.ASSERTED
                );
                propertyValues.add(pv);
            }

            @Override
            public void visit(OWLLiteral literal) {
                var pv = PropertyLiteralValue.get(
                        renderingManager.getDataPropertyData(property),
                        OWLLiteralData.get(literal),
                        State.ASSERTED
                );
                propertyValues.add(pv);
            }
        });
    }

    public void addAnnotationPropertyValue(@Nonnull OWLAnnotationProperty property,
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

    public void addAnnotationPropertyLiteral(OWLLiteral literal,
                                             @Nonnull OWLAnnotationProperty property) {
        var pv = PropertyAnnotationValue.get(
                renderingManager.getAnnotationPropertyData(property),
                OWLLiteralData.get(literal),
                State.ASSERTED
        );
        propertyValues.add(pv);
    }

    public void addAnnotationPropertyIri(IRI iri, @Nonnull OWLAnnotationProperty property) {
        var pv = PropertyAnnotationValue.get(
                renderingManager.getAnnotationPropertyData(property),
                IRIData.get(iri, ImmutableMap.of()),
                State.ASSERTED
        );
        propertyValues.add(pv);
    }

    public void add(@Nonnull OwlBinding binding,
                    @Nonnull IRI iri) {
        addObject(binding, iri);
    }

    public void add(@Nonnull OwlBinding binding,
                    @Nonnull FormFrameBuilder formFrameBuilder) {
        nestedFrames.put(formFrameBuilder, binding);
    }

    public void add(OwlBinding binding, FormSubject value) {
        value.accept(new FormSubject.FormDataSubjectVisitor() {
            @Override
            public void visit(@Nonnull FormEntitySubject formDataEntitySubject) {
                add(binding, formDataEntitySubject.getEntity());
            }

            @Override
            public void visit(@Nonnull FormIriSubject formDataIriSubject) {
                add(binding, formDataIriSubject.getIri());
            }
        });
    }

    private void addPlacementAxiom(OWLAxiom axiom) {
        placementAxioms.add(axiom);
    }

    public FormFrame build(@Nonnull FormSubjectResolver subjectResolver) {
        var nestedFramesBuilder = ImmutableSet.<FormFrame>builder();
        nestedFrames.forEach((nestedFrameBuilder, binding) -> {
            var resolvedNestedSubject = subjectResolver.resolveSubject(nestedFrameBuilder);
            resolvedNestedSubject.ifPresent(subj -> {
                // Add binding to the resolved nested subject
                FormFrameBuilder.this.add(binding, subj);
                // Reset the resolved subject in the nested frame builder
                nestedFrameBuilder.setSubject(subj);
                // Add placement to parents
                subjectResolver.getResolvedParents(nestedFrameBuilder)
                               .forEach(nestedFrameBuilder::addClass);
                // Build and add the nested frame to this frame
                var nestedFrame = nestedFrameBuilder.build(subjectResolver);
                nestedFramesBuilder.add(nestedFrame);
            });
        });
        var resolvedSubject = subjectResolver.resolveSubject(this)
                                             .orElseThrow();
        // Add placement axioms

        return FormFrame.get(resolvedSubject,
                             classes.build(),
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
}

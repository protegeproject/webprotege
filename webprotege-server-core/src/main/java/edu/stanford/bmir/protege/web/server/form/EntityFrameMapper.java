package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlClassBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlInstanceBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlPropertyBinding;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
public class EntityFrameMapper {

    @Nonnull
    private final EntityFrame<?> frame;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ClassAssertionAxiomsByClassIndex index;

    @Nonnull
    private final RenderingManager renderingManager;


    public EntityFrameMapper(@Nonnull EntityFrame<?> frame,
                             @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                             @Nonnull ClassAssertionAxiomsByClassIndex index,
                             @Nonnull RenderingManager renderingManager) {
        this.frame = checkNotNull(frame);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.index = checkNotNull(index);
        this.renderingManager = checkNotNull(renderingManager);
    }

    public ImmutableList<OWLPrimitiveData> getValues(@Nonnull OwlBinding binding) {
        if(binding instanceof OwlClassBinding) {
            if(frame instanceof ClassFrame) {
                return ImmutableList.copyOf(((ClassFrame) frame).getClassEntries());
            }
            else if(frame instanceof NamedIndividualFrame) {
                return ImmutableList.copyOf(((NamedIndividualFrame) frame).getClasses());
            }
            else {
                return ImmutableList.of();
            }
        }
        else if(binding instanceof OwlInstanceBinding) {
            if(frame instanceof ClassFrame) {
                var classFrame = (ClassFrame) frame;
                var subjectCls = classFrame.getSubject().getEntity();
                return projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> index.getClassAssertionAxioms(subjectCls, ontId))
                                      .map(OWLClassAssertionAxiom::getIndividual)
                                      .filter(OWLIndividual::isNamed)
                                      .map(OWLIndividual::asOWLNamedIndividual)
                                      .map(renderingManager::getIndividualData)
                                      .collect(toImmutableList());
            }
            else {
                return ImmutableList.of();
            }
        }
        else {
            var propertyBinding = (OwlPropertyBinding) binding;
            if(frame instanceof HasPropertyValues) {
                return ((HasPropertyValues) frame).getPropertyValues()
                        .stream()
                        .filter(propertyValue -> propertyValue.getProperty().getEntity().equals(propertyBinding.getProperty()))
                        .map(PropertyValue::getValue)
                        .collect(toImmutableList());
            }
            else {
                return ImmutableList.of();
            }
        }
    }
}

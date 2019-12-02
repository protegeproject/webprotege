package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormControlDescriptorPresenter implements FormControlDescriptorPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final FormDescriptorPresenter subFormPresenter;

    @Nonnull
    private final SubFormControlDescriptorView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public SubFormControlDescriptorPresenter(@Nonnull ProjectId projectId,
                                             @Nonnull FormDescriptorPresenter subFormPresenter,
                                             @Nonnull SubFormControlDescriptorView view,
                                             @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.subFormPresenter = checkNotNull(subFormPresenter);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void clear() {
        view.clear();
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        FormDescriptor subFormDescriptor = subFormPresenter.getFormDescriptor();
        EntityType entityType = view.getEntityType();
        List<String> parentTemplates = view.getParents()
                                           .stream()
                                           .filter(primitiveData -> primitiveData instanceof OWLClassData)
                                           .map(primitiveData -> (OWLClassData) primitiveData)
                                           .map(OWLClassData::getEntity)
                                           .map(OWLEntity::getIRI)
                                           .map(IRI::toQuotedString)
                                           .map(clsIri -> {
                                               if(entityType.equals(EntityType.CLASS)) {
                                                   return "SubClassOf(${subject.iri} " + clsIri + ")";
                                               }
                                               else {
                                                   return "ClassAssertion(" + clsIri + " ${subject.iri})";
                                               }
                                           })
                                           .collect(toList());

        EntityFormSubjectFactoryDescriptor subjectFactoryDescriptor = EntityFormSubjectFactoryDescriptor.get(
                entityType,
                "",
                ImmutableList.copyOf(parentTemplates),
                Optional.empty()
        );
        FormDescriptor aug = new FormDescriptor(
                subFormDescriptor.getFormId(),
                subFormDescriptor.getLabel(),
                subFormDescriptor.getElements(),
                Optional.of(subjectFactoryDescriptor)
        );
        return new SubFormControlDescriptor(aug);
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        view.clear();
        if(!(formControlDescriptor instanceof SubFormControlDescriptor)) {
            return;
        }
        SubFormControlDescriptor subFormFieldDescriptor = (SubFormControlDescriptor) formControlDescriptor;
        FormDescriptor subFormDescriptor = subFormFieldDescriptor.getFormDescriptor();
        subFormPresenter.setFormDescriptor(subFormDescriptor);
        subFormDescriptor.getSubjectFactoryDescriptor()
                         .ifPresent(fac -> {
                             view.setEntityType(fac.getEntityType());
                             renderAndSetParents(subFormDescriptor);
                         });
    }

    public void renderAndSetParents(FormDescriptor subFormDescriptor) {
        List<OWLClass> parents = getParents(subFormDescriptor);
        List<OWLPrimitiveData> parentsData = new ArrayList<>();
        parents.forEach(parent -> dispatchServiceManager.execute(new GetEntityRenderingAction(
                                                                         projectId,
                                                                         parent),
                                                                 result -> {
                                                                     parentsData.add(result.getEntityData());
                                                                     if(parentsData.size() == parents.size()) {
                                                                         view.setParents(
                                                                                 parentsData);
                                                                     }
                                                                 }));
    }

    private List<OWLClass> getParents(FormDescriptor formDescriptor) {
        return formDescriptor.getSubjectFactoryDescriptor()
                             .map(descriptor -> {
                                 return descriptor.getAxiomTemplates()
                                                  .stream()
                                                  .map(axiomTemplate -> new ParentClassAxiomParser().parseParentAxiom(
                                                          axiomTemplate))
                                                  .filter(Optional::isPresent)
                                                  .map(Optional::get)
                                                  .collect(toList());
                             })
                             .orElse(Collections.emptyList());
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        subFormPresenter.start(view.getSubFormContainer(), new SimpleEventBus());
    }
}

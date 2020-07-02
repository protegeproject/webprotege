package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static dagger.internal.codegen.DaggerStreams.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-14
 */
public class FormSubjectResolver {

    private final Map<FormFrameBuilder, FormSubject> formFrameFormSubjectMap = new IdentityHashMap<>();

    private final Map<FormFrameBuilder, Set<OWLClass>> resolvedParentMap = new IdentityHashMap<>();

    @Nonnull
    private final EntityFormSubjectFactory entityFormSubjectFactory;

    @Inject
    public FormSubjectResolver(@Nonnull EntityFormSubjectFactory entityFormSubjectFactory) {
        this.entityFormSubjectFactory = checkNotNull(entityFormSubjectFactory);
    }

    @Nonnull
    public FormSubject resolveSubject(FormFrameBuilder formFrame) {
            var existingSubject = formFrameFormSubjectMap.get(formFrame);
            if(existingSubject != null) {
                return existingSubject;
            }
            final FormSubject theSubject;
            final ImmutableSet<OWLClass> parents;
            var subject = formFrame.getSubject();
            if(subject.isEmpty()) {
                // The subject factory descriptor MUST be present.  If it isn't the form
                // descriptor has not been configured properly.
                var formSubjectFactoryDescriptor = formFrame.getSubjectFactoryDescriptor()
                                                            .orElseThrow(this::createSubjectFactoryDescriptorNotPresentException);
                parents = formSubjectFactoryDescriptor.getParent().stream().collect(toImmutableSet());
                var freshSubject = entityFormSubjectFactory.createSubject(formSubjectFactoryDescriptor);
                theSubject = FormSubject.get(freshSubject);
            }
            else {
                // Form subject is already present
                theSubject = subject.get();
                parents = ImmutableSet.of();
            }
            formFrameFormSubjectMap.put(formFrame, theSubject);
            resolvedParentMap.put(formFrame, parents);
            return theSubject;
    }

    public RuntimeException createSubjectFactoryDescriptorNotPresentException() {
        return new RuntimeException("FormSubjectFactoryDescriptor is not present.  Cannot create new form subjects.");
    }

    @Nonnull
    public Set<OWLClass> getResolvedParents(FormFrameBuilder formFrameBuilder) {
        var resolvedParent = resolvedParentMap.get(formFrameBuilder);
        if(resolvedParent == null) {
            return ImmutableSet.of();
        }
        else {
            return ImmutableSet.copyOf(resolvedParent);
        }
    }
}

package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-14
 */
public class FormSubjectResolver {

    private final Map<FormFrameBuilder, FormSubject> formFrameFormSubjectMap = new IdentityHashMap<>();

    @Nonnull
    private final EntityFormSubjectFactory entityFormSubjectFactory;

    @Inject
    public FormSubjectResolver(@Nonnull EntityFormSubjectFactory entityFormSubjectFactory) {
        this.entityFormSubjectFactory = checkNotNull(entityFormSubjectFactory);
    }

    @Nonnull
    public Optional<FormSubject> resolveSubject(FormFrameBuilder formFrame) {
            var existingSubject = formFrameFormSubjectMap.get(formFrame);
            if(existingSubject != null) {
                return Optional.of(existingSubject);
            }
            final Optional<FormSubject> theSubject;
            var subject = formFrame.getSubject();
            if(subject.isEmpty()) {
                var freshSubject = formFrame.getSubjectFactoryDescriptor()
                                            .map(subjectFactoryDescriptor -> {
                                                String pattern = getGeneratedNamePattern(subjectFactoryDescriptor);
                                                return entityFormSubjectFactory.createSubject(
                                                        pattern,
                                                        subjectFactoryDescriptor.getEntityType(),
                                                        Optional.empty()
                                                );
                                            })
                                            .map(FormEntitySubject::get);
                theSubject = freshSubject.map(s -> s);
            }
            else {
                theSubject = subject;
            }
            theSubject.ifPresent(s -> formFrameFormSubjectMap.put(formFrame, s));
            return theSubject;
    }

    public String getGeneratedNamePattern(FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        var generatedNamePattern = subjectFactoryDescriptor.getGeneratedNamePattern();
        String pattern;
        if(generatedNamePattern.isBlank()) {
            pattern = FormSubjectFactoryDescriptor.getDefaultGeneratedNamePattern();
        }
        else {
            pattern = generatedNamePattern;
        }
        return pattern;
    }
}

package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Collection;

import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormFrameFlattener {

    @Nonnull
    public ImmutableList<FormFrame> flattenAndMerge(@Nonnull FormFrame formFrame) {
        var framesBySubject = flattenNestedFrames(formFrame);
        return mergeFormFramesWithCommonSubject(framesBySubject);

    }

    @Nonnull
    private LinkedListMultimap<FormSubject, FormFrame> flattenNestedFrames(@Nonnull FormFrame formFrame) {
        var processingQueue = new ArrayDeque<FormFrame>();
        processingQueue.push(formFrame);
        var framesBySubject = LinkedListMultimap.<FormSubject, FormFrame>create();
        while(!processingQueue.isEmpty()) {
            var currentFrame = processingQueue.poll();
            processingQueue.addAll(currentFrame.getNestedFrames());
            framesBySubject.put(currentFrame.getSubject(), currentFrame);
        }
        return framesBySubject;
    }

    @Nonnull
    private ImmutableList<FormFrame> mergeFormFramesWithCommonSubject(@Nonnull LinkedListMultimap<FormSubject, FormFrame> framesBySubject) {
        return framesBySubject.asMap()
                       .values()
                       .stream()
                       .map(this::mergeFormFrames)
                       .collect(toImmutableList());
    }


    @Nonnull
    private FormFrame mergeFormFrames(@Nonnull Collection<FormFrame> formFrames) {
        if(formFrames.isEmpty()) {
            throw new RuntimeException("Form frame set is empty");
        }
        var parents = ImmutableSet.<OWLClass>builder();
        var subClasses = ImmutableSet.<OWLClass>builder();
        var instances = ImmutableSet.<OWLNamedIndividual>builder();
        var propertyValues = ImmutableSet.<PlainPropertyValue>builder();
        FormSubject subject = formFrames.iterator().next().getSubject();
        for(FormFrame formFrame : formFrames) {
            parents.addAll(formFrame.getClasses());
            instances.addAll(formFrame.getInstances());
            subClasses.addAll(formFrame.getSubClasses());
            propertyValues.addAll(formFrame.getPropertyValues());
            subject = formFrame.getSubject();
        }
        return FormFrame.get(subject,
                             parents.build(),
                             subClasses.build(),
                             instances.build(),
                             propertyValues.build(),
                             ImmutableSet.of());
    }

}

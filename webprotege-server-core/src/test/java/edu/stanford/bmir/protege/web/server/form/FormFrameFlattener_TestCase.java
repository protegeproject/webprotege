package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
@RunWith(MockitoJUnitRunner.class)
public class FormFrameFlattener_TestCase {

    private FormFrameFlattener formFrameFlattener;

    @Mock
    private FormSubject subject, otherSubject;

    @Mock
    private OWLClass parentA, parentB, subClassA, subClassB;

    @Mock
    private OWLNamedIndividual instanceA, instanceB;

    @Mock
    private PlainPropertyValue propertyValueA, propertyValueB;

    private FormFrame parentFrame;

    @Before
    public void setUp() {
        formFrameFlattener = new FormFrameFlattener();

        var parentsInA = ImmutableSet.of(parentA);
        var subClassesInA = ImmutableSet.of(subClassA);
        var instancesInA = ImmutableSet.of(instanceA);
        var propertyValuesInA = ImmutableSet.of(propertyValueA);

        var parentsInB = ImmutableSet.of(parentB);
        var subClassesInB = ImmutableSet.of(subClassB);
        var instancesInB = ImmutableSet.of(instanceB);
        var propertyValuesInB = ImmutableSet.of(propertyValueB);

        var frameA = FormFrame.get(subject,
                                   parentsInA,
                                   subClassesInA,
                                   instancesInA,
                                   propertyValuesInA,
                                   ImmutableSet.of());
        var frameB = FormFrame.get(subject,
                                   parentsInB,
                                   subClassesInB,
                                   instancesInB,
                                   propertyValuesInB,
                                   ImmutableSet.of());
        parentFrame = FormFrame.get(otherSubject,
                                        ImmutableSet.of(),
                                        ImmutableSet.of(),
                                        ImmutableSet.of(),
                                        ImmutableSet.of(),
                                        ImmutableSet.of(frameA, frameB));


    }

    @Test
    public void shouldFlattenFrames() {
        var flattenedFrames = formFrameFlattener.flattenAndMerge(parentFrame);
        assertThat(flattenedFrames.size(), is(2));
    }

    @Test
    public void shouldNotContainNesting() {
        var flattenedFrames = formFrameFlattener.flattenAndMerge(parentFrame);
        var frameWithOtherSubject = flattenedFrames.stream().filter(f -> f.getSubject().equals(otherSubject)).findFirst().orElseThrow();
        assertThat(frameWithOtherSubject.getSubject(), is(otherSubject));
        assertThat(frameWithOtherSubject.getNestedFrames(), hasSize(0));
    }

    @Test
    public void shouldMergeFramesWithSameSubject() {
        var flattenedFrames = formFrameFlattener.flattenAndMerge(parentFrame);
        var frameWithSubject = flattenedFrames.stream().filter(f -> f.getSubject().equals(subject)).findFirst().orElseThrow();
        assertThat(frameWithSubject.getSubject(), is(subject));
        assertThat(frameWithSubject.getClasses(), containsInAnyOrder(parentA, parentB));
        assertThat(frameWithSubject.getSubClasses(), containsInAnyOrder(subClassA, subClassB));
        assertThat(frameWithSubject.getInstances(), containsInAnyOrder(instanceA, instanceB));
        assertThat(frameWithSubject.getPropertyValues(), containsInAnyOrder(propertyValueA, propertyValueB));
    }
}

package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObject;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClass;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/11/2013
 */
public class BrowserTextMapTestCase {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullHasSignatureArgument() {
        HasSignature hasSignature = null;
        new BrowserTextMap(hasSignature, mock(BrowserTextProvider.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullBrowserTextProvider() {
        BrowserTextProvider browserTextProvider = null;
        new BrowserTextMap(mock(HasSignature.class), browserTextProvider);
    }

    @Test
    public void getOWLEntityBrowserTextReturnsCorrectBrowserText() {
        OWLClass clsA = mockOWLClass();
        Set<OWLObject> signature = new HashSet<OWLObject>();
        signature.add(clsA);

        BrowserTextProvider browserTextProvider = mock(BrowserTextProvider.class);
        when(browserTextProvider.getOWLEntityBrowserText(clsA)).thenReturn(Optional.<String>of("A"));

        BrowserTextMap map = new BrowserTextMap(signature, browserTextProvider);
        Optional<String> value = map.getBrowserText(clsA);
        assertEquals(true, value.isPresent());
        assertEquals("A", value.get());

        OWLClass clsB = mockOWLClass();
        assertEquals(false, map.getBrowserText(clsB).isPresent());
    }

    @Test
    public void getOWLEntityDataReturnsCorrectEntityData() {
        OWLClass clsA = mockOWLClass();
        Set<OWLObject> signature = new HashSet<OWLObject>();
        signature.add(clsA);

        BrowserTextProvider browserTextProvider = mock(BrowserTextProvider.class);
        when(browserTextProvider.getOWLEntityBrowserText(clsA)).thenReturn(Optional.<String>of("A"));

        BrowserTextMap map = new BrowserTextMap(signature, browserTextProvider);

        Collection<OWLEntityData> entityData = map.getOWLEntityData();
        assertEquals(Collections.<OWLEntityData>singleton(new OWLClassData(clsA, "A")), entityData);
    }

    @Test
    public void hashCodeReturnsSameHashCodeForEqualObjects() {
        OWLClass clsA = mockOWLClass();
        Set<OWLObject> signature = new HashSet<OWLObject>();
        signature.add(clsA);

        BrowserTextProvider browserTextProvider = mock(BrowserTextProvider.class);
        when(browserTextProvider.getOWLEntityBrowserText(clsA)).thenReturn(Optional.<String>of("A"));

        BrowserTextMap mapA = new BrowserTextMap(signature, browserTextProvider);
        BrowserTextMap mapB = new BrowserTextMap(signature, browserTextProvider);

        assertEquals(mapA.hashCode(), mapB.hashCode());

    }

    @Test
    public void equalsReturnsTrueForEqualObjects() {
        OWLClass clsA = mockOWLClass();
        Set<OWLObject> signature = new HashSet<OWLObject>();
        signature.add(clsA);

        BrowserTextProvider browserTextProvider = mock(BrowserTextProvider.class);
        when(browserTextProvider.getOWLEntityBrowserText(clsA)).thenReturn(Optional.<String>of("A"));

        BrowserTextMap mapA = new BrowserTextMap(signature, browserTextProvider);
        BrowserTextMap mapB = new BrowserTextMap(signature, browserTextProvider);

        assertEquals(mapA, mapB);
    }

    @Test
    public void buildProcessesNestedSignature() {
        OWLClass clsA = mockOWLClass();
        BrowserTextProvider browserTextProvider = mock(BrowserTextProvider.class);
        when(browserTextProvider.getOWLEntityBrowserText(clsA)).thenReturn(Optional.<String>of("A"));
        BrowserTextMap map = BrowserTextMap.build(browserTextProvider, Collections.singleton(clsA));
        Optional<String> value = map.getBrowserText(clsA);
        assertEquals(true, value.isPresent());
        assertEquals("A", value.get());
    }

    @Test
    public void buildProcessesDirectSignature() {
        OWLClass clsA = mockOWLClass();
        BrowserTextProvider browserTextProvider = mock(BrowserTextProvider.class);
        when(browserTextProvider.getOWLEntityBrowserText(clsA)).thenReturn(Optional.<String>of("A"));
        BrowserTextMap map = BrowserTextMap.build(browserTextProvider, clsA);
        Optional<String> value = map.getBrowserText(clsA);
        assertEquals(true, value.isPresent());
        assertEquals("A", value.get());
    }

}

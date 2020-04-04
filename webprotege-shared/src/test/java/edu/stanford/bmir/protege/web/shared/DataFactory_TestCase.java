package edu.stanford.bmir.protege.web.shared;

import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Aug 2018
 */
public class DataFactory_TestCase {


    @Test
    public void test_getFreshEntityReturnsFreshEntity() {
        OWLClass cls = DataFactory.getFreshOWLEntity(EntityType.CLASS, "X", Optional.empty());
        assertTrue(DataFactory.isFreshEntity(cls));
        assertEquals("X", DataFactory.getFreshEntityShortName(cls));
    }

    @Test
    public void test_getFreshEntityReturnsFreshEntityWithNoLangTag() {
        OWLClass cls = DataFactory.getFreshOWLEntity(EntityType.CLASS, "X", Optional.empty());
        assertTrue(DataFactory.isFreshEntity(cls));
        assertEquals("X", DataFactory.getFreshEntityShortName(cls));
        assertEquals(Optional.empty(), DataFactory.getFreshEntityLangTag(cls));
    }

    @Test
    public void test_getFreshEntityReturnsFreshEntityWithLangTag() {
        OWLClass cls = DataFactory.getFreshOWLEntity(EntityType.CLASS, "X", Optional.of("en"));
        assertTrue(DataFactory.isFreshEntity(cls));
        assertEquals("X", DataFactory.getFreshEntityShortName(cls));
        assertEquals(Optional.of("en"), DataFactory.getFreshEntityLangTag(cls));
    }

    @Test
    public void test_getFreshEntityReturnsFreshEntityWithLangTagTrimmed() {
        OWLClass cls = DataFactory.getFreshOWLEntity(EntityType.CLASS, "X", Optional.of("en "));
        assertTrue(DataFactory.isFreshEntity(cls));
        assertEquals("X", DataFactory.getFreshEntityShortName(cls));
        assertEquals(Optional.of("en"), DataFactory.getFreshEntityLangTag(cls));
    }
}

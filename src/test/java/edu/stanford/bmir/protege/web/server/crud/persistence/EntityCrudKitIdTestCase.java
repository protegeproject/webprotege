package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class EntityCrudKitIdTestCase {

    @Test(expected = NullPointerException.class)
    public void throwsNullPointerExceptionWithNullLexicalForm() {
        EntityCrudKitId.get(null);
    }

    @Test
    public void equalsIsTrueWithSameLexicalForms() {
        EntityCrudKitId idA = EntityCrudKitId.get("A");
        EntityCrudKitId idB = EntityCrudKitId.get("A");
        assertEquals(idA, idB);
    }

    @Test
    public void equalsIsFalseWithDifferentLexicalForms() {
        EntityCrudKitId idA = EntityCrudKitId.get("A");
        EntityCrudKitId idB = EntityCrudKitId.get("B");
        assertFalse(idA.equals(idB));
    }

    @Test
    public void getLexicalFormIsEqualToTheConstructorLexicalForm() {
        EntityCrudKitId id = EntityCrudKitId.get("A");
        assertEquals(id.getLexicalForm(), "A");
    }
}

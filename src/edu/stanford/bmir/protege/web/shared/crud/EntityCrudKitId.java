package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.HasLexicalForm;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudKitId implements Serializable, HasLexicalForm {

    private String lexicalForm;

    private EntityCrudKitId() {
    }

    private EntityCrudKitId(String lexicalForm) {
        this.lexicalForm = lexicalForm;
    }

    public static EntityCrudKitId get(String lexicalForm) {
        return new EntityCrudKitId(lexicalForm);
    }

    @Override
    public String getLexicalForm() {
        return lexicalForm;
    }
}

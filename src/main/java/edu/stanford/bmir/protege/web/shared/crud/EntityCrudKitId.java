package edu.stanford.bmir.protege.web.shared.crud;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasLexicalForm;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 * <p>
 *     Provides an identifier for an {@link EntityCrudKit} and its associated paraphernalia.
 * </p>
 */
public final class EntityCrudKitId implements Serializable, HasLexicalForm, IsSerializable {

    private String lexicalForm;

    /**
     * For serialization purposes only.
     */
    private EntityCrudKitId() {
    }

    /**
     * Constructs an instance of {@link EntityCrudKitId} for the specified lexical form.
     * @param lexicalForm The lexical form of the id.  Not {@code null}.
     * @throws NullPointerException if {@code lexicalForm} is {@code null}.
     */
    private EntityCrudKitId(String lexicalForm) {
        this.lexicalForm = checkNotNull(lexicalForm);
    }

    /**
     * Gets an instances if {@link EntityCrudKitId} for the specified lexical form.
     * @param lexicalForm The id lexical form.  Not {@code null}.
     * @return The id for the specified lexical form. Not {@code null}.
     * @throws NullPointerException if {@code lexicalForm} is {@code null}.
     */
    public static EntityCrudKitId get(String lexicalForm) {
        return new EntityCrudKitId(lexicalForm);
    }

    /**
     * Gets the lexical form for this id.
     * @return The lexical form of this id.  Not {@code null}.
     */
    @Override
    public String getLexicalForm() {
        return lexicalForm;
    }

    @Override
    public int hashCode() {
        return "EntityCrudKitId".hashCode() + lexicalForm.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EntityCrudKitId)) {
            return false;
        }
        EntityCrudKitId other = (EntityCrudKitId) obj;
        return this.lexicalForm.equals(other.lexicalForm);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("EntityCrudKitId").addValue(lexicalForm).toString();
    }

}

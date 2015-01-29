package edu.stanford.bmir.protege.web.server.render;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
public class NullDeprecatedEntityChecker implements DeprecatedEntityChecker {

    private static final NullDeprecatedEntityChecker INSTANCE = new NullDeprecatedEntityChecker();

    private NullDeprecatedEntityChecker() {
    }

    /**
     * Gets the singleton instance of NullDeprecatedEntityChecker.
     * @return The singleton instance of this class.  Not {@code null}.
     */
    public static NullDeprecatedEntityChecker get() {
        return INSTANCE;
    }

    /**
     * Always returns {@code false}.
     * @param entity The entity to check for.
     * @return {@code false} under all circumstances.
     */
    @Override
    public boolean isDeprecated(OWLEntity entity) {
        return false;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("NullDeprecatedEntityChecker")
                .toString();
    }

    @Override
    public int hashCode() {
        return "NullDeprecatedEntityChecker".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NullDeprecatedEntityChecker;
    }
}

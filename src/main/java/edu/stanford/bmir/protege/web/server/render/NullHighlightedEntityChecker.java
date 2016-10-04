package edu.stanford.bmir.protege.web.server.render;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@Singleton
public class NullHighlightedEntityChecker implements HighlightedEntityChecker {

    private static final NullHighlightedEntityChecker INSTANCE = new NullHighlightedEntityChecker();

    /**
     * Gets the singleton instance of this class.
     * @return The singleton instance of NullHighlightedEntityChecker.  Not {@code null}.
     */
    public static NullHighlightedEntityChecker get() {
        return INSTANCE;
    }


    @Override
    public boolean isHighlighted(OWLEntity entity) {
        return false;
    }

    @Override
    public int hashCode() {
        return "NullHighlightedEntityChecker".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("NullHighlightedEntityChecker")
                .toString();
    }
}

package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
@AutoFactory
public class SubClassOfMatcher implements Matcher<OWLEntity> {

    @Nonnull
    private final ClassHierarchyProvider provider;

    @Nonnull
    private final OWLClass cls;

    private final Set<OWLClass> descendants;

    private boolean filledDescedants = false;

    public SubClassOfMatcher(@Provided @Nonnull ClassHierarchyProvider provider, @Nonnull OWLClass cls) {
        this.provider = checkNotNull(provider);
        this.cls = checkNotNull(cls);
        descendants = new HashSet<>();
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        if(value.isOWLClass() && !filledDescedants) {
            filledDescedants = true;
            descendants.addAll(provider.getDescendants(cls));
        }
        return value.isOWLClass() && descendants.contains(value.asOWLClass());
    }
}

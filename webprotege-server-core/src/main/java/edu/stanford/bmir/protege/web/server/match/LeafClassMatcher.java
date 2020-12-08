package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-12-07
 */
@AutoFactory
public class LeafClassMatcher implements Matcher<OWLEntity> {

    @Nonnegative
    private final ClassHierarchyProvider hierarchyProvider;

    public LeafClassMatcher(@Provided ClassHierarchyProvider hierarchyProvider) {
        this.hierarchyProvider = checkNotNull(hierarchyProvider);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        if(!value.isOWLClass()) {
            return false;
        }
        var cls = value.asOWLClass();
        if(cls.isOWLNothing()) {
            return false;
        }
        if(cls.isOWLThing()) {
            return false;
        }
        return hierarchyProvider.isLeaf(cls);
    }
}

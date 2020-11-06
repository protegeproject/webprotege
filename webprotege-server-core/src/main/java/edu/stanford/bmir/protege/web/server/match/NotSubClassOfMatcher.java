package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-06
 */
@AutoFactory
public class NotSubClassOfMatcher implements Matcher<OWLEntity> {

    @Nonnull
    private final ClassHierarchyProvider provider;

    @Nonnull
    private final OWLClass cls;

    private final HierarchyFilterType filterType;

    @Inject
    public NotSubClassOfMatcher(@Provided @Nonnull ClassHierarchyProvider provider,
                                @Nonnull OWLClass cls,
                                @Nonnull HierarchyFilterType filterType) {
        this.provider = checkNotNull(provider);
        this.filterType = checkNotNull(filterType);
        this.cls = checkNotNull(cls);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        if (!value.isOWLClass()) {
            return false;
        }
        if (cls.isOWLThing()) {
            // Every class is a subclass of owl:Thing
            return false;
        }
        if(cls.isOWLNothing()) {
            // No class is a subclass of owl:Nothing
            return false;
        }
        // Config for strict?
        if (value.equals(cls)) {
            return true;
        }
        if (filterType == HierarchyFilterType.DIRECT) {
            return !provider.isParent(value.asOWLClass(), cls);
        }
        else {
            return !provider.isAncestor(value.asOWLClass(), cls);
        }
    }
}

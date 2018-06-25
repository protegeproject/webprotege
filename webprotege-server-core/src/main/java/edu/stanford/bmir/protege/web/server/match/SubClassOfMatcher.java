package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
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

    private final Set<OWLClass> subClasses;

    private final HierarchyFilterType filterType;

    private boolean filledClasses = false;

    public SubClassOfMatcher(@Provided @Nonnull ClassHierarchyProvider provider,
                             @Nonnull OWLClass cls,
                             @Nonnull HierarchyFilterType filterType) {
        this.provider = checkNotNull(provider);
        this.filterType = checkNotNull(filterType);
        this.cls = checkNotNull(cls);
        subClasses = new HashSet<>();
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        if(value.isOWLClass() && !filledClasses) {
            filledClasses = true;
            if (filterType == HierarchyFilterType.ALL) {
                subClasses.addAll(provider.getDescendants(cls));
            }
            else {
                subClasses.addAll(provider.getChildren(cls));
            }
        }
        return value.isOWLClass() && subClasses.contains(value.asOWLClass());
    }
}

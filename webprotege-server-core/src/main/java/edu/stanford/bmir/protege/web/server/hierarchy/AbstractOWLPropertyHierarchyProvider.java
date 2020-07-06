package edu.stanford.bmir.protege.web.server.hierarchy;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureIndex;
import edu.stanford.bmir.protege.web.server.index.OntologySignatureByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toSet;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jan-2007<br><br>
 */
public abstract class AbstractOWLPropertyHierarchyProvider<P extends OWLProperty> extends AbstractHierarchyProvider<P> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractOWLPropertyHierarchyProvider.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Set<P> subPropertiesOfRoot = new HashSet<>();

    @Nonnull
    private final P root;

    @Nonnull
    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    @Nonnull
    private final EntityType<P> entityType;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    private boolean stale = true;

    public AbstractOWLPropertyHierarchyProvider(@Nonnull ProjectId projectId,
                                                @Nonnull P root,
                                                @Nonnull EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex,
                                                @Nonnull EntityType<P> entityType,
                                                @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                @Nonnull OntologySignatureByTypeIndex ontologySignatureByTypeIndex) {
        this.projectId = checkNotNull(projectId);
        this.entitiesInProjectSignatureIndex = checkNotNull(entitiesInProjectSignatureIndex);
        this.entityType = checkNotNull(entityType);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.root = checkNotNull(root);
        this.ontologySignatureByTypeIndex = checkNotNull(ontologySignatureByTypeIndex);
    }

    public void dispose() {
        super.dispose();
    }

    protected void rebuildIfNecessary() {
        if(!this.stale) {
            return;
        }
        rebuildRoots();
    }

    protected abstract String getHierarchyName();

    public void handleChanges(List<OntologyChange> changes) {
        Set<P> properties = new HashSet<>(getPropertiesReferencedInChange(changes));
        for (P prop : properties) {
            if (isSubPropertyOfRoot(prop)) {
                subPropertiesOfRoot.add(prop);
            }
            else {
                if (getAncestors(prop).contains(prop)) {
                    subPropertiesOfRoot.add(prop);
                    for (P anc : getAncestors(prop)) {
                        if (getAncestors(anc).contains(prop)) {
                            subPropertiesOfRoot.add(anc);
                        }
                    }
                }
                else {
                    subPropertiesOfRoot.remove(prop);
                }
            }
        }
    }

    private Set<P> getPropertiesReferencedInChange(List<OntologyChange> changes) {
        return changes.stream()
                      .map(OntologyChange::getSignature)
                      .flatMap(Collection::stream)
                      .filter(entity -> entity.isType(entityType))
                      .map(entity -> (P) entity)
                      .collect(toSet());
    }

    protected Set<P> getChildrenOfRoot() {
        rebuildIfNecessary();
        return ImmutableSet.copyOf(subPropertiesOfRoot);
    }

    private boolean isSubPropertyOfRoot(P prop) {

        if (prop.equals(getRoot())){
            return false;
        }

        // We deem a property to be a sub of the top property if this is asserted
        // or if no named superproperties are asserted
        final Collection<P> parents = getParents(prop);
        if (parents.isEmpty() || parents.contains(getRoot())){
                if (containsReference(prop)) {
                    return true;
                }
        }
        // Additional condition: If we have  P -> Q and Q -> P, then
        // there is no path to the root, so put P and Q as root properties
        // Collapse any cycles and force properties that are equivalent
        // through cycles to appear at the root.
        return getAncestors(prop).contains(prop);
    }


    private void rebuildRoots() {
        this.stale = false;
        logger.info("{} Rebuilding {} hierarchy", projectId, getHierarchyName());
        Stopwatch stopwatch = Stopwatch.createStarted();
        subPropertiesOfRoot.clear();
        projectOntologiesIndex.getOntologyIds().forEach(ontologyId -> {
            for (P prop : getReferencedProperties(ontologyId)) {
                if (isSubPropertyOfRoot(prop)) {
                    subPropertiesOfRoot.add(prop);
                }
            }
        });
        logger.info("{} Rebuilt {} hierarchy in {} ms", projectId, getHierarchyName(), stopwatch.elapsed(MILLISECONDS));
    }

    protected boolean containsReference(P object) {
        return object.getEntityType().equals(entityType) && entitiesInProjectSignatureIndex.containsEntityInSignature(object);
    }

    /**
     * Gets the relevant properties in the specified ontology that are contained
     * within the property hierarchy.  For example, for an object property hierarchy
     * this would constitute the set of referenced object properties in the specified
     * ontology.
     * @param ont The ontology
     */
    protected Set<? extends P> getReferencedProperties(OWLOntologyID ont) {
        return ontologySignatureByTypeIndex.getSignature(entityType, ont).collect(toSet());
    }


    @Nonnull
    protected final P getRoot() {
        return root;
    }


    /**
     * Gets the objects that represent the roots of the hierarchy.
     */
    public Collection<P> getRoots() {
        rebuildIfNecessary();
        return Collections.singleton(getRoot());
    }
}

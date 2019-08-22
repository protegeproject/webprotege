package edu.stanford.bmir.protege.web.server.hierarchy;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.AxiomType.EQUIVALENT_OBJECT_PROPERTIES;
import static org.semanticweb.owlapi.model.AxiomType.SUB_OBJECT_PROPERTY;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jan-2007<br><br>
 */
@ProjectSingleton
public class OWLObjectPropertyHierarchyProvider extends AbstractOWLPropertyHierarchyProvider<OWLObjectProperty> {

    @Nonnull
    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    @Nonnull
    private final SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex;

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @AutoFactory
    @Inject
    public OWLObjectPropertyHierarchyProvider(@Provided @Nonnull ProjectId projectId,
                                              @Provided @ObjectPropertyHierarchyRoot OWLObjectProperty root,
                                              @Provided @Nonnull EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex,
                                              @Provided @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                              @Provided @Nonnull OntologySignatureByTypeIndex ontologySignatureByTypeIndex,
                                              @Provided @Nonnull SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex,
                                              @Provided @Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        super(projectId, root, entitiesInProjectSignatureIndex, EntityType.OBJECT_PROPERTY, projectOntologiesIndex,
              ontologySignatureByTypeIndex);
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
        this.subObjectPropertyAxiomsBySubPropertyIndex = checkNotNull(subObjectPropertyAxiomsBySubPropertyIndex);
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        rebuildRoots();
    }

    @Override
    protected String getHierarchyName() {
        return "ObjectProperty Hierarchy";
    }

    @Override
    public boolean containsReference(OWLObjectProperty object) {
        return entitiesInProjectSignatureIndex.containsEntityInSignature(object);
    }

    @Override
    public Set<OWLObjectProperty> getChildren(OWLObjectProperty object) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> axiomsByTypeIndex.getAxiomsByType(SUB_OBJECT_PROPERTY,
                                                                                         ontId))
                                     .filter(ax -> ax.getSuperProperty()
                                                     .equals(object))
                                     .map(OWLSubObjectPropertyOfAxiom::getSubProperty)
                                     .filter(OWLObjectPropertyExpression::isNamed)
                                     .map(OWLObjectPropertyExpression::asOWLObjectProperty)
                                     .collect(Collectors.toSet());
    }

    @Override
    public Set<OWLObjectProperty> getParents(OWLObjectProperty property) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> subObjectPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(
                                             property,
                                             ontId))
                                     .map(OWLSubPropertyAxiom::getSuperProperty)
                                     .filter(OWLObjectPropertyExpression::isNamed)
                                     .map(OWLObjectPropertyExpression::asOWLObjectProperty)
                                     .collect(Collectors.toSet());
    }

    @Override
    public Set<OWLObjectProperty> getEquivalents(OWLObjectProperty object) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> axiomsByTypeIndex.getAxiomsByType(EQUIVALENT_OBJECT_PROPERTIES,
                                                                                         ontId))
                                     .map(OWLEquivalentObjectPropertiesAxiom::getProperties)
                                     .flatMap(Collection::stream)
                                     .filter(OWLObjectPropertyExpression::isNamed)
                                     .filter(propertyExpression -> !propertyExpression.equals(object))
                                     .map(OWLObjectPropertyExpression::asOWLObjectProperty)
                                     .collect(Collectors.toSet());
    }
}

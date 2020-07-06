package edu.stanford.bmir.protege.web.server.hierarchy;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.AxiomType.SUB_OBJECT_PROPERTY;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jan-2007<br><br>
 */
@ProjectSingleton
public class ObjectPropertyHierarchyProviderImpl extends AbstractOWLPropertyHierarchyProvider<OWLObjectProperty> implements ObjectPropertyHierarchyProvider {

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
    public ObjectPropertyHierarchyProviderImpl(@Provided @Nonnull ProjectId projectId,
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
    public Collection<OWLObjectProperty> getChildren(OWLObjectProperty object) {
        rebuildIfNecessary();
        if(getRoot().equals(object)) {
            return getChildrenOfRoot();
        }
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
    public Collection<OWLObjectProperty> getParents(OWLObjectProperty property) {
        rebuildIfNecessary();
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> subObjectPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(
                                             property,
                                             ontId))
                                     .map(OWLSubPropertyAxiom::getSuperProperty)
                                     .filter(OWLObjectPropertyExpression::isNamed)
                                     .map(OWLObjectPropertyExpression::asOWLObjectProperty)
                                     .collect(Collectors.toSet());
    }

}

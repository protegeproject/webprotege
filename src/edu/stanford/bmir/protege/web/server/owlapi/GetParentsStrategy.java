package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.protege.editor.owl.model.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLDataPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLObjectPropertyHierarchyProvider;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 */
public class GetParentsStrategy extends OntologyServiceStrategy<List<EntityData>> {

    private String className;

    private boolean direct;

    public GetParentsStrategy(OWLAPIProject project, UserId userId, String className, boolean direct) {
        super(project, userId);
        this.className = className;
        this.direct = direct;
    }

    public List<EntityData> execute() {
        final List<EntityData> result = new ArrayList<EntityData>();

        final OWLAPIProject project = getProject();
        final RenderingManager rm = project.getRenderingManager();
        Set<OWLEntity> entites = rm.getEntities(className);
        if(entites.isEmpty()) {
            return Collections.emptyList();
        }
        for(OWLEntity entity : entites) {
            entity.accept(new OWLEntityVisitor() {
                public void visit(OWLClass owlClass) {
                    AssertedClassHierarchyProvider provider = project.getClassHierarchyProvider();
                    if(direct) {
                        result.addAll(rm.getEntityData(provider.getParents(owlClass)));
                    }
                    else {
                        result.addAll(rm.getEntityData(provider.getAncestors(owlClass)));
                    }
                }

                public void visit(OWLObjectProperty owlObjectProperty) {
                    OWLObjectPropertyHierarchyProvider provider = project.getObjectPropertyHierarchyProvider();
                    if(direct) {
                        result.addAll(rm.getEntityData(provider.getParents(owlObjectProperty)));
                    }
                    else {
                        result.addAll(rm.getEntityData(provider.getAncestors(owlObjectProperty)));
                    }
                }

                public void visit(OWLDataProperty owlDataProperty) {
                    OWLDataPropertyHierarchyProvider provider = project.getDataPropertyHierarchyProvider();
                    if(direct) {
                        result.addAll(rm.getEntityData(provider.getParents(owlDataProperty)));
                    }
                    else {
                        result.addAll(rm.getEntityData(provider.getAncestors(owlDataProperty)));
                    }
                }

                public void visit(OWLNamedIndividual owlNamedIndividual) {
                    // No parents
                }

                public void visit(OWLDatatype owlDatatype) {
                    // No parents
                }

                public void visit(OWLAnnotationProperty owlAnnotationProperty) {
                    OWLAnnotationPropertyHierarchyProvider provider = project.getAnnotationPropertyHierarchyProvider();
                    if(direct) {
                        result.addAll(rm.getEntityData(provider.getParents(owlAnnotationProperty)));
                    }
                    else {
                        result.addAll(rm.getEntityData(provider.getAncestors(owlAnnotationProperty)));
                    }
                }
            });
        }
        return result;
    }
}

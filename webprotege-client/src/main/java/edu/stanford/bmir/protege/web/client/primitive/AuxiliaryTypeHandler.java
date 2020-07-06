package edu.stanford.bmir.protege.web.client.primitive;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityDataVisitorEx;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;

import java.util.Collections;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/03/2014
 */
public abstract class AuxiliaryTypeHandler {

    public abstract boolean isApplicableTo(EntityType<?> entityType);


    public abstract Set<OWLAxiom> getAdditionalAxioms(OWLEntity entity);

    public abstract String getSuggestionText(OWLEntityData entityData);

    public static AuxiliaryTypeHandler get(OWLEntityData auxiliaryType) {
        return new NullAuxiliaryTypeHandler();
//        return auxiliaryType.accept(new OWLEntityDataVisitorEx<AuxiliaryTypeHandler>() {
//            @Override
//            public AuxiliaryTypeHandler getDefaultReturnValue(OWLEntityData data) {
//                return new NullAuxiliaryTypeHandler();
//            }
//
//            @Override
//            public AuxiliaryTypeHandler accept(OWLClassData data) {
//                return new AuxiliaryClassHandler(data);
//            }
//        });
    }


    private static class NullAuxiliaryTypeHandler extends AuxiliaryTypeHandler {

        @Override
        public boolean isApplicableTo(EntityType<?> entityType) {
            return false;
        }

        @Override
        public Set<OWLAxiom> getAdditionalAxioms(OWLEntity entity) {
            return Collections.emptySet();
        }

        @Override
        public String getSuggestionText(OWLEntityData entityData) {
            return "Create " + entityData.getBrowserText() + " as new " + entityData.getEntity().getEntityType().getPrintName();
        }
    }

//
//    private static class AuxiliaryClassHandler extends AuxiliaryTypeHandler {
//
//        private OWLClassData type;
//
//        private AuxiliaryClassHandler(OWLClassData type) {
//            this.type = type;
//        }
//
//        @Override
//        public boolean isApplicableTo(EntityType<?> entityType) {
//            return entityType == EntityType.CLASS || entityType == EntityType.NAMED_INDIVIDUAL;
//        }
//
//        @Override
//        public Set<OWLAxiom> getAdditionalAxioms(OWLEntity entity) {
//            return entity.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
//                @Override
//                protected Set<OWLAxiom> getDefaultReturnValue(OWLEntity object) {
//                    return Collections.emptySet();
//                }
//
//                @Override
//                public Set<OWLAxiom> accept(OWLClass desc) {
//                    return Collections.singleton(DataFactory.get().getOWLSubClassOfAxiom(desc, type.getEntity()));
//                }
//
//                @Override
//                public Set<OWLAxiom> accept(OWLNamedIndividual individual) {
//                    return Collections.singleton(DataFactory.get().getOWLClassAssertionAxiom(type.getEntity(), individual));
//                }
//            });
//        }
//
//        @Override
//        public String getSuggestionText(OWLEntityData entityData) {
//            return entityData.accept(new OWLEntityDataVisitorEx<String>() {
//                @Override
//                public String getDefaultReturnValue(OWLEntityData data) {
//                    return super.getDefaultReturnValue(data);
//                }
//
//                @Override
//                public String accept(OWLClassData data) {
//                    return format(data, " SubclassOf ", type);
//                }
//
//                @Override
//                public String accept(OWLNamedIndividualData data) {
//                    return format(data, " InstanceOf ", type);
//                }
//            });
//        }
//    }
//
//    private static String format(OWLEntityData entity, String axiomKwText, OWLEntityData auxiliaryType) {
//        return "<span class=\"new-keyword\">New </span><span style=\"font-weight: bold;\">"
//                + entity.getEntity().getEntityType().getPrintName()
//                + "</span> named "
//                + entity.getBrowserText()
//                + " as <span style=\"font-weight: bold;\">"
//                + axiomKwText
//                + "</span> "
//                + auxiliaryType.getBrowserText();
//    }
}

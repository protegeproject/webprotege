package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/12/15
 */
public class DeclarationParser {

    private OWLDataFactory dataFactory;

    public DeclarationParser(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public Optional<OWLEntity> parseEntity(String declaration) {
        if(!declaration.endsWith(")")) {
            return Optional.absent();
        }
        if(declaration.startsWith(EntityType.CLASS.getName())) {
            return getEntity(declaration, EntityType.CLASS);
        }
        else if(declaration.startsWith(EntityType.OBJECT_PROPERTY.getName())) {
            return getEntity(declaration, EntityType.OBJECT_PROPERTY);
        }
        else if(declaration.startsWith(EntityType.DATA_PROPERTY.getName())) {
            return getEntity(declaration, EntityType.DATA_PROPERTY);
        }
        else if(declaration.startsWith(EntityType.ANNOTATION_PROPERTY.getName())) {
            return getEntity(declaration, EntityType.ANNOTATION_PROPERTY);
        }
        else if(declaration.startsWith(EntityType.NAMED_INDIVIDUAL.getName())) {
            return getEntity(declaration, EntityType.NAMED_INDIVIDUAL);
        }
        else if(declaration.startsWith(EntityType.DATATYPE.getName())) {
            return getEntity(declaration, EntityType.DATATYPE);
        }
        else {
            return Optional.absent();
        }
    }

    private Optional<OWLEntity> getEntity(String declaration, EntityType<?> entityType) {
        if(!declaration.startsWith(entityType.getName() + "(")) {
            return Optional.absent();
        }
        if(!declaration.endsWith(")")) {
            return Optional.absent();
        }
        String iriString = declaration.substring(entityType.getName().length() + 1, declaration.length() - 1);
        if(iriString.startsWith("<")) {
            iriString = iriString.substring(1);
        }
        if(iriString.endsWith(">")) {
            iriString = iriString.substring(0, iriString.length() - 1);
        }
        return Optional.<OWLEntity>of(
                dataFactory.getOWLEntity(entityType, IRI.create(iriString))
        );
    }


}

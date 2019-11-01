package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import org.semanticweb.owlapi.model.*;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
public class EntityFormElementId {

    public static Optional<OWLProperty> toProperty(FormElementId elementId,
                                                   OWLEntityProvider dataFactory) {
        var id = elementId.getId();
        if(id.startsWith(EntityType.OBJECT_PROPERTY.getName())) {
            return parseIri(elementId).map(dataFactory::getOWLObjectProperty);
        }
        else if(id.startsWith(EntityType.DATA_PROPERTY.getName())) {
            return parseIri(elementId).map(dataFactory::getOWLDataProperty);
        }
        else if(id.startsWith(EntityType.ANNOTATION_PROPERTY.getName())) {
            return parseIri(elementId).map(dataFactory::getOWLAnnotationProperty);
        }
        else {
            return Optional.empty();
        }
    }

    public static FormElementId toElementId(OWLEntity property) {
        var stringForm = String.format("%s(<%s>)", property.getEntityType().getName(), property.getIRI().toString());
        return FormElementId.get(stringForm);
    }

    private static Optional<IRI> parseIri(FormElementId elementId) {
        var id = elementId.getId();
        var startIndex = id.indexOf("<");
        if(startIndex == -1) {
            return Optional.empty();
        }
        var endIndex = id.lastIndexOf(">");
        if(endIndex != id.length() - 2) {
            return Optional.empty();
        }
        return Optional.of(IRI.create(id.substring(startIndex + 1, endIndex)));
    }
}

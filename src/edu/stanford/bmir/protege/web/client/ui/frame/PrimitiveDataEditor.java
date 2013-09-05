package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasTextRendering;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBoxMode;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;

import java.util.SortedSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/12/2012
 * <p>
 *     An interface to an editor that can edit primitive values (classes, properties, individuals, datatypes, literals
 *     and IRIs).  The editor can be controlled to restrict the type of primitives that can be edited.  This interface
 *     contains various boolean setters and getters to do this so that it may be used with UIBinder.
 * </p>
 */
public interface PrimitiveDataEditor extends IsWidget, HasEnabled, ValueEditor<OWLPrimitiveData>, HasFocusHandlers, HasKeyUpHandlers, HasTextRendering, HasPlaceholder {

    /**
     * Gets the language editor for this primitive data editor
     * @return The language editor.  Not {@code null}.
     */
    LanguageEditor getLanguageEditor();

    /**
     * Specifies whether or not the editor should be a single or multiple line editor.
     * @param mode The mode of the editor.  Not {@code null}.
     */
    void setMode(ExpandingTextBoxMode mode);

    /**
     * Specifies whether or not a link should be shown when the content of the editor is an entity.
     * @param entityLinkMode The mode for showing entity links.  Not {@code null}.
     */
    void setEntityLinkMode(EntityLinkMode entityLinkMode);

    /**
     * A setter than can be used with UIBinder for setting whether or not a link should be shown when the content
     * of the editor is an entity.
     * @param showLinksForEntities {@code true} if links should be shown (equivalent to setting the link mode
     *                                         to {@link EntityLinkMode#SHOW_LINKS_FOR_ENTITIES}),
     *                                         otherwise {@code false} (equivalent to setting the link mode to
     *                                         {@link EntityLinkMode#DO_NOT_SHOW_LINKS_FOR_ENTITIES}).
     */
    void setShowLinksForEntities(boolean showLinksForEntities);

    /**
     * Sets the primitive types that this editor can edit.
     * @param primitiveTypes The set of primitive types that this editor can edit.  May be empty (although this doesn't
     *                       make much sense!).  Not {@code null}.
     */
    void setAllowedTypes(SortedSet<PrimitiveType> primitiveTypes);

    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.OWLClass}
     * entities to be edited.
     * @param classesAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.OWLClass}
     * entities to be edited, otherwise {@code false}.
     */
    void setClassesAllowed(boolean classesAllowed);


    /**
     * Determines whether or not this editor can edit {@link org.semanticweb.owlapi.model.OWLAnnotationProperty} entities.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.OWLAnnotationProperty} entities,
     * otherwise {@code false}.
     */
    boolean isAnnotationPropertiesAllowed();

    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.OWLAnnotationProperty}
     * entities to be edited.
     * @param annotationPropertiesAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.OWLAnnotationProperty}
     * entities to be edited, otherwise {@code false}.
     */
    void setAnnotationPropertiesAllowed(boolean annotationPropertiesAllowed);

    /**
     * Determines whether or not this editor can edit {@link org.semanticweb.owlapi.model.OWLDataProperty} entities.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.OWLDataProperty} entities,
     * otherwise {@code false}.
     */
    boolean isDataPropertiesAllowed();


    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.OWLDataProperty}
     * entities to be edited.
     * @param dataPropertiesAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.OWLDataProperty}
     * entities to be edited, otherwise {@code false}.
     */
    void setDataPropertiesAllowed(boolean dataPropertiesAllowed);


    /**
     * Determines whether or not this editor can edit {@link org.semanticweb.owlapi.model.OWLObjectProperty} entities.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.OWLObjectProperty} entities,
     * otherwise {@code false}.
     */
    boolean isObjectPropertiesAllowed();


    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.OWLObjectProperty}
     * entities to be edited.
     * @param objectPropertiesAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.OWLObjectProperty}
     * entities to be edited, otherwise {@code false}.
     */
    void setObjectPropertiesAllowed(boolean objectPropertiesAllowed);


    /**
     * Determines whether or not this editor can edit {@link org.semanticweb.owlapi.model.OWLClass} entities.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.OWLClass} entities,
     * otherwise {@code false}.
     */
    boolean isClassesAllowed();


    /**
     * Determines whether or not this editor can edit {@link org.semanticweb.owlapi.model.OWLDatatype} entities.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.OWLDatatype} entities,
     * otherwise {@code false}.
     */
    boolean isDatatypesAllowed();


    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.OWLDatatype}
     * entities to be edited.
     * @param datatypesAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.OWLDatatype}
     * entities to be edited, otherwise {@code false}.
     */
    void setDatatypesAllowed(boolean datatypesAllowed);


    /**
     * Determines whether or not this editor can edit {@link org.semanticweb.owlapi.model.OWLNamedIndividual} entities.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.OWLNamedIndividual} entities,
     * otherwise {@code false}.
     */
    boolean isNamedIndividualsAllowed();


    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.OWLNamedIndividual}
     * entities to be edited.
     * @param namedIndividualsAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.OWLNamedIndividual}
     * entities to be edited, otherwise {@code false}.
     */
    void setNamedIndividualsAllowed(boolean namedIndividualsAllowed);

    /**
     * Determines whether or not this editor can edit {@link org.semanticweb.owlapi.model.OWLLiteral}s.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.OWLLiteral}s,
     * otherwise {@code false}.
     */
    boolean isLiteralAllowed();


    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.OWLLiteral}s to be edited.
     * @param literalsAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.OWLLiteral}s
     *                                   to be edited, otherwise {@code false}.
     */
    void setLiteralAllowed(boolean literalsAllowed);


    /**
     * Determines whether or not this editor can edit raw {@link org.semanticweb.owlapi.model.IRI}s.
     * @return {@code true} if this editor can edit {@link org.semanticweb.owlapi.model.IRI}s,
     * otherwise {@code false}.
     */
    boolean isIRIAllowed();

    /**
     * Specifies whether or not the editor should allow {@link org.semanticweb.owlapi.model.IRI}s to be edited.
     * @param irisAllowed {@code true} if the editor should allow {@link org.semanticweb.owlapi.model.IRI}s
     *                                   to be edited, otherwise {@code false}.
     */
    void setIRIAllowed(boolean irisAllowed);

    /**
     * Specifies that the editor can edit the specified {@link PrimitiveType}.
     * @param type The type.  Not {@code null}.
     * @param allowed {@code true} if editing the specified type is allowed, otherwise {@code false}.
     */
    void setAllowedType(PrimitiveType type, boolean allowed);

    /**
     * Coerces the value in the editor to the specified entity type.
     * @param type The type to coerce to.  Not {@code null}.
     */
    void coerceToEntityType(EntityType<?> type);

    /**
     * Specifies whether or not the editor should offer the possibility of creating new types of entities for unrecognised
     * names, or whether the editor should only allow entities for recognised names.
     * @param mode The mode.  Not {@code null}.
     * @see PrimitiveDataEditorSuggestOracleMode
     */
    void setSuggestMode(PrimitiveDataEditorSuggestOracleMode mode);


    /**
     * Sets the FreshEntitiesHandler for this editor.
     * @param handler The handler.  Not {@code null}.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    void setFreshEntitiesHandler(FreshEntitiesHandler handler);


}

package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
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
 */
public interface PrimitiveDataEditor extends IsWidget, HasEnabled, ValueEditor<OWLPrimitiveData>, HasFocusHandlers, HasKeyUpHandlers, HasTextRendering, HasPlaceholder {

    LanguageEditor getLanguageEditor();
    
    void setAllowedTypes(SortedSet<PrimitiveType> primitiveTypes);

    void setShowLinkForEntities(boolean showLinkForEntities);

    /**
     * Sets the FreshEntitiesHandler for this editor.
     * @param handler The handler.  Not {@code null}.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    void setFreshEntitiesHandler(FreshEntitiesHandler handler);







    void setMode(ExpandingTextBoxMode mode);

    boolean isAnnotationPropertiesAllowed();

    void setAnnotationPropertiesAllowed(boolean annotationPropertiesAllowed);

    boolean isDataPropertiesAllowed();

    void setDataPropertiesAllowed(boolean dataPropertiesAllowed);

    boolean isObjectPropertiesAllowed();

    void setObjectPropertiesAllowed(boolean objectPropertiesAllowed);

    boolean isClassesAllowed();

    void setClassesAllowed(boolean classesAllowed);

    boolean isDatatypesAllowed();

    void setDatatypesAllowed(boolean datatypesAllowed);

    boolean isNamedIndividualsAllowed();

    void setNamedIndividualsAllowed(boolean namedIndividualsAllowed);

    boolean isLiteralAllowed();

    void setLiteralAllowed(boolean literalAllowed);

    boolean isIRIAllowed();

    void setIRIAllowed(boolean iriAllowed);

    void setAllowedType(PrimitiveType type, boolean allowed);

    void coerceToEntityType(EntityType<?> type);

    void setSuggestMode(PrimitiveDataEditorSuggestOracleMode mode);



}

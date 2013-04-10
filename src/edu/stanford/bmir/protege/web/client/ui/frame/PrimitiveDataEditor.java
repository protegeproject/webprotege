package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasTextRendering;
import edu.stanford.bmir.protege.web.client.ui.library.common.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBoxMode;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Set;
import java.util.SortedSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/12/2012
 */
public abstract class PrimitiveDataEditor extends FlowPanel implements HasWidgets, HasEnabled, ValueEditor<OWLPrimitiveData>, HasFocusHandlers, HasKeyUpHandlers, HasTextRendering, HasPlaceholder {

    public abstract void setAllowedTypes(SortedSet<PrimitiveType> primitiveTypes);

    public abstract void setShowLinkForEntities(boolean showLinkForEntities);

    /**
     * Sets the FreshEntitiesHandler for this editor.
     * @param handler The handler.  Not {@code null}.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    public abstract void setFreshEntitiesHandler(FreshEntitiesHandler handler);





    public abstract LanguageEditor getLanguageEditor();

    public abstract void setMode(ExpandingTextBoxMode mode);

    public abstract boolean isAnnotationPropertiesAllowed();

    public abstract void setAnnotationPropertiesAllowed(boolean annotationPropertiesAllowed);

    public abstract boolean isDataPropertiesAllowed();

    public abstract void setDataPropertiesAllowed(boolean dataPropertiesAllowed);

    public abstract boolean isObjectPropertiesAllowed();

    public abstract void setObjectPropertiesAllowed(boolean objectPropertiesAllowed);

    public abstract boolean isClassesAllowed();

    public abstract void setClassesAllowed(boolean classesAllowed);

    public abstract boolean isDatatypesAllowed();

    public abstract void setDatatypesAllowed(boolean datatypesAllowed);

    public abstract boolean isNamedIndividualsAllowed();

    public abstract void setNamedIndividualsAllowed(boolean namedIndividualsAllowed);

    public abstract boolean isLiteralAllowed();

    public abstract void setLiteralAllowed(boolean literalAllowed);

    public abstract boolean isIRIAllowed();

    public abstract void setIRIAllowed(boolean iriAllowed);

    public abstract void setAllowedType(PrimitiveType type, boolean allowed);

    public abstract void coerceToEntityType(EntityType<?> type);

    public abstract void setSuggestMode(PrimitiveDataEditorSuggestOracleMode mode);



}

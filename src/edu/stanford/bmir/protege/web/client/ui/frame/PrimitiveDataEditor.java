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

    public  void setAllowedTypes(SortedSet<PrimitiveType> primitiveTypes);

    public  void setShowLinkForEntities(boolean showLinkForEntities);

    /**
     * Sets the FreshEntitiesHandler for this editor.
     * @param handler The handler.  Not {@code null}.
     * @throws NullPointerException if {@code handler} is {@code null}.
     */
    public  void setFreshEntitiesHandler(FreshEntitiesHandler handler);





    public  LanguageEditor getLanguageEditor();

    public  void setMode(ExpandingTextBoxMode mode);

    public  boolean isAnnotationPropertiesAllowed();

    public  void setAnnotationPropertiesAllowed(boolean annotationPropertiesAllowed);

    public  boolean isDataPropertiesAllowed();

    public  void setDataPropertiesAllowed(boolean dataPropertiesAllowed);

    public  boolean isObjectPropertiesAllowed();

    public  void setObjectPropertiesAllowed(boolean objectPropertiesAllowed);

    public  boolean isClassesAllowed();

    public  void setClassesAllowed(boolean classesAllowed);

    public  boolean isDatatypesAllowed();

    public  void setDatatypesAllowed(boolean datatypesAllowed);

    public  boolean isNamedIndividualsAllowed();

    public  void setNamedIndividualsAllowed(boolean namedIndividualsAllowed);

    public  boolean isLiteralAllowed();

    public  void setLiteralAllowed(boolean literalAllowed);

    public  boolean isIRIAllowed();

    public  void setIRIAllowed(boolean iriAllowed);

    public  void setAllowedType(PrimitiveType type, boolean allowed);

    public  void coerceToEntityType(EntityType<?> type);

    public  void setSuggestMode(PrimitiveDataEditorSuggestOracleMode mode);



}

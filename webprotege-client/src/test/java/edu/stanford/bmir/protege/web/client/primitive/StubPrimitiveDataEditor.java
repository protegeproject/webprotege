package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-12
 */
public class StubPrimitiveDataEditor implements PrimitiveDataEditor {

    private StubLanguageEditor languageEditor = new StubLanguageEditor();

    @Nullable
    private OWLPrimitiveData value = null;

    @Override
    public LanguageEditor getLanguageEditor() {
        return languageEditor;
    }

    @Override
    public void setMode(PrimitiveDataEditorView.Mode mode) {

    }

    @Override
    public void setEntityLinkMode(EntityLinkMode entityLinkMode) {

    }

    @Override
    public void setShowLinksForEntities(boolean showLinksForEntities) {

    }

    @Override
    public void setAllowedTypes(Collection<PrimitiveType> primitiveTypes) {

    }

    @Override
    public void setClassesAllowed(boolean classesAllowed) {

    }

    @Override
    public boolean isAnnotationPropertiesAllowed() {
        return false;
    }

    @Override
    public void setAnnotationPropertiesAllowed(boolean annotationPropertiesAllowed) {

    }

    @Override
    public boolean isDataPropertiesAllowed() {
        return false;
    }

    @Override
    public void setDataPropertiesAllowed(boolean dataPropertiesAllowed) {

    }

    @Override
    public boolean isObjectPropertiesAllowed() {
        return false;
    }

    @Override
    public void setObjectPropertiesAllowed(boolean objectPropertiesAllowed) {

    }

    @Override
    public boolean isClassesAllowed() {
        return false;
    }

    @Override
    public boolean isDatatypesAllowed() {
        return false;
    }

    @Override
    public void setDatatypesAllowed(boolean datatypesAllowed) {

    }

    @Override
    public boolean isNamedIndividualsAllowed() {
        return false;
    }

    @Override
    public void setNamedIndividualsAllowed(boolean namedIndividualsAllowed) {

    }

    @Override
    public boolean isLiteralAllowed() {
        return false;
    }

    @Override
    public void setLiteralAllowed(boolean literalsAllowed) {

    }

    @Override
    public boolean isIRIAllowed() {
        return false;
    }

    @Override
    public void setIRIAllowed(boolean irisAllowed) {

    }

    @Override
    public void setAllowedType(PrimitiveType type, boolean allowed) {

    }

    @Override
    public void coerceToEntityType(EntityType<?> type) {

    }

    @Override
    public void setFreshEntitiesSuggestStrategy(FreshEntitySuggestStrategy suggestStrategy) {

    }

    @Override
    public Optional<EntitySuggestion> getSelectedSuggestion() {
        return Optional.empty();
    }

    @Override
    public void setFreshEntitiesHandler(FreshEntitiesHandler handler) {

    }

    @Override
    public void setAutoSelectSuggestions(boolean autoSelectSuggestions) {

    }

    @Override
    public void setWrap(boolean wrap) {

    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getValueAsAnnotationPropertyData() {
        return Optional.empty();
    }

    @Override
    public void addStyleName(@Nonnull String styleName) {

    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return null;
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return null;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OWLPrimitiveData>> handler) {
        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setValue(OWLPrimitiveData object) {
        this.value = object;
    }

    @Override
    public void clearValue() {
        value = null;
    }

    @Override
    public Optional<OWLPrimitiveData> getValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public Widget asWidget() {
        throw new RuntimeException();
    }

    @Override
    public String getPlaceholder() {
        return "";
    }

    @Override
    public void setPlaceholder(String placeholder) {

    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public void requestFocus() {

    }

    @Override
    public void setPrimitiveDataPlaceholder(Optional<OWLPrimitiveData> placeholder) {

    }

    @Override
    public Optional<OWLPrimitiveData> getPrimitiveDataPlaceholder() {
        return Optional.empty();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return () -> {};
    }

    @Override
    public boolean isWellFormed() {
        return value != null;
    }
}

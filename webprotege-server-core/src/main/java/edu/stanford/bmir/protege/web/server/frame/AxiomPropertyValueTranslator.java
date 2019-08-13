package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 * <p>
 * Translates a set of axioms into a set of property values.
 * </p>
 * <p>
 * The translation is as follows:
 * <table>
 * <tr>
 * <td>SubClassOf(A ObjectSomeValuesFrom(R B))</td><td>{@link PropertyClassValue}(R, B)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A ObjectHasValue(R a))</td><td>{@link PropertyIndividualValue}(R, a)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A DataSomeValuesFrom(R T))</td><td>{@link PropertyDatatypeValue}(R, T)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A DataHasValue(R l))</td><td>{@link PropertyLiteralValue}(R, l)</td>
 * </tr>
 * <tr>
 * <td>AnnotationAssertion(P :A l)</td><td>{@link PropertyAnnotationValue}(P, l)</td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * The translation does not do any splitting of axioms.  e.g.
 * SubClassOf(A  ObjectIntersection(B C)) is not split into SubClassOf(A B)  SubClassOf(A C) before the translation.
 * </p>
 */
public class AxiomPropertyValueTranslator extends OWLAxiomVisitorAdapter {

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesIndex;

    @Nonnull
    private final ContextRenderer ren;


    @Inject
    public AxiomPropertyValueTranslator(@Nonnull ContextRenderer ren,
                                        @Nonnull EntitiesInProjectSignatureByIriIndex entitiesIndex) {
        this.ren = checkNotNull(ren);
        this.entitiesIndex = checkNotNull(entitiesIndex);
    }

    public Set<PropertyValue> getPropertyValues(OWLEntity subject,
                                                OWLAxiom axiom,
                                                State initialState) {
        final AxiomTranslator visitor = new AxiomTranslator(subject, initialState, entitiesIndex, ren);
        Set<PropertyValue> result = axiom.accept(visitor);
        if (result == null) {
            return Collections.emptySet();
        }
        else {
            return result;
        }
    }

    public Set<OWLAxiom> getAxioms(OWLEntity subject,
                                   PropertyValue propertyValue,
                                   Mode mode) {
        if (propertyValue.getState() == State.DERIVED) {
            return Collections.emptySet();
        }
        PropertyValueTranslator translator = new PropertyValueTranslator(subject, mode);
        return propertyValue.accept(translator);
    }
}

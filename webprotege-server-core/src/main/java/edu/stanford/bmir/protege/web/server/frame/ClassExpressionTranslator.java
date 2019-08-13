package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorExAdapter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
class ClassExpressionTranslator extends OWLClassExpressionVisitorExAdapter<Set<PropertyValue>> {

    @Nonnull
    private final ContextRenderer ren;

    @Nonnull
    private State state;


    ClassExpressionTranslator(@Nonnull State initialState,
                              @Nonnull ContextRenderer ren) {
        super(null);
        this.state = checkNotNull(initialState);
        this.ren = checkNotNull(ren);
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLObjectIntersectionOf ce) {
        state = State.DERIVED;
        Set<PropertyValue> result = new HashSet<>();
        for(OWLClassExpression op : ce.asConjunctSet()) {
            Set<PropertyValue> accept = op.accept(this);
            if(accept != null) {
                result.addAll(accept);
            }
        }
        return result;
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLObjectSomeValuesFrom desc) {
        if(!desc.getProperty().isAnonymous()) {
            if(!desc.getFiller().isAnonymous()) {
                return Collections.singleton(PropertyClassValue.get(ren.getObjectPropertyData(desc
                                                                                                      .getProperty()
                                                                                                      .asOWLObjectProperty()), ren
                                                                            .getClassData(desc
                                                                                                  .getFiller()
                                                                                                  .asOWLClass()), state));
            }
            else {
                Set<PropertyValue> result = Sets.newHashSet();
                for(OWLClassExpression ce : desc.getFiller().asConjunctSet()) {
                    if(!ce.isAnonymous()) {
                        result.add(PropertyClassValue.get(ren.getObjectPropertyData(desc
                                                                                            .getProperty()
                                                                                            .asOWLObjectProperty()), ren
                                                                  .getClassData(ce.asOWLClass()), State.DERIVED));
                    }
                }
                return result;
            }
        }
        else {
            return null;
        }
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLObjectMinCardinality ce) {
        if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
            return Collections.singleton(PropertyClassValue.get(ren.getObjectPropertyData(ce
                                                                                                  .getProperty()
                                                                                                  .asOWLObjectProperty()), ren
                                                                        .getClassData(ce
                                                                                              .getFiller()
                                                                                              .asOWLClass()), state));
        }
        else {
            return Collections.singleton(PropertyClassValue.get(ren.getObjectPropertyData(ce
                                                                                                  .getProperty()
                                                                                                  .asOWLObjectProperty()), ren
                                                                        .getClassData(ce
                                                                                              .getFiller()
                                                                                              .asOWLClass()), State.DERIVED));
        }
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLObjectExactCardinality ce) {
        if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
            return Collections.singleton(PropertyClassValue.get(ren.getObjectPropertyData(ce
                                                                                                  .getProperty()
                                                                                                  .asOWLObjectProperty()), ren
                                                                        .getClassData(ce
                                                                                              .getFiller()
                                                                                              .asOWLClass()), State.DERIVED));
        }
        else {
            return null;
        }
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLObjectHasValue desc) {
        if(!desc.getProperty().isAnonymous() && !desc.getValue().isAnonymous()) {
            return Collections.singleton(PropertyIndividualValue.get(ren.getObjectPropertyData(desc
                                                                                                       .getProperty()
                                                                                                       .asOWLObjectProperty()), ren
                                                                             .getIndividualData(desc
                                                                                                        .getFiller()
                                                                                                        .asOWLNamedIndividual()), state));
        }
        else {
            return null;
        }
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLDataSomeValuesFrom desc) {
        if(desc.getFiller().isDatatype()) {
            return Collections.singleton(PropertyDatatypeValue.get(ren.getDataPropertyData(desc
                                                                                                   .getProperty()
                                                                                                   .asOWLDataProperty()), ren
                                                                           .getDatatypeData(desc
                                                                                                    .getFiller()
                                                                                                    .asOWLDatatype()), state));
        }
        else {
            return null;
        }
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLDataMinCardinality ce) {
        if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
            return Collections.singleton(PropertyDatatypeValue.get(ren.getDataPropertyData(ce
                                                                                                   .getProperty()
                                                                                                   .asOWLDataProperty()), ren
                                                                           .getDatatypeData(ce
                                                                                                    .getFiller()
                                                                                                    .asOWLDatatype()), state));
        }
        else {
            return null;
        }
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLDataExactCardinality ce) {
        if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
            return Collections.singleton(PropertyDatatypeValue.get(ren.getDataPropertyData(ce
                                                                                                   .getProperty()
                                                                                                   .asOWLDataProperty()), ren
                                                                           .getDatatypeData(ce
                                                                                                    .getFiller()
                                                                                                    .asOWLDatatype()), state));
        }
        else {
            return null;
        }
    }

    @Nonnull
    @Override
    public Set<PropertyValue> visit(OWLDataHasValue desc) {
        return Collections.singleton(PropertyLiteralValue.get(ren.getDataPropertyData(desc
                                                                                              .getProperty()
                                                                                              .asOWLDataProperty()), OWLLiteralData
                                                                      .get(desc.getFiller()), state));
    }
}

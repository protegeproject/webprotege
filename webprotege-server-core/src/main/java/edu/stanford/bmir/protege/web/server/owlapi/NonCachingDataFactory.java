package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import uk.ac.manchester.cs.owl.owlapi.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-30
 */
public class NonCachingDataFactory implements OWLDataFactory {

    @Override
    @Nonnull
    public OWLClass getOWLThing() {
        return delegate.getOWLThing();
    }

    @Override
    @Nonnull
    public OWLClass getOWLNothing() {
        return delegate.getOWLNothing();
    }

    @Override
    @Nonnull
    public OWLObjectProperty getOWLTopObjectProperty() {
        return delegate.getOWLTopObjectProperty();
    }

    @Override
    @Nonnull
    public OWLDataProperty getOWLTopDataProperty() {
        return delegate.getOWLTopDataProperty();
    }

    @Override
    @Nonnull
    public OWLObjectProperty getOWLBottomObjectProperty() {
        return delegate.getOWLBottomObjectProperty();
    }

    @Override
    @Nonnull
    public OWLDataProperty getOWLBottomDataProperty() {
        return delegate.getOWLBottomDataProperty();
    }

    @Override
    @Nonnull
    public OWLDatatype getTopDatatype() {
        return delegate.getTopDatatype();
    }

    @Override
    @Nonnull
    public OWLClass getOWLClass(@Nonnull String abbreviatedIRI,
                                @Nonnull PrefixManager prefixManager) {
        return delegate.getOWLClass(abbreviatedIRI, prefixManager);
    }

    @Override
    @Nonnull
    public OWLObjectProperty getOWLObjectProperty(@Nonnull String abbreviatedIRI,
                                                  @Nonnull PrefixManager prefixManager) {
        return delegate.getOWLObjectProperty(abbreviatedIRI, prefixManager);
    }

    @Override
    @Nonnull
    public OWLObjectInverseOf getOWLObjectInverseOf(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLObjectInverseOf(property);
    }

    @Override
    @Nonnull
    public OWLDataProperty getOWLDataProperty(@Nonnull String abbreviatedIRI,
                                              @Nonnull PrefixManager prefixManager) {
        return delegate.getOWLDataProperty(abbreviatedIRI, prefixManager);
    }

    @Override
    @Nonnull
    public OWLNamedIndividual getOWLNamedIndividual(@Nonnull String abbreviatedIRI,
                                                    @Nonnull PrefixManager prefixManager) {
        return delegate.getOWLNamedIndividual(abbreviatedIRI, prefixManager);
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull String abbreviatedIRI,
                                                          @Nonnull PrefixManager prefixManager) {
        return delegate.getOWLAnnotationProperty(abbreviatedIRI, prefixManager);
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getRDFSLabel() {
        return delegate.getRDFSLabel();
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getRDFSComment() {
        return delegate.getRDFSComment();
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getRDFSSeeAlso() {
        return delegate.getRDFSSeeAlso();
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getRDFSIsDefinedBy() {
        return delegate.getRDFSIsDefinedBy();
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getOWLVersionInfo() {
        return delegate.getOWLVersionInfo();
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getOWLBackwardCompatibleWith() {
        return delegate.getOWLBackwardCompatibleWith();
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getOWLIncompatibleWith() {
        return delegate.getOWLIncompatibleWith();
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getOWLDeprecated() {
        return delegate.getOWLDeprecated();
    }

    @Override
    @Nonnull
    public OWLDatatype getRDFPlainLiteral() {
        return delegate.getRDFPlainLiteral();
    }

    @Override
    @Nonnull
    public OWLDatatype getOWLDatatype(@Nonnull String abbreviatedIRI,
                                      @Nonnull PrefixManager prefixManager) {
        return delegate.getOWLDatatype(abbreviatedIRI, prefixManager);
    }

    @Override
    @Nonnull
    public OWLDatatype getIntegerOWLDatatype() {
        return delegate.getIntegerOWLDatatype();
    }

    @Override
    @Nonnull
    public OWLDatatype getFloatOWLDatatype() {
        return delegate.getFloatOWLDatatype();
    }

    @Override
    @Nonnull
    public OWLDatatype getDoubleOWLDatatype() {
        return delegate.getDoubleOWLDatatype();
    }

    @Override
    @Nonnull
    public OWLDatatype getBooleanOWLDatatype() {
        return delegate.getBooleanOWLDatatype();
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(@Nonnull String lexicalValue,
                                    @Nonnull OWLDatatype datatype) {
        return new OWLLiteralImpl(lexicalValue, null, datatype);
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(@Nonnull String lexicalValue,
                                    @Nonnull OWL2Datatype datatype) {
        return new OWLLiteralImpl(lexicalValue, null, datatype.getDatatype(this));
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(int value) {
        return delegate.getOWLLiteral(value);
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(double value) {
        return delegate.getOWLLiteral(value);
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(boolean value) {
        return delegate.getOWLLiteral(value);
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(float value) {
        return delegate.getOWLLiteral(value);
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(@Nonnull String value) {
        return new OWLLiteralImplString(value);
    }

    @Override
    @Nonnull
    public OWLLiteral getOWLLiteral(@Nonnull String literal,
                                    @Nullable String lang) {
        return new OWLLiteralImpl(literal, lang, getRDFPlainLiteral());
    }

    @Override
    @Nonnull
    public OWLDataOneOf getOWLDataOneOf(@Nonnull Set<? extends OWLLiteral> values) {
        return delegate.getOWLDataOneOf(values);
    }

    @Override
    @Nonnull
    public OWLDataOneOf getOWLDataOneOf(@Nonnull OWLLiteral... values) {
        return delegate.getOWLDataOneOf(values);
    }

    @Override
    @Nonnull
    public OWLDataComplementOf getOWLDataComplementOf(@Nonnull OWLDataRange dataRange) {
        return delegate.getOWLDataComplementOf(dataRange);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeRestriction(@Nonnull OWLDatatype dataType,
                                                            @Nonnull Set<OWLFacetRestriction> facetRestrictions) {
        return delegate.getOWLDatatypeRestriction(dataType, facetRestrictions);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeRestriction(@Nonnull OWLDatatype dataType,
                                                            @Nonnull OWLFacet facet,
                                                            @Nonnull OWLLiteral typedLiteral) {
        return delegate.getOWLDatatypeRestriction(dataType, facet, typedLiteral);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeRestriction(@Nonnull OWLDatatype dataType,
                                                            @Nonnull OWLFacetRestriction... facetRestrictions) {
        return delegate.getOWLDatatypeRestriction(dataType, facetRestrictions);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinInclusiveRestriction(int minInclusive) {
        return delegate.getOWLDatatypeMinInclusiveRestriction(minInclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMaxInclusiveRestriction(int maxInclusive) {
        return delegate.getOWLDatatypeMaxInclusiveRestriction(maxInclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinMaxInclusiveRestriction(int minInclusive,
                                                                           int maxInclusive) {
        return delegate.getOWLDatatypeMinMaxInclusiveRestriction(minInclusive, maxInclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinExclusiveRestriction(int minExclusive) {
        return delegate.getOWLDatatypeMinExclusiveRestriction(minExclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMaxExclusiveRestriction(int maxExclusive) {
        return delegate.getOWLDatatypeMaxExclusiveRestriction(maxExclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinMaxExclusiveRestriction(int minExclusive,
                                                                           int maxExclusive) {
        return delegate.getOWLDatatypeMinMaxExclusiveRestriction(minExclusive, maxExclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinInclusiveRestriction(double minInclusive) {
        return delegate.getOWLDatatypeMinInclusiveRestriction(minInclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMaxInclusiveRestriction(double maxInclusive) {
        return delegate.getOWLDatatypeMaxInclusiveRestriction(maxInclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinMaxInclusiveRestriction(double minInclusive,
                                                                           double maxInclusive) {
        return delegate.getOWLDatatypeMinMaxInclusiveRestriction(minInclusive, maxInclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinExclusiveRestriction(double minExclusive) {
        return delegate.getOWLDatatypeMinExclusiveRestriction(minExclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMaxExclusiveRestriction(double maxExclusive) {
        return delegate.getOWLDatatypeMaxExclusiveRestriction(maxExclusive);
    }

    @Override
    @Nonnull
    public OWLDatatypeRestriction getOWLDatatypeMinMaxExclusiveRestriction(double minExclusive,
                                                                           double maxExclusive) {
        return delegate.getOWLDatatypeMinMaxExclusiveRestriction(minExclusive, maxExclusive);
    }

    @Override
    @Nonnull
    public OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
                                                      @Nonnull OWLLiteral facetValue) {
        return delegate.getOWLFacetRestriction(facet, facetValue);
    }

    @Override
    @Nonnull
    public OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
                                                      int facetValue) {
        return delegate.getOWLFacetRestriction(facet, facetValue);
    }

    @Override
    @Nonnull
    public OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
                                                      double facetValue) {
        return delegate.getOWLFacetRestriction(facet, facetValue);
    }

    @Override
    @Nonnull
    public OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
                                                      float facetValue) {
        return delegate.getOWLFacetRestriction(facet, facetValue);
    }

    @Override
    @Nonnull
    public OWLDataUnionOf getOWLDataUnionOf(@Nonnull Set<? extends OWLDataRange> dataRanges) {
        return delegate.getOWLDataUnionOf(dataRanges);
    }

    @Override
    @Nonnull
    public OWLDataUnionOf getOWLDataUnionOf(@Nonnull OWLDataRange... dataRanges) {
        return delegate.getOWLDataUnionOf(dataRanges);
    }

    @Override
    @Nonnull
    public OWLDataIntersectionOf getOWLDataIntersectionOf(@Nonnull Set<? extends OWLDataRange> dataRanges) {
        return delegate.getOWLDataIntersectionOf(dataRanges);
    }

    @Override
    @Nonnull
    public OWLDataIntersectionOf getOWLDataIntersectionOf(@Nonnull OWLDataRange... dataRanges) {
        return delegate.getOWLDataIntersectionOf(dataRanges);
    }

    @Override
    @Nonnull
    public OWLObjectIntersectionOf getOWLObjectIntersectionOf(@Nonnull Set<? extends OWLClassExpression> operands) {
        return delegate.getOWLObjectIntersectionOf(operands);
    }

    @Override
    @Nonnull
    public OWLObjectIntersectionOf getOWLObjectIntersectionOf(@Nonnull OWLClassExpression... operands) {
        return delegate.getOWLObjectIntersectionOf(operands);
    }

    @Override
    @Nonnull
    public OWLDataSomeValuesFrom getOWLDataSomeValuesFrom(@Nonnull OWLDataPropertyExpression property,
                                                          @Nonnull OWLDataRange dataRange) {
        return delegate.getOWLDataSomeValuesFrom(property, dataRange);
    }

    @Override
    @Nonnull
    public OWLDataAllValuesFrom getOWLDataAllValuesFrom(@Nonnull OWLDataPropertyExpression property,
                                                        @Nonnull OWLDataRange dataRange) {
        return delegate.getOWLDataAllValuesFrom(property, dataRange);
    }

    @Override
    @Nonnull
    public OWLDataExactCardinality getOWLDataExactCardinality(int cardinality,
                                                              @Nonnull OWLDataPropertyExpression property) {
        return delegate.getOWLDataExactCardinality(cardinality, property);
    }

    @Override
    @Nonnull
    public OWLDataExactCardinality getOWLDataExactCardinality(int cardinality,
                                                              @Nonnull OWLDataPropertyExpression property,
                                                              @Nonnull OWLDataRange dataRange) {
        return delegate.getOWLDataExactCardinality(cardinality, property, dataRange);
    }

    @Override
    @Nonnull
    public OWLDataMaxCardinality getOWLDataMaxCardinality(int cardinality,
                                                          @Nonnull OWLDataPropertyExpression property) {
        return delegate.getOWLDataMaxCardinality(cardinality, property);
    }

    @Override
    @Nonnull
    public OWLDataMaxCardinality getOWLDataMaxCardinality(int cardinality,
                                                          @Nonnull OWLDataPropertyExpression property,
                                                          @Nonnull OWLDataRange dataRange) {
        return delegate.getOWLDataMaxCardinality(cardinality, property, dataRange);
    }

    @Override
    @Nonnull
    public OWLDataMinCardinality getOWLDataMinCardinality(int cardinality,
                                                          @Nonnull OWLDataPropertyExpression property) {
        return delegate.getOWLDataMinCardinality(cardinality, property);
    }

    @Override
    @Nonnull
    public OWLDataMinCardinality getOWLDataMinCardinality(int cardinality,
                                                          @Nonnull OWLDataPropertyExpression property,
                                                          @Nonnull OWLDataRange dataRange) {
        return delegate.getOWLDataMinCardinality(cardinality, property, dataRange);
    }

    @Override
    @Nonnull
    public OWLDataHasValue getOWLDataHasValue(@Nonnull OWLDataPropertyExpression property,
                                              @Nonnull OWLLiteral value) {
        return delegate.getOWLDataHasValue(property, value);
    }

    @Override
    @Nonnull
    public OWLObjectComplementOf getOWLObjectComplementOf(@Nonnull OWLClassExpression operand) {
        return delegate.getOWLObjectComplementOf(operand);
    }

    @Override
    @Nonnull
    public OWLObjectOneOf getOWLObjectOneOf(@Nonnull Set<? extends OWLIndividual> values) {
        return delegate.getOWLObjectOneOf(values);
    }

    @Override
    @Nonnull
    public OWLObjectOneOf getOWLObjectOneOf(@Nonnull OWLIndividual... individuals) {
        return delegate.getOWLObjectOneOf(individuals);
    }

    @Override
    @Nonnull
    public OWLObjectAllValuesFrom getOWLObjectAllValuesFrom(@Nonnull OWLObjectPropertyExpression property,
                                                            @Nonnull OWLClassExpression classExpression) {
        return delegate.getOWLObjectAllValuesFrom(property, classExpression);
    }

    @Override
    @Nonnull
    public OWLObjectSomeValuesFrom getOWLObjectSomeValuesFrom(@Nonnull OWLObjectPropertyExpression property,
                                                              @Nonnull OWLClassExpression classExpression) {
        return delegate.getOWLObjectSomeValuesFrom(property, classExpression);
    }

    @Override
    @Nonnull
    public OWLObjectExactCardinality getOWLObjectExactCardinality(int cardinality,
                                                                  @Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLObjectExactCardinality(cardinality, property);
    }

    @Override
    @Nonnull
    public OWLObjectExactCardinality getOWLObjectExactCardinality(int cardinality,
                                                                  @Nonnull OWLObjectPropertyExpression property,
                                                                  @Nonnull OWLClassExpression classExpression) {
        return delegate.getOWLObjectExactCardinality(cardinality, property, classExpression);
    }

    @Override
    @Nonnull
    public OWLObjectMinCardinality getOWLObjectMinCardinality(int cardinality,
                                                              @Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLObjectMinCardinality(cardinality, property);
    }

    @Override
    @Nonnull
    public OWLObjectMinCardinality getOWLObjectMinCardinality(int cardinality,
                                                              @Nonnull OWLObjectPropertyExpression property,
                                                              @Nonnull OWLClassExpression classExpression) {
        return delegate.getOWLObjectMinCardinality(cardinality, property, classExpression);
    }

    @Override
    @Nonnull
    public OWLObjectMaxCardinality getOWLObjectMaxCardinality(int cardinality,
                                                              @Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLObjectMaxCardinality(cardinality, property);
    }

    @Override
    @Nonnull
    public OWLObjectMaxCardinality getOWLObjectMaxCardinality(int cardinality,
                                                              @Nonnull OWLObjectPropertyExpression property,
                                                              @Nonnull OWLClassExpression classExpression) {
        return delegate.getOWLObjectMaxCardinality(cardinality, property, classExpression);
    }

    @Override
    @Nonnull
    public OWLObjectHasSelf getOWLObjectHasSelf(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLObjectHasSelf(property);
    }

    @Override
    @Nonnull
    public OWLObjectHasValue getOWLObjectHasValue(@Nonnull OWLObjectPropertyExpression property,
                                                  @Nonnull OWLIndividual individual) {
        return delegate.getOWLObjectHasValue(property, individual);
    }

    @Override
    @Nonnull
    public OWLObjectUnionOf getOWLObjectUnionOf(@Nonnull Set<? extends OWLClassExpression> operands) {
        return delegate.getOWLObjectUnionOf(operands);
    }

    @Override
    @Nonnull
    public OWLObjectUnionOf getOWLObjectUnionOf(@Nonnull OWLClassExpression... operands) {
        return delegate.getOWLObjectUnionOf(operands);
    }

    @Override
    @Nonnull
    public OWLDeclarationAxiom getOWLDeclarationAxiom(@Nonnull OWLEntity owlEntity) {
        return delegate.getOWLDeclarationAxiom(owlEntity);
    }

    @Override
    @Nonnull
    public OWLDeclarationAxiom getOWLDeclarationAxiom(@Nonnull OWLEntity owlEntity,
                                                      @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDeclarationAxiom(owlEntity, annotations);
    }

    @Override
    @Nonnull
    public OWLSubClassOfAxiom getOWLSubClassOfAxiom(@Nonnull OWLClassExpression subClass,
                                                    @Nonnull OWLClassExpression superClass) {
        return delegate.getOWLSubClassOfAxiom(subClass, superClass);
    }

    @Override
    @Nonnull
    public OWLSubClassOfAxiom getOWLSubClassOfAxiom(@Nonnull OWLClassExpression subClass,
                                                    @Nonnull OWLClassExpression superClass,
                                                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLSubClassOfAxiom(subClass, superClass, annotations);
    }

    @Override
    @Nonnull
    public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(@Nonnull Set<? extends OWLClassExpression> classExpressions) {
        return delegate.getOWLEquivalentClassesAxiom(classExpressions);
    }

    @Override
    @Nonnull
    public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(@Nonnull Set<? extends OWLClassExpression> classExpressions,
                                                                  @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLEquivalentClassesAxiom(classExpressions, annotations);
    }

    @Override
    @Nonnull
    public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(@Nonnull OWLClassExpression... classExpressions) {
        return delegate.getOWLEquivalentClassesAxiom(classExpressions);
    }

    @Override
    @Nonnull
    public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(@Nonnull OWLClassExpression clsA,
                                                                  @Nonnull OWLClassExpression clsB) {
        return delegate.getOWLEquivalentClassesAxiom(clsA, clsB);
    }

    @Override
    @Nonnull
    public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(@Nonnull OWLClassExpression clsA,
                                                                  @Nonnull OWLClassExpression clsB,
                                                                  @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLEquivalentClassesAxiom(clsA, clsB, annotations);
    }

    @Override
    @Nonnull
    public OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(@Nonnull Set<? extends OWLClassExpression> classExpressions) {
        return delegate.getOWLDisjointClassesAxiom(classExpressions);
    }

    @Override
    @Nonnull
    public OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(@Nonnull OWLClassExpression... classExpressions) {
        return delegate.getOWLDisjointClassesAxiom(classExpressions);
    }

    @Override
    @Nonnull
    public OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(@Nonnull Set<? extends OWLClassExpression> classExpressions,
                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDisjointClassesAxiom(classExpressions, annotations);
    }

    @Override
    @Nonnull
    public OWLDisjointUnionAxiom getOWLDisjointUnionAxiom(@Nonnull OWLClass owlClass,
                                                          @Nonnull Set<? extends OWLClassExpression> classExpressions) {
        return delegate.getOWLDisjointUnionAxiom(owlClass, classExpressions);
    }

    @Override
    @Nonnull
    public OWLDisjointUnionAxiom getOWLDisjointUnionAxiom(@Nonnull OWLClass owlClass,
                                                          @Nonnull Set<? extends OWLClassExpression> classExpressions,
                                                          @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDisjointUnionAxiom(owlClass, classExpressions, annotations);
    }

    @Override
    @Nonnull
    public OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(@Nonnull OWLObjectPropertyExpression subProperty,
                                                                      @Nonnull OWLObjectPropertyExpression superProperty) {
        return delegate.getOWLSubObjectPropertyOfAxiom(subProperty, superProperty);
    }

    @Override
    @Nonnull
    public OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(@Nonnull OWLObjectPropertyExpression subProperty,
                                                                      @Nonnull OWLObjectPropertyExpression superProperty,
                                                                      @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLSubObjectPropertyOfAxiom(subProperty, superProperty, annotations);
    }

    @Override
    @Nonnull
    public OWLSubPropertyChainOfAxiom getOWLSubPropertyChainOfAxiom(@Nonnull List<? extends OWLObjectPropertyExpression> chain,
                                                                    @Nonnull OWLObjectPropertyExpression superProperty) {
        return delegate.getOWLSubPropertyChainOfAxiom(chain, superProperty);
    }

    @Override
    @Nonnull
    public OWLSubPropertyChainOfAxiom getOWLSubPropertyChainOfAxiom(@Nonnull List<? extends OWLObjectPropertyExpression> chain,
                                                                    @Nonnull OWLObjectPropertyExpression superProperty,
                                                                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLSubPropertyChainOfAxiom(chain, superProperty, annotations);
    }

    @Override
    @Nonnull
    public OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(@Nonnull Set<? extends OWLObjectPropertyExpression> properties) {
        return delegate.getOWLEquivalentObjectPropertiesAxiom(properties);
    }

    @Override
    @Nonnull
    public OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(@Nonnull Set<? extends OWLObjectPropertyExpression> properties,
                                                                                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLEquivalentObjectPropertiesAxiom(properties, annotations);
    }

    @Override
    @Nonnull
    public OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(@Nonnull OWLObjectPropertyExpression... properties) {
        return delegate.getOWLEquivalentObjectPropertiesAxiom(properties);
    }

    @Override
    @Nonnull
    public OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(@Nonnull OWLObjectPropertyExpression propertyA,
                                                                                    @Nonnull OWLObjectPropertyExpression propertyB) {
        return delegate.getOWLEquivalentObjectPropertiesAxiom(propertyA, propertyB);
    }

    @Override
    @Nonnull
    public OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(@Nonnull OWLObjectPropertyExpression propertyA,
                                                                                    @Nonnull OWLObjectPropertyExpression propertyB,
                                                                                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLEquivalentObjectPropertiesAxiom(propertyA, propertyB, annotations);
    }

    @Override
    @Nonnull
    public OWLDisjointObjectPropertiesAxiom getOWLDisjointObjectPropertiesAxiom(@Nonnull Set<? extends OWLObjectPropertyExpression> properties) {
        return delegate.getOWLDisjointObjectPropertiesAxiom(properties);
    }

    @Override
    @Nonnull
    public OWLDisjointObjectPropertiesAxiom getOWLDisjointObjectPropertiesAxiom(@Nonnull OWLObjectPropertyExpression... properties) {
        return delegate.getOWLDisjointObjectPropertiesAxiom(properties);
    }

    @Override
    @Nonnull
    public OWLDisjointObjectPropertiesAxiom getOWLDisjointObjectPropertiesAxiom(@Nonnull Set<? extends OWLObjectPropertyExpression> properties,
                                                                                @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDisjointObjectPropertiesAxiom(properties, annotations);
    }

    @Override
    @Nonnull
    public OWLInverseObjectPropertiesAxiom getOWLInverseObjectPropertiesAxiom(@Nonnull OWLObjectPropertyExpression forwardProperty,
                                                                              @Nonnull OWLObjectPropertyExpression inverseProperty) {
        return delegate.getOWLInverseObjectPropertiesAxiom(forwardProperty, inverseProperty);
    }

    @Override
    @Nonnull
    public OWLInverseObjectPropertiesAxiom getOWLInverseObjectPropertiesAxiom(@Nonnull OWLObjectPropertyExpression forwardProperty,
                                                                              @Nonnull OWLObjectPropertyExpression inverseProperty,
                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLInverseObjectPropertiesAxiom(forwardProperty, inverseProperty, annotations);
    }

    @Override
    @Nonnull
    public OWLObjectPropertyDomainAxiom getOWLObjectPropertyDomainAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                        @Nonnull OWLClassExpression classExpression) {
        return delegate.getOWLObjectPropertyDomainAxiom(property, classExpression);
    }

    @Override
    @Nonnull
    public OWLObjectPropertyDomainAxiom getOWLObjectPropertyDomainAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                        @Nonnull OWLClassExpression classExpression,
                                                                        @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLObjectPropertyDomainAxiom(property, classExpression, annotations);
    }

    @Override
    @Nonnull
    public OWLObjectPropertyRangeAxiom getOWLObjectPropertyRangeAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                      @Nonnull OWLClassExpression range) {
        return delegate.getOWLObjectPropertyRangeAxiom(property, range);
    }

    @Override
    @Nonnull
    public OWLObjectPropertyRangeAxiom getOWLObjectPropertyRangeAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                      @Nonnull OWLClassExpression range,
                                                                      @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLObjectPropertyRangeAxiom(property, range, annotations);
    }

    @Override
    @Nonnull
    public OWLFunctionalObjectPropertyAxiom getOWLFunctionalObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLFunctionalObjectPropertyAxiom(property);
    }

    @Override
    @Nonnull
    public OWLFunctionalObjectPropertyAxiom getOWLFunctionalObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                                @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLFunctionalObjectPropertyAxiom(property, annotations);
    }

    @Override
    @Nonnull
    public OWLInverseFunctionalObjectPropertyAxiom getOWLInverseFunctionalObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLInverseFunctionalObjectPropertyAxiom(property);
    }

    @Override
    @Nonnull
    public OWLInverseFunctionalObjectPropertyAxiom getOWLInverseFunctionalObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLInverseFunctionalObjectPropertyAxiom(property, annotations);
    }

    @Override
    @Nonnull
    public OWLReflexiveObjectPropertyAxiom getOWLReflexiveObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLReflexiveObjectPropertyAxiom(property);
    }

    @Override
    @Nonnull
    public OWLReflexiveObjectPropertyAxiom getOWLReflexiveObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLReflexiveObjectPropertyAxiom(property, annotations);
    }

    @Override
    @Nonnull
    public OWLIrreflexiveObjectPropertyAxiom getOWLIrreflexiveObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLIrreflexiveObjectPropertyAxiom(property);
    }

    @Override
    @Nonnull
    public OWLIrreflexiveObjectPropertyAxiom getOWLIrreflexiveObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                                  @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLIrreflexiveObjectPropertyAxiom(property, annotations);
    }

    @Override
    @Nonnull
    public OWLSymmetricObjectPropertyAxiom getOWLSymmetricObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLSymmetricObjectPropertyAxiom(property);
    }

    @Override
    @Nonnull
    public OWLSymmetricObjectPropertyAxiom getOWLSymmetricObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLSymmetricObjectPropertyAxiom(property, annotations);
    }

    @Override
    @Nonnull
    public OWLAsymmetricObjectPropertyAxiom getOWLAsymmetricObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression propertyExpression) {
        return delegate.getOWLAsymmetricObjectPropertyAxiom(propertyExpression);
    }

    @Override
    @Nonnull
    public OWLAsymmetricObjectPropertyAxiom getOWLAsymmetricObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression propertyExpression,
                                                                                @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLAsymmetricObjectPropertyAxiom(propertyExpression, annotations);
    }

    @Override
    @Nonnull
    public OWLTransitiveObjectPropertyAxiom getOWLTransitiveObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property) {
        return delegate.getOWLTransitiveObjectPropertyAxiom(property);
    }

    @Override
    @Nonnull
    public OWLTransitiveObjectPropertyAxiom getOWLTransitiveObjectPropertyAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                                @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLTransitiveObjectPropertyAxiom(property, annotations);
    }

    @Override
    @Nonnull
    public OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(@Nonnull OWLDataPropertyExpression subProperty,
                                                                  @Nonnull OWLDataPropertyExpression superProperty) {
        return delegate.getOWLSubDataPropertyOfAxiom(subProperty, superProperty);
    }

    @Override
    @Nonnull
    public OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(@Nonnull OWLDataPropertyExpression subProperty,
                                                                  @Nonnull OWLDataPropertyExpression superProperty,
                                                                  @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLSubDataPropertyOfAxiom(subProperty, superProperty, annotations);
    }

    @Override
    @Nonnull
    public OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(@Nonnull Set<? extends OWLDataPropertyExpression> properties) {
        return delegate.getOWLEquivalentDataPropertiesAxiom(properties);
    }

    @Override
    @Nonnull
    public OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(@Nonnull Set<? extends OWLDataPropertyExpression> properties,
                                                                                @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLEquivalentDataPropertiesAxiom(properties, annotations);
    }

    @Override
    @Nonnull
    public OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(@Nonnull OWLDataPropertyExpression... properties) {
        return delegate.getOWLEquivalentDataPropertiesAxiom(properties);
    }

    @Override
    @Nonnull
    public OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(@Nonnull OWLDataPropertyExpression propertyA,
                                                                                @Nonnull OWLDataPropertyExpression propertyB) {
        return delegate.getOWLEquivalentDataPropertiesAxiom(propertyA, propertyB);
    }

    @Override
    @Nonnull
    public OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(@Nonnull OWLDataPropertyExpression propertyA,
                                                                                @Nonnull OWLDataPropertyExpression propertyB,
                                                                                @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLEquivalentDataPropertiesAxiom(propertyA, propertyB, annotations);
    }

    @Override
    @Nonnull
    public OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(@Nonnull OWLDataPropertyExpression... dataProperties) {
        return delegate.getOWLDisjointDataPropertiesAxiom(dataProperties);
    }

    @Override
    @Nonnull
    public OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(@Nonnull Set<? extends OWLDataPropertyExpression> properties) {
        return delegate.getOWLDisjointDataPropertiesAxiom(properties);
    }

    @Override
    @Nonnull
    public OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(@Nonnull Set<? extends OWLDataPropertyExpression> properties,
                                                                            @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDisjointDataPropertiesAxiom(properties, annotations);
    }

    @Override
    @Nonnull
    public OWLDataPropertyDomainAxiom getOWLDataPropertyDomainAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                    @Nonnull OWLClassExpression domain) {
        return delegate.getOWLDataPropertyDomainAxiom(property, domain);
    }

    @Override
    @Nonnull
    public OWLDataPropertyDomainAxiom getOWLDataPropertyDomainAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                    @Nonnull OWLClassExpression domain,
                                                                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDataPropertyDomainAxiom(property, domain, annotations);
    }

    @Override
    @Nonnull
    public OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                  @Nonnull OWLDataRange owlDataRange) {
        return delegate.getOWLDataPropertyRangeAxiom(property, owlDataRange);
    }

    @Override
    @Nonnull
    public OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                  @Nonnull OWLDataRange owlDataRange,
                                                                  @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDataPropertyRangeAxiom(property, owlDataRange, annotations);
    }

    @Override
    @Nonnull
    public OWLFunctionalDataPropertyAxiom getOWLFunctionalDataPropertyAxiom(@Nonnull OWLDataPropertyExpression property) {
        return delegate.getOWLFunctionalDataPropertyAxiom(property);
    }

    @Override
    @Nonnull
    public OWLFunctionalDataPropertyAxiom getOWLFunctionalDataPropertyAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                            @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLFunctionalDataPropertyAxiom(property, annotations);
    }

    @Override
    @Nonnull
    public OWLHasKeyAxiom getOWLHasKeyAxiom(@Nonnull OWLClassExpression ce,
                                            @Nonnull Set<? extends OWLPropertyExpression> properties) {
        return delegate.getOWLHasKeyAxiom(ce, properties);
    }

    @Override
    @Nonnull
    public OWLHasKeyAxiom getOWLHasKeyAxiom(@Nonnull OWLClassExpression ce,
                                            @Nonnull OWLPropertyExpression... properties) {
        return delegate.getOWLHasKeyAxiom(ce, properties);
    }

    @Override
    @Nonnull
    public OWLHasKeyAxiom getOWLHasKeyAxiom(@Nonnull OWLClassExpression ce,
                                            @Nonnull Set<? extends OWLPropertyExpression> objectProperties,
                                            @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLHasKeyAxiom(ce, objectProperties, annotations);
    }

    @Override
    @Nonnull
    public OWLDatatypeDefinitionAxiom getOWLDatatypeDefinitionAxiom(@Nonnull OWLDatatype datatype,
                                                                    @Nonnull OWLDataRange dataRange) {
        return delegate.getOWLDatatypeDefinitionAxiom(datatype, dataRange);
    }

    @Override
    @Nonnull
    public OWLDatatypeDefinitionAxiom getOWLDatatypeDefinitionAxiom(@Nonnull OWLDatatype datatype,
                                                                    @Nonnull OWLDataRange dataRange,
                                                                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDatatypeDefinitionAxiom(datatype, dataRange, annotations);
    }

    @Override
    @Nonnull
    public OWLSameIndividualAxiom getOWLSameIndividualAxiom(@Nonnull Set<? extends OWLIndividual> individuals) {
        return delegate.getOWLSameIndividualAxiom(individuals);
    }

    @Override
    @Nonnull
    public OWLSameIndividualAxiom getOWLSameIndividualAxiom(@Nonnull OWLIndividual... individual) {
        return delegate.getOWLSameIndividualAxiom(individual);
    }

    @Override
    @Nonnull
    public OWLSameIndividualAxiom getOWLSameIndividualAxiom(@Nonnull Set<? extends OWLIndividual> individuals,
                                                            @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLSameIndividualAxiom(individuals, annotations);
    }

    @Override
    @Nonnull
    public OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(@Nonnull Set<? extends OWLIndividual> individuals) {
        return delegate.getOWLDifferentIndividualsAxiom(individuals);
    }

    @Override
    @Nonnull
    public OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(@Nonnull OWLIndividual... individuals) {
        return delegate.getOWLDifferentIndividualsAxiom(individuals);
    }

    @Override
    @Nonnull
    public OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(@Nonnull Set<? extends OWLIndividual> individuals,
                                                                        @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDifferentIndividualsAxiom(individuals, annotations);
    }

    @Override
    @Nonnull
    public OWLClassAssertionAxiom getOWLClassAssertionAxiom(@Nonnull OWLClassExpression classExpression,
                                                            @Nonnull OWLIndividual individual) {
        return delegate.getOWLClassAssertionAxiom(classExpression, individual);
    }

    @Override
    @Nonnull
    public OWLClassAssertionAxiom getOWLClassAssertionAxiom(@Nonnull OWLClassExpression classExpression,
                                                            @Nonnull OWLIndividual individual,
                                                            @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLClassAssertionAxiom(classExpression, individual, annotations);
    }

    @Override
    @Nonnull
    public OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                              @Nonnull OWLIndividual individual,
                                                                              @Nonnull OWLIndividual object) {
        return delegate.getOWLObjectPropertyAssertionAxiom(property, individual, object);
    }

    @Override
    @Nonnull
    public OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                              @Nonnull OWLIndividual individual,
                                                                              @Nonnull OWLIndividual object,
                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLObjectPropertyAssertionAxiom(property, individual, object, annotations);
    }

    @Override
    @Nonnull
    public OWLNegativeObjectPropertyAssertionAxiom getOWLNegativeObjectPropertyAssertionAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                                              @Nonnull OWLIndividual subject,
                                                                                              @Nonnull OWLIndividual object) {
        return delegate.getOWLNegativeObjectPropertyAssertionAxiom(property, subject, object);
    }

    @Override
    @Nonnull
    public OWLNegativeObjectPropertyAssertionAxiom getOWLNegativeObjectPropertyAssertionAxiom(@Nonnull OWLObjectPropertyExpression property,
                                                                                              @Nonnull OWLIndividual subject,
                                                                                              @Nonnull OWLIndividual object,
                                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLNegativeObjectPropertyAssertionAxiom(property, subject, object, annotations);
    }

    @Override
    @Nonnull
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                          @Nonnull OWLIndividual subject,
                                                                          @Nonnull OWLLiteral object) {
        return delegate.getOWLDataPropertyAssertionAxiom(property, subject, object);
    }

    @Override
    @Nonnull
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                          @Nonnull OWLIndividual subject,
                                                                          @Nonnull OWLLiteral object,
                                                                          @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLDataPropertyAssertionAxiom(property, subject, object, annotations);
    }

    @Override
    @Nonnull
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                          @Nonnull OWLIndividual subject,
                                                                          int value) {
        return delegate.getOWLDataPropertyAssertionAxiom(property, subject, value);
    }

    @Override
    @Nonnull
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                          @Nonnull OWLIndividual subject,
                                                                          double value) {
        return delegate.getOWLDataPropertyAssertionAxiom(property, subject, value);
    }

    @Override
    @Nonnull
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                          @Nonnull OWLIndividual subject,
                                                                          float value) {
        return delegate.getOWLDataPropertyAssertionAxiom(property, subject, value);
    }

    @Override
    @Nonnull
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                          @Nonnull OWLIndividual subject,
                                                                          boolean value) {
        return delegate.getOWLDataPropertyAssertionAxiom(property, subject, value);
    }

    @Override
    @Nonnull
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                          @Nonnull OWLIndividual subject,
                                                                          @Nonnull String value) {
        return delegate.getOWLDataPropertyAssertionAxiom(property, subject, value);
    }

    @Override
    @Nonnull
    public OWLNegativeDataPropertyAssertionAxiom getOWLNegativeDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                                          @Nonnull OWLIndividual subject,
                                                                                          @Nonnull OWLLiteral object) {
        return delegate.getOWLNegativeDataPropertyAssertionAxiom(property, subject, object);
    }

    @Override
    @Nonnull
    public OWLNegativeDataPropertyAssertionAxiom getOWLNegativeDataPropertyAssertionAxiom(@Nonnull OWLDataPropertyExpression property,
                                                                                          @Nonnull OWLIndividual subject,
                                                                                          @Nonnull OWLLiteral object,
                                                                                          @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLNegativeDataPropertyAssertionAxiom(property, subject, object, annotations);
    }

    @Override
    @Nonnull
    public OWLAnnotation getOWLAnnotation(@Nonnull OWLAnnotationProperty property,
                                          @Nonnull OWLAnnotationValue value) {
        return delegate.getOWLAnnotation(property, value);
    }

    @Override
    @Nonnull
    public OWLAnnotation getOWLAnnotation(@Nonnull OWLAnnotationProperty property,
                                          @Nonnull OWLAnnotationValue value,
                                          @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLAnnotation(property, value, annotations);
    }

    @Override
    @Nonnull
    public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(@Nonnull OWLAnnotationProperty property,
                                                                      @Nonnull OWLAnnotationSubject subject,
                                                                      @Nonnull OWLAnnotationValue value) {
        return delegate.getOWLAnnotationAssertionAxiom(property, subject, value);
    }

    @Override
    @Nonnull
    public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(@Nonnull OWLAnnotationSubject subject,
                                                                      @Nonnull OWLAnnotation annotation) {
        return delegate.getOWLAnnotationAssertionAxiom(subject, annotation);
    }

    @Override
    @Nonnull
    public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(@Nonnull OWLAnnotationProperty property,
                                                                      @Nonnull OWLAnnotationSubject subject,
                                                                      @Nonnull OWLAnnotationValue value,
                                                                      @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLAnnotationAssertionAxiom(property, subject, value, annotations);
    }

    @Override
    @Nonnull
    public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(@Nonnull OWLAnnotationSubject subject,
                                                                      @Nonnull OWLAnnotation annotation,
                                                                      @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLAnnotationAssertionAxiom(subject, annotation, annotations);
    }

    @Override
    @Nonnull
    public OWLAnnotationAssertionAxiom getDeprecatedOWLAnnotationAssertionAxiom(@Nonnull IRI subject) {
        return delegate.getDeprecatedOWLAnnotationAssertionAxiom(subject);
    }

    @Override
    @Nonnull
    public OWLImportsDeclaration getOWLImportsDeclaration(@Nonnull IRI importedOntologyIRI) {
        return delegate.getOWLImportsDeclaration(importedOntologyIRI);
    }

    @Override
    @Nonnull
    public OWLAnnotationPropertyDomainAxiom getOWLAnnotationPropertyDomainAxiom(@Nonnull OWLAnnotationProperty prop,
                                                                                @Nonnull IRI domain) {
        return delegate.getOWLAnnotationPropertyDomainAxiom(prop, domain);
    }

    @Override
    @Nonnull
    public OWLAnnotationPropertyDomainAxiom getOWLAnnotationPropertyDomainAxiom(@Nonnull OWLAnnotationProperty prop,
                                                                                @Nonnull IRI domain,
                                                                                @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLAnnotationPropertyDomainAxiom(prop, domain, annotations);
    }

    @Override
    @Nonnull
    public OWLAnnotationPropertyRangeAxiom getOWLAnnotationPropertyRangeAxiom(@Nonnull OWLAnnotationProperty prop,
                                                                              @Nonnull IRI range) {
        return delegate.getOWLAnnotationPropertyRangeAxiom(prop, range);
    }

    @Override
    @Nonnull
    public OWLAnnotationPropertyRangeAxiom getOWLAnnotationPropertyRangeAxiom(@Nonnull OWLAnnotationProperty prop,
                                                                              @Nonnull IRI range,
                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLAnnotationPropertyRangeAxiom(prop, range, annotations);
    }

    @Override
    @Nonnull
    public OWLSubAnnotationPropertyOfAxiom getOWLSubAnnotationPropertyOfAxiom(@Nonnull OWLAnnotationProperty sub,
                                                                              @Nonnull OWLAnnotationProperty sup) {
        return delegate.getOWLSubAnnotationPropertyOfAxiom(sub, sup);
    }

    @Override
    @Nonnull
    public OWLSubAnnotationPropertyOfAxiom getOWLSubAnnotationPropertyOfAxiom(@Nonnull OWLAnnotationProperty sub,
                                                                              @Nonnull OWLAnnotationProperty sup,
                                                                              @Nonnull Set<? extends OWLAnnotation> annotations) {
        return delegate.getOWLSubAnnotationPropertyOfAxiom(sub, sup, annotations);
    }

    @Override
    public void purge() {
        delegate.purge();
    }

    @Override
    @Nonnull
    public SWRLRule getSWRLRule(@Nonnull Set<? extends SWRLAtom> body,
                                @Nonnull Set<? extends SWRLAtom> head) {
        return delegate.getSWRLRule(body, head);
    }

    @Override
    @Nonnull
    public SWRLRule getSWRLRule(@Nonnull Set<? extends SWRLAtom> body,
                                @Nonnull Set<? extends SWRLAtom> head,
                                @Nonnull Set<OWLAnnotation> annotations) {
        return delegate.getSWRLRule(body, head, annotations);
    }

    @Override
    @Nonnull
    public SWRLClassAtom getSWRLClassAtom(@Nonnull OWLClassExpression predicate,
                                          @Nonnull SWRLIArgument arg) {
        return delegate.getSWRLClassAtom(predicate, arg);
    }

    @Override
    @Nonnull
    public SWRLDataRangeAtom getSWRLDataRangeAtom(@Nonnull OWLDataRange predicate,
                                                  @Nonnull SWRLDArgument arg) {
        return delegate.getSWRLDataRangeAtom(predicate, arg);
    }

    @Override
    @Nonnull
    public SWRLObjectPropertyAtom getSWRLObjectPropertyAtom(@Nonnull OWLObjectPropertyExpression property,
                                                            @Nonnull SWRLIArgument arg0,
                                                            @Nonnull SWRLIArgument arg1) {
        return delegate.getSWRLObjectPropertyAtom(property, arg0, arg1);
    }

    @Override
    @Nonnull
    public SWRLDataPropertyAtom getSWRLDataPropertyAtom(@Nonnull OWLDataPropertyExpression property,
                                                        @Nonnull SWRLIArgument arg0,
                                                        @Nonnull SWRLDArgument arg1) {
        return delegate.getSWRLDataPropertyAtom(property, arg0, arg1);
    }

    @Override
    @Nonnull
    public SWRLBuiltInAtom getSWRLBuiltInAtom(@Nonnull IRI builtInIRI,
                                              @Nonnull List<SWRLDArgument> args) {
        return delegate.getSWRLBuiltInAtom(builtInIRI, args);
    }

    @Override
    @Nonnull
    public SWRLVariable getSWRLVariable(@Nonnull IRI var) {
        return delegate.getSWRLVariable(var);
    }

    @Override
    @Nonnull
    public SWRLIndividualArgument getSWRLIndividualArgument(@Nonnull OWLIndividual individual) {
        return delegate.getSWRLIndividualArgument(individual);
    }

    @Override
    @Nonnull
    public SWRLLiteralArgument getSWRLLiteralArgument(@Nonnull OWLLiteral literal) {
        return delegate.getSWRLLiteralArgument(literal);
    }

    @Override
    @Nonnull
    public SWRLSameIndividualAtom getSWRLSameIndividualAtom(@Nonnull SWRLIArgument arg0,
                                                            @Nonnull SWRLIArgument arg1) {
        return delegate.getSWRLSameIndividualAtom(arg0, arg1);
    }

    @Override
    @Nonnull
    public SWRLDifferentIndividualsAtom getSWRLDifferentIndividualsAtom(@Nonnull SWRLIArgument arg0,
                                                                        @Nonnull SWRLIArgument arg1) {
        return delegate.getSWRLDifferentIndividualsAtom(arg0, arg1);
    }

    @Override
    @Nonnull
    public OWLClass getOWLClass(@Nonnull IRI iri) {
        return new OWLClassImpl(iri);
    }

    @Override
    @Nonnull
    public OWLObjectProperty getOWLObjectProperty(@Nonnull IRI iri) {
        return new OWLObjectPropertyImpl(iri);
    }

    @Override
    @Nonnull
    public OWLDataProperty getOWLDataProperty(@Nonnull IRI iri) {
        return new OWLDataPropertyImpl(iri);
    }

    @Override
    @Nonnull
    public OWLNamedIndividual getOWLNamedIndividual(@Nonnull IRI iri) {
        return new OWLNamedIndividualImpl(iri);
    }

    @Override
    @Nonnull
    public OWLDatatype getOWLDatatype(@Nonnull IRI iri) {
        return new OWLDatatypeImpl(iri);
    }

    @Override
    @Nonnull
    public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull IRI iri) {
        return new OWLAnnotationPropertyImpl(iri);
    }

    @Override
    @Nonnull
    public <E extends OWLEntity> E getOWLEntity(@Nonnull EntityType<E> entityType,
                                                @Nonnull IRI iri) {
        return delegate.getOWLEntity(entityType, iri);
    }

    @Override
    @Nonnull
    public OWLAnonymousIndividual getOWLAnonymousIndividual() {
        return delegate.getOWLAnonymousIndividual();
    }

    @Override
    @Nonnull
    public OWLAnonymousIndividual getOWLAnonymousIndividual(@Nonnull String nodeId) {
        return delegate.getOWLAnonymousIndividual(nodeId);
    }

    private final OWLDataFactory delegate;

    @Inject
    public NonCachingDataFactory(OWLDataFactory delegate) {
        this.delegate = checkNotNull(delegate);
    }
}

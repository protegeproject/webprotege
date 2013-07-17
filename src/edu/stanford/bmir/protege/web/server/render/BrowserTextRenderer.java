package edu.stanford.bmir.protege.web.server.render;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleIRIShortFormProvider;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

import static org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class BrowserTextRenderer {

    private OWLAPIProject project;


    public BrowserTextRenderer(OWLAPIProject project) {
        this.project = project;
    }


    public String render(OWLObject object, HighlightChecker highlightChecker, DeprecatedChecker checker) {
        StringBuilder sb = new StringBuilder();
        EntityRenderer entityRenderer = new EntityRenderer(sb, project.getRenderingManager().getShortFormProvider(), new EntityIRIChecker() {
            @Override
            public boolean isEntityIRI(IRI iri) {
                return iri.isTopEntity() || iri.isBottomEntity() || project.getRootOntology().containsEntityInSignature(iri, true);
            }

            @Override
            public Collection<OWLEntity> getEntitiesWithIRI(IRI iri) {
                return project.getRootOntology().getEntitiesInSignature(iri, true);
            }
        }, highlightChecker, checker);
        object.accept(entityRenderer);
        return sb.toString();
    }


    public static interface EntityIRIChecker {

        boolean isEntityIRI(IRI iri);

        Collection<OWLEntity> getEntitiesWithIRI(IRI iri);
    }

    public static interface HighlightChecker {

        boolean isHighlighted(OWLEntity entity);
    }

    public static interface DeprecatedChecker {

        boolean isDeprecated(OWLEntity entity);
    }

    private static class EntityRenderer implements OWLObjectVisitor {

        private StringBuilder stringBuilder;

        private ShortFormProvider sfp;

        private IRIShortFormProvider iriSfp = new SimpleIRIShortFormProvider();

        private EntityIRIChecker entityIRIChecker;

        private HighlightChecker highlightChecker;

        private DeprecatedChecker deprecatedChecker;


        private EntityRenderer(StringBuilder stringBuilder, ShortFormProvider sfp, EntityIRIChecker entityIRIChecker, HighlightChecker highlightChecker, DeprecatedChecker deprecatedChecker) {
            this.stringBuilder = stringBuilder;
            this.sfp = sfp;
            this.entityIRIChecker = entityIRIChecker;
            this.highlightChecker = highlightChecker;
            this.deprecatedChecker = deprecatedChecker;
        }

        private void renderEntity(OWLEntity entity) {
            String shortForm = sfp.getShortForm(entity);
            if (shortForm.contains(" ")) {
                stringBuilder.append("'");
                renderEntityShortForm(shortForm, entity);
                stringBuilder.append("'");
            }
            else {
                renderEntityShortForm(shortForm, entity);
            }
        }

        private void renderEntityShortForm(String shortForm, OWLEntity entity) {
            StringBuilder classNamesBuilder = new StringBuilder();
            final boolean highlighted = highlightChecker.isHighlighted(entity);
            if (highlighted) {
                classNamesBuilder.append(" highlight");
            }
            final boolean deprecated = deprecatedChecker.isDeprecated(entity);
            if (deprecated) {
                classNamesBuilder.append(" deprecated");
            }
            stringBuilder.append("<span title=\"").append(entity.getEntityType().getName()).append(": ");
            stringBuilder.append(entity.getIRI());
            stringBuilder.append("\" class=\"").append(classNamesBuilder.toString().trim()).append("\">");
            stringBuilder.append(new SafeHtmlBuilder().appendEscaped(shortForm).toSafeHtml().asString());
            stringBuilder.append("</span>");
        }


        private void renderKeyword(ManchesterOWLSyntax keyword) {
            String styleName = "";
            if (keyword.isAxiomKeyword()) {
                styleName = "axiom-kw";
            }
            else if (keyword.isClassExpressionConnectiveKeyword()) {
                styleName = "connective-kw";
            }
            else if (keyword.isClassExpressionQuantiferKeyword()) {
                styleName = "quantifier-kw";
            }
            else if (keyword.isSectionKeyword()) {
                styleName = "section-kw";
            }

            stringBuilder.append("<span class=\"").append(styleName).append("\"").append(">");
            stringBuilder.append(keyword);
            stringBuilder.append("</span>");
        }

        private void renderUnaryKeyword(ManchesterOWLSyntax keyword) {
            renderKeyword(keyword);
            renderSpace();
        }

        private void renderBinaryKeyword(ManchesterOWLSyntax keyword) {
            renderSpace();
            renderKeyword(keyword);
            renderSpace();
        }

        private void renderSectionKeyword(ManchesterOWLSyntax keyword) {
            renderKeyword(keyword);
            stringBuilder.append(": ");
        }


        @Override
        public void visit(OWLClass cls) {
            renderEntity(cls);
        }

        @Override
        public void visit(OWLObjectProperty property) {
            renderEntity(property);
        }

        @Override
        public void visit(OWLDataProperty property) {
            renderEntity(property);
        }

        @Override
        public void visit(OWLNamedIndividual individual) {
            renderEntity(individual);
        }

        @Override
        public void visit(OWLDatatype datatype) {
            renderEntity(datatype);
        }

        @Override
        public void visit(OWLAnnotationProperty property) {
            renderEntity(property);
        }


        @Override
        public void visit(OWLOntology ontology) {
        }

        @Override
        public void visit(OWLAnnotation node) {
            node.getProperty().accept(this);
            renderSpace();
            node.getValue().accept(this);
        }


        @Override
        public void visit(IRI iri) {
            boolean entityIRI = entityIRIChecker.isEntityIRI(iri);
            StringBuilder iriStringBuilder = new StringBuilder();
            if (!entityIRI) {
                iriStringBuilder.append("<a target=\"_blank\" href=\"");
                iriStringBuilder.append(iri.toString());
                iriStringBuilder.append("\">");
                iriStringBuilder.append("<span class=\"iri\">");
                renderEscapedIRI(iri, iriStringBuilder);
                iriStringBuilder.append("</span>");
                iriStringBuilder.append("</a>");
                stringBuilder.append(iriStringBuilder.toString());
            }
            else {
                for (OWLEntity entity : entityIRIChecker.getEntitiesWithIRI(iri)) {
                    entity.accept(this);
                }
            }

        }

        private void renderEscapedIRI(IRI iri, StringBuilder sb) {
            SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
            htmlBuilder.appendEscaped(iri.toString());
            sb.append(htmlBuilder.toSafeHtml().asString());
        }

        private void renderCollection(Collection<? extends OWLObject> collection, String separator) {
            for (Iterator<? extends OWLObject> it = collection.iterator(); it.hasNext(); ) {
                OWLObject object = it.next();
                object.accept(this);
                if (it.hasNext()) {
                    stringBuilder.append(separator);
                    renderSpace();
                }
            }
        }

        private void renderCollection(Collection<? extends OWLObject> collection, ManchesterOWLSyntax separator, boolean bracket, boolean prettyPrint) {
            if (prettyPrint) {
                stringBuilder.append("<div style=\"display: inline-block; vertical-align: top;\">");
            }
            if (bracket) {
                renderOpenBracket();
            }
            if (prettyPrint) {
                stringBuilder.append("<div class=\"exp-block\">");
            }
            Iterator<? extends OWLObject> it = collection.iterator();
            if(it.hasNext()) {
                OWLObject firstObject = it.next();
                if (prettyPrint) {
                    stringBuilder.append("<div>");
                }
                firstObject.accept(this);
                if (prettyPrint) {
                    stringBuilder.append("</div>");
                }
                while(it.hasNext()) {
                    if (prettyPrint) {
                        stringBuilder.append("<div>");
                    }
                    renderSpace();
                    renderKeyword(separator);
                    renderSpace();
                    OWLObject object = it.next();
                    object.accept(this);
                    if(!it.hasNext()) {
                        renderCloseBracket();
                    }
                    if (prettyPrint) {
                        stringBuilder.append("</div>");
                    }
                }
            }
            if (prettyPrint) {
                stringBuilder.append("</div>");
                stringBuilder.append("</div>");
            }
        }

        private void renderNaryCollection(ManchesterOWLSyntax sectionKeyword, Collection<? extends OWLObject> elements, ManchesterOWLSyntax binaryKeyword) {
            if (elements.size() == 2) {
                Iterator<? extends OWLObject> it = elements.iterator();
                OWLObject first = it.next();
                OWLObject second = it.next();
                first.accept(this);
                renderBinaryKeyword(binaryKeyword);
                second.accept(this);
            }
            else {
                renderSectionKeyword(sectionKeyword);
                renderCollection(elements, ",");
            }
        }

        @Override
        public void visit(OWLDeclarationAxiom axiom) {
            stringBuilder.append("<span style=\"section-kw\">");
            stringBuilder.append(axiom.getEntity().getEntityType().getName());
            stringBuilder.append(": ");
            stringBuilder.append("</span>");
            axiom.getEntity().accept(this);
        }

        @Override
        public void visit(OWLSubClassOfAxiom axiom) {
            axiom.getSubClass().accept(this);
            renderBinaryKeyword(SUBCLASS_OF);
            axiom.getSuperClass().accept(this);
        }

        @Override
        public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            renderUnaryKeyword(NOT);
            renderOpenBracket();
            axiom.getSubject().accept(this);
            renderSpace();
            axiom.getProperty().accept(this);
            renderSpace();
            axiom.getObject().accept(this);
            renderCloseBracket();
        }

        private void renderSpace() {
            stringBuilder.append(" ");
        }

        private void renderCloseBrace() {
            stringBuilder.append("}");
        }

        private void renderOpenBrace() {
            stringBuilder.append("{");
        }


        private void renderCloseSquareBracket() {
            stringBuilder.append("]");
        }

        private void renderOpenSquareBracket() {
            stringBuilder.append("[");
        }


        @Override
        public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            renderPropertyCharacteristic(ASYMMETRIC, axiom);
        }

        @Override
        public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
            renderPropertyCharacteristic(REFLEXIVE, axiom);
        }

        @Override
        public void visit(OWLDisjointClassesAxiom axiom) {
            renderNaryCollection(DISJOINT_CLASSES, axiom.getClassExpressions(), DISJOINT_WITH);
        }

        @Override
        public void visit(OWLDataPropertyDomainAxiom axiom) {
            renderDomainAxiom(axiom);
        }

        private void renderDomainAxiom(OWLPropertyDomainAxiom<?> axiom) {
            axiom.getProperty().accept(this);
            renderBinaryKeyword(DOMAIN);
            axiom.getDomain().accept(this);
        }

        @Override
        public void visit(OWLObjectPropertyDomainAxiom axiom) {
            renderDomainAxiom(axiom);
        }

        @Override
        public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            renderNaryCollection(EQUIVALENT_PROPERTIES, axiom.getProperties(), EQUIVALENT_TO);
        }

        @Override
        public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            renderUnaryKeyword(NOT);
            renderPropertyAssertion(axiom);
        }

        @Override
        public void visit(OWLDifferentIndividualsAxiom axiom) {
            renderNaryCollection(DIFFERENT_INDIVIDUALS, axiom.getIndividuals(), DIFFERENT_FROM);
        }

        @Override
        public void visit(OWLDisjointDataPropertiesAxiom axiom) {
            renderNaryCollection(DISJOINT_PROPERTIES, axiom.getProperties(), DISJOINT_WITH);
        }

        @Override
        public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
            renderNaryCollection(DISJOINT_PROPERTIES, axiom.getProperties(), DISJOINT_WITH);
        }

        private void renderRangeAxiom(OWLPropertyRangeAxiom<?, ?> axiom) {
            axiom.getProperty().accept(this);
            renderBinaryKeyword(RANGE);
            axiom.getRange().accept(this);
        }

        @Override
        public void visit(OWLObjectPropertyRangeAxiom axiom) {
            renderRangeAxiom(axiom);
        }

        @Override
        public void visit(OWLObjectPropertyAssertionAxiom axiom) {
            renderPropertyAssertion(axiom);
        }

        private void renderPropertyAssertion(OWLPropertyAssertionAxiom<?, ?> axiom) {
            axiom.getSubject().accept(this);
            renderSpace();
            axiom.getProperty().accept(this);
            renderSpace();
            axiom.getObject().accept(this);
        }

        @Override
        public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
            renderPropertyCharacteristic(FUNCTIONAL, axiom);
        }

        @Override
        public void visit(OWLSubObjectPropertyOfAxiom axiom) {
            renderSubPropertyOfAxiom(axiom);
        }

        private void renderSubPropertyOfAxiom(OWLSubPropertyAxiom<?> axiom) {
            axiom.getSubProperty().accept(this);
            renderBinaryKeyword(SUB_PROPERTY_OF);
            axiom.getSuperProperty().accept(this);
        }

        @Override
        public void visit(OWLDisjointUnionAxiom axiom) {
            axiom.getOWLClass().accept(this);
            renderBinaryKeyword(DISJOINT_UNION_OF);
            renderCollection(axiom.getClassExpressions(), ",");
        }

        @Override
        public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
            renderPropertyCharacteristic(SYMMETRIC, axiom);
        }

        @Override
        public void visit(OWLDataPropertyRangeAxiom axiom) {
            renderRangeAxiom(axiom);
        }

        @Override
        public void visit(OWLFunctionalDataPropertyAxiom axiom) {
            renderPropertyCharacteristic(FUNCTIONAL, axiom);
        }

        @Override
        public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
            renderNaryCollection(EQUIVALENT_PROPERTIES, axiom.getProperties(), EQUIVALENT_TO);
        }

        @Override
        public void visit(OWLClassAssertionAxiom axiom) {
            axiom.getIndividual().accept(this);
            renderBinaryKeyword(TYPE);
            axiom.getClassExpression().accept(this);
        }

        @Override
        public void visit(OWLEquivalentClassesAxiom axiom) {
            renderNaryCollection(EQUIVALENT_CLASSES, axiom.getClassExpressions(), EQUIVALENT_TO);
        }

        @Override
        public void visit(OWLDataPropertyAssertionAxiom axiom) {
            renderPropertyAssertion(axiom);
        }

        @Override
        public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
            renderPropertyCharacteristic(TRANSITIVE, axiom);
        }

        private void renderPropertyCharacteristic(ManchesterOWLSyntax keyword, OWLUnaryPropertyAxiom<?> axiom) {
            renderSectionKeyword(keyword);
            axiom.getProperty().accept(this);
        }

        @Override
        public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            renderPropertyCharacteristic(IRREFLEXIVE, axiom);
        }

        @Override
        public void visit(OWLSubDataPropertyOfAxiom axiom) {
            renderSubPropertyOfAxiom(axiom);
        }

        @Override
        public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            renderPropertyCharacteristic(INVERSE_FUNCTIONAL, axiom);
        }

        @Override
        public void visit(OWLSameIndividualAxiom axiom) {
            renderNaryCollection(SAME_INDIVIDUAL, axiom.getIndividuals(), SAME_AS);
        }

        @Override
        public void visit(OWLSubPropertyChainOfAxiom axiom) {
            renderCollection(axiom.getPropertyChain(), "o");
            renderBinaryKeyword(SUB_PROPERTY_OF);
            axiom.getSuperProperty().accept(this);
        }

        @Override
        public void visit(OWLInverseObjectPropertiesAxiom axiom) {
            axiom.getFirstProperty().accept(this);
            renderBinaryKeyword(INVERSE_OF);
            axiom.getSecondProperty().accept(this);
        }

        @Override
        public void visit(OWLHasKeyAxiom axiom) {
            axiom.getClassExpression().accept(this);
            renderBinaryKeyword(HAS_KEY);
            renderCollection(axiom.getPropertyExpressions(), ",");
        }

        @Override
        public void visit(OWLDatatypeDefinitionAxiom axiom) {
            axiom.getDatatype().accept(this);
            renderBinaryKeyword(EQUIVALENT_TO);
            axiom.getDataRange().accept(this);
        }

        @Override
        public void visit(SWRLRule rule) {
            renderCollection(rule.getBody(), ",");
            stringBuilder.append(" -> ");
            renderCollection(rule.getHead(), ",");
        }

        @Override
        public void visit(OWLAnnotationAssertionAxiom axiom) {
            axiom.getSubject().accept(this);
            renderSpace();
            axiom.getProperty().accept(this);
            renderSpace();
            axiom.getValue().accept(this);
        }

        @Override
        public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            axiom.getSubProperty().accept(this);
            renderBinaryKeyword(SUB_PROPERTY_OF);
            axiom.getSuperProperty().accept(this);
        }

        @Override
        public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
            axiom.getProperty().accept(this);
            renderBinaryKeyword(DOMAIN);
            axiom.getDomain().accept(this);
        }

        @Override
        public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
            axiom.getProperty().accept(this);
            renderBinaryKeyword(RANGE);
            axiom.getRange().accept(this);
        }


        @Override
        public void visit(OWLObjectIntersectionOf ce) {
            boolean wrap = shouldWrapOperands(ce);
            renderCollection(ce.getOperands(), AND, true, wrap);
        }

        private void renderCloseBracket() {
            stringBuilder.append("<span class=\"r-par\">)</span>");
        }

        private void renderOpenBracket() {
            stringBuilder.append("<span class=\"l-par\">(</span>");
        }

        private boolean isBracketedClassExpression(OWLObject ce) {
            return ce instanceof OWLClassExpression && !((OWLClassExpression) ce).isClassExpressionLiteral() && !(ce instanceof OWLObjectOneOf);
        }


        @Override
        public void visit(OWLObjectUnionOf ce) {
            boolean wrap = shouldWrapOperands(ce);
            renderCollection(ce.getOperands(), OR, true, wrap);
        }

        private boolean shouldWrapOperands(OWLNaryBooleanClassExpression ce) {
            boolean wrap = false;
            for(OWLClassExpression op : ce.getOperands()) {
                if(op.isAnonymous()) {
                    wrap = true;
                }
            }
            return wrap;
        }

        @Override
        public void visit(OWLObjectComplementOf ce) {
            renderUnaryKeyword(NOT);
            renderBracketed(ce.getOperand());
        }

        private void renderBracketed(OWLObject ce) {
            boolean bracketed = isBracketedClassExpression(ce);
            if (bracketed) {
                renderOpenBracket();
                ce.accept(this);
                renderCloseBracket();
            }
            else {
                ce.accept(this);
            }
        }

        @Override
        public void visit(OWLObjectSomeValuesFrom ce) {
            renderRestriction(ce, SOME);
        }

        private void renderRestriction(OWLQuantifiedRestriction<?, ?, ?> ce, ManchesterOWLSyntax keyword) {
            ce.getProperty().accept(this);
            renderBinaryKeyword(keyword);
            renderBracketed(ce.getFiller());
        }

        private void renderRestriction(OWLHasValueRestriction<?, ?, ?> ce, ManchesterOWLSyntax keyword) {
            ce.getProperty().accept(this);
            renderBinaryKeyword(keyword);
            renderBracketed(ce.getValue());
        }

        private void renderRestriction(OWLCardinalityRestriction<?, ?, ?> ce, ManchesterOWLSyntax keyword) {
            ce.getProperty().accept(this);
            renderBinaryKeyword(keyword);
            stringBuilder.append(ce.getCardinality());
            renderSpace();
            renderBracketed(ce.getFiller());
        }

        @Override
        public void visit(OWLObjectAllValuesFrom ce) {
            renderRestriction(ce, ONLY);
        }

        @Override
        public void visit(OWLObjectHasValue ce) {
            renderRestriction(ce, VALUE);
        }

        @Override
        public void visit(OWLObjectMinCardinality ce) {
            renderRestriction(ce, MIN);
        }

        @Override
        public void visit(OWLObjectExactCardinality ce) {
            renderRestriction(ce, EXACTLY);
        }

        @Override
        public void visit(OWLObjectMaxCardinality ce) {
            renderRestriction(ce, MAX);
        }

        @Override
        public void visit(OWLObjectHasSelf ce) {
            ce.getProperty().accept(this);
            renderBinaryKeyword(SOME);
            renderBinaryKeyword(SELF);
        }

        @Override
        public void visit(OWLObjectOneOf ce) {
            renderOpenBrace();
            renderCollection(ce.getIndividuals(), ",");
            renderCloseBrace();
        }


        @Override
        public void visit(OWLDataSomeValuesFrom ce) {
            renderRestriction(ce, SOME);
        }

        @Override
        public void visit(OWLDataAllValuesFrom ce) {
            renderRestriction(ce, ONLY);
        }

        @Override
        public void visit(OWLDataHasValue ce) {
            renderRestriction(ce, VALUE);
        }

        @Override
        public void visit(OWLDataMinCardinality ce) {
            renderRestriction(ce, MIN);
        }

        @Override
        public void visit(OWLDataExactCardinality ce) {
            renderRestriction(ce, EXACTLY);
        }

        @Override
        public void visit(OWLDataMaxCardinality ce) {
            renderRestriction(ce, MAX);
        }

        @Override
        public void visit(OWLLiteral node) {
            if (node.isInteger() || node.isBoolean() || node.isDouble()) {
                stringBuilder.append(node.getLiteral());
            }
            else if (node.isFloat()) {
                stringBuilder.append(node.getLiteral());
                stringBuilder.append("f");
            }
            else {
                stringBuilder.append("<span class=\"literal\">\"");
                stringBuilder.append(node.getLiteral());
                stringBuilder.append("\"</span>");
                if (node.isRDFPlainLiteral()) {
                    if (node.hasLang()) {
                        stringBuilder.append("<span style=\"color: gray;\">");
                        stringBuilder.append(" [language: ");
                        stringBuilder.append(node.getLang());
                        stringBuilder.append("]");
                        stringBuilder.append("</span>");
                    }
                }
                else {
                    stringBuilder.append("^^");
                    node.getDatatype().accept(this);
                }
            }
        }

        @Override
        public void visit(OWLFacetRestriction node) {
            stringBuilder.append(node.getFacet().getShortName());
            renderSpace();
            node.getFacetValue().accept(this);
        }

        @Override
        public void visit(OWLDataOneOf node) {
            renderOpenBrace();
            renderCollection(node.getValues(), ",");
            renderCloseBrace();
        }

        @Override
        public void visit(OWLDataComplementOf node) {
            renderUnaryKeyword(NOT);
            if (node.isDatatype()) {
                node.getDataRange().accept(this);
            }
            else {
                renderOpenBracket();
                node.getDataRange().accept(this);
                renderCloseBracket();
            }
        }

        @Override
        public void visit(OWLDataIntersectionOf node) {
            renderCollection(node.getOperands(), AND, false, false);
        }

        @Override
        public void visit(OWLDataUnionOf node) {
            renderCollection(node.getOperands(), OR, false, false);
        }

        @Override
        public void visit(OWLDatatypeRestriction node) {
            node.getDatatype().accept(this);
            renderOpenSquareBracket();
            renderCollection(node.getFacetRestrictions(), ",");
            renderCloseSquareBracket();
        }


        @Override
        public void visit(OWLAnonymousIndividual individual) {
            stringBuilder.append(individual.getID().getID());
        }

        @Override
        public void visit(OWLObjectInverseOf property) {
            renderUnaryKeyword(INVERSE_OF);
            property.getInverse().accept(this);
        }

        @Override
        public void visit(SWRLClassAtom node) {
            node.getPredicate().accept(this);
            renderOpenBracket();
            renderCollection(node.getAllArguments(), ",");
            renderCloseBracket();
        }

        @Override
        public void visit(SWRLDataRangeAtom node) {
            node.getPredicate().accept(this);
            renderOpenBracket();
            renderCollection(node.getAllArguments(), ",");
            renderCloseBracket();
        }

        @Override
        public void visit(SWRLObjectPropertyAtom node) {
            node.getPredicate().accept(this);
            renderOpenBracket();
            renderCollection(node.getAllArguments(), ",");
            renderCloseBracket();
        }

        @Override
        public void visit(SWRLDataPropertyAtom node) {
            node.getPredicate().accept(this);
            renderOpenBracket();
            renderCollection(node.getAllArguments(), ",");
            renderCloseBracket();

        }

        @Override
        public void visit(SWRLBuiltInAtom node) {
            node.getPredicate().accept(this);
            renderOpenBracket();
            renderCollection(node.getAllArguments(), ",");
            renderCloseBracket();
        }

        @Override
        public void visit(SWRLVariable node) {
            String iriShortForm = iriSfp.getShortForm(node.getIRI());
            stringBuilder.append("?");
            stringBuilder.append(iriShortForm);
        }

        @Override
        public void visit(SWRLIndividualArgument node) {
            node.getIndividual().accept(this);
        }

        @Override
        public void visit(SWRLLiteralArgument node) {
            node.getLiteral().accept(this);
        }

        @Override
        public void visit(SWRLSameIndividualAtom node) {
            stringBuilder.append("SameIndividual");
            renderOpenBracket();
            renderCollection(node.getAllArguments(), ",");
            renderCloseBracket();
        }

        @Override
        public void visit(SWRLDifferentIndividualsAtom node) {
            stringBuilder.append("DifferentIndividuals");
            renderOpenBracket();
            renderCollection(node.getAllArguments(), ",");
            renderCloseBracket();
        }
    }


}

package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.ConditionItem;
import edu.stanford.bmir.protege.web.client.rpc.data.ConditionItemType;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 */
public class ConditionItemRenderer {

    public static final String EQUIVALENT_TO_TEXT = "Equivalent To";

    public static final String SUBCLASS_OF_TEXT = "SubClass Of";

    public static final String SUBCLASS_OF_ANCESTORS_TEXT = "SubClass Of Ancestor Class";


    /**
     * Converts a Java {@link Color} object to a string that is the CSS style attribute value for the color.
     * @param color The color to be converted.
     * @return A CSS representation off the color.
     */
    private static String colorToCSS(Color color) {
        StringBuilder sb = new StringBuilder();
        sb.append("rgba(");
        sb.append(color.getRed());
        sb.append(",");
        sb.append(color.getGreen());
        sb.append(",");
        sb.append(color.getBlue());
        sb.append(",");
        sb.append("1.0)");
        return sb.toString();
    }


    public static final Color HEADER_COLOR = new Color(150, 150, 150);

    public static final String HEADER_CSS_STYLE = "color: " + colorToCSS(HEADER_COLOR) + ";";

    public static final String CLASS_EXPRESSION_PADDING = "50px";


    public static final String EQUIVALENT_TO_HTML = "<span style=\"" + HEADER_CSS_STYLE + "\">" + EQUIVALENT_TO_TEXT + "</span>";

    public static final String SUBCLASS_OF_HTML = "<span style=\"" + HEADER_CSS_STYLE + "\">" + SUBCLASS_OF_TEXT + "</span>";

    public static final String SUBCLASS_OF_ANCESTOR_HTML = "<span style=\"" + HEADER_CSS_STYLE + "\">" + SUBCLASS_OF_ANCESTORS_TEXT + "</span>";


    private OWLAPIProject project;

    private OWLClass rootClass;

    public ConditionItemRenderer(OWLAPIProject project, OWLClass rootClass) {
        this.project = project;
        this.rootClass = rootClass;
    }

    /**
     * Gets the condition items for the root class that this renderer pertains to.
     * @return The list of condition items that this root class pertains to.
     */
    public List<ConditionItem> getConditionItems() {
        List<ConditionItem> result = new ArrayList<ConditionItem>();
        ConditionItem equivalentToHeader = getEquivalentToHeader();
        result.add(equivalentToHeader);
        Set<OWLOntology> importsClosure = project.getRootOntology().getImportsClosure();
        for (OWLOntology ontology : importsClosure) {
            for (OWLEquivalentClassesAxiom ax : ontology.getEquivalentClassesAxioms(rootClass)) {
                result.add(getConditionItem(ax));
            }
        }
        ConditionItem subClassOfHeader = getSubClassOfHeader();
        result.add(subClassOfHeader);
        for (OWLOntology ontology : importsClosure) {
            for (OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(rootClass)) {
                result.add(getConditionItem(ax));
            }

        }
        List<ConditionItem> ancestorItems = getAncestorConditionItems();
        if (!ancestorItems.isEmpty()) {
            ConditionItem inheritedFromItem = getInheritedConditionItem();
            result.add(inheritedFromItem);
            result.addAll(ancestorItems);
        }

        return result;
    }

    private List<ConditionItem> getAncestorConditionItems() {
        OWLOntology rootOntology = project.getRootOntology();
        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        Set<OWLClass> ancestorClasses = project.getClassHierarchyProvider().getAncestors(rootClass);
        List<ConditionItem> ancestorItems = new ArrayList<ConditionItem>();
        for (OWLClass ancestorClass : ancestorClasses) {
            if (!ancestorClass.equals(rootClass)) {
                for (OWLOntology ontology : importsClosure) {
                    for (OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(ancestorClass)) {
                        ancestorItems.add(getConditionItem(ax));
                    }
                }
            }
        }
        return ancestorItems;
    }

    /**
     * Gets the {@link ConditionItem} representing the Equivalent To Header (Necessary & Sufficient separator).
     * @return The ConditionItem.  Not null.
     */
    public ConditionItem getEquivalentToHeader() {
        ConditionItem equivalentToHeader = new ConditionItem(ConditionItem.NS_SEP);
        equivalentToHeader.setBrowserText(EQUIVALENT_TO_HTML);
        return equivalentToHeader;
    }

    /**
     * Gets the {@link ConditionItem} representing the SubClassof Header (Necessary Separator).
     * @return The ConditionItem.  Not null.
     */
    public ConditionItem getSubClassOfHeader() {
        ConditionItem subClassOfHeader = new ConditionItem(ConditionItem.N_SEP);
        String subClassOfHtml = "<span style=\"padding-top: 20px;\">" + SUBCLASS_OF_HTML + "</span>";
        subClassOfHeader.setBrowserText(subClassOfHtml);
        return subClassOfHeader;
    }

    public ConditionItem getInheritedConditionItem() {
        ConditionItem conditionItem = new ConditionItem(ConditionItem.INH_SEP);
        conditionItem.setBrowserText(SUBCLASS_OF_ANCESTOR_HTML);
        return conditionItem;
    }

    /**
     * Gets the {@link ConditionItem} for an {@link OWLSubClassOfAxiom}.  This will be a Necessary & Sufficient
     * item.
     * @param axiom The equivalent classes axiom.  Not null.
     * @return A {@link ConditionItem} for the SubClassOf classes axiom.
     */
    public ConditionItem getConditionItem(OWLSubClassOfAxiom axiom) {
        OWLClassExpression clsExpression = axiom.getSuperClass();
        ConditionItem ci = getConditionItem(clsExpression, ConditionItemType.NECESSARY);
        if (!axiom.getSubClass().equals(rootClass)) {
            final RenderingManager rm = project.getRenderingManager();
            String classExpressionRendering = rm.getBrowserText(axiom.getSuperClass());
            String cleanedClassExpressionRendering = classExpressionRendering.replaceAll("\\s+", " ");  
            ci.setInheritedFromName(cleanedClassExpressionRendering);
            ci.setInheritedFromBrowserText(cleanedClassExpressionRendering);

        }
        return ci;
    }

    /**
     * Gets the {@link ConditionItem} for an {@link OWLEquivalentClassesAxiom}.  This will be a Necessary
     * item.
     * @param axiom The equivalent classes axiom.  Not null.
     * @return A {@link ConditionItem} for the EquivalentClasses axiom.
     */
    public ConditionItem getConditionItem(OWLEquivalentClassesAxiom axiom) {
        if (axiom.getClassExpressions().size() <= 1) {
            throw new IllegalArgumentException("At least two class expressions must be present in the equivalent classes axiom");
        }
        Set<OWLClassExpression> remainingClasses = axiom.getClassExpressionsMinus(rootClass);
        OWLClassExpression ce = remainingClasses.iterator().next();
        ConditionItem ci = getConditionItem(ce, ConditionItemType.NECESSARY_AND_SUFFICIENT);
        if (!axiom.contains(rootClass)) {
            Set<OWLClassExpression> otherClassExpressions = axiom.getClassExpressionsMinus(ce);
            if (!otherClassExpressions.isEmpty()) {
                OWLClassExpression otherCE = otherClassExpressions.iterator().next();
                final RenderingManager rm = project.getRenderingManager();
                String otherCERendering = rm.getBrowserText(otherCE);
                ci.setInheritedFromName(otherCERendering);
                ci.setInheritedFromBrowserText(otherCERendering);
            }
        }
        return ci;
    }

    /**
     * Gets a {@link ConditionItem} for a given class expression of the given type.
     * @param clsExpression The class expression.  Not null.
     * @param type The {@link ConditionItemType}.  Not null.
     * @return The ConditionItem for the given class expression of the given type.
     */
    public ConditionItem getConditionItem(OWLClassExpression clsExpression, ConditionItemType type) {
        RenderingManager rm = project.getRenderingManager();
        String shortForm = rm.getBrowserText(clsExpression);
        String cleanedShortForm = shortForm.replaceAll("\\s+", " ");
        ConditionItem conditionItem = new ConditionItem(cleanedShortForm);
        String html = rm.getHTMLBrowserText(clsExpression);
        String paddedHtml = "<span style=\"padding-left: " + CLASS_EXPRESSION_PADDING + ";\">" + html + "</span>";
        conditionItem.setBrowserText(paddedHtml);
        
        conditionItem.setConditionItemType(type);
        return conditionItem;
    }


    /**
     * Parses a ConditionItem into an OWLAxiom.  The supplied condition item MUST have its {@link ConditionItemType}
     * set to either {@link ConditionItemType#NECESSARY} or {@link ConditionItemType#NECESSARY_AND_SUFFICIENT}, otherwise
     * a runtime exception will be thrown.
     * @param conditionItem The condition item.  This condition item MUST have its {@link ConditionItemType}
     * set to either {@link ConditionItemType#NECESSARY} or {@link ConditionItemType#NECESSARY_AND_SUFFICIENT}, otherwise
     * a runtime exception will be thrown.
     * @return The axiom corresponding to the condition item.  This axiom is obtained by parsing the class expression
     *         (name!) stored in the condition item, and them generating the appropriate axiom depending on the condition item
     *         type, and the root class.
     * @throws ParserException
     */
    public OWLAxiom parseConditionItem(ConditionItem conditionItem) throws ParserException {
        String classExpressionText = conditionItem.getName();
        ConditionItemParser parser = new ConditionItemParser(project);
        OWLClassExpression ce = parser.parse(classExpressionText);
        if (conditionItem.getConditionItemType() == ConditionItemType.NECESSARY) {
            return project.getDataFactory().getOWLSubClassOfAxiom(rootClass, ce);
        }
        else if (conditionItem.getConditionItemType() == ConditionItemType.NECESSARY_AND_SUFFICIENT) {
            return project.getDataFactory().getOWLEquivalentClassesAxiom(rootClass, ce);
        }
        else {
            throw new RuntimeException("ConditionItem type is not defined");
        }
    }



}
